package com.call.gys.crdeit.callme.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.UserMode;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.utils.ContextUtil;
import com.call.gys.crdeit.callme.utils.DateUtil;
import com.call.gys.crdeit.callme.view.AflView;
import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * Created by 郭月森 on 2018/10/24.
 */

public class AddRemindActivity extends BaseActivity {
    AflView iView;
    int day=-1;
    String time = "";
    int index = -1;
    AutoFlowLayout mFlowLayout;
    TextView textView;
    TextView bodyText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hiedBar(this);
        ButterKnife.bind(this);
        setContentView(R.layout.activity_add_remind);
        textView = findViewById(R.id.remind_time);
        new TitleBuder(this).setTitleText("添加提醒").setLeftImage(R.mipmap.back_to).setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bodyText = findViewById(R.id.addremind_body);
        mFlowLayout = findViewById(R.id.afl_cotent);
        setiView(getList(0));
        //list.add("其它");

        mFlowLayout.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int i, View view) {

                ((AflView)view).click();
                if (day==-1){
                    day = i;
                    long date = DateUtil.getMidnight()+day*24*60*60*1000;
                    textView.setText("提醒时间:"+ ContextUtil.dataTostr(date,"yyyy-MM-dd"));
                    setiView(getList(1));
                }else if (index == -1){
                    setiView(getTimeList(i));
                    index = i;
                }else {
                    if (iView!=null){
                        iView.click();
                    }
                    iView = (AflView)view;
                    long date = DateUtil.getMidnight()+day*24*60*60*1000+(index*5+6+i)*60*60*1000;
                    time = ContextUtil.dataTostr(date,"yyyy-MM-dd HH:mm:ss");
                    textView.setText("提醒时间:"+ time);

                }
            }
        });
    findViewById(R.id.remind_submit).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            submit();
        }
    });

    }

    private void submit() {
        if (time.isEmpty()){
            Toast.makeText(this,"请选择提醒时间",Toast.LENGTH_LONG).show();
            return;
        }
        String body = bodyText.getText().toString();
        if (body.isEmpty()){
            Toast.makeText(this,"请输入提醒内容",Toast.LENGTH_LONG).show();
            return;
        }
        MyVolley.addRequest(new VolleyRequest(Action.addRemind+"staffId="+ UserMode.Id+"&time="+time+"&data="+body, new MyVolleyCallback() {
            @Override
            public void CallBack(JSONObject jsonObject) {
                try {
                    if (jsonObject.getInt("code")==200){
                        Toast.makeText(AddRemindActivity.this,"添加成功",Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Toast.makeText(AddRemindActivity.this,jsonObject.getString("msg"),Toast.LENGTH_LONG).show();
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

    public void setiView(List<String> list){
        mFlowLayout.removeAllViews();
        mFlowLayout.setAdapter(new FlowAdapter(list) {
            @Override
            public View getView(int position) {
                AflView item = new AflView(AddRemindActivity.this);
                item.setText(list.get(position));
                return item;
            }
        });
    }
    public List<String> getList(int type){
        List<String> list = new ArrayList<>();
        switch (type){
            case 0:
                list.add("今天");
                list.add("明天");
                list.add("后天");
                list.add("大后天");
                break;
            case 1:
                list.add("上午6:00-11:00");
                list.add("中午11:00-16:00");
                list.add("下午16:00-20:00");
                break;
        }
        return list;
    }
    public List<String> getTimeList(int type){
        List<String> list = new ArrayList<>();
        switch (type){
            case 0:
                list.add("06:00");
                list.add("07:00");
                list.add("08:00");
                list.add("09:00");
                list.add("10:00");
                break;
            case 1:
                list.add("11:00");
                list.add("12:00");
                list.add("13:00");
                list.add("14:00");
                list.add("15:00");
                break;
            case 2:
                list.add("16:00");
                list.add("17:00");
                list.add("18:00");
                list.add("19:00");
                list.add("20:00");
                break;
        }
        return list;
    }
}
