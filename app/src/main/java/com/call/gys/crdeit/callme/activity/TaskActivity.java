package com.call.gys.crdeit.callme.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.ContextDataUtil;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.utils.ContextUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郭月森 on 2018/10/25.
 */

public class TaskActivity extends BaseActivity {
    JSONArray array;
    ThisAdapter adapter;
    @BindView(R.id.task_task_all)
    TextView taskTaskAll;
    @BindView(R.id.task_task_dial)
    TextView taskTaskDial;
    @BindView(R.id.task_task_no)
    TextView taskTaskNo;
    @BindView(R.id.task_acticity_task)
    ListView taskActicityTask;
    JSONObject object;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiedBar(this);
        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        array = new JSONArray();
        adapter = new ThisAdapter(array, this);
        taskActicityTask.setAdapter(adapter);
        taskActicityTask.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    CustomerInfoActivity.initActivity(TaskActivity.this,array.getJSONObject(i),0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        initView();

    }

    private void initView() {
        //IntentSerializable serializable = (IntentSerializable) getIntent().getSerializableExtra("Serializable");
        object = ContextDataUtil.object;
        try {
            taskTaskAll.setText(object.getString("AllNum"));
            taskTaskDial.setText(object.getString("calledNum"));
            taskTaskNo.setText(object.getString("callNot"));
            new TitleBuder(this).setTitleText(object.getString("task_name")).setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            initList(object.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initList(String id) {
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.getTaskPhone + "taskId=" + id, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    array = jsonObject.getJSONArray("data");
                    adapter.notifyDataSetChanged(array);
                    int dial = 0;
                    int nocon = 0;
                    for (int i = 0;i<array.length();i++) {
                        JSONObject o = array.getJSONObject(i);
                        if (o.getInt("dial_type") != 0) {
                            dial++;
                            if (o.getInt("converse_time") < 1) {
                                nocon++;
                            }
                        }
                    }
                    taskTaskAll.setText(""+array.length());
                    taskTaskDial.setText(""+dial);
                    taskTaskNo.setText(""+nocon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.call_this, R.id.call_nimin})
    public void onViewClicked(View view) {
        ContextDataUtil.array = array;
        switch (view.getId()) {
            case R.id.call_this:
                startActivity(new Intent(this,ContinuityCallActivity.class));
                break;
            case R.id.call_nimin:
                startActivity(new Intent(this,ContinuityCallActivity.class));
                break;
        }
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
                item.phone.setText(object.getString("phone_name")+"  "+object.getString("phone_number"));
                if (object.getInt("dial_type") == 0) {
                    item.bdqk.setVisibility(View.INVISIBLE);
                } else {
                    item.bdqk.setVisibility(View.VISIBLE);

                }
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
                item.bd.setText((null==object.getString("dial_time")||"null".equals(object.getString("dial_time")))?"":ContextUtil.dataTostr(object.getLong("dial_time"), "MM-dd hh:mm"));
                item.th.setText(object.getString("converse_time") + "秒");
                item.cz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AddCustomerActivity.startActivity(context,object,0);
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
}
