package com.philips.easykey.lock.activity.device.wifilock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.bean.DateBean;
import com.bigkoo.pickerview.listener.OnPagerChangeListener;
import com.bigkoo.pickerview.listener.OnSingleChooseListener;
import com.bigkoo.pickerview.utils.CalendarUtil;
import com.bigkoo.pickerview.view.CalendarView;
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
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class PhilipsWifiLockRecordActivity extends BaseActivity<IWifiLockVideoRecordView,
        WifiVideoLockRecordPresenter<IWifiLockVideoRecordView>> implements IWifiLockVideoRecordView,View.OnClickListener  {

    ImageView ivBack;
    TextView tvContent;
    TextView tvOpenLockRecord;
    TextView tvWarnInformation;
    TextView tvVistorRecord;
    ViewPager viewPager;
    ImageView mIvRecordScreen;
    PhilipsWifiLockOpenRecordFragment openRecordFragment;
    WifiLockAlarmRecordFragment alarmRecordFragment;
    PhilipsWifiLockVistorRecordFragment vistorRecordFragment;
    PhilipsWifiVideoLockAlarmRecordFragment videoLockAlarmRecordFragment;
    private String wifiSn;
    private boolean isVideoLock = false;
    private List<Fragment> fragments = new ArrayList<>();
    private FragmentPagerAdapter adapter;
    private Dialog mBottomCalendarDialog;
    private DateBean screenDateBean ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_equipment_dynamic);
        LogUtils.d("是否支持操作记录   ");

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        tvOpenLockRecord = findViewById(R.id.tv_open_lock_record);
        tvWarnInformation = findViewById(R.id.tv_warn_information);
        tvVistorRecord = findViewById(R.id.tv_visitor_record);
        viewPager = findViewById(R.id.vp_home);
        mIvRecordScreen = findViewById(R.id.iv_record_screen);

        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.philips_dynamic_record_message));
        tvOpenLockRecord.setOnClickListener(this);
        tvWarnInformation.setOnClickListener(this);
        tvVistorRecord.setOnClickListener(this);
        mIvRecordScreen.setOnClickListener(this);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        screenDateBean = null;
    }

    private void initData() {
        if(!wifiSn.isEmpty()){
            Bundle bundle = new Bundle();
            bundle.putString(KeyConstants.WIFI_SN,wifiSn);
            if(isVideoLock){
                if(openRecordFragment == null){
                    openRecordFragment = new PhilipsWifiLockOpenRecordFragment();
                }
                openRecordFragment.setArguments(bundle);
                fragments.add(openRecordFragment);
                if(vistorRecordFragment == null){
                    vistorRecordFragment = new PhilipsWifiLockVistorRecordFragment();
                }
                vistorRecordFragment.setArguments(bundle);
                fragments.add(vistorRecordFragment);
                if(videoLockAlarmRecordFragment == null){
                    videoLockAlarmRecordFragment = new PhilipsWifiVideoLockAlarmRecordFragment();
                }
                videoLockAlarmRecordFragment.setArguments(bundle);
                fragments.add(videoLockAlarmRecordFragment);
            }else {
                if(openRecordFragment == null){
                    openRecordFragment = new PhilipsWifiLockOpenRecordFragment();
                }
                openRecordFragment.setArguments(bundle);
                fragments.add(openRecordFragment);
                if(alarmRecordFragment == null){
                    alarmRecordFragment = new WifiLockAlarmRecordFragment();
                }
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

                @Override
                public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                    //super.destroyItem(container, position, object);
                }
            };
            viewPager.setAdapter(adapter);
            if(isVideoLock){
                viewPager.setOffscreenPageLimit(3);
            }
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
                showBottomCalendarDialog();
                break;
        }
    }

   private void showBottomCalendarDialog(){
       if(mBottomCalendarDialog == null){
           mBottomCalendarDialog = new Dialog(this, R.style.DialogTransparent);
       }
       int[] cDate = CalendarUtil.getCurrentDate();
       View contentView = LayoutInflater.from(this).inflate(R.layout.philips_calendar_dialog, null);
       CalendarView calendarView = contentView.findViewById(R.id.calendar);
       TextView title = contentView.findViewById(R.id.title);
       ImageView ivLast = contentView.findViewById(R.id.ivLast);
       ImageView ivNext = contentView.findViewById(R.id.ivNext);
       if(screenDateBean !=  null){
           calendarView.setInitDate(screenDateBean.getSolar()[0] + "." + screenDateBean.getSolar()[1])
                   .setSingleDate(screenDateBean.getSolar()[0] + "." + screenDateBean.getSolar()[1] + "." + screenDateBean.getSolar()[2])
                   .init();
           title.setText(screenDateBean.getSolar()[0] + "年" + screenDateBean.getSolar()[1] + "月");
       }else {
           calendarView.setInitDate(cDate[0] + "." + cDate[1])
                   .setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                   .init();
           title.setText(cDate[0] + "年" + cDate[1] + "月");
       }
       ivLast.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               calendarView.lastMonth();
           }
       });
       ivNext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               calendarView.nextMonth();
           }
       });
       calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
           @Override
           public void onPagerChanged(int[] date) {
               title.setText(date[0] + "年" + date[1] + "月");
           }
       });
       calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
           @Override
           public void onSingleChoose(View view, DateBean dateBean) {
               mBottomCalendarDialog.dismiss();
               mBottomCalendarDialog = null;
               screenDateBean = dateBean;
               String date  = dateBean.getSolar()[0] + "-" + dateBean.getSolar()[1] + "-" + dateBean.getSolar()[2];
               getScreenedRecord(date);
           }
       });
       mBottomCalendarDialog.setContentView(contentView);
       ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
       layoutParams.width = getResources().getDisplayMetrics().widthPixels;
       contentView.setLayoutParams(layoutParams);
       mBottomCalendarDialog.getWindow().setGravity(Gravity.BOTTOM);
       mBottomCalendarDialog.setCanceledOnTouchOutside(true);
       mBottomCalendarDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
       mBottomCalendarDialog.show();
   }

   private void getScreenedRecord(String date){
       if(openRecordFragment != null){
           openRecordFragment.getOpenScreenedRecordFromServer(true , date);
       }
       if(vistorRecordFragment != null){
           vistorRecordFragment.getWifiVideoLockGetDoorbellFilterList(true , date);
       }
       if(videoLockAlarmRecordFragment != null){
           videoLockAlarmRecordFragment.getWifiVideoLockGetAlarmFilterList(true ,date);
       }
   }

    @Override
    public void finish() {
        super.finish();
    }

}
