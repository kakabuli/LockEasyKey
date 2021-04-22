package com.philips.easykey.lock.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.philips.easykey.lock.R;

public class BadgeNumberView extends View {

    private RectF rectf;
    private Rect rect;
    private int textSize = 30;          // 文字大小
    private String contentText = "";    // 文字内容
    private int textColor = Color.WHITE;     // 文字颜色
    private int backgroundColor = Color.RED; // 背景颜色
    private Paint paint;

    public BadgeNumberView(Context context){
        this(context,null);
        init();
    }

    public BadgeNumberView(Context context, AttributeSet attrs){
        super(context,attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BadgeNumberView);
        textSize = typedArray.getInteger(R.styleable.BadgeNumberView_textSize,textSize);
        textColor = typedArray.getColor(R.styleable.BadgeNumberView_textColor, Color.WHITE);
        contentText = typedArray.getString(R.styleable.BadgeNumberView_text);
        backgroundColor = typedArray.getColor(R.styleable.BadgeNumberView_backgroundColor, Color.RED);
        init();
    }

    public BadgeNumberView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init();
    }

    // 初始化变量
    private void init(){
        paint = new Paint();
        // 设置画笔为抗锯齿
        paint.setAntiAlias(true);
        // 默认画笔颜色为红色
        paint.setColor(Color.RED);
        // 设置绘制模式为填充
        paint.setStyle(Paint.Style.FILL);

        rect = new Rect();
        rectf = new RectF();
        paint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        // 画圆
        paint.setAntiAlias(true);
        paint.setColor(backgroundColor);
        int r = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredWidth():getMeasuredHeight();
        rectf.set(getPaddingLeft(),getPaddingTop(),r-getPaddingRight(),r-getPaddingBottom());
        canvas.drawArc(rectf,0,360,false,paint);

        // 画文字
        paint.setAntiAlias(true);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.getTextBounds(contentText,0,contentText.length(),rect);
        canvas.drawText(contentText,getWidth()/2-rect.width()/2,getHeight()/2+rect.height()/2,paint);
    }

    public BadgeNumberView setText(int text){
        this.contentText = String.valueOf(text);
        return this;
    }

    public BadgeNumberView setText(String text){
        this.contentText=text;
        return this;
    }

    public BadgeNumberView setTextColor(String textColor){
        this.textColor = Color.parseColor(textColor);
        return this;
    }

    public BadgeNumberView setBackColor(String backgroundColor){
        this.backgroundColor = Color.parseColor(backgroundColor);
        return this;
    }

    public BadgeNumberView setTextSize(int textSize){
        this.textSize = textSize;
        return this;
    }
}
