package com.call.gys.crdeit.callme.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
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

import butterknife.ButterKnife;

/**
 * Created by 郭月森 on 2018/11/20.
 */

public class ConverseCountActivity extends BaseActivity {
    JSONArray array;
    ThisAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converse_count);
        hiedBar(this);
        ButterKnife.bind(this);
        new TitleBuder(this).setTitleText("通话统计").setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ListView listView = findViewById(R.id.converse_count_list);
        array = new JSONArray();
        adapter = new ThisAdapter(this,array);
        listView.setAdapter(adapter);
        MyVolley.addRequest(new VolleyRequest(Request.Method.GET, Action.getTongji+"staffId="+ UserMode.Id, new MyVolleyCallback() {
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
            try {
                return array.getJSONObject(i);
            } catch (JSONException e) {
                return array;
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
            if (view == null){
                item = new ThisItem();
                view = LayoutInflater.from(context).inflate(R.layout.item_converse_count,null);
                item.t1 = view.findViewById(R.id.count_date);
                item.t2 = view.findViewById(R.id.count_call);
                item.t3 = view.findViewById(R.id.count_all);
                item.t4 = view.findViewById(R.id.count_average);
                view.setTag(item);
            }else {
                item = (ThisItem) view.getTag();
            }

            switch (i){
                case 0:
                    item.t1.setText("今日统计");
                    break;
                case 1:
                    item.t1.setText("昨日统计");
                    break;
                case 2:
                    item.t1.setText("前日统计");
                    break;
                case 3:
                    item.t1.setText("本周统计");
                    break;
                case 4:
                    item.t1.setText("上周统计");
                    break;
                case 5:
                    item.t1.setText("本月统计");
                    break;
                case 6:
                    item.t1.setText("上月统计");
                    break;
            }
            try {
                JSONObject object = array.getJSONObject(i);
                item.t2.setText(object.getString("effective")+"/"+object.getString("dialnum"));
                item.t3.setText(object.getString("allDate"));
                item.t4.setText((int) (object.getDouble("averagetime"))+"");
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
        TextView t4;
    }
}
