package com.philips.easykey.lock.normal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.blankj.utilcode.util.ClickUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.ble.BleService;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttService;
import com.philips.easykey.lock.utils.LoadingDialog;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.StatusBarUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/10/24
 *     desc  : base about activity
 * </pre>
 */
public abstract class NormalBaseActivity extends BaseAddToApplicationActivity
        implements IBaseView {

    private final View.OnClickListener mClickListener = this::onDebouncingClick;
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    public MqttService mqttService = MyApplication.getInstance().getMqttService();
    public BleService bleService = MyApplication.getInstance().getBleService();

    public View     mContentView;
    public Activity mActivity;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        initData(getIntent().getExtras());
        setContentView();
        initView(savedInstanceState, mContentView);
        MyApplication.getInstance().addActivity(this);

        //第三种方法
        StatusBarUtils.with(this)
                .init();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initMQttAndBle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        doBusiness();
    }

    @Override
    protected void onStop() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void setContentView() {
        if (bindLayout() <= 0) return;
        mContentView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(mContentView);
    }

    private void initMQttAndBle() {
        if (mqttService == null) {
            LogUtils.d("mqttService  为空   异常情况  " + (MyApplication.getInstance().getMqttService() == null));
            mqttService = MyApplication.getInstance().getMqttService();
            LogUtils.d("mqttService  为" + mqttService);
        }
        if (bleService == null) {
            LogUtils.d("bleService  为空   异常情况  " + (MyApplication.getInstance().getBleService() == null));
            bleService = MyApplication.getInstance().getBleService();
//            MyApplication.getInstance().reStartApp();
            LogUtils.d("bleService 为 " + bleService);
        }
    }

    public void applyDebouncingClickListener(View... views) {
        ClickUtils.applyGlobalDebouncing(views, mClickListener);
        ClickUtils.applyPressedViewScale(views);
    }

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 375);
    }

    public void setStatusBarColor(@ColorRes int id) {
        Window window = getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, id));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void toDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    public void showLoading(String Content) {
        mLoadingDialog = LoadingDialog.getInstance(this);
        if (!isFinishing()) {
            mLoadingDialog.show(Content);
        }
    }

    public void hiddenLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    public void showLoadingNoCancel(String content) {
        mLoadingDialog = LoadingDialog.getInstance(this);
        mLoadingDialog.setCancelable(false);
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    /**
     * 点击输入框之外的地方  hidden软键盘
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if ((v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
    /**
     * 多种隐藏软件盘方法的其中一种
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
