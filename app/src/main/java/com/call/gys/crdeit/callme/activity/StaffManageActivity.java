package com.call.gys.crdeit.callme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 郭月森 on 2018/11/26.
 */

public class StaffManageActivity extends BaseActivity {

    ListView staffManageList;
    JSONArray array;
    ThisAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiedBar(this);
        setContentView(R.layout.activity_staff_manage);
        staffManageList = findViewById(R.id.staff_manage_list);
        new TitleBuder(this).setTitleText("员工管理").setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.add_staff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(StaffManageActivity.this, AddStaffActivity.class));
            }
        });
        staffManageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    UpdateStaffActivity.initActivity(StaffManageActivity.this,array.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //startActivity(new Intent(StaffManageActivity.this,UpdateStaffActivity.class));
            }
        });
        array =new JSONArray();
        adapter = new ThisAdapter(this,array);
        staffManageList.setAdapter(adapter);
        MyVolley.addRequest(new VolleyRequest(Action.getStaff+"staffid="+ UserMode.Id, new MyVolleyCallback() {
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

    }
    class ThisAdapter extends BaseAdapter{
        Context context;
        JSONArray array;

        public ThisAdapter(Context context, JSONArray array) {
            this.context = context;
            this.array = array;
        }

        @Override
        public int getCount() {
            return array.length();
        }

        @Override
        public Object getItem(int i) {
            return array;
        }


        public void notifyDataSetChanged(JSONArray array) {
            this.array = array;
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ThisItem item = null;
            if (view == null){
                item = new ThisItem();
                view = LayoutInflater.from(context).inflate(R.layout.item_staff,null);
                item.t1 = view.findViewById(R.id.item_staff_t1);
                item.t2 = view.findViewById(R.id.item_staff_t2);
                item.t3 = view.findViewById(R.id.item_staff_t3);
                view.setTag(item);
            }else {
                item = (ThisItem) view.getTag();
            }
            try {
                JSONObject object = array.getJSONObject(i);
                item.t1.setText(object.getString("staffName").substring(0,1));
                item.t2.setText(object.getString("staffName"));
                item.t3.setText(object.getString("staffPhone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
    class ThisItem{
        TextView t1;
        TextView t2;
        TextView t3;
    }
}
