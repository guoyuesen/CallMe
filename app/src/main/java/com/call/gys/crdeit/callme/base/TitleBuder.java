package com.call.gys.crdeit.callme.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.call.gys.crdeit.callme.R;

/**
 * Created by 郭月森 on 2018/6/21.
 */

public class TitleBuder {
    private Context context;
    private View view;
    private ImageButton Limg;
    private ImageButton Rimg;
    private TextView CText;
    private TextView LText;
    private TextView RText;
    public TitleBuder(Activity context) {
        this.context = context;
        view = context.findViewById(R.id.base_title_bar);
        view.setVisibility(View.VISIBLE);
        Limg = view.findViewById(R.id.title_left_img);
        Rimg = view.findViewById(R.id.title_right_img);
        LText = view.findViewById(R.id.title_left_text);
        RText = view.findViewById(R.id.title_right_text);
        CText = view.findViewById(R.id.title_center_text);
    }
    public TitleBuder setBackgrund(int color){
        view.setBackgroundColor(color);
        return this;
    }
    public TitleBuder setLeftImage(int id){
        Limg.setImageResource(id);
        Limg.setVisibility(View.VISIBLE);
        return this;
    }
    public TitleBuder setRightImage(int id){
        Rimg.setImageResource(id);
        Rimg.setVisibility(View.VISIBLE);
        return this;
    }
    public TitleBuder setLeftText(String str){
        LText.setText(str);
        LText.setVisibility(View.VISIBLE);
        return this;
    }
    public TitleBuder setRightText(String str){
        RText.setText(str);
        RText.setVisibility(View.VISIBLE);
        return this;
    }
    public TitleBuder setTitleText(String str){
        CText.setText(str);
        CText.setVisibility(View.VISIBLE);
        return this;
    }
    public TitleBuder setLeftListener(View.OnClickListener listener){
        if (LText.getVisibility()== View.VISIBLE){
            LText.setOnClickListener(listener);
        }
        if (Limg.getVisibility()== View.VISIBLE){
            Limg.setOnClickListener(listener);
        }
        return this;
    }
    public TitleBuder setRightListener(View.OnClickListener listener){
        if (RText.getVisibility()== View.VISIBLE){
            RText.setOnClickListener(listener);
        }
        if (Rimg.getVisibility()== View.VISIBLE){
            Rimg.setOnClickListener(listener);
        }
        return this;
    }
    public View Buder(){
        return view;
    }
}
