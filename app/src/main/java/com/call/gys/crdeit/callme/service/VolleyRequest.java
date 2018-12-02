package com.call.gys.crdeit.callme.service;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by 郭月森 on 2018/6/21.
 */

public class VolleyRequest extends Request<String> {
    private MyVolleyCallback callback=null;
    private Map<String,String> smap;
    private String url;

    public VolleyRequest(String url1, Map<String, String> smap, MyVolleyCallback callback) {
        super(Method.POST, url1, callback);
        this.callback = callback;
        this.smap = smap;
        this.url = url1;
    }
    public VolleyRequest(int method, String url1, Map<String, String> smap, MyVolleyCallback callback) {
        super(method, url1, callback);
        this.callback = callback;
        this.smap = smap;
        this.url = url1;
    }

    public VolleyRequest(int method, String url, MyVolleyCallback listener) {
        super(method, url, listener);
        this.url = url;
        this.callback = listener;
    }
    public VolleyRequest(String url, MyVolleyCallback listener) {
        super(Method.GET, url, listener);
        this.url = url;
        this.callback = listener;
    }
    @Override
    protected void onFinish() {
        super.onFinish();
        callback = null;
    }

    @Override
    protected void deliverResponse(String response) {
        Log.d(url.substring(url.length()-10)+"返回结果：==》",response);
        JSONObject object;
        if (callback != null) {
            if (response.isEmpty()){}else {
                try {
                    object = new JSONObject(response);
                    callback.CallBack(object);
                } catch (JSONException e) {
                    object = new JSONObject();
                    callback.onErrorResponse(new VolleyError("json转换错误"));
                }
            }
        }
    }
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Log.d(url+"请求参数：==》",smap.toString());
        return smap;
    }
}
