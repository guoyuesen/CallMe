package com.call.gys.crdeit.callme.service;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 郭月森 on 2018/6/21.
 */

public class formRequest extends Request<String> {
    private MyVolleyCallback callback=null;
    private Map<String,String> smap;
    private String url;
    private final String BOUNDARY = "------" + UUID.randomUUID().toString(); // 随机生成边界值
    private final String NEW_LINE = "\r\n"; // 换行符
    private final String MULTIPART_FORM_DATA = "multipart/form-data"; // 数据类型
    private String charSet = "utf-8"; // 编码


    public formRequest(String url1, Map<String, String> smap, MyVolleyCallback callback) {
        super(Method.POST, url1, callback);
        this.callback = callback;
        this.smap = smap;
        this.url = url1;
    }
    public formRequest(int method, String url1, Map<String, String> smap, MyVolleyCallback callback) {
        super(method, url1, callback);
        this.callback = callback;
        this.smap = smap;
        this.url = url1;
    }

    public formRequest(int method, String url, MyVolleyCallback listener) {
        super(method, url, listener);
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
    public byte[] getBody() throws AuthFailureError {
        if (smap == null || smap.size() <= 0) {
            return super.getBody();
        }
        // ------WebKitFormBoundarykR96Kta4gvMACHfq                 第一行
        // Content-Disposition: form-data; name="login_username"    第二行
        //                                                          第三行
        // abcde                                                    第四行

        // ------WebKitFormBoundarykR96Kta4gvMACHfq--               结束行

        // 开始拼接数据
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : smap.keySet()) {
            Object value = smap.get(key);
            stringBuffer.append("--" + BOUNDARY).append(NEW_LINE); // 第一行
            stringBuffer.append("Content-Disposition: form-data; name=\"").append(key).append("\"").append(NEW_LINE); // 第二行
            stringBuffer.append(NEW_LINE); // 第三行
            stringBuffer.append(value).append(NEW_LINE); // 第四行
        }
        // 所有参数拼接完成，拼接结束行
        stringBuffer.append("--" + BOUNDARY + "--").append(NEW_LINE);// 结束行
        Log.d("====>",stringBuffer.toString());
        try {
            return stringBuffer.toString().getBytes(charSet);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // 使用默认的编码方式，Android为utf-8
            return stringBuffer.toString().getBytes();
        }
    }

    /**
     * 该方法的作用：在 http 头部中声明内容类型为表单数据
     *
     * @return
     */
    @Override
    public String getBodyContentType() {
        // multipart/form-data; boundary=----WebKitFormBoundarykR96Kta4gvMACHfq
        return MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY;
    }
}
