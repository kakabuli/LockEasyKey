package com.philips.easykey.lock.fragment.help;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;

public class WifiVideoLockToConfigureHelpFragment extends Fragment {

    TextView tv_1;
    ImageView iv_1;
    TextView tv_2;
    ImageView iv_2;
    TextView tv_3;
    ImageView iv_3;
    TextView tv_4;
    ImageView iv_4;
    TextView tv_5;
    ImageView iv_5;
    TextView tv_6;
    ImageView iv_6;
    TextView tv_7;
    ImageView iv_7;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_video_lock_configure_help, null);

        tv_1 = view.findViewById(R.id.tv_1);
        iv_1 = view.findViewById(R.id.iv_1);
        tv_2 = view.findViewById(R.id.tv_2);
        iv_2 = view.findViewById(R.id.iv_2);
        tv_3 = view.findViewById(R.id.tv_3);
        iv_3 = view.findViewById(R.id.iv_3);
        tv_4 = view.findViewById(R.id.tv_4);
        iv_4 = view.findViewById(R.id.iv_4);
        tv_5 = view.findViewById(R.id.tv_5);
        iv_5 = view.findViewById(R.id.iv_5);
        tv_6 = view.findViewById(R.id.tv_6);
        iv_6 = view.findViewById(R.id.iv_6);
        tv_7 = view.findViewById(R.id.tv_7);
        iv_7 = view.findViewById(R.id.iv_7);

        view.findViewById(R.id.rl_1).setOnClickListener(v -> {
            if(iv_1.isSelected()){
                tv_1.setVisibility(View.GONE);
                iv_1.setSelected(false);
            }else{
                tv_1.setVisibility(View.VISIBLE);
                iv_1.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_2).setOnClickListener(v -> {
            if(iv_2.isSelected()){
                tv_2.setVisibility(View.GONE);
                iv_2.setSelected(false);
            }else{
                tv_2.setVisibility(View.VISIBLE);
                iv_2.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_3).setOnClickListener(v -> {
            if(iv_3.isSelected()){
                tv_3.setVisibility(View.GONE);
                iv_3.setSelected(false);
            }else{
                tv_3.setVisibility(View.VISIBLE);
                iv_3.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_4).setOnClickListener(v -> {
            if(iv_4.isSelected()){
                tv_4.setVisibility(View.GONE);
                iv_4.setSelected(false);
            }else{
                tv_4.setVisibility(View.VISIBLE);
                iv_4.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_5).setOnClickListener(v -> {
            if(iv_5.isSelected()){
                tv_5.setVisibility(View.GONE);
                iv_5.setSelected(false);
            }else{
                tv_5.setVisibility(View.VISIBLE);
                iv_5.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_6).setOnClickListener(v -> {
            if(iv_6.isSelected()){
                tv_6.setVisibility(View.GONE);
                iv_6.setSelected(false);
            }else{
                tv_6.setVisibility(View.VISIBLE);
                iv_6.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_7).setOnClickListener(v -> {
            if(iv_7.isSelected()){
                tv_7.setVisibility(View.GONE);
                iv_7.setSelected(false);
            }else{
                tv_7.setVisibility(View.VISIBLE);
                iv_7.setSelected(true);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
