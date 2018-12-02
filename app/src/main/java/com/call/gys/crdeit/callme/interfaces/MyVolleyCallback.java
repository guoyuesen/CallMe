package com.call.gys.crdeit.callme.interfaces;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by 郭月森 on 2018/6/21.
 */

public interface MyVolleyCallback extends Response.ErrorListener{
    public abstract void CallBack(JSONObject jsonObject);
}
