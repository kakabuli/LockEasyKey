package com.philips.easykey.lock.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.login.PhilipsLoginActivity;


/**
 * Created by David on 2019/4/15
 */
public class PhilipsGuidePageOneFragment extends Fragment implements View.OnClickListener {

    TextView tvSkip;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.philips_fragment_guide_page_one, container, false);
        }

        tvSkip = mView.findViewById(R.id.tv_skip);

        tvSkip.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_skip:
                startActivity(new Intent(getActivity(), PhilipsLoginActivity.class));
                getActivity().finish();
                break;
        }
    }
}
