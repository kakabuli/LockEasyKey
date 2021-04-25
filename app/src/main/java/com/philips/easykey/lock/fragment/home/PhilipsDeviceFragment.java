package com.philips.easykey.lock.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.PhilipsDeviceTypeAdapter;
import com.philips.easykey.lock.adapter.PhilipsRvHomeDeviceAdapter;
import com.philips.easykey.lock.adapter.PhilipsVpHomeDevicesAdapter;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.bean.PhilipsDeviceTypeBean;

import java.util.ArrayList;

/**
 * author :
 * time   : 2021/4/21
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsDeviceFragment extends Fragment {

    private PhilipsDeviceTypeAdapter mDeviceTypeAdapter;
    private LinearLayout mllNoDevice;
    private ViewPager mVPDevices;
    private RecyclerView mRvDevices;
    private TextView mTvCount, mTvCurrentPage;
    private ImageView mIvGrid, mIvList;

    private PhilipsVpHomeDevicesAdapter mVpHomeDevicesAdapter;
    private PhilipsRvHomeDeviceAdapter mRvHomeDeviceAdapter;
    private boolean isCardShow = true;

    private int mCurrentTab = 0;

    private final ArrayList<PhilipsDeviceBean> mAllDeviceBeans = new ArrayList<>();
    private final ArrayList<PhilipsDeviceBean> mWillShowDeviceBeans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_device, container, false);

        mllNoDevice = root.findViewById(R.id.llNoDevice);
        mTvCount = root.findViewById(R.id.tvCount);
        mTvCurrentPage = root.findViewById(R.id.tvCurrentPage);

        initDataViewFromCardType(root);
        initTab(root);
        initSelectBtn(root);
        initDataViewFromListType(root);

        return root;
    }

    private void initDataViewFromListType(View root) {
        mRvDevices = root.findViewById(R.id.rvDevices);
        mRvHomeDeviceAdapter = new PhilipsRvHomeDeviceAdapter(R.layout.philips_item_home_device_rv);
        mRvDevices.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvDevices.setAdapter(mRvHomeDeviceAdapter);
    }

    private void initSelectBtn(View root) {
        mIvGrid = root.findViewById(R.id.ivGrid);
        mIvList = root.findViewById(R.id.ivList);
        mIvGrid.setOnClickListener(v -> {
            isCardShow = true;
            showCardView();
        });
        mIvList.setOnClickListener(v -> {
            isCardShow = false;
            showListView();
        });
    }

    private void initDataViewFromCardType(View root) {
        mVPDevices = root.findViewById(R.id.vpDevices);
        mVPDevices.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvCurrentPage.setText(StringUtils.format("%1d", mWillShowDeviceBeans.size()==0?0:mVPDevices.getCurrentItem()+1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpHomeDevicesAdapter = new PhilipsVpHomeDevicesAdapter(getContext(), R.layout.philips_item_home_device_vp, mWillShowDeviceBeans);
        mVPDevices.setOffscreenPageLimit(4);
        mVPDevices.setPageTransformer(false, new PhilipsVpTransform());
        mVPDevices.setAdapter(mVpHomeDevicesAdapter);
    }

    private void initTab(View root) {
        mDeviceTypeAdapter = new PhilipsDeviceTypeAdapter(R.layout.philips_item_device_tap);
        mDeviceTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (PhilipsDeviceTypeBean bean : mDeviceTypeAdapter.getData()) {
                bean.setSelected(false);
            }
            mDeviceTypeAdapter.getData().get(position).setSelected(true);
            mCurrentTab = position;
            initCardData(position);
            mDeviceTypeAdapter.notifyDataSetChanged();
        });

        RecyclerView rvDeviceTap = root.findViewById(R.id.rvDeviceTap);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDeviceTap.setLayoutManager(layoutManager);
        rvDeviceTap.setAdapter(mDeviceTypeAdapter);
    }

    private void initCardData(int position) {
        if(position == 0) {
            changedWillShowDevice(0);
        } else if(position == 1) {
            changedWillShowDevice(1);
        } else if(position == 2) {
            changedWillShowDevice(2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initTabData();
        initDevices();
    }

    private void initTabData() {
        ArrayList<PhilipsDeviceTypeBean> list = new ArrayList<>();
        PhilipsDeviceTypeBean bean1 = new PhilipsDeviceTypeBean();
        bean1.setTypeName("所有设备");
        bean1.setSelected(true);
        list.add(bean1);
        PhilipsDeviceTypeBean bean2 = new PhilipsDeviceTypeBean();
        bean2.setTypeName("智能锁");
        bean2.setSelected(false);
        list.add(bean2);
        PhilipsDeviceTypeBean bean3 = new PhilipsDeviceTypeBean();
        bean3.setTypeName("晾衣机");
        bean3.setSelected(false);
        list.add(bean3);
        mDeviceTypeAdapter.setList(list);
    }

    private void initDevices() {
        PhilipsDeviceBean bean1 = new PhilipsDeviceBean();
        bean1.setDeviceName("视频锁1号");
        bean1.setLastRecordDetail("小明指纹00开锁");
        bean1.setLastRecordTime(1619075698000L);
        bean1.setDeviceType(1);
        mAllDeviceBeans.add(bean1);
        PhilipsDeviceBean bean2 = new PhilipsDeviceBean();
        bean2.setDeviceName("视频锁2号");
        bean2.setLastRecordDetail("小明指纹00开锁");
        bean2.setLastRecordTime(1619075798000L);
        bean2.setDeviceType(1);
        mAllDeviceBeans.add(bean2);
        PhilipsDeviceBean bean3 = new PhilipsDeviceBean();
        bean3.setDeviceName("晾衣机1号");
        bean3.setLastRecordDetail("小明指纹00开锁");
        bean3.setLastRecordTime(1619075898000L);
        bean3.setDeviceType(2);
        mAllDeviceBeans.add(bean3);
        PhilipsDeviceBean bean4 = new PhilipsDeviceBean();
        bean4.setDeviceName("晾衣机2号");
        bean4.setLastRecordDetail("小明指纹00开锁");
        bean4.setLastRecordTime(1619075998000L);
        bean4.setDeviceType(2);
        mAllDeviceBeans.add(bean4);
        PhilipsDeviceBean bean5 = new PhilipsDeviceBean();
        bean5.setDeviceName("晾衣机3号");
        bean5.setLastRecordDetail("小明指纹00开锁");
        bean5.setLastRecordTime(1619075998000L);
        bean5.setDeviceType(2);
        mAllDeviceBeans.add(bean5);
        PhilipsDeviceBean bean6 = new PhilipsDeviceBean();
        bean6.setDeviceName("晾衣机4号");
        bean6.setLastRecordDetail("小明指纹00开锁");
        bean6.setLastRecordTime(1619075998000L);
        bean6.setDeviceType(2);
        mAllDeviceBeans.add(bean6);
        mWillShowDeviceBeans.addAll(mAllDeviceBeans);
        mVpHomeDevicesAdapter.notifyDataSetChanged();
        mTvCurrentPage.setText(StringUtils.format("%1d", mWillShowDeviceBeans.size()==0?0:mVPDevices.getCurrentItem()+1));
        mTvCount.setText(getString(R.string.philips_device_count, mWillShowDeviceBeans.size()));
    }

    private void changedWillShowDevice(int type) {
        mWillShowDeviceBeans.clear();
        if(type == 0) {
            mWillShowDeviceBeans.addAll(mAllDeviceBeans);
        } else {
            for (PhilipsDeviceBean bean : mAllDeviceBeans) {
                if(bean.getDeviceType() == type) {
                    mWillShowDeviceBeans.add(bean);
                }
            }
        }
        if(isCardShow) {
            mVpHomeDevicesAdapter.notifyDataSetChanged();
            if(!mWillShowDeviceBeans.isEmpty()) {
                mVPDevices.setCurrentItem(0);
            }
            mTvCurrentPage.setText(StringUtils.format("%1d", mWillShowDeviceBeans.size()==0?0:mVPDevices.getCurrentItem()+1));
            mTvCount.setText(getString(R.string.philips_device_count, mWillShowDeviceBeans.size()));
        } else {
            mRvHomeDeviceAdapter.setList(mWillShowDeviceBeans);
        }

        LogUtils.d("changedWillShowDevice type: " + type + " size: " + mWillShowDeviceBeans.size());
    }

    private void showCardView() {
        if(getContext() == null) return;
        mIvList.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.philips_icon_list_default));
        mIvGrid.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.philips_icon_card_selected));
        mTvCurrentPage.setVisibility(View.VISIBLE);
        mTvCount.setVisibility(View.VISIBLE);
        mVPDevices.setVisibility(View.VISIBLE);
        mRvDevices.setVisibility(View.GONE);

        initCardData(mCurrentTab);
    }

    private void showListView() {
        if(getContext() == null) return;
        mIvList.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.philips_icon_list_selected));
        mIvGrid.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.philips_icon_card_default));
        mTvCurrentPage.setVisibility(View.GONE);
        mTvCount.setVisibility(View.GONE);
        mVPDevices.setVisibility(View.GONE);
        mRvDevices.setVisibility(View.VISIBLE);

        mRvHomeDeviceAdapter.setList(mWillShowDeviceBeans);
    }

}
