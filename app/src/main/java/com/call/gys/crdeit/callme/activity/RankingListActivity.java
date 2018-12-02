package com.call.gys.crdeit.callme.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.utils.DateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 郭月森 on 2018/11/20.
 */

public class RankingListActivity extends BaseActivity {
    @BindView(R.id.ranking_list_toptext)
    TextView rankingListToptext;
    @BindView(R.id.ranking_list_text1)
    TextView rankingListText1;
    @BindView(R.id.ranking_list_img1)
    ImageView rankingListImg1;
    @BindView(R.id.ranking_list_text2)
    TextView rankingListText2;
    @BindView(R.id.ranking_list_img2)
    ImageView rankingListImg2;
    @BindView(R.id.ranking_list_text3)
    TextView rankingListText3;
    @BindView(R.id.ranking_list_img3)
    ImageView rankingListImg3;
    @BindView(R.id.ranking_list_text4)
    TextView rankingListText4;
    @BindView(R.id.ranking_list_img4)
    ImageView rankingListImg4;
    @BindView(R.id.ranking_list_text5)
    TextView rankingListText5;
    @BindView(R.id.ranking_list_img5)
    ImageView rankingListImg5;
    @BindView(R.id.ranking_list_list)
    ListView rankingListList;
    TextView textView;
    View imgview;
    int layourid;
    @BindView(R.id.ranking_list_back)
    ImageView rankingListBack;
    ThisAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_list);
        hiedBar(this);
        ButterKnife.bind(this);
        textView = rankingListText1;
        imgview = rankingListImg1;
        layourid = R.id.ranking_list_layout1;
        rankingListBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        adapter = new ThisAdapter(new JSONArray(),this);
        rankingListList.setAdapter(adapter);
        donLowd(0);
    }

    @OnClick({R.id.ranking_list_layout1, R.id.ranking_list_layout2, R.id.ranking_list_layout3, R.id.ranking_list_layout4, R.id.ranking_list_layout5})
    public void onViewClicked(View view) {
        if (view.getId() == layourid) {
            return;
        }
        switch (view.getId()) {
            case R.id.ranking_list_layout1:
                initView(rankingListText1,rankingListImg1,view.getId());
                donLowd(0);
                break;
            case R.id.ranking_list_layout2:
                initView(rankingListText2,rankingListImg2,view.getId());
                donLowd(1);
                break;
            case R.id.ranking_list_layout3:
                initView(rankingListText3,rankingListImg3,view.getId());
                donLowd(2);
                break;
            case R.id.ranking_list_layout4:
                initView(rankingListText4,rankingListImg4,view.getId());
                donLowd(3);
                break;
            case R.id.ranking_list_layout5:
                initView(rankingListText5,rankingListImg5,view.getId());
                donLowd(4);
                break;
        }
    }

    private void donLowd(int i) {
        Httpshow(this);
        adapter.notifyDataSetChanged(new JSONArray());
        MyVolley.addRequest(new VolleyRequest(Action.getRankingList + "staffId=" + UserMode.Id+"&type="+i, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                Httpdismiss();
                try {
                    if (jsonObject.getInt("code")==200){
                        //Toast.makeText(RankingListActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                        //finish();
                        adapter.notifyDataSetChanged(jsonObject.getJSONArray("data"));
                    }else {
                        Toast.makeText(RankingListActivity.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Httpdismiss();
            }
        }));
    }

    private void initView(TextView rankingListText1, ImageView rankingListImg1, int id) {
        textView.setTextSize(14);
        textView.setTextColor(Color.parseColor("#bfffffff"));
        imgview.setVisibility(View.INVISIBLE);
        textView = rankingListText1;
        imgview = rankingListImg1;
        textView.setTextSize(18);
        textView.setTextColor(Color.parseColor("#bfffffff"));
        imgview.setVisibility(View.VISIBLE);
        layourid = id;
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
                view = LayoutInflater.from(context).inflate(R.layout.item_rankinglist,null);
                item.t1 = view.findViewById(R.id.rankinglist_t1);
                item.t2 = view.findViewById(R.id.rankinglist_t2);
                item.t3 = view.findViewById(R.id.rankinglist_t3);
                item.t4 = view.findViewById(R.id.rankinglist_t4);
                item.t5 = view.findViewById(R.id.rankinglist_t5);
                view.setTag(view);
            }else {
                item = (ThisItem) view.getTag();
            }
            try {
                JSONObject object = array.getJSONObject(i);
                item.t1.setText((i+1)+"");
                item.t1.setBackgroundColor(Color.parseColor("#ffffff"));
                switch (i){
                    case 0:
                        item.t1.setText("");
                        item.t1.setBackgroundResource(R.mipmap.rankinglist1);
                        break;
                    case 1:
                        item.t1.setText("");
                        item.t1.setBackgroundResource(R.mipmap.rankinglist2);
                        break;
                    case 2:
                        item.t1.setText("");
                        item.t1.setBackgroundResource(R.mipmap.rankinglist3);
                        break;
                }
                item.t2.setText(object.getString("staffName"));
                int allDate = object.getInt("allDate");
                int effective = object.getInt("effective");
                item.t3.setText(effective+"/"+object.getString("dialnum"));
                item.t4.setText((allDate/60)+"分"+(allDate%60)+"秒");
                int averagetime = (int)object.getDouble("averagetime");
                item.t5.setText((averagetime/60)+"分"+(averagetime%60)+"秒");
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
        TextView t5;
    }
}
