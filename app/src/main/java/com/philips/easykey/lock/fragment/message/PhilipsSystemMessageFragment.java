package com.philips.easykey.lock.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.SystemMessageFragmentPresenter;
import com.philips.easykey.lock.mvp.view.ISystemMessageView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PhilipsSystemMessageFragment extends BaseFragment<ISystemMessageView, SystemMessageFragmentPresenter<ISystemMessageView>> implements ISystemMessageView {

    private View mView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.philips_fragment_system_message, container, false);
        }
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    protected SystemMessageFragmentPresenter<ISystemMessageView> createPresent() {
        return new SystemMessageFragmentPresenter<>();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
