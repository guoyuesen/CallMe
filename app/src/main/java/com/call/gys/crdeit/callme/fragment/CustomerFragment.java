package com.call.gys.crdeit.callme.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.activity.AddCustomerActivity;
import com.call.gys.crdeit.callme.activity.CustomerInfoActivity;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.view.AflView;
import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭月森 on 2018/10/23.
 */

public class CustomerFragment extends Fragment {
    AutoFlowLayout aflSchedule;
    AutoFlowLayout aflStar;
    AflView Schedule;
    int Scheduleint=-1;
    AflView Star;
    int Starint=-1;
    ThisAdapter adapter;
    JSONArray array;
    TextView textView;
    EditText editText;
    View view1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer,null);
        ListView listView = view.findViewById(R.id.customer_list);
        aflSchedule = view.findViewById(R.id.afl_schedule);
        aflStar = view.findViewById(R.id.afl_star);
        textView = view.findViewById(R.id.kehuzongshuliang);
        editText = view.findViewById(R.id.customer_log);
        array = new JSONArray();
        adapter = new ThisAdapter(array,getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                /*Intent intent = new Intent(getContext(), CustomerInfoActivity.class);
                intent.putExtra("jsonObject",array.getJSONObject(i).toString());
                startActivity(intent);*/
                    CustomerInfoActivity.initActivity(getContext(),array.getJSONObject(i),0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        view1 = view.findViewById(R.id.shaixuan_view);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        view.findViewById(R.id.customer_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getContext(), AddCustomerActivity.class));
            }
        });
        view.findViewById(R.id.shai_xuan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.setVisibility(view1.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
            }
        });
        view.findViewById(R.id.customer_resetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initAfl();
                editText.setText("");
            }
        });
        view.findViewById(R.id.customer_renovate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initShaixuan();
            }
        });
        initView();
        return view;
    }

    private void initShaixuan() {
        String url = Action.getCustomerScreen+ "staffid=" + UserMode.Id+"&Schedule="+(Scheduleint+1)+"&Star="+(Starint+1)+"&Text="+editText.getText().toString();
        Log.d("url:",url);
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, url, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    array = jsonObject.getJSONArray("data");
                    adapter.notifyDataSetChanged(array);
                    view1.setVisibility(view1.getVisibility() == View.VISIBLE?View.GONE:View.VISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }

    private class ThisAdapter extends BaseAdapter {
        JSONArray array;
        Context context;

        public ThisAdapter(JSONArray array, Context context) {
            this.array = array;
            this.context = context;
        }


        public void notifyDataSetChanged(JSONArray array) {
            this.array = array;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return array.length();
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
            if (view==null){
                item = new ThisItem();
                view = LayoutInflater.from(context).inflate(R.layout.item_customer_list,null);
                item.name = view.findViewById(R.id.customername);
                item.remarks = view.findViewById(R.id.customertext);
                item.type = view.findViewById(R.id.customertype);
                item.img1 = view.findViewById(R.id.customerimg1);
                item.img2 = view.findViewById(R.id.customerimg2);
                item.img3 = view.findViewById(R.id.customerimg3);
                item.gs = view.findViewById(R.id.customer_company_name);
                view.setTag(item);
            }else {
                item = (ThisItem) view.getTag();
            }
            try {
                JSONObject object = array.getJSONObject(i);
                item.name.setText(object.getString("phone_name")+"  "+object.getString("phone_number"));
                item.gs.setText(object.getString("company_name"));
                item.remarks.setText(object.getString("remarks"));
                switch (object.getInt("schedule")) {
                    case -1:
                        item.type.setText("无效");
                        break;
                    case 0:
                        item.type.setText("未标记");
                        break;
                    case 1:
                        item.type.setText("初访");
                        break;
                    case 2:
                        item.type.setText("回访");
                        break;
                    case 3:
                        item.type.setText("再访");
                        break;
                    case 4:
                        item.type.setText("结束");
                        break;
                    case 5:
                        item.type.setText("其它");
                        break;
                }
                if (object.getInt("star") >0){
                    item.img1.setVisibility(View.VISIBLE);
                    if (object.getInt("star") >1){
                        item.img2.setVisibility(View.VISIBLE);
                        if (object.getInt("star") >2){
                            item.img3.setVisibility(View.VISIBLE);
                        }else {
                            item.img3.setVisibility(View.GONE);
                        }
                    }else {
                        item.img2.setVisibility(View.GONE);
                        item.img3.setVisibility(View.GONE);
                    }
                }else {
                    item.img1.setVisibility(View.GONE);
                    item.img2.setVisibility(View.GONE);
                    item.img3.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
        class ThisItem{
            TextView name;
            TextView type;
            TextView remarks;
            View img1;
            View img2;
            View img3;
            TextView gs;
        }
    }
    private void initView() {
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.getCustomer + "comid=" + UserMode.CompanyId + "&staffid=" + UserMode.Id, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    array = jsonObject.getJSONArray("data");
                    adapter.notifyDataSetChanged(array);
                    textView.setText(""+array.length());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));

        initAfl();
        aflSchedule.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int i, View view) {
                if (Schedule!=null){
                    Schedule.click();
                }
                ((AflView)view).click();
                Schedule = (AflView)view;
                Scheduleint = i;
            }
        });

        aflStar.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int i, View view) {
                if (Star!=null){
                    Star.click();
                }
                ((AflView)view).click();
                Star = (AflView)view;
                Starint = i;
            }
        });
    }
    public void initAfl(){
        List<String> list = new ArrayList<>();
        list.add("初访");
        list.add("回访");
        list.add("再访");
        list.add("结束");
        list.add("其它");
        List<String> list1 = new ArrayList<>();
        list1.add("一星");
        list1.add("二星");
        list1.add("三星");
        list1.add("无效客户");
        aflSchedule.removeAllViews();
        aflStar.removeAllViews();
        Schedule = null;
        Scheduleint=-1;
        Star = null;
        Starint=-1;
        aflSchedule.setAdapter(new FlowAdapter(list) {
            @Override
            public View getView(int position) {
                AflView item = new AflView(getContext());
                item.setText(list.get(position));
                return item;
            }
        });
        aflStar.setAdapter(new FlowAdapter(list1) {
            @Override
            public View getView(int position) {
                AflView item = new AflView(getContext());
                item.setText(list1.get(position));
                return item;
            }
        });
    }
}
