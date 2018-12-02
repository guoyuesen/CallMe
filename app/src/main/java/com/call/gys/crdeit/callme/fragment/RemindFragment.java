package com.call.gys.crdeit.callme.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.activity.AddRemindActivity;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by 郭月森 on 2018/10/23.
 */

public class RemindFragment extends Fragment {
    JSONArray array;
    ThisAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remind, null);
        ListView listView = view.findViewById(R.id.remind_list);
        array = new JSONArray();
        adapter = new ThisAdapter(array,getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //startActivity(new Intent(getContext(), AddRemindActivity.class));
            }
        });
        view.findViewById(R.id.remind_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),AddRemindActivity.class));
            }
        });
        initData();
        return view;
    }

    private void initData() {
        MyVolley.addRequest(new VolleyRequest(Action.getRemind+"staffId="+ UserMode.Id, new MyVolleyCallback() {
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

        @Override
        public Object getItem(int i) {
            try {
                return array.get(i);
            } catch (JSONException e) {
                return new Object();
            }
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
            if (view==null){
                item = new ThisItem();
                view = LayoutInflater.from(context).inflate(R.layout.item_remind_list,null);
                item.t1 = view.findViewById(R.id.remind_item_week);
                item.t2 = view.findViewById(R.id.remind_item_time);
                item.t3 = view.findViewById(R.id.remind_item_body);
                view.setTag(view);
            }else {
                item = (ThisItem) view.getTag();
            }
            try {
                JSONObject object = array.getJSONObject(i);
                item.t1.setText(DateUtil.getWeek(object.getString("remindDate")));
                item.t2.setText(object.getString("remindDate"));
                item.t3.setText(object.getString("body"));
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
