package com.call.gys.crdeit.callme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.base.BaseActivity;
import com.call.gys.crdeit.callme.base.TitleBuder;
import com.call.gys.crdeit.callme.interfaces.MyVolleyCallback;
import com.call.gys.crdeit.callme.model.GetCallLogUtil;
import com.call.gys.crdeit.callme.service.Action;
import com.call.gys.crdeit.callme.service.MyVolley;
import com.call.gys.crdeit.callme.service.VolleyRequest;
import com.call.gys.crdeit.callme.view.AflView;
import com.example.library.AutoFlowLayout;
import com.example.library.FlowAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 郭月森 on 2018/10/25.
 */

public class AddCustomerActivity extends BaseActivity {
    @BindView(R.id.afl_schedule)
    AutoFlowLayout aflSchedule;
    @BindView(R.id.afl_star)
    AutoFlowLayout aflStar;
    @BindView(R.id.customer_log)
    EditText customerLog;
    @BindView(R.id.customer_name)
    EditText customerName;
    @BindView(R.id.customer_phone)
    EditText customerPhone;
    @BindView(R.id.customer_company)
    EditText customerCompany;
    @BindView(R.id.customer_remarks)
    EditText customerRemarks;
    AflView Schedule;
    int Scheduleint=-1;
    AflView Star;
    int Starint=-1;
    private static JSONObject object;
    private static int type;
    public static void startActivity(Context context ,JSONObject object, int type){
        AddCustomerActivity.object = object;
        AddCustomerActivity.type = type;
        Intent intent = new Intent(context,AddCustomerActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        hiedBar(this);
        ButterKnife.bind(this);

        new TitleBuder(this).setTitleText("一键标注").setLeftText("取消").setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }).setRightText("保存").setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
        initView();
        try {
            if (object != null) {
                if (object.getInt("schedule") != 0) {
                    Scheduleint = object.getInt("schedule") - 1;
                    Schedule = ((AflView) aflSchedule.getChildAt(object.getInt("schedule") - 1));
                    ((AflView) aflSchedule.getChildAt(object.getInt("schedule") - 1)).click();
                }
                if (object.getInt("star") != 0) {
                    Starint = object.getInt("star") - 1;
                    Star = ((AflView) aflStar.getChildAt(object.getInt("star") - 1));
                    ((AflView) aflStar.getChildAt(object.getInt("star") - 1)).click();
                }
                //customerLog.setText(object.getString("remarks"));1
                if(type == 0){
                    customerName.setText(object.getString("phone_name"));
                    customerPhone.setText(object.getString("phone_number"));
                }else {
                    customerName.setText(object.getString("name"));
                    customerPhone.setText(object.getString("phoneNumber"));
                }
                customerRemarks.setText(object.getString("remarks"));
                customerCompany.setText(object.getString("company_name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void submit() {
        try {
            int id = object.getInt("id");
            int star=Starint;
            String log = customerLog.getText().toString();
            String remarks=customerRemarks.getText().toString();
            int schedule=Scheduleint;
            String phone_name=customerName.getText().toString();
            StringBuffer urlsb = new StringBuffer();
                if(type == 0){
                    urlsb.append(Action.setSign+"id="+id+"&star="+(star+1)+"&customerLog="+log+"&schedule="+(schedule+1)+"&phone_name="+phone_name+"&remarks="+remarks);
                }else {
                    urlsb.append(Action.updateStaffCall).append("id="+id+"&star="+(star+1)+"&customerLog="+log+"&schedule="+(schedule+1)+"&name="+phone_name+"&remarks="+remarks);
                }
            Log.d("===<",urlsb.toString());
                MyVolley.addRequest(new VolleyRequest(Request.Method.GET, urlsb.toString(), new MyVolleyCallback() {
                    @Override
                    public void CallBack(JSONObject jsonObject) {

                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }));

            MyVolley.addRequest(new VolleyRequest(Request.Method.GET, urlsb.toString(), new MyVolleyCallback() {
                @Override
                public void CallBack(JSONObject jsonObject) {
                    Toast.makeText(AddCustomerActivity.this,"保存成功",Toast.LENGTH_LONG).show();
                    finish();
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }));


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
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
        aflSchedule.setAdapter(new FlowAdapter(list) {
            @Override
            public View getView(int position) {
                AflView item = new AflView(AddCustomerActivity.this);
                item.setText(list.get(position));
                return item;
            }
        });
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
        aflStar.setAdapter(new FlowAdapter(list1) {
            @Override
            public View getView(int position) {
                AflView item = new AflView(AddCustomerActivity.this);
                item.setText(list1.get(position));
                return item;
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
}
