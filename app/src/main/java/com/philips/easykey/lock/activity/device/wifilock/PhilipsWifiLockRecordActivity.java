package com.philips.easykey.lock.activity.device.wifilock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.fragment.record.PhilipsWifiLockOpenRecordFragment;
import com.philips.easykey.lock.fragment.record.PhilipsWifiLockVistorRecordFragment;
import com.philips.easykey.lock.fragment.record.PhilipsWifiVideoLockAlarmRecordFragment;
import com.philips.easykey.lock.fragment.record.WifiLockAlarmRecordFragment;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockRecordPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVideoRecordView;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhilipsWifiLockRecordActivity extends BaseActivity<IWifiLockVideoRecordView,
        WifiVideoLockRecordPresenter<IWifiLockVideoRecordView>> implements IWifiLockVideoRecordView,View.OnClickListener  {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_open_lock_record)
    TextView tvOpenLockRecord;
    @BindView(R.id.tv_warn_information)
    TextView tvWarnInformation;
    @BindView(R.id.tv_visitor_record)
    TextView tvVistorRecord;
    @BindView(R.id.vp_home)
    ViewPager viewPager;
    @BindView(R.id.iv_record_screen)
    ImageView mIvRecordScreen;
    PhilipsWifiLockOpenRecordFragment openRecordFragment;
    WifiLockAlarmRecordFragment alarmRecordFragment;
    PhilipsWifiLockVistorRecordFragment vistorRecordFragment;
    PhilipsWifiVideoLockAlarmRecordFragment videoLockAlarmRecordFragment;
    private String wifiSn;
    private boolean isVideoLock = false;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_equipment_dynamic);
        LogUtils.d("是否支持操作记录   ");
        ButterKnife.bind(this);
        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.philips_dynamic_record_message));
        tvOpenLockRecord.setOnClickListener(this);
        tvWarnInformation.setOnClickListener(this);
        tvVistorRecord.setOnClickListener(this);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);

    }

    private void initData() {
        if(!wifiSn.isEmpty()){
            Bundle bundle = new Bundle();
            bundle.putString(KeyConstants.WIFI_SN,wifiSn);
            if(isVideoLock){
                openRecordFragment = new PhilipsWifiLockOpenRecordFragment();
                openRecordFragment.setArguments(bundle);
                fragments.add(openRecordFragment);
                vistorRecordFragment = new PhilipsWifiLockVistorRecordFragment();
                vistorRecordFragment.setArguments(bundle);
                fragments.add(vistorRecordFragment);
                videoLockAlarmRecordFragment = new PhilipsWifiVideoLockAlarmRecordFragment();
                videoLockAlarmRecordFragment.setArguments(bundle);
                fragments.add(videoLockAlarmRecordFragment);
            }else {
                openRecordFragment = new PhilipsWifiLockOpenRecordFragment();
                openRecordFragment.setArguments(bundle);
                fragments.add(openRecordFragment);
                alarmRecordFragment = new WifiLockAlarmRecordFragment();
                alarmRecordFragment.setArguments(bundle);
                fragments.add(alarmRecordFragment);
            }
        }

        if (adapter == null) {
            adapter = new FragmentPagerAdapter(getSupportFragmentManager(),FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
                @Override
                public Fragment getItem(int i) {
                    return fragments.get(i);
                }

                @Override
                public long getItemId(int position) {
                    int hashCode = fragments.get(position).hashCode();
                    return hashCode;
                }

                @Override
                public int getItemPosition(@NonNull Object object) {
                    return POSITION_NONE;
                }

                @Override
                public int getCount() {
                    return fragments.size();
                }
            };
            viewPager.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(isVideoLock){
                    if(position == 0){
                        tvOpenLockRecord.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                        tvOpenLockRecord.setTextColor(getColor(R.color.c0066A1));
                        tvVistorRecord.setBackgroundResource(0);
                        tvVistorRecord.setTextColor(getColor(R.color.c999999));
                        tvWarnInformation.setTextColor(getColor(R.color.c999999));
                        tvWarnInformation.setBackgroundResource(0);
                        tvWarnInformation.setTextColor(getColor(R.color.c999999));
                    }else if(position == 1){
                        tvOpenLockRecord.setBackgroundResource(0);
                        tvOpenLockRecord.setTextColor(getColor(R.color.c999999));
                        tvWarnInformation.setBackgroundResource(0);
                        tvWarnInformation.setTextColor(getColor(R.color.c999999));
                        tvVistorRecord.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                        tvVistorRecord.setTextColor(getColor(R.color.c0066A1));
                    }else{
                        tvOpenLockRecord.setBackgroundResource(0);
                        tvOpenLockRecord.setTextColor(getColor(R.color.c999999));
                        tvVistorRecord.setBackgroundResource(0);
                        tvVistorRecord.setTextColor(getColor(R.color.c999999));
                        tvWarnInformation.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                        tvWarnInformation.setTextColor(getColor(R.color.c0066A1));
                    }
                }else{
                    if(position == 0){
                        tvOpenLockRecord.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                        tvOpenLockRecord.setTextColor(getColor(R.color.c0066A1));
                        tvVistorRecord.setBackgroundResource(0);
                        tvVistorRecord.setTextColor(getColor(R.color.c999999));
                        tvWarnInformation.setTextColor(getColor(R.color.c999999));
                        tvWarnInformation.setBackgroundResource(0);;
                        tvWarnInformation.setTextColor(getColor(R.color.c999999));
                    }else{
                        tvOpenLockRecord.setBackgroundResource(0);
                        tvOpenLockRecord.setTextColor(getColor(R.color.c999999));
                        tvVistorRecord.setBackgroundResource(0);
                        tvVistorRecord.setTextColor(getColor(R.color.c999999));
                        tvWarnInformation.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                        tvWarnInformation.setTextColor(getColor(R.color.c0066A1));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        if(MyApplication.getInstance().getWifiVideoLockTypeBySn(wifiSn) == HomeShowBean.TYPE_WIFI_VIDEO_LOCK){
            tvVistorRecord.setVisibility(View.VISIBLE);
            isVideoLock = true;
        }else{
            tvVistorRecord.setVisibility(View.GONE);
            isVideoLock = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        if(MyApplication.getInstance().getWifiVideoLockTypeBySn(wifiSn) == HomeShowBean.TYPE_WIFI_VIDEO_LOCK){
            tvVistorRecord.setVisibility(View.VISIBLE);
            isVideoLock = true;
        }else{
            tvVistorRecord.setVisibility(View.GONE);
            isVideoLock = false;
        }
        initView();
        initData();
        if(getIntent().getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_WANDERING_ALARM_PIR_FLAG,0) == 1){
            if(isVideoLock){
                viewPager.setCurrentItem(2);
                tvOpenLockRecord.setBackgroundResource(0);
                tvOpenLockRecord.setTextColor(getColor(R.color.c999999));
                tvVistorRecord.setBackgroundResource(0);
                tvVistorRecord.setTextColor(getColor(R.color.c999999));
                tvWarnInformation.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                tvWarnInformation.setTextColor(getColor(R.color.c0066A1));
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected WifiVideoLockRecordPresenter<IWifiLockVideoRecordView> createPresent() {
        return new WifiVideoLockRecordPresenter<>();
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction ;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_open_lock_record:
                //开锁记录
                tvOpenLockRecord.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                tvOpenLockRecord.setTextColor(getColor(R.color.c0066A1));
                tvVistorRecord.setBackgroundResource(0);
                tvVistorRecord.setTextColor(getColor(R.color.c999999));
                tvWarnInformation.setTextColor(getColor(R.color.c999999));
                tvWarnInformation.setBackgroundResource(0);
                tvWarnInformation.setTextColor(getColor(R.color.c999999));
                viewPager.setCurrentItem(0);
                break;
            case R.id.tv_warn_information:
                //警告信息
                tvOpenLockRecord.setBackgroundResource(0);
                tvOpenLockRecord.setTextColor(getColor(R.color.c999999));
                tvVistorRecord.setBackgroundResource(0);
                tvVistorRecord.setTextColor(getColor(R.color.c999999));
                tvWarnInformation.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                tvWarnInformation.setTextColor(getColor(R.color.c0066A1));
                if(isVideoLock){
                    viewPager.setCurrentItem(2);
                }else{
                    viewPager.setCurrentItem(1);
                }
                break;

            case R.id.tv_visitor_record:
                //访客记录
                tvOpenLockRecord.setBackgroundResource(0);
                tvOpenLockRecord.setTextColor(getColor(R.color.c999999));
                tvWarnInformation.setBackgroundResource(0);
                tvWarnInformation.setTextColor(getColor(R.color.c999999));
                tvVistorRecord.setBackgroundResource(R.drawable.philips_bg_line_0066a1_3);
                tvVistorRecord.setTextColor(getColor(R.color.c0066A1));
                viewPager.setCurrentItem(1);
                break;
            case R.id.iv_record_screen:
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

}
