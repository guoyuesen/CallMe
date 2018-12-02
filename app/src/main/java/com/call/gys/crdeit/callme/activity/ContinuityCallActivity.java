package com.call.gys.crdeit.callme.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.GetCallLogUtil;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.ContextDataUtil;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.PhoneReceiver;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.utils.MyPhoneStateListener;
import com.call.gys.crdeit.callme.utils.PhoneUtils;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郭月森 on 2018/11/4.
 */

public class ContinuityCallActivity extends BaseActivity {
    @BindView(R.id.call_ing)
    TextView callIng;
    @BindView(R.id.call_type)
    TextView callType;
    @BindView(R.id.call_yanshi)
    TextView callYanshi;
    @BindView(R.id.call_next)
    TextView callNext;
    @BindView(R.id.call_name)
    TextView callName;
    @BindView(R.id.call_liji)
    ImageView callLiji;
    @BindView(R.id.call_beizhu)
    TextView callBeizhu;
    @BindView(R.id.call_jixu)
    TextView callJixu;
    @BindView(R.id.call_lijiboda)
    TextView callLijiboda;
    @BindView(R.id.call_ing_phone)
    TextView callIngPhone;
    @BindView(R.id.call_ing_comname)
    TextView callIngComname;
    private String calling;
    private String callnext;
    int callint;
    /*private String calling;
    private String calling;*/
    private JSONArray array;

