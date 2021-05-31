package com.philips.easykey.lock.activity.device.wifilock.password;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddFaceFirstActivity;
import com.philips.easykey.lock.adapter.PhilipsWifiLockCardAndFingerAdapter;
import com.philips.easykey.lock.adapter.WifiLockFacePasswordAdapter;
import com.philips.easykey.lock.adapter.PhilipsWifiLockPasswordAdapter;
import com.philips.easykey.lock.bean.WiFiLockCardAndFingerShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockPasswordManagerPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockPasswordManagerView;
import com.philips.easykey.lock.publiclibrary.bean.FacePassword;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.publiclibrary.bean.WiFiLockPassword;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class PhilipsWiFiLockPasswordManagerActivity extends BaseActivity<IWifiLockPasswordManagerView, WifiLockPasswordManagerPresenter<IWifiLockPasswordManagerView>>
        implements IWifiLockPasswordManagerView {

    ImageView back;
    TextView headTitle;
    RecyclerView recycleview;
    TextView tvNoPassword;
    SmartRefreshLayout refreshLayout;
    LinearLayout llNoPassword;
    ImageView ivNoPassword;
    TextView tvHowToAdd;


    private PhilipsWifiLockPasswordAdapter passwordAdapter;
    private List<ForeverPassword> passwordList = new ArrayList<>();
    private WifiLockFacePasswordAdapter facepasswordAdapter;
    private List<FacePassword> facepasswordList = new ArrayList<>();
    private int type;  // 1 密码  2指纹  3 卡片 4 面容
    private List<WiFiLockCardAndFingerShowBean> cardAndFingerList = new ArrayList<>();
    private PhilipsWifiLockCardAndFingerAdapter wifiLockCardAndFingerAdapter;
    private boolean havePassword = false;
    private String wifiSn;
    private static final int REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wi_fi_lock_password_manager);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        recycleview = findViewById(R.id.recycleview);
        tvNoPassword = findViewById(R.id.tv_no_password);
        refreshLayout = findViewById(R.id.refreshLayout);
        llNoPassword = findViewById(R.id.ll_no_password);
        ivNoPassword = findViewById(R.id.iv_no_password);
        tvHowToAdd = findViewById(R.id.tv_how_to_add);

        back.setOnClickListener(v -> finish());
        tvHowToAdd.setOnClickListener(v -> {
            Intent nextIntent = new Intent(this, WifiLockAddFaceFirstActivity.class);
            startActivity(nextIntent);
        });

        type = getIntent().getIntExtra(KeyConstants.KEY_TYPE, 1);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        initView();
        initData();
        refreshLayout.autoRefresh();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initView() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新   如果正在同步，不刷新
                mPresenter.getPasswordList(wifiSn);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        String localPasswordCache = (String) SPUtils.get(KeyConstants.WIFI_LOCK_PASSWORD_LIST + wifiSn, "");
        WiFiLockPassword wiFiLockPassword = null;
        if (!TextUtils.isEmpty(localPasswordCache)) {
            wiFiLockPassword = new Gson().fromJson(localPasswordCache, WiFiLockPassword.class);
        }
        if (type == 1) {
            headTitle.setText(R.string.philips_share_permanent_password);
            passwordList = mPresenter.getShowPasswords(wiFiLockPassword);
            if (passwordList != null && passwordList.size() > 0) {
                havePassword = true;
            } else {
                havePassword = false;
            }
            ivNoPassword.setImageResource(R.mipmap.wifi_lock_no_password);
            tvNoPassword.setText(getString(R.string.no_pwd_sync));
            tvHowToAdd.setVisibility(View.INVISIBLE);//不显示
            initPasswordAdapter();
        } else if (type == 2) {
            headTitle.setText(R.string.fingerprint);
            tvNoPassword.setText(getString(R.string.no_finger_sync));
            cardAndFingerList = mPresenter.getShowCardsFingers(wiFiLockPassword, type);
            if (cardAndFingerList != null && cardAndFingerList.size() > 0) {
                havePassword = true;
            } else {
                havePassword = false;
            }
            ivNoPassword.setImageResource(R.mipmap.wifi_lock_no_finger);
            tvHowToAdd.setVisibility(View.INVISIBLE);//不显示
            initCardAndFingerAdapter();
        } else if (type == 3) {
            headTitle.setText(R.string.philips_rfid);
            cardAndFingerList = mPresenter.getShowCardsFingers(wiFiLockPassword, type);
            if (cardAndFingerList != null && cardAndFingerList.size() > 0) {
                havePassword = true;
            } else {
                havePassword = false;
            }
            ivNoPassword.setImageResource(R.mipmap.wifi_lock_no_card);
            tvNoPassword.setText(getString(R.string.no_card_sync));
            tvHowToAdd.setVisibility(View.INVISIBLE);//不显示
            initCardAndFingerAdapter();
        }
        else if (type == 4) {
            headTitle.setText(R.string.face_password);
            facepasswordList = mPresenter.getShowFacePasswords(wiFiLockPassword);
            if (facepasswordList != null && facepasswordList.size() > 0) {
                havePassword = true;
            } else {
                havePassword = false;
            }
            ivNoPassword.setImageResource(R.mipmap.wifi_lock_no_face_pwd);
            tvNoPassword.setText(getString(R.string.no_face_password));
            tvHowToAdd.setText(getString(R.string.how_to_add_face_password));
            initFacePasswordAdapter();
        }
        changeState();
    }

    private void changeState() {
        if (havePassword) {
            recycleview.setVisibility(View.VISIBLE);
            llNoPassword.setVisibility(View.GONE);
        } else {
            recycleview.setVisibility(View.GONE);
            llNoPassword.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected WifiLockPasswordManagerPresenter<IWifiLockPasswordManagerView> createPresent() {
        return new WifiLockPasswordManagerPresenter<>();
    }

    private void initPasswordAdapter() {
        passwordAdapter = new PhilipsWifiLockPasswordAdapter(passwordList, R.layout.philips_item_password);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(passwordAdapter);
        passwordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(PhilipsWiFiLockPasswordManagerActivity.this, PhilipsWifiLockPasswordDetailActivity.class);
                ForeverPassword foreverPassword = passwordList.get(position);
                //输入密码类型
                intent.putExtra(KeyConstants.PASSWORD_TYPE, type);
                intent.putExtra(KeyConstants.TO_PWD_DETAIL, foreverPassword);  //密码数据
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);  //WiFiSN
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void initCardAndFingerAdapter() {
        wifiLockCardAndFingerAdapter = new PhilipsWifiLockCardAndFingerAdapter(cardAndFingerList, R.layout.philips_item_door_card_manager);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(wifiLockCardAndFingerAdapter);
        wifiLockCardAndFingerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(PhilipsWiFiLockPasswordManagerActivity.this, PhilipsWifiLockPasswordDetailActivity.class);
                WiFiLockCardAndFingerShowBean wiFiLockCardAndFingerShowBean = cardAndFingerList.get(position);
                //输入密码类型
                intent.putExtra(KeyConstants.PASSWORD_TYPE, type);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);  //WiFiSN
                intent.putExtra(KeyConstants.TO_PWD_DETAIL, wiFiLockCardAndFingerShowBean);  //密码数据
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    private void initFacePasswordAdapter() {
        facepasswordAdapter = new WifiLockFacePasswordAdapter(facepasswordList, R.layout.item_bluetooth_password);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(facepasswordAdapter);
        facepasswordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(PhilipsWiFiLockPasswordManagerActivity.this, PhilipsWifiLockPasswordDetailActivity.class);
                FacePassword facePassword = facepasswordList.get(position);
                //输入密码类型
                intent.putExtra(KeyConstants.PASSWORD_TYPE, type);
                intent.putExtra(KeyConstants.TO_PWD_DETAIL, facePassword);  //密码数据
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);  //WiFiSN
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }
    @Override
    public void onGetPasswordSuccess(WiFiLockPassword wiFiLockPassword) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
        initData();
    }

    @Override
    public void onGetPasswordFailedServer(BaseResult baseResult) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
        ToastUtils.showLong(R.string.refresh_failed_please_retry_later);
    }

    @Override
    public void onGetPasswordFailed(Throwable throwable) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
        ToastUtils.showLong(R.string.refresh_failed_please_retry_later);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            mPresenter.getPasswordList(wifiSn);
        }
    }

}
