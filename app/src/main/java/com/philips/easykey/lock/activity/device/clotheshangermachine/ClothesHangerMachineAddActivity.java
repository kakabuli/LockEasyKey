package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.clotheshangermachinepresenter.ClothesHangerMachineAddPresenter;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineAddView;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.clothesHangerMachineUtil.ClothesHangerMachineUtil;
import com.philips.easykey.lock.utils.dialog.MessageDialog;
import com.king.zxing.Intents;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClothesHangerMachineAddActivity extends BaseActivity<IClothesHangerMachineAddView, ClothesHangerMachineAddPresenter<IClothesHangerMachineAddView>>
        implements IClothesHangerMachineAddView {

    @BindView(R.id.wifi_lock_choose_to_scan)
    LinearLayout wifi_lock_choose_to_scan;
    @BindView(R.id.wifi_lock_choose_to_input)
    EditText wifi_lock_choose_to_input;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.add)
    TextView add;

    private MessageDialog messageDialog;

    @Override
    protected ClothesHangerMachineAddPresenter<IClothesHangerMachineAddView> createPresent() {
        return new ClothesHangerMachineAddPresenter<>() ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_choose_and_scan);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.back,R.id.add,R.id.wifi_lock_choose_to_scan})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.add:
                String name = wifi_lock_choose_to_input.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShort(R.string.not_empty);
                    return;
                }
                if (!StringUtil.nicknameJudge(name)) {
                    ToastUtils.showShort(R.string.nickname_verify_error);
                    return;
                }
                mPresenter.searchClothesMachine(name);
                break;
            case R.id.wifi_lock_choose_to_scan:
                Intent scanIntent = new Intent(this, ClothesHangerMachineQrCodeScanActivity.class);
                scanIntent.putExtra(KeyConstants.SCAN_TYPE, 1);
                startActivityForResult(scanIntent, KeyConstants.SCANGATEWAYNEW_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case KeyConstants.SCANGATEWAYNEW_REQUEST_CODE:
                    String result = data.getStringExtra(Intents.Scan.RESULT);
                    LogUtils.d("扫描结果是   " + result);
                    if(result.contains("_WiFi&BLE_")){
                        String[] str = result.split("_");
                        if(str.length > 0){
                            if(str.length >= 4 && result.contains("SmartHanger")){
                                if(ClothesHangerMachineUtil.pairMode(str[1]).equals(str[2])){
                                    Intent clothesMachineIntent = new Intent(this, ClothesHangerMachineAddFirstActivity.class);
                                    clothesMachineIntent.putExtra("wifiModelType",str[2]);
                                    startActivity(clothesMachineIntent);
                                    return;
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void searchClothesMachineSuccessForWiFi(String pairMode) {

    }

    @Override
    public void searchClothesMachineSuccessForWiFiAndBLE(String pairMode) {
        Intent clothesMachineIntent = new Intent(this, ClothesHangerMachineAddFirstActivity.class);
        clothesMachineIntent.putExtra("wifiModelType","WiFi&BLE");
        startActivity(clothesMachineIntent);
    }

    @Override
    public void searchClothesMachineThrowable() {
        //信息
        messageDialog = new MessageDialog.Builder(this)
                .setMessage(R.string.input_right_clothes_machine_or_scan)
                .create();
        messageDialog.show();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(messageDialog != null){
                    messageDialog.dismiss();

                }
            }
        }, 3000); //延迟3秒消失
    }
}
