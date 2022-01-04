package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.clotheshangermachinepresenter.ClothesHangerMachineAddPresenter;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineAddView;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.clothesHangerMachineUtil.ClothesHangerMachineUtil;
import com.philips.easykey.lock.utils.dialog.MessageDialog;


public class ClothesHangerMachineAddActivity extends BaseActivity<IClothesHangerMachineAddView, ClothesHangerMachineAddPresenter<IClothesHangerMachineAddView>>
        implements IClothesHangerMachineAddView {

    LinearLayout wifi_lock_choose_to_scan;
    EditText wifi_lock_choose_to_input;
    ImageView back;
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

        wifi_lock_choose_to_scan = findViewById(R.id.wifi_lock_choose_to_scan);
        wifi_lock_choose_to_input = findViewById(R.id.wifi_lock_choose_to_input);
        back = findViewById(R.id.back);
        add = findViewById(R.id.add);

        back.setOnClickListener(v -> finish());
        add.setOnClickListener(v -> {
            String name = wifi_lock_choose_to_input.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShort(getString(R.string.not_empty));
                return;
            }
            if (!StringUtil.nicknameJudge(name)) {
                ToastUtils.showShort(getString(R.string.nickname_verify_error));
                return;
            }
            mPresenter.searchClothesMachine(name);
        });
        wifi_lock_choose_to_scan.setOnClickListener(v -> {
            Intent scanIntent = new Intent(this, ClothesHangerMachineQrCodeScanActivity.class);
            scanIntent.putExtra(KeyConstants.SCAN_TYPE, 1);
            startActivityForResult(scanIntent, KeyConstants.SCANGATEWAYNEW_REQUEST_CODE);
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case KeyConstants.SCANGATEWAYNEW_REQUEST_CODE:
                    String result = data.getStringExtra(Constants.SCAN_QR_CODE_RESULT);
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
