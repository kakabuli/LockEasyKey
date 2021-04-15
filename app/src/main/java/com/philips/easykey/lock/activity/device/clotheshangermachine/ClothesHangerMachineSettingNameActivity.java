package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.clotheshangermachinepresenter.ClothesHangerMachineNicknamePresenter;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineNicknameView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.greenDao.bean.ClothesHangerMachineAllBean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClothesHangerMachineSettingNameActivity extends BaseActivity<IClothesHangerMachineNicknameView, ClothesHangerMachineNicknamePresenter<IClothesHangerMachineNicknameView>>
        implements IClothesHangerMachineNicknameView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.et_nick_name)
    EditText etNickname;

    private String wifiSN = "";
    private ClothesHangerMachineAllBean hangerInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_nickname);
        ButterKnife.bind(this);

        wifiSN = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        hangerInfo = MyApplication.getInstance().getClothesHangerMachineBySn(wifiSN);
        if(hangerInfo != null){
            etNickname.setHint(hangerInfo.getHangerNickName() + "");
        }

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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected ClothesHangerMachineNicknamePresenter<IClothesHangerMachineNicknameView> createPresent() {
        return new ClothesHangerMachineNicknamePresenter<>();
    }

    @OnClick({R.id.back,R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_save:
                if(!etNickname.getText().toString().trim().isEmpty()){
                    mPresenter.setNickname(wifiSN,etNickname.getText().toString().trim());
                }
                break;
        }
    }

    @Override
    public void onSettingNicknameSuccess() {
        showLoading(getString(R.string.modify_success));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent(ClothesHangerMachineSettingNameActivity.this,ClothesHangerMachineDetailActivity.class);
                data.putExtra("hanger_nick_name",etNickname.getText().toString().trim());
                LogUtils.e("shulan hanger_nick_name-----> " +etNickname.getText().toString().trim());
                setResult(RESULT_OK,data);
                hiddenLoading();
                finish();
            }
        },800);

    }

    @Override
    public void onSettingNicknameFailed(BaseResult result) {
        showLoading(getString(R.string.modify_failed));
    }

    @Override
    public void onSettingNicknameThrowable(Throwable throwable) {

    }
}
