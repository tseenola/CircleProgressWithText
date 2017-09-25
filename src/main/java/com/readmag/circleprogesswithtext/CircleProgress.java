package com.readmag.circleprogesswithtext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Created by lenovo on 2017/9/25.
 * 描述：
 */

public class CircleProgress extends View {
    private String mSuffix;
    int mCircleProgressColor;
    int mCircleColor;
    float mCircleWidth;
    int mTextSize;
    int mTextColor;
    int mMax;
    boolean mShowText;
    int mStyle;
    private static final int STROKE = 0;
    private static final int FILL = 1;
    private Paint mPaint;
    private int mProcess;
    public CircleProgress(Context context) {
        super(context);
    }


    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray lTypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        mCircleProgressColor = lTypedArray.getColor(R.styleable.CustomProgressBar_circleProgressColor, Color.RED);
        mCircleColor =   lTypedArray.getColor(R.styleable.CustomProgressBar_circleColor,Color.BLACK);
        mCircleWidth = lTypedArray.getFloat(R.styleable.CustomProgressBar_circleWidth,80);
        mTextSize = lTypedArray.getInt(R.styleable.CustomProgressBar_textSize,100);
        mTextColor = lTypedArray.getColor(R.styleable.CustomProgressBar_textColor,Color.GREEN);
        mMax = lTypedArray.getInt(R.styleable.CustomProgressBar_max,100);
        mShowText = lTypedArray.getBoolean(R.styleable.CustomProgressBar_showText,true);
        mStyle = lTypedArray.getInt(R.styleable.CustomProgressBar_style,STROKE);
        mSuffix = lTypedArray.getString(R.styleable.CustomProgressBar_suffix);
        Log.i("vbvb","mSuffix："+mSuffix);
        if (TextUtils.isEmpty(mSuffix)){
            mSuffix = "%";
        }
        mPaint = new Paint();
        lTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.i("vbvb","onDraw getWidth():"+getWidth());
        //画背景圆环
        int center = getWidth()/2;//画布中央
        float radius = center - mCircleWidth/2;//半径
        mPaint.setColor(mCircleColor);//圆圈颜色
        mPaint.setStyle(Paint.Style.STROKE);//空心
        mPaint.setStrokeWidth(mCircleWidth);//圆环的宽度
        mPaint.setAntiAlias(true);//抗锯齿
        canvas.drawCircle(center,center,radius,mPaint);

        //画中间的文字
        mPaint.setColor(mTextColor);//设置字体颜色
        mPaint.setTextSize(mTextSize);//设置字体大小
        mPaint.setStrokeWidth(0);//设置字体宽度
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置Cap
        mPaint.setTypeface(Typeface.MONOSPACE);
        int percent = (int)(mProcess/(float)mMax * 100);
        String strPercent = percent+mSuffix;
        Paint.FontMetricsInt lFontMetricsInt = mPaint.getFontMetricsInt();
        canvas.drawText(strPercent,center-mPaint.measureText(strPercent)/2,
                center+(lFontMetricsInt.bottom-lFontMetricsInt.top)/2-lFontMetricsInt.bottom,mPaint);

        //画圆弧
        RectF oval = new RectF(center-radius,center-radius,
                center+radius,center+radius);
        mPaint.setColor(mCircleProgressColor);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStyle(Paint.Style.STROKE);//空心
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(oval,0,360 * mProcess/mMax,false,mPaint);
    }

    public void setProgress(int pProcess){
        if (pProcess<0){
            throw new IllegalArgumentException("进度不能小于0");
        }
        if (pProcess>mMax){
            mProcess = mMax;
        }
        if (pProcess <= mMax){
            mProcess = pProcess;
            postInvalidate();
        }
    }
}
