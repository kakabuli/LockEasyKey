package com.philips.easykey.lock.mvp.mvpbase;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.DisplayCutout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;

import com.philips.easykey.lock.utils.LoadingDialog;


/**
 * Create By lxj  on 2019/2/15
 * Describe
 */
public abstract class BaseFragment<T extends IBaseView, V
        extends BasePresenter<T>> extends Fragment implements IBaseView {

    protected V mPresenter;
    private LoadingDialog loadingDialog;
    private Handler bHandler = new Handler();



    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresent();
        mPresenter.attachView((T) this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    /**
     * 切换到了当前fragment
     */
    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * 切换到了当前fragment  且显示出来
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.detachView();
    }

    /**
     * 子类实现具体的构建过程
     *
     * @return
     */
    protected abstract V createPresent();

    @Override
    public void showLoading(String Content) {
        loadingDialog = LoadingDialog.getInstance(getContext());
        if (!getActivity().isFinishing()) {
            loadingDialog.show(Content);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.detachView();
    }


    @Override
    public void hiddenLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void showLoadingNoCancel(String content) {
        loadingDialog = LoadingDialog.getInstance(getContext());
        loadingDialog.setCancelable(false);
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 20;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            try {
                final View decorView = getActivity().getWindow().getDecorView();
                WindowInsets rootWindowInsets = decorView.getRootWindowInsets();
                if (rootWindowInsets == null) {
                    return result;
                }
                DisplayCutout displayCutout = rootWindowInsets.getDisplayCutout();
                result = displayCutout.getSafeInsetTop();
                return result;
            }catch (Exception e){
                // TODO: 2021/8/24 部分Android10 以上的手机 displayCutout.getSafeInsetTop()会报空指针
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = getResources().getDimensionPixelSize(resourceId);
                }
                return result;
            }
        }else {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = getResources().getDimensionPixelSize(resourceId);
            }
            return result;
        }
    }
}