    private PhoneReceiver recevier;
    private IntentFilter intentFilter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                callNextto();
            } else if (msg.what == 999) {
                getDialMsg();
                therdMiss();
            } else {
                showmiss(msg.what);
            }

        }
    };
    private static int TherdMiss = 5;
    private static boolean isStop = true;
    private static boolean isNextStop = false;


    private void callNextto() {
        if (callint != array.length() - 1) {
            calling = callnext;
            try {
                //callIng.setText(array.getJSONObject(callint).getString("phone_name"));
                callint++;
                callIngPhone.setText(array.getJSONObject(callint).getString("phone_number"));
                callIng.setText(array.getJSONObject(callint).getString("phone_name"));
                callIngComname.setText(array.getJSONObject(callint).getString("company_name"));
                if (callint != array.length() - 1) {
                    JSONObject jsonObject = array.getJSONObject(callint + 1);
                    callnext = jsonObject.getString("phone_number");
                    callNext.setText(jsonObject.getString("phone_number"));
                    callName.setText(jsonObject.getString("phone_name"));
                }
                callType.setText("通话中");
                callYanshi.setText("下一个号码");
                callPhone(calling);
                TherdMiss = 5;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            callnext = null;
            callNext.setText("");
            callName.setText("");
        }

    }

    private void showmiss(int what) {
        callYanshi.setText(what + "秒后即将拨打下一个号码");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuity_call);
        hiedBar(this);
        ButterKnife.bind(this);
        array = ContextDataUtil.array;
        initView();
    }

    private void initView() {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject object = array.getJSONObject(i);
                if (object.getInt("dial_type") == 0) {
                    callIngPhone.setText(object.getString("phone_number"));
                    callIng.setText(object.getString("phone_name"));
                    calling = object.getString("phone_number");
                    callIngComname.setText(array.getJSONObject(callint).getString("company_name"));
                    callint = i;
                    if (i != (array.length() - 1)) {
                        JSONObject jsonObject = array.getJSONObject(i + 1);
                        callnext = jsonObject.getString("phone_number");
                        callNext.setText(jsonObject.getString("phone_number"));
                        callName.setText(jsonObject.getString("phone_name"));
                    }
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void therdMiss() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (callint != array.length() - 1) {
                    Log.d("===>", "  isStop:" + isStop + "  isNextStop:" + isNextStop + "  TherdMiss:" + TherdMiss);
                    while (!isStop && !isNextStop && TherdMiss > 0) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.what = TherdMiss;
                        handler.sendMessage(message);
                        TherdMiss--;
                    }
                    if (!isStop && !isNextStop) {
                        Message message = new Message();
                        message.what = 1;
                        handler.sendMessage(message);
                    }
                }

            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //isNextStop = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //isNextStop = false;
    }

    @OnClick({R.id.call_biaozhu, R.id.call_chongbo, R.id.call_tianjia, R.id.call_zanyin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.call_biaozhu:
                startActivity(new Intent(this, AddCustomerActivity.class));
                break;
            case R.id.call_chongbo:
                break;
            case R.id.call_tianjia:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(calling);
                Toast.makeText(this, "号码已复制成功", Toast.LENGTH_LONG).show();
                getWechatApi();
                break;
            case R.id.call_zanyin:
                Log.d("---", callJixu.getText().toString());
                if (callJixu.getText().toString().equals("开始拨号")) {
                    callJixu.setText("暂停拨号");
                    callKaishi();
                    isStop = false;
                    break;
                }
                if (callJixu.getText().toString().equals("暂停拨号")) {
                    isStop = true;
                    callJixu.setText("继续拨号");

                } else {
                    isStop = false;
                    callJixu.setText("暂停拨号");
                    therdMiss();
                }
                //callKaishi();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recevier != null) {
            unregisterReceiver(recevier);
        }
        isNextStop = true;
    }

    long time = 0;
    long calltime = 0;

    private void callKaishi() {
        recevier = new PhoneReceiver(new MyPhoneStateListener() {

            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                // 注意，方法必须写在super方法后面，否则incomingNumber无法获取到值。
                super.onCallStateChanged(state, incomingNumber);
                //Log.d("通话状态:",state+"");
                if (state == 2) {
                    if (new Date().getTime() - calltime < 1000) {
                        calltime = new Date().getTime();
                    } else {
                        calltime = new Date().getTime();
                        Log.d("电话操作：", "开始拨打");
                    }
                } else if (state == 0) {
                    if (new Date().getTime() - time < 1000) {
                        time = new Date().getTime();
                    } else {
                        time = new Date().getTime();
                        Log.d("电话操作：", "挂断");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(500);
                                    Message message = new Message();
                                    message.what = 999;
                                    handler.sendMessage(message);
                                } catch (Exception e) {
                                    Toast.makeText(ContinuityCallActivity.this, "code:0", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).start();

                    }
                } else {
                    Log.d("电话操作：", "其它");
                }
            }

        }, new PhoneReceiver.NullNotf() {
            @Override
            public void notf() {
                time = calltime;
                Log.d("电话操作：", "挂断");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                            Message message = new Message();
                            message.what = 999;
                            handler.sendMessage(message);
                        } catch (Exception e) {
                            Toast.makeText(ContinuityCallActivity.this, "code:0", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        XXPermissions.with(this)
                .permission(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS})
                .request(new OnPermission() {

                    @SuppressLint("MissingPermission")
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            if (PhoneUtils.isMIUI()){

                            }else {
                                registerReceiver(recevier, intentFilter);
                            }
                            callPhone(calling);
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }

    /**
     * 跳转到微信
     */
    private void getWechatApi() {
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            Toast.makeText(this, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 拨打电话（直接拨打电话）
     *
     * @param phoneNum 电话号码
     */
    private long Dial_Time = 0;

    public void callPhone(String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        XXPermissions.with(this)
                .permission(new String[]{Manifest.permission.CALL_PHONE,})
                .request(new OnPermission() {

                    @SuppressLint("MissingPermission")
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            Dial_Time = new Date().getTime();
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });

    }

    public void getDialMsg() {
        int callHistoryList = GetCallLogUtil.getRecordForMiui(ContinuityCallActivity.this);
        try {
            MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.setTaskPhone + "id=" + array.getJSONObject(callint).getString("id") +
                    "&converse=" + callHistoryList + "&dialTime=" + Dial_Time, new MyVolleyCallback() {
                @Override
                public void CallBack(JSONObject jsonObject) {

                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
