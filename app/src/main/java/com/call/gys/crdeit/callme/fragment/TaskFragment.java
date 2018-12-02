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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.activity.TaskActivity;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.IntentSerializable;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.ContextDataUtil;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.utils.ContextUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


/**
 * Created by 郭月森 on 2018/10/23.
 */

public class TaskFragment extends Fragment {
    JSONArray array;
    ThisAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, null);
        ListView listView = view.findViewById(R.id.task_list);
        array = new JSONArray();
        adapter = new ThisAdapter(array,getContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                Intent intent=  new Intent(getContext(), TaskActivity.class);
                //IntentSerializable serializable = new IntentSerializable();
                    ContextDataUtil.object = array.getJSONObject(i);
                //intent.putExtra("Serializable", serializable);
                getActivity().startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        initList();
        return view;
    }

    private void initList() {
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.getTask+"userId="+ UserMode.Id+"&companyId="+UserMode.CompanyId, new MyVolleyCallback() {
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
            super.notifyDataSetChanged();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ThisItem item= null;
            if (view==null){
                item = new ThisItem();
                view = LayoutInflater.from(context).inflate(R.layout.item_task_list,null);
                item.name = view.findViewById(R.id.task_name);
                item.time = view.findViewById(R.id.task_time);
                item.allNum = view.findViewById(R.id.task_allnum);
                item.caNum = view.findViewById(R.id.task_callednum);
                item.remarks = view.findViewById(R.id.task_remarks);
                item.staff = view.findViewById(R.id.task_staff);
                view.setTag(item);
            }else {
                item = (ThisItem) view.getTag();
            }
            try {
                JSONObject object = array.getJSONObject(i);
                item.name.setText(object.getString("task_name"));
                item.time.setText(ContextUtil.dataTostr(object.getLong("lssuer_time"),"MM.dd")+"发放");
                item.allNum.setText(object.getString("AllNum"));
                item.caNum.setText(object.getString("calledNum"));
                item.remarks.setText("备注:"+object.getString("remarks"));
                item.staff.setText("发放人:"+object.getString("staffName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }
    }
    private class ThisItem{
        TextView name;
        TextView time;
        TextView allNum;
        TextView caNum;
        TextView remarks;
        TextView staff;
    }
}
