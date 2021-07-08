package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsDayResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsSevenDayResult;

public interface IDoorLockMessageView extends IBaseView {

    /**
     * 锁状态更新
     */
    void onWifiLockActionUpdate();

    /**
     * 获取当天门锁统计
     * @param getStatisticsDayResult
     */
    void getDtatisticsDay(GetStatisticsDayResult getStatisticsDayResult);

    /**
     * 获取七天天门锁统计
     * @param getStatisticsSevenDayResult
     */
    void getDtatisticsSevenDay(GetStatisticsSevenDayResult getStatisticsSevenDayResult);
}
