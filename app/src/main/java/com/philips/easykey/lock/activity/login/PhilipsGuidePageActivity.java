package com.philips.easykey.lock.activity.login;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.fragment.PhilipsGuidePageOneFragment;
import com.philips.easykey.lock.fragment.PhilipsGuidePageThreeFragment;
import com.philips.easykey.lock.fragment.PhilipsGuidePageTwoFragment;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 2019/4/15
 */
public class PhilipsGuidePageActivity extends BaseAddToApplicationActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        ButterKnife.bind(this);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        initView();
    }

    private void initView() {

        fragments = new ArrayList<Fragment>();
        fragments.add(new PhilipsGuidePageOneFragment());
        fragments.add(new PhilipsGuidePageTwoFragment());
        fragments.add(new PhilipsGuidePageThreeFragment());
//        pager.setOffscreenPageLimit(3);

        viewPager.setAdapter(new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int arg0) {
                return fragments.get(arg0);
            }
        });

        // 添加页面切换事件的监听器
        viewPager.setOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
