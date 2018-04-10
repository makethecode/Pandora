package com.dengyangwu.codekiller.pandora.entity;

/**
 * Created by DELL on 2018/4/10.
 */
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.text.TextUtils;

public class MyApplication extends Application {

    public static final String TAG = "MyApplication";
    private RequestQueue queues;
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue(){
        if (queues == null) {
            queues = Volley.newRequestQueue(getApplicationContext());
        }
        return queues;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        VolleyLog.d("Adding request to queue: %s", req.getUrl());
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (queues != null) {
            queues.cancelAll(tag);
        }
    }
}