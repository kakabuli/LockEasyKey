package com.philips.easykey.lock.fragment.device;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;
import com.philips.easykey.lock.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by asqw1 on 2018/3/14.
 */

public class DeviceTotalFragment extends Fragment {

    RadioButton rbDevice;
    RadioButton rvScene;
    RadioGroup rgDevice;
    NoScrollViewPager vpDevice;

    private View mView;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_device_total, container, false);
        }

        rbDevice = mView.findViewById(R.id.rb_device);
        rvScene = mView.findViewById(R.id.rv_scene);
        rgDevice = mView.findViewById(R.id.rg_device);
        vpDevice = mView.findViewById(R.id.vp_device);

        rbDevice.setOnClickListener(v -> rgDevice.check(R.id.rb_device));
        rvScene.setOnClickListener(v -> rgDevice.check(R.id.rv_scene));
        mView.findViewById(R.id.iv_add_device).setOnClickListener(v -> {
            if (vpDevice.getCurrentItem() == 0){
                //添加设备
                startActivity(new Intent(getContext(),DeviceAdd2Activity.class));
            }else if (vpDevice.getCurrentItem() == 1){
                //添加场景
            }
        });

        fragments.add(new DeviceFragment());
        fragments.add(new SceneFragment());
        rbDevice.setOnCheckedChangeListener((compoundButton, b) -> {
            switch (compoundButton.getId()){
                case R.id.rb_device:
                    vpDevice.setCurrentItem(0);
                    break;
                case R.id.rv_scene:
                    vpDevice.setCurrentItem(1);
                    break;
            }
        });
        vpDevice.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public Fragment getItem(int i) {
                return fragments.get(i);
            }
        });
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
