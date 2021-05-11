package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.clotheshangermachinepresenter.ClothesHangerMachineAddSecondPresenter;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineAddSecondView;
import com.philips.easykey.lock.utils.AlertDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClothesHangerMachineAddSecondActivity extends BaseActivity<IClothesHangerMachineAddSecondView,
        ClothesHangerMachineAddSecondPresenter<IClothesHangerMachineAddSecondView>> implements IClothesHangerMachineAddSecondView {

    @BindView(R.id.button_next)
    TextView button_next;
    @BindView(R.id.back)
    ImageView back;

    private String wifiModelType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_add_second);
        ButterKnife.bind(this);

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

    @OnClick({R.id.button_next,R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.button_next:
                //检查蓝牙是否打开
                if(mPresenter.isBluetoothEnable()){
                    Intent intent = new Intent(this,ClothesHangerMachineAddThirdActivity.class);
                    intent.putExtra("wifiModelType",wifiModelType);
                    startActivity(intent);
                    finish();
                }else{
                    showBluetoothEnableDialog();
                }
                break;
        }
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
