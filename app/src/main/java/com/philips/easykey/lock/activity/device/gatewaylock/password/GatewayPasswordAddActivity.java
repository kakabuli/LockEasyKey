package com.philips.easykey.lock.activity.device.gatewaylock.password;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.fragment.GatewayPasswordWeekFragment;
import com.philips.easykey.lock.fragment.GatewayPasswordTemporaryFragment;
import com.philips.easykey.lock.fragment.GatewayPasswordYearFragment;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David
 */
public class GatewayPasswordAddActivity extends BaseAddToApplicationActivity implements View.OnClickListener {


    ViewPager viewPager;
    SlidingTabLayout slidingTabLayout;
    ImageView ivBack;
    TextView tvContent;
    private ListFragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragments = new ArrayList<Fragment>();
    public String gatewayId;
    public String deviceId;

    private String[] tabs;
    public static String gatewayModel=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_password_add);

        viewPager = findViewById(R.id.viewpager);
        slidingTabLayout = findViewById(R.id.sliding_tab_layout);
        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);

        gatewayModel =getIntent().getStringExtra(KeyConstants.GATEWAY_MODEL);
        gatewayId = getIntent().getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId = getIntent().getStringExtra(KeyConstants.DEVICE_ID);

        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.pwd_header_add_tv));
        tabs = getResources().getStringArray(R.array.home_top);
        initViewPager();
        slidingTabLayout.setViewPager(viewPager);
    }


    /**
     * 初始化ViewPager控件
     */
    private void initViewPager() {
        //关闭预加载，默认一次只加载一个Fragment
        //mViewPager.setOffscreenPageLimit(1);
        //添加Fragment
        // 这里可以从Activity中传递数据到Fragment中
        viewPager.setOffscreenPageLimit(3);
        mFragments.add(new GatewayPasswordYearFragment());
        mFragments.add(new GatewayPasswordWeekFragment());
        mFragments.add(new GatewayPasswordTemporaryFragment());

        //适配器
        mPagerAdapter = new ListFragmentPagerAdapter(getSupportFragmentManager(), mFragments);

        viewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public class ListFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final int TAB_COUNT = 2;

        private List<Fragment> mFragmentList;


        public ListFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.mFragmentList = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return (mFragmentList == null || mFragmentList.size() < TAB_COUNT) ? null : mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList == null ? 0 : mFragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs == null || tabs.length == 0 ? "" : tabs[position];
        }

    }
}
