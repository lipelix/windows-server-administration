package cz.mowin.communication;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import cz.mowin.AppController;

/**
 * Class which making actual requests. Uses {@link com.android.volley} library. Implements singleton pattern.
 */
public class VolleySingleton {

    private static String TAG = "Volley";
    private static VolleySingleton instance = null;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private Collection<List<?>> trustedCAN;

    private static char[] KEYSTORE_PASSWORD = "123".toCharArray();


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

    /**
     * Get instance of class
     * @return instance of class
     */
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
                String peer = session.getPeerHost();
                Iterator<List<?>> it = trustedCAN.iterator();

                while (it.hasNext()) {
                    List<?> item = it.next();
                    String name = item.get(item.size() - 1).toString();
                    if (name.equals(peer)) {
                        Log.i(TAG, "SSL | SAN matches: " + item.get(item.size() - 1));
                        return true;
                    }
                }

                Log.e(TAG, "SSL | Peer refused: " + peer);
                return false;
            }
        };
    }

    /**
     * Get request queue, register url stack and connection verifier.
     * @return request queue
     */
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

    /**
     * Get trust manager user for storing certificates and check certificate.
     * @param trustManagers trust managers list
     * @return verified trust manager
     */
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
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };
    }

    /**
     * Create SSL connection, checks and verify connection according to certificate.
     * @return secured connection or null if error
     */
    public SSLSocketFactory newSslSocketFactory() {
        try {
            InputStream caInput = AppController.getAppContext().openFileInput(AppController.CERT_FILE_NAME); 
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(caInput, KEYSTORE_PASSWORD);
            X509Certificate ca = (X509Certificate) keyStore.getCertificate(keyStore.aliases().nextElement());
            keyStore.setCertificateEntry("ca", ca);

            trustedCAN = ca.getSubjectAlternativeNames();
            Log.i(TAG, "SSL | DN: " + ca.getSubjectDN() + " SAN: " + trustedCAN.toString());

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, wrappedTrustManagers, new SecureRandom());

            return sslContext.getSocketFactory();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "SSL | Error during certificate file opening");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

        Toast.makeText(AppController.getAppContext(), "SSL error during certificate file opening", Toast.LENGTH_LONG).show();
        return null;
    }

//    public ImageLoader getImageLoader(){
//        return imageLoader;
//    }
}