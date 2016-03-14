package cz.lip.windowsserveradministration.communication;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import cz.lip.windowsserveradministration.AppController;

public class VolleySingleton {
    private static VolleySingleton instance = null;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;

    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(AppController.getAppContext());
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

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}