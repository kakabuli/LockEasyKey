package com.philips.easykey.lock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.philips.easykey.lock.R;

/**
 * author : Jack
 * time   : 2021/5/8
 * E-mail : wengmaowei@kaadas.com
 * desc   : 自定义圆形进度条
 */
public class PhilipsWifiVideoCircleProgress extends View {

    private Paint mBgPaint;
    private Paint mProgressPaint;

    private float mCircleStroke = 10;
    private float mSweepAngle = 0;

    private Point mCirclePoint = new Point();

    private RectF mRectF = new RectF();

    public PhilipsWifiVideoCircleProgress(Context context) {
        super(context);
        initPaint();
    }

    public PhilipsWifiVideoCircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        initAttrs(attrs);
    }

    public PhilipsWifiVideoCircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
        initAttrs(attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBg(canvas);
        drawProcess(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float minSize = Math.min(w, h);
        //获取圆心,圆半径
        float radius = minSize / 2;
        //获取圆的相关参数
        mCirclePoint.x = w / 2;
        mCirclePoint.y = h / 2;

        //绘制圆弧的边界(画圆弧(或圆环)先要画矩形)
        mRectF.left = mCirclePoint.x - radius + mCircleStroke;
        mRectF.top = mCirclePoint.y - radius + mCircleStroke;
        mRectF.right = mCirclePoint.x + radius - mCircleStroke;
        mRectF.bottom = mCirclePoint.y + radius - mCircleStroke;

    }

    private void initPaint() {
        mBgPaint = new Paint();
        mBgPaint.setColor(getContext().getColor(R.color.cE6E6E6));
        mBgPaint.setStrokeWidth(mCircleStroke);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeCap(Paint.Cap.ROUND);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(getContext().getColor(R.color.c0066A1));
        mProgressPaint.setStrokeWidth(mCircleStroke);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PhilipsWifiVideoCircleProgress);
        mCircleStroke = typedArray.getDimension(R.styleable.PhilipsWifiVideoCircleProgress_circleStroke, 10);
    }

    private void drawBg(Canvas canvas) {
        canvas.drawArc(mRectF, 270, 359.99f, false, mBgPaint);
    }
    
    private void drawProcess(Canvas canvas) {
        canvas.drawArc(mRectF, 270, mSweepAngle, false, mProgressPaint);
    }


    public void updateProcess(int percentage) {
        if(percentage < 0) {
            percentage = 0;
        } else if(percentage > 100) {
            percentage = 100;
        }
        mSweepAngle = 359.99f / 100 * percentage;
        invalidate();
    }

}
