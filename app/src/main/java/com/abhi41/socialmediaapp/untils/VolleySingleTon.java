package com.abhi41.socialmediaapp.untils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class VolleySingleTon {

    private static VolleySingleTon mInstance;
    private  RequestQueue mRequestQueue;
    private  Context mContext;

    private VolleySingleTon(Context mContext) {
        this.mContext = mContext;
        this.mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleTon getInstance(Context context){
        // If RequestQueue is null the initialize new RequestQueue
        if(mInstance == null){
            mInstance = new VolleySingleTon(context);
        }

        // Return RequestQueue
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        // If RequestQueue is null the initialize new RequestQueue
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(mContext);
        }

        // Return RequestQueue
        return mRequestQueue;
    }


    public<T> void addToRequestQueue(Request<T> request){
        // Add the specified request to the request queue
        getRequestQueue().add(request);
    }
}
