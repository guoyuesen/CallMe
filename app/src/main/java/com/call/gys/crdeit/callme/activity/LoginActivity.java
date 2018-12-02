package com.call.gys.crdeit.callme.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 郭月森 on 2018/11/3.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_kf)
    ImageView loginKf;
    @BindView(R.id.login_zc)
    TextView loginZc;
    @BindView(R.id.login_gs)
    TextView loginGs;
    @BindView(R.id.login_yg)
    TextView loginYg;
    @BindView(R.id.login_dl)
    Button loginDl;
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_pass)
    EditText loginPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hiedBar(this);
        ButterKnife.bind(this);
        getSerialNumber();
        loginDl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = loginPhone.getText().toString();
                String pass = loginPass.getText().toString();
                if (phone.isEmpty()){
                    Toast.makeText(LoginActivity.this,"请输入账号",Toast.LENGTH_LONG).show();
                }
                if (pass.isEmpty()){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_LONG).show();
                }
                login(phone,pass);
                //
            }
        });
    }

    public void getSerialNumber() {
        loginZc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        XXPermissions.with(this)
                .permission(new String[]{Manifest.permission.READ_PHONE_STATE,})
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            TelephonyManager tm = (TelephonyManager) LoginActivity.this.getSystemService(Context.TELEPHONY_SERVICE);
                            //String deviceid = tm.getDeviceId();
                            @SuppressLint("MissingPermission") String tel = tm.getLine1Number();//手机号码
                            //String imei = tm.getSimSerialNumber();
                            //String imsi = tm.getSubscriberId();

                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }

    private void login(String tel,String pass) {
        Httpshow(this);
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.login + "userPhone="+tel+"&pass="+pass, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                Httpdismiss();
                try {
                    if (jsonObject.getInt("code") == 200) {
                        JSONObject object = jsonObject.getJSONObject("data");
                        Log.d("qingqiu", object.toString());
                        UserMode.initMode(jsonObject.getJSONObject("data"));
                        loginYg.setText(UserMode.StaffName + "  " + tel);
                        loginGs.setText(UserMode.CompanyName);
                        SharedPreferences sp = getSharedPreferences("SP_PEOPLE", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("KEY_LOGING_PHONE", tel) ; //存入json串
                        editor.putString("KEY_LOGING_PASS", pass) ;
                        editor.commit() ; //提交
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        loginYg.setText(UserMode.StaffName + "  " + tel);
                        Toast.makeText(LoginActivity.this, "获取用户失败", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Httpdismiss();
                Log.d("qingqiu", error.getMessage());
            }
        }));
    }
}
