package com.call.gys.crdeit.callme.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.call.gys.crdeit.callme.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 郭月森 on 2018/11/22.
 */

public class CustimerinfoItem extends RelativeLayout {
    public CustimerinfoItem(Context context, @NonNull String time, @NonNull int type) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_custimerinfo_item,null);
        TextView t1 = view.findViewById(R.id.view_item_time);
        t1.setText(time);
        TextView t2 = view.findViewById(R.id.view_item_con);
        if (type>0) {
            t2.setText("接通 "+type+"秒");
        }else {
            t2.setText("未接通");
        }
        addView(view);
    }
    public CustimerinfoItem(Context context,  @NonNull String time, @NonNull String type,Object o) throws JSONException {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.view_custimerinfo_item,null);
        TextView t1 = view.findViewById(R.id.view_item_time);
        t1.setTextSize(8);
        t1.setText(time);
        TextView t2 = view.findViewById(R.id.view_item_con);
        t2.setTextSize(12);
        t2.setText(type);
        addView(view);
    }

    public CustimerinfoItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustimerinfoItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
