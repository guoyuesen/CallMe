package com.call.gys.crdeit.callme.activity;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.GetCallLogUtil;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.PhoneReceiver;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.utils.ContextUtil;
import com.call.gys.crdeit.callme.utils.MyPhoneStateListener;
import com.call.gys.crdeit.callme.utils.PhoneUtils;
import com.call.gys.crdeit.callme.view.CustimerinfoItem;
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

import static com.call.gys.crdeit.callme.utils.PhoneUtils.isMIUI;

/**
 * Created by 郭月森 on 2018/11/19.
 */

public class CustomerInfoActivity extends BaseActivity {
    @BindView(R.id.customerinfo_name)
    TextView customerinfoName;
    @BindView(R.id.customerinfo_remarks)
    TextView customerinfoRemarks;
    @BindView(R.id.customerinfo_phone)
    TextView customerinfoPhone;
    @BindView(R.id.customerinfo_yys)
    TextView customerinfoYys;
    @BindView(R.id.customerinfo_schedule)
    TextView customerinfoSchedule;
    @BindView(R.id.customerinfo_star)
    TextView customerinfoStar;
    static JSONObject object;
    static int type;
    @BindView(R.id.dial_history)
    LinearLayout dialHistory;
    @BindView(R.id.remarks_history)
    LinearLayout remarksHistory;
    private PhoneReceiver recevier;
    private IntentFilter intentFilter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 999) {
                getDialMsg();
            }

        }
    };

    public static void initActivity(Context context, JSONObject object, int type) {
        CustomerInfoActivity.object = object;
        CustomerInfoActivity.type = type;
        context.startActivity(new Intent(context, CustomerInfoActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiedBar(this);
        setContentView(R.layout.activity_custimerinfo);
        ButterKnife.bind(this);
        //String str = getIntent().getStringExtra("jsonObject");
        try {
            initActivity();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initActivity() throws JSONException {
        if (type == 0) {
            customerinfoName.setText(object.getString("phone_name"));
            customerinfoPhone.setText(object.getString("phone_number"));
        } else {
            customerinfoName.setText(object.getString("name"));
            customerinfoPhone.setText(object.getString("phoneNumber"));
        }
        customerinfoRemarks.setText(object.getString("remarks"));
        customerinfoYys.setText(object.getString("attribution"));
        StringBuffer Schedulesb = new StringBuffer("跟进状态：");
        StringBuffer Starsb = new StringBuffer("客户星级：");
        switch (object.getInt("schedule")) {
            case -1:
                Schedulesb.append("无效");
                break;
            case 0:
                Schedulesb.append("未标记");
                break;
            case 1:
                Schedulesb.append("初访");
                break;
            case 2:
                Schedulesb.append("回访");
                break;
            case 3:
                Schedulesb.append("再访");
                break;
            case 4:
                Schedulesb.append("结束");
                break;
            case 5:
                Schedulesb.append("其它");
                break;
        }
        switch (object.getInt("star")) {
            case 1:
                Starsb.append("一星客户");
                break;
            case 2:
                Starsb.append("二星客户");
                break;
            case 3:
                Starsb.append("三星客户");
                break;
        }
        customerinfoSchedule.setText(Schedulesb.toString());
        customerinfoStar.setText(Starsb.toString());
        try {
            showLog();
        }catch (Exception e){

        }
    }

    private void showLog() throws JSONException {
        String url = Action.getTaskCallHistory+"taskphoneid="+object.get("id");
        if (type == 1){
            url = Action.getStaffCallHistory+"staffcallid="+object.get("id");
        }
            MyVolley.addRequest(new VolleyRequest(Request.Method.GET,url , new MyVolleyCallback() {
                @Override
                public void CallBack(JSONObject jsonObject) {
                    try {
                        JSONArray array = jsonObject.getJSONArray("data");
                        for (int i = 0;i<array.length();i++) {
                            JSONObject item = array.getJSONObject(i);
                            CustimerinfoItem itemLeft = new CustimerinfoItem(CustomerInfoActivity.this, ContextUtil.dataTostr(
                                    item.getLong("dialTime"), "MM-dd hh:ss"), item.getInt("converseTime"));
                            CustimerinfoItem itemReft = new CustimerinfoItem(CustomerInfoActivity.this,ContextUtil.dataTostr(
                                    item.getLong("dialTime"), "MM-dd hh:ss"), item.getString("remarks"),0);
                            dialHistory.addView(itemLeft);
                            remarksHistory.addView(itemReft);
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

    @OnClick({R.id.customerinfo_back, R.id.customerinfo_remind, R.id.customerinfo_sign, R.id.customerinfo_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.customerinfo_back:
                finish();
                break;
            case R.id.customerinfo_remind:
                break;
            case R.id.customerinfo_sign:
                /*Intent intent = new Intent(this,AddCustomerActivity.class);
                intent.putExtra("",object.toString());
                startActivity(intent);*/
                AddCustomerActivity.startActivity(this, object, type);
                break;
            case R.id.customerinfo_call:
                callKaishi();
                break;
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
        StringBuffer urlsb = new StringBuffer();
        int callHistoryList = GetCallLogUtil.getRecordForMiui(this);
        try {
            if (type == 0) {
                urlsb.append(Action.setTaskPhone).append("id=").append(object.getString("id")).append("&converse=" + callHistoryList + "&dialTime=" + Dial_Time);
            } else {
                urlsb.append(Action.setStaffCall).append("id=").append(object.getString("id")).append("&converseTime=" + callHistoryList + "&dialTime=" + Dial_Time);
            }
            MyVolley.addRequest(new VolleyRequest(Request.Method.GET, urlsb.toString(), new MyVolleyCallback() {
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
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
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
                            Toast.makeText(CustomerInfoActivity.this, "code:0", Toast.LENGTH_LONG).show();
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
                            try {
                                if (PhoneUtils.isMIUI()){

                                }else {
                                    registerReceiver(recevier, intentFilter);
                                }
                                if (type == 0) {
                                    callPhone(object.getString("phone_number"));
                                } else {
                                    callPhone(object.getString("phoneNumber"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (recevier != null) {
            unregisterReceiver(recevier);
        }
    }
}
