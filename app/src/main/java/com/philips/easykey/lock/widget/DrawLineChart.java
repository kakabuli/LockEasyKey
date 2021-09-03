package com.philips.easykey.lock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SizeUtils;

import java.text.DecimalFormat;

public class DrawLineChart extends View {
    private static final String TAG = "DrawLineChart";
    /**View宽度*/
    private int mViewWidth;
    /** View高度*/
    private int mViewHeight;
    /**边框线画笔*/
    private Paint mBorderLinePaint;
    /**文本画笔*/
    private Paint mTextPaint;
    /**要绘制的折线线画笔*/
    private Paint mBrokenLinePaint;
    /**折线文本画笔*/
    private Paint mBrokenLineTextPaint;
    /**横线画笔*/
    private Paint mHorizontalLinePaint;
    /**边框的左边距*/
    private float mBrokenLineLeft = SizeUtils.dp2px(30);
    /**边框的上边距*/
    private float mBrokenLineTop = SizeUtils.dp2px(10);
    /**边框的下边距*/
    private float mBrokenLineBottom = SizeUtils.dp2px(20);
    /**边框的右边距*/
    private float mBrokenLinerRight = SizeUtils.dp2px(10);
    /**圆的半径*/
    private float radius = 2;
    /**需要绘制的宽度*/
    private float mNeedDrawWidth;
    /**需要绘制的高度*/
    private float mNeedDrawHeight;
    private String[] mTransverseValue = new String[]{};
    /**数据值*/
    private int[] mOrdinateValue = new int[]{0,0,23,10,24,42,18};
    /**图表的最大值*/
    private float maxVlaue = 27.55f;
    /**图表的最小值*/
    private float minValue = -19.12f;
    /**要计算的总值*/
    private float calculateValue;
    /**框线平均值*/
    private float averageValue;
    /**横线数量*/
    private float numberLine = 5;
    /**边框线颜色*/
    private int mBorderLineColor = 0xffededed;
    /**边框线的宽度*/
    private  int mBorderWidth = 2;
    /**边框文本颜色*/
    private int mBorderTextColor = 0xff999999;
    /**边框文本大小*/
    private float mBorderTextSize = SizeUtils.dp2px(10);
    /**边框横线颜色*/
    private int mBorderTransverseLineColor = 0xffededed;
    /**边框横线宽度*/
    private float mBorderTransverseLineWidth = 2;
    /**折线颜色*/
    private int mBrokenLineColor = 0xff1E9D8B;
    /**折线文本颜色*/
    private  int mBrokenLineTextColor = Color.GRAY;
    /**折线宽度*/
    private  float mBrokenLineWidth = 6;
    /**折线文本大小*/
    private  float mBrokenLineTextSize = 15;
    /**计算后的x，y坐标*/
    public Point[] mPoints;
    /**设置边框左上右下边距*/
    public  void  setBrokenLineLTRB(float l,float t,float  r,float b){
        mBrokenLineLeft = SizeUtils.dp2px(l);
        mBrokenLineTop = SizeUtils.dp2px(t);
        mBrokenLinerRight = SizeUtils.dp2px(r);
        mBrokenLineBottom = SizeUtils.dp2px(b);
    }

    public int getViewWidth() {
        return mViewWidth;
    }

    public int getViewHeight() {
        return mViewHeight;
    }

    public float getBrokenLineLeft() {
        return mBrokenLineLeft;
    }

    public float getBrokenLineTop() {
        return mBrokenLineTop;
    }

    public float getBrokenLineBottom() {
        return mBrokenLineBottom;
    }

    public float getBrokenLinerRight() {
        return mBrokenLinerRight;
    }

    public float getNeedDrawWidth() {
        return mNeedDrawWidth;
    }

    public float getNeedDrawHeight() {
        return mNeedDrawHeight;
    }

    public Point[] getPoints() {
        return mPoints;
    }

    public String[] getTransverseValue() {
        return mTransverseValue;
    }

    public void setTransverseValue(String[] mTransverseValue) {
        this.mTransverseValue = mTransverseValue;
    }

    public int[] getOrdinateValue() {
        return mOrdinateValue;
    }

    public void setOrdinateValue(int[] mOrdinateValue) {
        this.mOrdinateValue = mOrdinateValue;
    }

