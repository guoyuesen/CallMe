package com.call.gys.crdeit.callme.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.view.WiperSwitch;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郭月森 on 2018/11/22.
 */

public class AddStaffActivity extends BaseActivity {
    @BindView(R.id.addstaff_name)
    EditText addstaffName;
    @BindView(R.id.addstaff_phone)
    EditText addstaffPhone;
    @BindView(R.id.addstaff_pass)
    EditText addstaffPass;
    @BindView(R.id.addstaff_code)
    EditText addstaffCode;
    @BindView(R.id.addstaff_maneger)
    WiperSwitch addstaffManeger;
    int state = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstaff);
        hiedBar(this);
        ButterKnife.bind(this);
        new TitleBuder(this).setTitleText("添加员工").setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addstaffManeger.setChecked(false);
        addstaffManeger.setOnChangedListener(new WiperSwitch.OnChangedListener() {
            @Override
            public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
                if (checkState){
                    state = 1;
                }else if (!checkState){
                    state = 0;
                }

            }
        });
    }

    @OnClick(R.id.addstaff_submit)
    public void onViewClicked() {
        String name = addstaffName.getText().toString();
        String phone = addstaffPhone.getText().toString();
        String pass = addstaffPass.getText().toString();
        if (name.isEmpty()||phone.isEmpty()||pass.isEmpty()){
            Toast.makeText(this,"请认真填写相关资料",Toast.LENGTH_LONG).show();
            return;
        }
        MyVolley.addRequest(new VolleyRequest(Action.addStaff + "staffid=" + UserMode.Id + "&staffName=" + name + "&staffPhone=" + phone + "&staffPass=" + pass + "&manage=" + state, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("code")==200){
                        Toast.makeText(AddStaffActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Toast.makeText(AddStaffActivity.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
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
