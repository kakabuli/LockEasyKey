package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.clotheshangermachinepresenter.ClothesHangerMachineAddSecondPresenter;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineAddSecondView;
import com.philips.easykey.lock.utils.AlertDialogUtil;


public class ClothesHangerMachineAddSecondActivity extends BaseActivity<IClothesHangerMachineAddSecondView,
        ClothesHangerMachineAddSecondPresenter<IClothesHangerMachineAddSecondView>> implements IClothesHangerMachineAddSecondView {

    TextView button_next;
    ImageView back;

    private String wifiModelType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_add_second);

        button_next = findViewById(R.id.button_next);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> finish());
        button_next.setOnClickListener(v -> {
            //检查蓝牙是否打开
            if(mPresenter.isBluetoothEnable()){
                Intent intent = new Intent(this,ClothesHangerMachineAddThirdActivity.class);
                intent.putExtra("wifiModelType",wifiModelType);
                startActivity(intent);
                finish();
            }else{
                showBluetoothEnableDialog();
            }
        });

        wifiModelType = getIntent().getStringExtra("wifiModelType") + "";
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected ClothesHangerMachineAddSecondPresenter<IClothesHangerMachineAddSecondView> createPresent() {
        return new ClothesHangerMachineAddSecondPresenter<>();
    }

    private void showBluetoothErrorDialog() {
        AlertDialogUtil.getInstance().noEditTitleOneButtonDialog(
                ClothesHangerMachineAddSecondActivity.this,
                getString(R.string.set_failed),
                getString(R.string.philips_confirm), "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                    }

                    @Override
                    public void right() {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void showBluetoothEnableDialog() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                ClothesHangerMachineAddSecondActivity.this,
                getString(R.string.philips_activity_clothes_hanger_machine_add_dialog_4),
                getString(R.string.close), getString(R.string.settting),
                "#9A9A9A", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        mPresenter.enableBle();
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    @Override
    public void openBluetoothStatus(boolean flag) {
        if(!flag){
            showBluetoothErrorDialog();
        }
    }

}
