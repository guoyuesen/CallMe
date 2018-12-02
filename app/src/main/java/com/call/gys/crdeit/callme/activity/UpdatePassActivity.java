package com.call.gys.crdeit.callme.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郭月森 on 2018/11/28.
 */

public class UpdatePassActivity extends BaseActivity {
    @BindView(R.id.up_pass_01)
    EditText upPass01;
    @BindView(R.id.up_pass_02)
    EditText upPass02;
    @BindView(R.id.up_pass_03)
    EditText upPass03;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass);
        hiedBar(this);
        ButterKnife.bind(this);
        new TitleBuder(this).setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).setTitleText("修改密码");
    }

    @OnClick(R.id.up_pass_submit)
    public void onViewClicked() {
        String pass = upPass01.getText().toString();
        String newPass1 = upPass02.getText().toString();
        String newPass2 = upPass03.getText().toString();
        if (pass.isEmpty()){
            Toast.makeText(this,"请输入原密码",Toast.LENGTH_LONG).show();
            return;
        }
        if (newPass1.isEmpty()){
            Toast.makeText(this,"请输入新密码",Toast.LENGTH_LONG).show();
            return;
        }
        if (newPass2.isEmpty()){
            Toast.makeText(this,"请再次输入新密码",Toast.LENGTH_LONG).show();
            return;
        }
        if (newPass1.equals(newPass2)){
            Toast.makeText(this,"两次密码输入不一致",Toast.LENGTH_LONG).show();
            return;
        }
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.updatePass+"staffid="+ UserMode.Id+"&pass="+pass+"&newPass="+newPass1, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("cade")==200){
                        Toast.makeText(UpdatePassActivity.this,"密码修改成功",Toast.LENGTH_LONG).show();
                        SharedPreferences sp = getSharedPreferences("SP_PEOPLE", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("KEY_LOGING_PHONE", "") ; //存入json串
                        editor.putString("KEY_LOGING_PASS", "") ;
                        editor.commit() ; //提交
                        Intent intent = new Intent(UpdatePassActivity.this,LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }else {
                        Toast.makeText(UpdatePassActivity.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
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
