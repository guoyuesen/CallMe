package com.call.gys.crdeit.callme.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.call.gys.crdeit.callme.R;

/**
 * Created by 郭月森 on 2018/11/4.
 */

public class AflView extends RelativeLayout {
    private View view;
    private boolean TF = false;
    private TextView textView;
    private ImageView imageView;
    public AflView(Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.view_afl,null);
        textView = view.findViewById(R.id.afl_text);
        imageView = view.findViewById(R.id.afl_img);
        addView(view);
    }

    public AflView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AflView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void click(){
        if (TF){
            textView.setBackgroundResource(R.drawable.button_shape);
            imageView.setVisibility(INVISIBLE);
            TF = false;
        }else {
            textView.setBackgroundResource(R.drawable.button_blu_shape);
            imageView.setVisibility(VISIBLE);
            TF = true;
        }
    }
    public void setText(String s){
        textView.setText(s);
    }
    public String getText(){
        return textView.getText().toString();
    }
}
