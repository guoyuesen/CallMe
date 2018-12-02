package com.call.gys.crdeit.callme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郭月森 on 2018/11/8.
 */

public class RegisterActivity2 extends BaseActivity {
    public static String comName = "";
    public static String comCor = "";
    @BindView(R.id.manage_name)
    EditText manageName;
    @BindView(R.id.manage_phone)
    EditText managePhone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        hiedBar(this);
        ButterKnife.bind(this);
        new TitleBuder(this).setTitleText("管理员信息");
    }

    @OnClick(R.id.register)
    public void onViewClicked() {
        String name = manageName.getText().toString();
        String phone = managePhone.getText().toString();
        if (name.isEmpty()){
            Toast.makeText(this,"请输入管理员姓名",Toast.LENGTH_LONG).show();
            return;
        }
        if (phone.isEmpty()){
            Toast.makeText(this,"请输入管理员手机号",Toast.LENGTH_LONG).show();
            return;
        }
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.registerCom+"ComName="+comName+"&corporation="+comCor+"&manage="+name+"&phone="+phone, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("code")==200){
                        Toast.makeText(RegisterActivity2.this,"注册成功",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity2.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Toast.makeText(RegisterActivity2.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }
}
