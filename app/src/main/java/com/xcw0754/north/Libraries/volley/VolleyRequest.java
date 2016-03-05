package com.xcw0754.north.Libraries.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.xcw0754.north.MyApp;

import java.util.Map;

/**
 * Created by Administrator on 2016/3/3.
 */
public class VolleyRequest {
    public static StringRequest stringRequest;
    public static Context context;

    public static void RequestGet(Context context,String url, String tag, VolleyInterface vif)
    {

        MyApp.getHttpQueues().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.GET,url,vif.loadingListener(),vif.errorListener());
        stringRequest.setTag(tag);
        MyApp.getHttpQueues().add(stringRequest);
        // 不写也能执行
//        MyApplication.getHttpQueues().start();
    }

    public static void RequestPost(Context context, String url, String tag, final Map<String, String> params, VolleyInterface vif)
    {
        MyApp.getHttpQueues().cancelAll(tag);
        stringRequest = new StringRequest(Request.Method.POST,url,vif.loadingListener(),vif.errorListener())
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        stringRequest.setTag(tag);
        MyApp.getHttpQueues().add(stringRequest);
        // 不写也能执行
//        MyApplication.getHttpQueues().start();
    }
}
