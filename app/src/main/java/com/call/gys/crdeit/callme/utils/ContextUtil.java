package com.call.gys.crdeit.callme.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;

/**
 * Created by 郭月森 on 2018/7/6.
 */

public class ContextUtil {
    public static int X = 0;
    public static int Y = 0;
    public ContextUtil(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        X = metric.widthPixels;     // 屏幕宽度（像素）
        Y = metric.heightPixels;   // 屏幕高度（像素）
    }

    public static int getX(Activity activity) {
        if (X==0)
            new ContextUtil(activity);
        return X;
    }

    public static int getY(Activity activity) {
        if (Y==0)
            new ContextUtil(activity);
        return Y;
    }
    public static int dip2px(Context context, int dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static int px2dip(Context context, int pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static String dataTostr(long time, String f){
        SimpleDateFormat format =  new SimpleDateFormat(f);
        return format.format(time);
    }
}
