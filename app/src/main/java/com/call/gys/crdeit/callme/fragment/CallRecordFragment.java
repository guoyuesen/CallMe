package com.call.gys.crdeit.callme.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.activity.AddCustomerActivity;
import com.call.gys.crdeit.callme.activity.CustomerInfoActivity;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.GetCallLogUtil;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.PhoneReceiver;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.utils.ContextUtil;
import com.call.gys.crdeit.callme.utils.MyPhoneStateListener;
import com.call.gys.crdeit.callme.utils.PhoneUtils;
import com.call.gys.crdeit.callme.view.CallDialog;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.XXPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

/**
 * Created by 郭月森 on 2018/10/23.
 */

public class CallRecordFragment extends Fragment {
    JSONArray array;
    private PhoneReceiver recevier;
    private IntentFilter intentFilter;
    String phonenumber;
    ThisAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if  (msg.what==999){
                getDialMsg();
            }

        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_record, null);
        ListView listView = view.findViewById(R.id.record_list);
        array = new JSONArray();
        adapter = new ThisAdapter(array,getContext());
        listView.setAdapter(adapter);
        view.findViewById(R.id.call_d).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CallDialog dialog = new CallDialog(getContext(), new CallDialog.callBack() {
                    @Override
                    public void back(String a) {
                        phonenumber = a;
                        callKaishi(a);
                    }
                });
                dialog.show();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    CustomerInfoActivity.initActivity(getContext(),array.getJSONObject(i),1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.getStaffCall + "staffid=" + UserMode.Id, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    array = jsonObject.getJSONArray("data");
                    adapter.notifyDataSetChanged(array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
        return view;
    }
    private class ThisAdapter extends BaseAdapter {
        JSONArray array;
        Context context;


        public ThisAdapter(JSONArray array, Context context) {
            this.array = array;
            this.context = context;
        }

        @Override
        public int getCount() {
            return array.length();
        }

        public void notifyDataSetChanged(JSONArray array) {
            this.array = array;
            notifyDataSetChanged();
        }

        @Override
        public Object getItem(int i) {
            try {
                return array.get(i);
            } catch (JSONException e) {
                return new Object();
            }
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ThisItem item = null;
            if (view == null) {
                item = new ThisItem();
                view = LayoutInflater.from(context).inflate(R.layout.item_callrecord_list, null);
                item.bdqk = view.findViewById(R.id.task_call_type);
                item.yys = view.findViewById(R.id.task_call_yys);
                item.jd = view.findViewById(R.id.task_call_jd);
                item.bd = view.findViewById(R.id.task_call_sj);
                item.th = view.findViewById(R.id.task_call_th);
                item.xj1 = view.findViewById(R.id.task_call_xj1);
                item.xj2 = view.findViewById(R.id.task_call_xj2);
                item.xj3 = view.findViewById(R.id.task_call_xj3);
                item.cz = view.findViewById(R.id.task_call_cz);
                item.phone = view.findViewById(R.id.task_call_phone);
                view.setTag(item);
            } else {
                item = (ThisItem) view.getTag();
            }
            try {//{"schedule":null,"dial_time":null,"star":1,"carrieroperator":"重庆","dial_type":null,"phone_id":40,"converse_time":0,"task_id":2,"id":1,"attribution":"中国移动","remarks":null,"phone_number":"13527586651"}
                JSONObject object = array.getJSONObject(i);
                if (object.getString("name")==null||object.getString("name").equals("null")) {
                    item.phone.setText(object.getString("phoneNumber"));
                }else {
                    item.phone.setText(object.getString("phoneNumber"));
                }
                /*if (object.getInt("dial_type") == 0) {
                    item.bdqk.setVisibility(View.INVISIBLE);
                } else {*/
                    item.bdqk.setVisibility(View.VISIBLE);

                //}
                item.yys.setText(object.getString("attribution"));
                item.jd.setTextColor(Color.parseColor("#ff974b07"));
                switch (object.getInt("schedule")) {
                    case -1:
                        item.jd.setText("无效");
                        item.jd.setTextColor(Color.parseColor("#ff8f8f8f"));
                        break;
                    case 0:
                        item.jd.setText("未标记");
                        item.jd.setTextColor(Color.parseColor("#ff8f8f8f"));
                        break;
                    case 1:
                        item.jd.setText("初访");
                        break;
                    case 2:
                        item.jd.setText("回访");
                        break;
                    case 3:
                        item.jd.setText("再访");
                        break;
                    case 4:
                        item.jd.setText("结束");
                        break;
                    case 5:
                        item.jd.setText("其它");
                        break;
                }
                if (object.getInt("star") >0){
                    item.xj1.setVisibility(View.VISIBLE);
                    if (object.getInt("star") >1){
                        item.xj2.setVisibility(View.VISIBLE);
                        if (object.getInt("star") >2){
                            item.xj3.setVisibility(View.VISIBLE);
                        }else {
                            item.xj3.setVisibility(View.GONE);
                        }
                    }else {
                        item.xj2.setVisibility(View.GONE);
                        item.xj3.setVisibility(View.GONE);
                    }
                }else {
                    item.xj1.setVisibility(View.GONE);
                    item.xj2.setVisibility(View.GONE);
                    item.xj3.setVisibility(View.GONE);
                }
                item.bd.setText((null==object.getString("dialTime")||"null".equals(object.getString("dialTime")))?"": ContextUtil.dataTostr(object.getLong("dialTime"), "MM-dd hh:mm"));
                item.th.setText(object.getString("converseTime") + "秒");
                item.cz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddCustomerActivity.startActivity(context,object,1);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return view;
        }
    }

    private class ThisItem {
        TextView phone;
        ImageView bdqk;
        TextView yys;
        TextView jd;
        TextView bd;
        TextView th;
        ImageView xj1;
        ImageView xj2;
        ImageView xj3;
        ImageView cz;

    }
    private long Dial_Time = 0;
    public void getDialMsg() {
        //Toast.makeText(getContext(),"code:2",Toast.LENGTH_LONG).show();
        StringBuffer urlsb = new StringBuffer();
        //int callHistoryList = GetCallLogUtil.getCallHistoryList(getActivity(), 1);
        int callHistoryList = GetCallLogUtil.getRecordForMiui(getActivity());
            urlsb.append(Action.setStaffCall).append("staffid=").append(UserMode.Id).append("&converseTime=" + callHistoryList + "&dialTime=" + Dial_Time+"&phoneNumber="+phonenumber);
            MyVolley.addRequest(new VolleyRequest(Request.Method.GET, urlsb.toString(), new MyVolleyCallback() {
                @Override
                public void CallBack(JSONObject jsonObject) {
                    try {
                        array = jsonObject.getJSONArray("data");
                        adapter.notifyDataSetChanged(array);
                    } catch (Exception e) {
                        Toast.makeText(getContext(),"code:1",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }));
    }
    long time = 0;
    long calltime = 0;
    private void callKaishi(String c) {
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
                                    Toast.makeText(getContext(), "code:0", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getContext(), "code:0", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
            }
        });
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PHONE_STATE");
        //当网络发生变化的时候，系统广播会发出值为android.net.conn.CONNECTIVITY_CHANGE这样的一条广播
        XXPermissions.with(getActivity())
                .permission(new String[]{Manifest.permission.CALL_PHONE,Manifest.permission.READ_CALL_LOG,Manifest.permission.READ_CONTACTS})
                .request(new OnPermission() {

                    @SuppressLint("MissingPermission")
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            if (PhoneUtils.isMIUI()){

                            }else {
                                getActivity().registerReceiver(recevier, intentFilter);
                            }
                                callPhone(c);
                        }
                    }
                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (recevier!=null) {
            getActivity().unregisterReceiver(recevier);
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
        XXPermissions.with(getActivity())
                .permission(new String[]{Manifest.permission.CALL_PHONE,})
                .request(new OnPermission() {

                    @SuppressLint("MissingPermission")
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            Dial_Time = new Date().getTime();
                            getContext().startActivity(intent);
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {

                    }
                });
    }
}
