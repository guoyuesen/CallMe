package com.call.gys.crdeit.callme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
 * Created by 郭月森 on 2018/11/29.
 */

public class UpdateStaffActivity extends BaseActivity {
    private static JSONObject data;
    @BindView(R.id.updatestaff_name)
    TextView updatestaffName;
    @BindView(R.id.updatestaff_phone)
    TextView updatestaffPhone;
    @BindView(R.id.updatestaff_pass)
    EditText updatestaffPass;
    @BindView(R.id.updatestaff_maneger)
    WiperSwitch updatestaffManeger;
    @BindView(R.id.updatestaff_stop)
    WiperSwitch updatestaffStop;
    int maneger = 0;
    int stop = 1;

    public static void initActivity(Context context, JSONObject object) {
        data = object;
        context.startActivity(new Intent(context, UpdateStaffActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_staff);
        hiedBar(this);
        ButterKnife.bind(this);
        new TitleBuder(this).setTitleText("员工资料修改").setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            updatestaffName.setText(data.getString("staffName"));
            updatestaffPhone.setText(data.getString("staffPhone"));
            if (data.getInt("staffManage")==2) {
                maneger = 2;
                updatestaffManeger.setChecked(true);
            }else {
                updatestaffManeger.setChecked(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        updatestaffManeger.setOnChangedListener(new WiperSwitch.OnChangedListener() {
            @Override
            public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
                if (checkState){
                    maneger = 2;
                }else {
                    maneger = 1;
                }
            }
        });
        updatestaffStop.setOnChangedListener(new WiperSwitch.OnChangedListener() {
            @Override
            public void OnChanged(WiperSwitch wiperSwitch, boolean checkState) {
                if (checkState){stop = 0;}else {
                    stop = 1;
                }
            }
        });
    }

    @OnClick(R.id.updatestaff_submit)
    public void onViewClicked() {
        try {
        String pass = updatestaffPass.getText().toString();
        StringBuffer sb=new StringBuffer(Action.updateStaff);
        sb.append("staffid="+ UserMode.Id);
        sb.append("&state="+stop);
        if (!pass.isEmpty()) {
            sb.append("pass" + pass);
        }
         sb.append("&updateStaffId="+data.getString("id"));
            MyVolley.addRequest(new VolleyRequest(sb.toString(), new MyVolleyCallback() {
                @Override
                public void CallBack(JSONObject jsonObject) {
                    try {
                        if (jsonObject.getInt("code")==200){
                            Toast.makeText(UpdateStaffActivity.this,"修改成功",Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(UpdateStaffActivity.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UpdateStaffActivity.this,"网络请求错误",Toast.LENGTH_LONG).show();
                }
            }));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
