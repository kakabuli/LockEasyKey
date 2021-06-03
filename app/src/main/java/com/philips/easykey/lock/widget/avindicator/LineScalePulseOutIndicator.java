package com.philips.easykey.lock.widget.avindicator;

import android.animation.ValueAnimator;

import java.util.ArrayList;

/**
 * author :
 * time   : 2021/6/3
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class LineScalePulseOutIndicator extends LineScaleIndicator {

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators=new ArrayList<>();
        long[] delays=new long[]{500,250,0,250,500};
        for (int i = 0; i < 5; i++) {
            final int index=i;
            ValueAnimator scaleAnim=ValueAnimator.ofFloat(1,0.3f,1);
            scaleAnim.setDuration(900);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            addUpdateListener(scaleAnim, animation -> {
                scaleYFloats[index] = (float) animation.getAnimatedValue();
                postInvalidate();
            });
            animators.add(scaleAnim);
        }
        return animators;
    }

}
