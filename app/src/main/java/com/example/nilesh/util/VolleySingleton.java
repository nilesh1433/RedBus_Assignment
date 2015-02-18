package com.example.nilesh.util;

/**
 * Created by Nilesh on 2/17/2015.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

    private static VolleySingleton instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    //making sure that only one object is there for whole application
    private VolleySingleton(Context context) {

        //creating request queue for adding request
        requestQueue = Volley.newRequestQueue(context);

        //creating imageloader with LRUCache capable of holding 10 images at a time,
        // if size of overall images exceeds least recently used image is dropped.
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);

            @Override
            public void putBitmap(String key, Bitmap value) {
                // TODO Auto-generated method stub
                cache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                // TODO Auto-generated method stub
                return cache.get(key);
            }
        });
    }

    // for getting the instance of VolleySingelton class
    public static VolleySingleton getInstance(Context context)
    {
        if(instance==null)
            instance = new VolleySingleton(context);
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    ///adding request to requestQueue
    public <T> void addToRequestQueue(Request<T> req)
    {
        req.setTag("App");
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}

