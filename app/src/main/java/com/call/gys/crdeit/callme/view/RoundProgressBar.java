package com.call.gys.crdeit.callme.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by 郭月森 on 2018/8/31.
 */

public class RoundProgressBar extends View {
    private int progress = 0;
    public RoundProgressBar(Context context) {
        super(context);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF(10,10,getWidth()-10,(getWidth()-10));//外圈
        Paint p = new Paint();
        p.setStrokeWidth(5);//线条宽度
        p.setAntiAlias(true);//抗锯齿
        p.setColor(Color.parseColor("#30FEBE00"));
        canvas.drawArc(rectF,270,360,false,p);
        p.setStyle(Paint.Style.STROKE);//空心
        p.setColor(Color.parseColor("#60FEBE00"));
        canvas.drawArc(rectF,270,360,false,p);
        p.setColor(Color.parseColor("#FEBE00"));
        canvas.drawArc(rectF,270,progress*3.6f,false,p);
        p.setStrokeWidth(5);//线条宽度
        p.setTextSize(70);
        p.setColor(Color.parseColor("#FEBE00"));
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(progress+"%",getWidth()/2.0f,getWidth()/2.0f+30,p);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
