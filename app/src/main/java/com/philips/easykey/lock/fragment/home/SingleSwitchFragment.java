package com.philips.easykey.lock.fragment.home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.SingleSwitchTimerAdapter;
import com.philips.easykey.lock.bean.SingleSwitchTimerShowBean;

import java.util.ArrayList;
import java.util.List;


public class SingleSwitchFragment extends Fragment {


    ImageView ivSwitchState;
    TextView tvName;
    RecyclerView rvTimerList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_single_switch_layout, null);

        ivSwitchState = view.findViewById(R.id.iv_switch_state);
        tvName = view.findViewById(R.id.tv_name);
        rvTimerList = view.findViewById(R.id.rv_timer_list);

        return view;
    }

    private void initView(){
        List<SingleSwitchTimerShowBean> singleSwitchTimerShowBeans = new ArrayList<>();
        rvTimerList.setLayoutManager(new LinearLayoutManager(getContext()));
        SingleSwitchTimerAdapter adapter = new SingleSwitchTimerAdapter(singleSwitchTimerShowBeans);
        rvTimerList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
