package com.call.gys.crdeit.callme.view;

/**
 * Created by 郭月森 on 2018/7/9.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.call.gys.crdeit.callme.R;


/**
 * 自定义底部弹出对话框
 * Created by zhaomac on 2017/9/8.
 */

public class HttpsDialogView extends Dialog {

    private View view;
    private Context context;

    //这里的view其实可以替换直接传layout过来的 因为各种原因没传(lan)
    public HttpsDialogView(Context context) {
        super(context, R.style.HttpsDialog);
        this.context = context;
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.mipmap.jiazaiimg);
        this.view = imageView;
        Animation circle_anim = AnimationUtils.loadAnimation(context, R.anim.anim_round_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            imageView.startAnimation(circle_anim);  //开始动画
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(view);//这行一定要写在前面
        setCancelable(true);//点击外部不可dismiss
        setCanceledOnTouchOutside(false);
        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }



}

