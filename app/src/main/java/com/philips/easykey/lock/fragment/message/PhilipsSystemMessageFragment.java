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


public class PhilipsSystemMessageFragment extends BaseFragment<ISystemMessageView, SystemMessageFragmentPresenter<ISystemMessageView>> implements ISystemMessageView {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.philips_fragment_system_message, container, false);
        }
        return mView;
    }

    @Override
    protected SystemMessageFragmentPresenter<ISystemMessageView> createPresent() {
        return new SystemMessageFragmentPresenter<>();
    }

}
