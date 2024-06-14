package com.example.degrees;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * A singleton class managing the request queue and image loading using Volley library.
 */
public class VolleySingleton2 {

    private static VolleySingleton2 instance;
    private RequestQueue requestQueue;
    private final ImageLoader imageLoader;
    private static Context ctx;

    private VolleySingleton2(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

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
     * Retrieves the singleton instance of the VolleySingleton2 class.
     *
     * @param context The application context.
     * @return The instance of VolleySingleton2.
     */
    public static synchronized VolleySingleton2 getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton2(context);
        }
        return instance;
    }


    /**
     * Retrieves the request queue or creates a new one if it's null.
     *
     * @return The RequestQueue instance.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Adds a request to the request queue.
     *
     * @param req The request to be added.
     * @param <T> The type of the request.
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Retrieves the ImageLoader associated with this instance.
     *
     * @return The ImageLoader instance.
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
