package com.philips.easykey.lock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

import androidx.annotation.Nullable;


public class CurveChart extends  DrawLineChart {
    public CurveChart(Context context) {
        super(context);
    }

    public CurveChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void DrawLine(Canvas canvas) {
        Path mPath = new Path();
        for(int j = 0; j < mPoints.length; j++) {
            Point startp = mPoints[j];
            Point endp;
            if (j != mPoints.length - 1) {
                endp = mPoints[j + 1];
                int wt = (startp.x + endp.x) / 2;
                Point p3 = new Point();
                Point p4 = new Point();
                p3.y = startp.y;
                p3.x = wt;
                p4.y = endp.y;
                p4.x = wt;
                if (j == 0) {
                    mPath.moveTo(startp.x, startp.y);
                }
                mPath.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            }

        }
        canvas.drawPath(mPath,getBrokenLinePaint());
    }
}