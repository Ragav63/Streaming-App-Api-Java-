package com.example.streamingappapi.movie;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MovieVolleySingleton {
    private RequestQueue requestQueue;
    private static MovieVolleySingleton mInstance;

    private MovieVolleySingleton(Context context){
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized MovieVolleySingleton getInstance(Context context){

        if (mInstance == null){
            mInstance = new MovieVolleySingleton(context);
        }
        return mInstance;
    }
    public RequestQueue getMovieRequestQueue(){return requestQueue;}
}
