package com.philips.easykey.lock.fragment.help;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;


public class PersonalFAQHangerHelpFragment extends Fragment {

    TextView lly_1;
    ImageView iv_1;
    TextView lly_2;
    ImageView iv_2;
    TextView lly_3;
    ImageView iv_3;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_faq_hanger_help, null);

        lly_1 = view.findViewById(R.id.tv_1);
        iv_1 = view.findViewById(R.id.iv_1);
        lly_2 = view.findViewById(R.id.tv_2);
        iv_2 = view.findViewById(R.id.iv_2);
        lly_3 = view.findViewById(R.id.tv_3);
        iv_3 = view.findViewById(R.id.iv_3);

        view.findViewById(R.id.rl_1).setOnClickListener(v -> {
            if (iv_1.isSelected()) {
                lly_1.setVisibility(View.GONE);
                iv_1.setSelected(false);
            } else {
                lly_1.setVisibility(View.VISIBLE);
                iv_1.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_2).setOnClickListener(v -> {
            if (iv_2.isSelected()) {
                lly_2.setVisibility(View.GONE);
                iv_2.setSelected(false);
            } else {
                lly_2.setVisibility(View.VISIBLE);
                iv_2.setSelected(true);
            }
        });
        view.findViewById(R.id.rl_3).setOnClickListener(v -> {
            if (iv_3.isSelected()) {
                lly_3.setVisibility(View.GONE);
                iv_3.setSelected(false);
            } else {
                lly_3.setVisibility(View.VISIBLE);
                iv_3.setSelected(true);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
