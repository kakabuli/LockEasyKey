package com.philips.easykey.lock.mvp.view.clotheshangermachineview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.MultiCheckOTAResult;

import java.util.List;

public interface IClothesHangerMachineDetailView extends IBaseView {

    void needUpdate(List<MultiCheckOTAResult.UpgradeTask> upgradeTasks);

    void noUpdate();

    void checkUpdateFailed(BaseResult baseResult);

    void checkUpdateThrowable(Throwable throwable);

    void updateSuccess();

    void updateFailed(BaseResult baseResult);

    void updateThrowable(Throwable throwable);

    void onDeleteDeviceSuccess();

    void onDeleteDeviceFailed(BaseResult result);

    void onDeleteDeviceThrowable(Throwable throwable);
}
