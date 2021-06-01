package com.philips.easykey.lock.fragment.help;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.easykey.lock.R;


public class WifiVideoLockCommonProblemHelpFragment extends Fragment {

    LinearLayout lly_1;
    ImageView iv_1;
    LinearLayout lly_2;
    ImageView iv_2;
    LinearLayout lly_3;
    ImageView iv_3;
    LinearLayout lly_4;
    ImageView iv_4;
    LinearLayout lly_5;
    ImageView iv_5;
    LinearLayout lly_6;
    ImageView iv_6;
    LinearLayout lly_7;
    ImageView iv_7;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_video_lock_common_help, null);

        lly_1 = view.findViewById(R.id.lly_1);
        iv_1 = view.findViewById(R.id.iv_1);
        lly_2 = view.findViewById(R.id.lly_2);
        iv_2 = view.findViewById(R.id.iv_2);
        lly_3 = view.findViewById(R.id.lly_3);
        iv_3 = view.findViewById(R.id.iv_3);
        lly_4 = view.findViewById(R.id.lly_4);
        iv_4 = view.findViewById(R.id.iv_4);
        lly_5 = view.findViewById(R.id.lly_5);
        iv_5 = view.findViewById(R.id.iv_5);
        lly_6 = view.findViewById(R.id.lly_6);
        iv_6 = view.findViewById(R.id.iv_6);
        lly_7 = view.findViewById(R.id.lly_7);
        iv_7 = view.findViewById(R.id.iv_7);

        view.findViewById(R.id.rl_1).setOnClickListener(v -> {
            if(iv_1.isSelected()){
                lly_1.setVisibility(View.GONE);
                iv_1.setSelected(false);
            }else{
                lly_1.setVisibility(View.VISIBLE);
                iv_1.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_2).setOnClickListener(v -> {
            if(iv_2.isSelected()){
                lly_2.setVisibility(View.GONE);
                iv_2.setSelected(false);
            }else{
                lly_2.setVisibility(View.VISIBLE);
                iv_2.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_3).setOnClickListener(v -> {
            if(iv_3.isSelected()){
                lly_3.setVisibility(View.GONE);
                iv_3.setSelected(false);
            }else{
                lly_3.setVisibility(View.VISIBLE);
                iv_3.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_4).setOnClickListener(v -> {
            if(iv_4.isSelected()){
                lly_4.setVisibility(View.GONE);
                iv_4.setSelected(false);
            }else{
                lly_4.setVisibility(View.VISIBLE);
                iv_4.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_5).setOnClickListener(v -> {
            if(iv_5.isSelected()){
                lly_5.setVisibility(View.GONE);
                iv_5.setSelected(false);
            }else{
                lly_5.setVisibility(View.VISIBLE);
                iv_5.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_6).setOnClickListener(v -> {
            if(iv_6.isSelected()){
                lly_6.setVisibility(View.GONE);
                iv_6.setSelected(false);
            }else{
                lly_6.setVisibility(View.VISIBLE);
                iv_6.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_7).setOnClickListener(v -> {
            if(iv_7.isSelected()){
                lly_7.setVisibility(View.GONE);
                iv_7.setSelected(false);
            }else{
                lly_7.setVisibility(View.VISIBLE);
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
