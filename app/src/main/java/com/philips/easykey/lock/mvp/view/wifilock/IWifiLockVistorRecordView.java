package com.philips.easykey.lock.mvp.view.wifilock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.xmitech.sdk.MP4Info;

import java.util.List;

public interface IWifiLockVistorRecordView extends IBaseView {


    /**
     * 从服务器获取开锁记录   page 请求的是第几页数据
     */
    void onLoadServerRecord(List<WifiVideoLockAlarmRecord> alarmRecords, int page);

    /**
     * 从服务器获取开锁记录失败
     */
    void onLoadServerRecordFailed(Throwable throwable);

    /**
     * 从服务器获取开锁记录失败 服务器返回错误码
     */
    void onLoadServerRecordFailedServer(BaseResult result);

    /**
     * 服务器没有数据   如果page
     */
    void onServerNoData();

    /**
     * 没有更多数据
     */
    void noMoreData();


}
