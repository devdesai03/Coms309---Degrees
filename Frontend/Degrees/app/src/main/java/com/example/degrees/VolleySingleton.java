package com.example.degrees;



import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * This VolleySingleton class for managing network requests via the Volley library.
 */
public class VolleySingleton {

    public  static VolleySingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context ctx;

    private VolleySingleton(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    /**
                     * Stores a bitmap in the cache associated with a given URL.
                     * @param url The URL of the image that the bitmap represents.
                     * @param bitmap The {@link Bitmap} object to be stored in the cache.
                     */
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }
/**
 * Returns the single instance of {@code VolleySingleton} that exists for the entire application's lifecycle.
 */
 public static synchronized VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }
/**
 * Gets the request queue for networking requests.
 */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

        /**This method delegates the actual request queuing to the RequestQueue instance. It is generic,
         * allowing for any type of request to be added (e.g., StringRequest, JsonObjectRequest).
         */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
/**
 * Retrieves the instance of ImageLoader used for loading network images.
 */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
