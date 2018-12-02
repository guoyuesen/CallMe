package com.call.gys.crdeit.callme.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.call.gys.crdeit.callme.R;
import com.call.gys.crdeit.callme.utils.ContextUtil;

/**
 * Created by 郭月森 on 2018/11/29.
 */

public class WiperSwitch extends View implements View.OnTouchListener {
    private Bitmap bg_on, bg_off, slipper_btn;
    /**
     * 按下时的x和当前的x
     */
    private float downX, nowX;

    /**
     * 记录用户是否在滑动
     */
    private boolean onSlip = false;

    /**
     * 当前的状态
     */
    private boolean nowStatus = false;

    /**
     * 监听接口
     */
    private OnChangedListener listener;


    public WiperSwitch(Context context) {
        super(context);
        init(context);
    }

    public WiperSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        //载入图片资源
        bg_on = setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.kai), ContextUtil.dip2px(context,46),ContextUtil.dip2px(context,22));
        bg_off = setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.guan), ContextUtil.dip2px(context,46),ContextUtil.dip2px(context,22));
        slipper_btn = setImgSize(BitmapFactory.decodeResource(getResources(), R.drawable.dian), ContextUtil.dip2px(context,21),ContextUtil.dip2px(context,21));

        setOnTouchListener(this);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        float x = 0;

        //根据nowX设置背景，开或者关状态
        if (nowX < (bg_on.getWidth()/2)){
            canvas.drawBitmap(bg_off, matrix, paint);//画出关闭时的背景
        }else{
            canvas.drawBitmap(bg_on, matrix, paint);//画出打开时的背景
        }
        if (onSlip) {//是否是在滑动状态,
            if(nowX >= bg_on.getWidth())//是否划出指定范围,不能让滑块跑到外头,必须做这个判断
                x = bg_on.getWidth() - slipper_btn.getWidth()/2;//减去滑块1/2的长度
            else
                x = nowX - slipper_btn.getWidth()/2;
        }else {
            if(nowStatus){//根据当前的状态设置滑块的x值
                x = bg_on.getWidth() - slipper_btn.getWidth();
            }else{
                x = 0;
            }
        }

        //对滑块滑动进行异常处理，不能让滑块出界
        if (x < 0 ){
            x = 0;
        }
        else if(x > bg_on.getWidth() - slipper_btn.getWidth()){
            x = bg_on.getWidth() - slipper_btn.getWidth();
        }

        //画出滑块
        canvas.drawBitmap(slipper_btn, x , 0, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                if (event.getX() > bg_off.getWidth() || event.getY() > bg_off.getHeight()){
                    return false;
                }else{
                    onSlip = true;
                    downX = event.getX();
                    nowX = downX;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                nowX = event.getX();
                break;
            }
            case MotionEvent.ACTION_UP:{
                onSlip = false;
                if(event.getX() >= (bg_on.getWidth()/2)){
                    nowStatus = true;
                    nowX = bg_on.getWidth() - slipper_btn.getWidth();
                }else{
                    nowStatus = false;
                    nowX = 0;
                }

                if(listener != null){
                    listener.OnChanged(WiperSwitch.this, nowStatus);
                }
                break;
            }
        }
        //刷新界面
        invalidate();
        return true;
    }



    /**
     * 为WiperSwitch设置一个监听，供外部调用的方法
     */
    public void setOnChangedListener(OnChangedListener listener){
        this.listener = listener;
    }


    /**
     * 设置滑动开关的初始状态，供外部调用
     */
    public void setChecked(boolean checked){
        if(checked){
            nowX = bg_off.getWidth();
        }else{
            nowX = 0;
        }
        nowStatus = checked;
    }


    /**
     * 回调接口
     *
     */
    public interface OnChangedListener {
        public void OnChanged(WiperSwitch wiperSwitch, boolean checkState);
    }
    public Bitmap setImgSize(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高.
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

}

