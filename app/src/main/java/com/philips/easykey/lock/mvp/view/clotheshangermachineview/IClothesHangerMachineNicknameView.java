package com.philips.easykey.lock.mvp.view.clotheshangermachineview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.MultiCheckOTAResult;

import java.util.List;

public interface IClothesHangerMachineNicknameView extends IBaseView {

    void onSettingNicknameSuccess();

    void onSettingNicknameFailed(BaseResult result);

    void onSettingNicknameThrowable(Throwable throwable);
}
