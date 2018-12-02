package com.call.gys.crdeit.callme.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp = getSharedPreferences("SP_PEOPLE", Activity.MODE_PRIVATE);
        String pass = sp.getString("KEY_LOGING_PASS","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        String people = sp.getString("KEY_LOGING_PHONE","");  //取出key为"KEY_PEOPLE_DATA"的值，如果值为空，则将第二个参数作为默认值赋值
        if (people.isEmpty()) {
            startActivity(new Intent(this, LoginActivity.class));
        }else {
            login(people,pass);
        }
        //finish();
    }
    private void login(String tel,String pass) {
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.login + "userPhone="+tel+"&pass="+pass, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        Log.d("qingqiu", object.toString());
                        UserMode.initMode(jsonObject.getJSONObject("data"));
                        SharedPreferences sp = getSharedPreferences("SP_PEOPLE", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("KEY_LOGING_PHONE", tel) ; //存入json串
                        editor.putString("KEY_LOGING_PASS", pass) ;
                        editor.commit() ; //提交
                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "获取用户失败", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("qingqiu", error.getMessage());
            }
        }));
    }
}
