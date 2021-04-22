package com.philips.easykey.lock.fragment.home;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

/**
 * author :
 * time   : 2021/4/22
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsVpTransform implements ViewPager.PageTransformer {

    private float MIN_SCALE = 0.8f;
    private float MIN_ALPHA = 0.5f;
    private float MIN_TRANSLATE = 0.5f;

    @Override
    public void transformPage(View page, float position) {
        if (position <-1 || position >1){ //不是最前面的三张图片的话，设置默认
            page.setAlpha(MIN_ALPHA);
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        }else {
            //应该缩放的大小
            float curScale = (1 - Math.abs(position))*(1-MIN_SCALE) + MIN_SCALE;
            curScale = Math.max(curScale, MIN_ALPHA);
            float curAlpha = (1 - Math.abs(position))*(1-MIN_ALPHA) + MIN_ALPHA;
            curAlpha = Math.max(curScale, curAlpha);
            page.setAlpha(curAlpha);
            page.setScaleX(curScale);
            page.setScaleY(curScale);
            float curTranZ = (1 - Math.abs(position))*(1-MIN_ALPHA) + MIN_ALPHA;
            curAlpha = Math.max(curTranZ, MIN_ALPHA);
            page.setTranslationZ(curTranZ);

        }

    }

}