    /**图表显示最大值*/
    public void setMaxVlaue(float maxVlaue) {
        this.maxVlaue = maxVlaue;
    }
    /**图表显示最小值*/
    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }
    /**图表横线数量*/
    public void setNumberLine(float numberLine) {
        this.numberLine = numberLine;
    }
    /**边框线颜色*/
    public void setBorderLineColor(int borderLineColor) {
        mBorderLineColor = borderLineColor;
    }
    /**边框文本颜色*/
    public void setBorderTextColor(int borderTextColor) {
        mBorderTextColor = borderTextColor;
    }
    /**边框文本大小*/
    public void setBorderTextSize(float borderTextSize) {
        mBorderTextSize = SizeUtils.dp2px(borderTextSize);
    }
    /**框线横线 颜色*/
    public void setBorderTransverseLineColor(int borderTransverseLineColor) {
        mBorderTransverseLineColor = borderTransverseLineColor;
    }
    /**边框内折线颜色*/
    public void setBrokenLineColor(int brokenLineColor) {
        mBrokenLineColor = brokenLineColor;
    }
    /** 折线文本颜色*/
    public void setBrokenLineTextColor(int brokenLineTextColor) {
        mBrokenLineTextColor = brokenLineTextColor;
    }
    /**折线文本大小*/
    public void setBrokenLineTextSize(float brokenLineTextSize) {
        mBrokenLineTextSize = SizeUtils.dp2px(brokenLineTextSize);
    }
    /**边框线宽度*/
    public void setBorderWidth(float borderWidth) {
        mBorderWidth = SizeUtils.dp2px(borderWidth);
    }
    /**边框横线宽度*/
    public void setBorderTransverseLineWidth(float borderTransverseLineWidth) {
        mBorderTransverseLineWidth = SizeUtils.dp2px(borderTransverseLineWidth);
    }
    /**折线宽度*/
    public void setBrokenLineWidth(float brokenLineWidth) {
        mBrokenLineWidth = SizeUtils.dp2px(brokenLineWidth);
    }

    /**获取框线画笔*/
    public Paint getBorderLinePaint() {
        return mBorderLinePaint;
    }

    /**获取边框文本画笔*/
    public Paint getTextPaint() {
        return mTextPaint;
    }

    /**获取折线画笔*/
    public Paint getBrokenLinePaint() {
        return mBrokenLinePaint;
    }

    /**获取折线文本画笔*/
    public Paint getBrokenLineTextPaint() {
        return mBrokenLineTextPaint;
    }

    /**获取边框横线画笔*/
    public Paint getHorizontalLinePaint() {
        return mHorizontalLinePaint;
    }


    public DrawLineChart(Context context) {
        super(context);
        initPaint();

    }

    public DrawLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewHeight = getMeasuredHeight();
        mViewWidth = getMeasuredWidth();

        /**计算总值*/
        calculateValue = maxVlaue - minValue;

        initNeedDrawWidthAndHeight();

        /**计算框线横线间隔的数据平均值*/
        averageValue = calculateValue/(numberLine-1);

    }


    /**初始化绘制折线图的宽高*/
    private void initNeedDrawWidthAndHeight(){
        mNeedDrawWidth = mViewWidth - mBrokenLineLeft - mBrokenLinerRight;
        mNeedDrawHeight = mViewHeight - mBrokenLineTop - mBrokenLineBottom;
    }
    /**初始化画笔*/
    private void initPaint() {

        /**初始化边框文本画笔*/
        if(mTextPaint==null){
            mTextPaint=new Paint();
            mTextPaint.setAntiAlias(true);
        }
        mTextPaint.setTextSize(mBorderTextSize);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);
        mTextPaint.setColor(mBorderTextColor);

        /**初始化边框线画笔*/
        if(mBorderLinePaint==null){
            mBorderLinePaint=new Paint();
            mBorderLinePaint.setAntiAlias(true);
        }

        mBorderLinePaint.setTextSize(mBorderTextSize);
        mBorderLinePaint.setStrokeWidth(mBorderWidth);
        mBorderLinePaint.setColor(mBorderLineColor);

        /**初始化折线画笔*/
        if(mBrokenLinePaint == null){
            mBrokenLinePaint = new Paint();
            initPaint(mBrokenLinePaint);
        }

        mBrokenLinePaint.setStrokeWidth(mBrokenLineWidth);
        mBrokenLinePaint.setColor(mBrokenLineColor);
        mBrokenLinePaint.setShadowLayer(SizeUtils.dp2px(10), 0, 0, 0xFF5BBBB7);


        /**折线文本画笔*/
        if (mBrokenLineTextPaint == null){
            mBrokenLineTextPaint = new Paint();
            initPaint(mBrokenLineTextPaint);
        }
        mBrokenLineTextPaint.setTextAlign(Paint.Align.CENTER);
        mBrokenLineTextPaint.setColor(mBrokenLineTextColor);
        mBrokenLineTextPaint.setTextSize(mBrokenLineTextSize);

        /**横线画笔*/
        if (mHorizontalLinePaint == null){
            mHorizontalLinePaint = new Paint();
            initPaint(mHorizontalLinePaint);
        }

        mHorizontalLinePaint.setStrokeWidth(mBorderTransverseLineWidth);
        mHorizontalLinePaint.setColor(mBorderTransverseLineColor);
    }

    /**初始化画笔默认属性*/
    private void initPaint(Paint paint){
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPoints = getPoints(mOrdinateValue,mNeedDrawHeight,mNeedDrawWidth,calculateValue,minValue,mBrokenLineLeft ,mBrokenLineTop);
        /**绘制边框线和边框文本*/
        DrawBorderLineAndText(canvas);
        /**根据数据绘制线*/
        DrawLine(canvas);
    }
    /**根据值绘制折线*/
    public void DrawLine(Canvas canvas) {
        Path mPath=new Path();
        for (int i = 0; i < mPoints.length; i++) {
            Point point = mPoints[i];
            if(i == 0){
                mPath.moveTo(point.x,point.y);
            }else {
                mPath.lineTo(point.x ,point.y);
            }
            canvas.drawText(mOrdinateValue[i]+"",point.x,point.y - radius,mBrokenLineTextPaint);
        }
        canvas.drawPath(mPath,mBrokenLinePaint);
    }

    /**绘制边框线和边框文本*/
    private void DrawBorderLineAndText(Canvas canvas) {
        /**对应的属性
         * drawLine(float startX, float startY, float stopX, float stopY, Paint paint);
         * startX   开始的x坐标
         * startY   开始的y坐标
         * stopX    结束的x坐标
         * stopY    结束的y坐标
         * paint    绘制该线的画笔
         * */
        /**绘制边框竖线*/
        //canvas.drawLine(mBrokenLineLeft,mBrokenLineTop-dip2px(5),mBrokenLineLeft,mViewHeight-mBrokenLineBottom,mBorderLinePaint);
        /**绘制边框横线*/
        canvas.drawLine(mBrokenLineLeft,mViewHeight-mBrokenLineBottom,mViewWidth,mViewHeight-mBrokenLineBottom,mBorderLinePaint);


        /**绘制边框分段横线与分段文本*/
        float averageHeight=mNeedDrawHeight / (numberLine - 1);

        for (int i = 0; i < numberLine; i++) {
            float nowadayHeight = averageHeight * i;
            float v = averageValue * (numberLine - 1 - i ) + minValue;

            /**最后横线无需绘制，否则会将边框横线覆盖*/
            if(i != numberLine - 1) {
                canvas.drawLine(mBrokenLineLeft, nowadayHeight + mBrokenLineTop, mViewWidth, nowadayHeight + mBrokenLineTop, mHorizontalLinePaint);
            }
            canvas.drawText(floatKeepTwoDecimalPlaces(v,"0"),mBrokenLineLeft - SizeUtils.dp2px(13),nowadayHeight + mBrokenLineTop + SizeUtils.dp2px(4),mTextPaint);
        }

        mTextPaint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < mPoints.length; i++) {
            canvas.drawText(mTransverseValue[i],mBrokenLineLeft + SizeUtils.dp2px(5) + (mViewWidth / mPoints.length  - mBrokenLinerRight / (mPoints.length - 1)) * i, mViewHeight - 2,mTextPaint);
        }
    }
    /**保留几位小数*/
    private String  floatKeepTwoDecimalPlaces(float f,String pattern){
        DecimalFormat decimalFormat=new DecimalFormat(pattern);//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p = decimalFormat.format(f);
        return p;
    }


    /**根据值计算在该值的 x，y坐标*/
    public Point[] getPoints(int[] values, float height, float width, float max ,float min, float left,float top) {
        float leftPading = width / (values.length-1);//绘制边距
        Point[] points = new Point[values.length];
        for (int i = 0; i < values.length; i++) {
            double value = values[i]-min;
            //计算每点高度所以对应的值
            double mean = (double) max/height;
            //获取要绘制的高度
            float drawHeight = (float) (value / mean);
            int pointY = (int) (height+top  - drawHeight);
            int pointX = (int) (leftPading * i + left);
            Point point = new Point(pointX, pointY);
            points[i] = point;
        }
        return points;
    }
}
