package cz.lip.windowsserveradministration.communication;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cz.lip.windowsserveradministration.AppController;
import cz.lip.windowsserveradministration.R;

public class VolleySingleton {

    private static VolleySingleton instance = null;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private static char[] KEYSTORE_PASSWORD = "123456".toCharArray();

    private VolleySingleton() {
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            int size = (int) ((Runtime.getRuntime().maxMemory() / 1024) / 8);
            private LruCache<String, Bitmap> lruCache = new LruCache<>(size);

            @Override
            public Bitmap getBitmap(String url) {
                return lruCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                lruCache.put(url, bitmap);
            }
        });
    }

    public static VolleySingleton getInstance() {
        if (instance == null) {
            instance = new VolleySingleton();
        }
        return instance;
    }

    /**
     * Verifies hostname of server machine
     * @return HostnameVerifier
     */
    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
//                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
//                return hv.verify("192.168.0.100", session);
            }
        };
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {

            HurlStack hurlStack = new HurlStack() {
                @Override
                protected HttpURLConnection createConnection(URL url) throws IOException {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                    try {
                        httpsURLConnection.setSSLSocketFactory(VolleySingleton.getInstance().newSslSocketFactory());
                        httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return httpsURLConnection;
                }
            };

            requestQueue = Volley.newRequestQueue(AppController.getAppContext(), hurlStack);
        }
        return requestQueue;
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException ignored) {
                        }
                    }
                }
        };
    }

    public SSLSocketFactory newSslSocketFactory() {
        try {
            InputStream caInput = AppController.getAppContext().getResources().openRawResource(R.raw.localcert); // this cert file stored in \app\src\main\res\raw folder path
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(caInput, KEYSTORE_PASSWORD);
            X509Certificate ca = (X509Certificate) keyStore.getCertificate(keyStore.aliases().nextElement());
            Log.d("SSL", "DN - " + ca.getSubjectDN() + " PubK - " + ca.getPublicKey());
            keyStore.setCertificateEntry("ca", ca);


            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, wrappedTrustManagers, null);

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}