package com.call.gys.crdeit.callme.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.call.gys.crdeit.callme.utils.TypefaceUtil;
import com.call.gys.crdeit.callme.utils.UtilHelpers;
import com.call.gys.crdeit.callme.view.HttpsDialogView;


/**
 * Created by 郭月森 on 2018/6/21.
 */

public class BaseActivity extends AppCompatActivity {
    protected HttpsDialogView dialog;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        //hiedBar();
        super.onCreate(savedInstanceState, persistentState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        TypefaceUtil.replaceFont(this, "fonts/Regular.ttf");
        //Window window =

    }

    public static void hiedBar(Activity activity){
        Window window = activity.getWindow();
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //如果为全透明模式，取消设置Window半透明的Flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //设置状态栏为透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#00ffffff"));
        }
        //设置window的状态栏不可见
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Httpdismiss();
    }
    public void Httpshow(Context context) {

        if (dialog==null){
            dialog = new HttpsDialogView(context);
            dialog.show();
        }else if (!dialog.isShowing()){
            dialog.show();
        }
    }
    public void Httpdismiss(){
        if (dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view = getCurrentFocus();
                UtilHelpers.hideKeyboard(ev, view, BaseActivity.this);//调用方法判断是否需要隐藏键盘
                break;

            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


}
