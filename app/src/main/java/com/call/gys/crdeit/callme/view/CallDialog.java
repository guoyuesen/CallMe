package com.call.gys.crdeit.callme.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.BaseDialog;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by 郭月森 on 2018/10/24.
 */

public class CallDialog extends BaseDialog implements View.OnClickListener {
    View view;
    TextView textView;
    Context context;
    callBack callBack;

    public CallDialog(@NonNull Context context,callBack callBack) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.callBack = callBack;
        view = LayoutInflater.from(context).inflate(R.layout.dialog_call, null);
        textView = view.findViewById(R.id.call_phone);
        view.findViewById(R.id.call_number_0).setOnClickListener(this);
        view.findViewById(R.id.call_number_1).setOnClickListener(this);
        view.findViewById(R.id.call_number_2).setOnClickListener(this);
        view.findViewById(R.id.call_number_3).setOnClickListener(this);
        view.findViewById(R.id.call_number_4).setOnClickListener(this);
        view.findViewById(R.id.call_number_5).setOnClickListener(this);
        view.findViewById(R.id.call_number_6).setOnClickListener(this);
        view.findViewById(R.id.call_number_7).setOnClickListener(this);
        view.findViewById(R.id.call_number_8).setOnClickListener(this);
        view.findViewById(R.id.call_number_9).setOnClickListener(this);
        view.findViewById(R.id.call_number_p).setOnClickListener(this);
        view.findViewById(R.id.call_number_w).setOnClickListener(this);
        view.findViewById(R.id.call_dialog_close).setOnClickListener(this);
        view.findViewById(R.id.call_dialog_backspace).setOnClickListener(this);
        view.findViewById(R.id.dialog_call_that).setOnClickListener(this);
        view.findViewById(R.id.dialog_call_this).setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(view);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        //String phone = textView.getText();
        switch (view.getId()) {
            case R.id.call_number_0:
                textView.setText(textView.getText().toString() + "0");
                break;
            case R.id.call_number_1:
                textView.setText(textView.getText().toString() + "1");
                break;
            case R.id.call_number_2:
                textView.setText(textView.getText().toString() + "2");
                break;
            case R.id.call_number_3:
                textView.setText(textView.getText().toString() + "3");
                break;
            case R.id.call_number_4:
                textView.setText(textView.getText().toString() + "4");
                break;
            case R.id.call_number_5:
                textView.setText(textView.getText().toString() + "5");
                break;
            case R.id.call_number_6:
                textView.setText(textView.getText().toString() + "6");
                break;
            case R.id.call_number_7:
                textView.setText(textView.getText().toString() + "7");
                break;
            case R.id.call_number_8:
                textView.setText(textView.getText().toString() + "8");
                break;
            case R.id.call_number_9:
                textView.setText(textView.getText().toString() + "9");
                break;
            case R.id.call_number_p:
                textView.setText(textView.getText().toString() + "*");
                break;
            case R.id.call_number_w:
                textView.setText(textView.getText().toString() + "#");
                break;
            case R.id.call_dialog_close:
                dismiss();
                break;
            case R.id.call_dialog_backspace:
                String str = textView.getText().toString();
                textView.setText(str.length() > 0 ? str.substring(0, str.length() - 1) : "");
                break;
            case R.id.dialog_call_this:
                if (textView.getText().toString().length()!=11){
                    Toast.makeText(context,"请输入正确的手机号",Toast.LENGTH_LONG).show();
                }else {
                    callBack.back(textView.getText().toString());
                    //callPhone(textView.getText().toString());
                }
                break;
            case R.id.dialog_call_that:
                if (textView.getText().toString().length()!=11){
                    Toast.makeText(context,"请输入正确的手机号",Toast.LENGTH_LONG).show();
                }else {
                    TelephonyManager tm = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
                    @SuppressLint("MissingPermission") String tel = tm.getLine1Number();
                    BaseActivity baseActivity = new BaseActivity();
                    baseActivity.Httpshow(getContext());
                    MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.callTial+"phone1="+tel.substring(tel.length()-11)+"&phone2="+textView.getText().toString(), new MyVolleyCallback() {
                        @Override
                        public void CallBack(JSONObject jsonObject) {
                            baseActivity.Httpdismiss();
                            try {
                                if (jsonObject.getInt("code") == 200){
                                    String telphone = jsonObject.getString("data");
                                    callBack.back(telphone);
                                }else {
                                    Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(context,"",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            baseActivity.Httpdismiss();
                            Toast.makeText(context,"请求错误",Toast.LENGTH_LONG).show();
                        }
                    }));
                    //callBack.back(textView.getText().toString());
                    //callPhone(textView.getText().toString());
                }
                break;

        }
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
            XXPermissions.with((Activity) context)
                    .permission(new String[]{Manifest.permission.CALL_PHONE,})
                    .request(new OnPermission() {

                        @SuppressLint("MissingPermission")
                        @Override
                        public void hasPermission(List<String> granted, boolean isAll) {
                            if (isAll) {
                                getContext().startActivity(intent);
                            }
                        }

                        @Override
                        public void noPermission(List<String> denied, boolean quick) {

                        }
                    });

    }
    public interface callBack{
        void back(String a);
    }

}
