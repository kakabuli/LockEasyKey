package com.philips.easykey.lock.publiclibrary.http;

import android.util.Log;

import androidx.annotation.NonNull;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.bean.PhoneMessage;
import com.philips.easykey.lock.bean.PushBean;
import com.philips.easykey.lock.bean.PushSwitch;
import com.philips.easykey.lock.bean.PushSwitchBean;
import com.philips.easykey.lock.bean.VersionBean;
import com.philips.easykey.lock.publiclibrary.bean.TmallDeviceListBean;
import com.philips.easykey.lock.publiclibrary.ble.bean.WarringRecord;
import com.philips.easykey.lock.publiclibrary.http.postbean.AddDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.AddPasswordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.AddUserBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.AllBindDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.CodeLoginBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.DoorLockStatisticsBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetWeChatOpenIdBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetWeChatUserPhoneBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.HangerMultiOTABean;
import com.philips.easykey.lock.publiclibrary.http.postbean.HangerUpgradeMultiOTABean;
import com.philips.easykey.lock.publiclibrary.http.postbean.HttpAllBindDevicesBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ClothesHangerMachineDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.DeleteDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.DeleteMessageBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.DeletePasswordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.DeleteUserBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ForgetPasswordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetDevicesBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetHelpLogBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetLockRecordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetMessageBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetOpenCountBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetOperationRecordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetPasswordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetPwdBySNBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetSinglePasswordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetUserNickBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetUserProtocolContentBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.GetWarringRecordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.HangerUpdateNickNameBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.LoginByEmailBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.LoginByPhoneBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ModifyFunctionSetBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ModifyLockNickBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ModifyPasswordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ModifyPasswordNickBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ModifySwitchNickBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ModifyUserNickBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.MultiOTABean;
import com.philips.easykey.lock.publiclibrary.http.postbean.OTABean;
import com.philips.easykey.lock.publiclibrary.http.postbean.PutMessageBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.RegisterByPhoneBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.RegisterWeChatAndBindPhoneBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ResetDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.SearchUSerBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.SendEmailBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.SendMessageBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.SettingPwdDuressAccountBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.SettingPwdDuressAlarmBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.SettingPwdDuressAlarmSwitchBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.TmallAddDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.TmallDelDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.TmallDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.TmallQueryDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UpdateBleVersionBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UpdateSoftwareVersionBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UpgradeMultiOTABean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UploadAppRecordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UploadBinRecordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UploadOperationRecordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UploadOtaBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UploadOtaResultBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UploadWarringRecordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WeChatLoginBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockBindBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockVideoBindBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockVideoUpdateBindBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockViewBindFailBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockWifiSNAndUid;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockUpdateNickNameBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockDeleteShareBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockRecordBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockRecordScreenedBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockShareBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockUpdateInfoBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockUpdatePushSwitchBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockUpdatePwdNickBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WifiLockUpdateShareNickBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.ZigBeenInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.result.ClothesHangerMachineBindResult;
import com.philips.easykey.lock.publiclibrary.http.result.ClothesHangerMachineCheckBindingResult;
import com.philips.easykey.lock.publiclibrary.http.result.ClothesHangerMachineUnBindResult;
import com.philips.easykey.lock.publiclibrary.http.result.DeleteMessageResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetDeviceResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetHelpLogResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetOpenCountResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetPasswordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetProductionModelListResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetPwdBySnResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsDayResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsSevenDayResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWarringRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWeChatOpenIdResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWeChatUserPhoneResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiLockAlarmRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiLockOperationRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiLockOperationScreenedRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiVideoLockAlarmRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiVideoLockAlarmScreenedRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiVideoLockDoorbellScreenedRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.LockRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.LoginResult;
import com.philips.easykey.lock.publiclibrary.http.result.MultiCheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.result.OperationRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.RegisterResult;
import com.philips.easykey.lock.publiclibrary.http.result.RegisterWeChatAndBindPhoneResult;
import com.philips.easykey.lock.publiclibrary.http.result.SinglePasswordResult;
import com.philips.easykey.lock.publiclibrary.http.result.SwitchStatusResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallAddDeviceResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallDeviceListResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallQueryDeviceListResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserNickResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserProtocolResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserProtocolVersionResult;
import com.philips.easykey.lock.publiclibrary.http.result.WeChatLoginResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiDeviceListResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockGetPasswordListResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockVideoBindResult;
import com.philips.easykey.lock.publiclibrary.http.temp.postbean.CheckBind;
import com.philips.easykey.lock.publiclibrary.http.temp.postbean.DeleteDeviceNormalUserBean;
import com.philips.easykey.lock.publiclibrary.http.temp.postbean.GetDeviceGeneralAdministratorBean;
import com.philips.easykey.lock.publiclibrary.http.temp.postbean.OpenLockAuth;
import com.philips.easykey.lock.publiclibrary.http.temp.resultbean.CheckBindResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.publiclibrary.http.util.RetrofitServiceManager;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;


import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;


/**
 * Create By lxj  on 2019/2/27
 * Describe
 */
public class XiaokaiNewServiceImp {

    //////////////////////////////////////// 注册登陆////////////////////////////////////////

    /**
     * 通过手机号注册
     * name	    是	String	手机号码
     * password	是	String	密码
     * tokens	是	String	手机验证码
     *
     * @return
     */
    public static Observable<RegisterResult> registerByPhone(String name, String password, String tokens) {
        RegisterByPhoneBean registerByPhoneBean = new RegisterByPhoneBean(name, password, tokens);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .registerByPhone(timestamp,new HttpUtils<RegisterByPhoneBean>().getBodyNoToken(registerByPhoneBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 通过手机号注册
     * name	    是	String	邮箱
     * password	是	String	密码
     * tokens	是	String	邮箱验证码
     *
     * @return
     */
    public static Observable<RegisterResult> registerByEmail(String name, String password, String tokens) {
        RegisterByPhoneBean registerByPhoneBean = new RegisterByPhoneBean(name, password, tokens);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .registerByEmail(timestamp,new HttpUtils<RegisterByPhoneBean>().getBodyNoToken(registerByPhoneBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 通过手机号登陆
     * 参数名	必选	类型	说明
     * tel	    是	    String	手机号码
     * password	是	    String	密码
     *
     * @return
     */
    public static Observable<LoginResult> loginByPhone(String tel, String password) {
        LoginByPhoneBean loginByPhoneBean = new LoginByPhoneBean(tel, password);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .loginByPhone(timestamp,new HttpUtils<LoginByPhoneBean>().getBodyNoToken(loginByPhoneBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 通过邮箱登陆
     * 参数名	必选	类型	说明
     * Email	    是	    String	邮箱
     * password	是	    String	密码
     *
     * @return
     */
    public static Observable<LoginResult> loginByEmail(String Email, String password) {
        LoginByEmailBean loginByEmailBean = new LoginByEmailBean(Email, password);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .loginByEmail(timestamp,new HttpUtils<LoginByEmailBean>().getBodyNoToken(loginByEmailBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 忘记密码
     * 参数名	必选	类型	说明
     * name	    是	    String	手机号码
     * pwd	    是	    String	密码
     * type	    是	    int	类型：1
     * tokens	是	    string	手机验证码
     *
     * @return
     */
    public static Observable<BaseResult> forgetPassword(String name, String pwd, int type, String tokens) {
        ForgetPasswordBean forgetPasswordBean = new ForgetPasswordBean(name, pwd, type, tokens);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .forgetPassword(timestamp,new HttpUtils<ForgetPasswordBean>().getBodyNoToken(forgetPasswordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 修改密码   APP内  需要token
     * 参数名	必选	类型	说明
     * uid	    是	    String	用户ID
     * newpwd	是	    String	新密码
     * oldpwd	是	    String	旧密码
     *
     * @return
     */
    public static Observable<BaseResult> modifyPassword(String uid, String newpwd, String oldpwd) {
        ModifyPasswordBean modifyPasswordBean = new ModifyPasswordBean(uid, newpwd, oldpwd);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .modifyPassword(timestamp,new HttpUtils<ModifyPasswordBean>().getBodyToken(modifyPasswordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 退出登陆
     *
     * @return
     */
    public static Observable<BaseResult> loginOut() {
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .loginOut()
                .compose(RxjavaHelper.observeOnMainThread());
    }


    //////////////////////////////////////////////用户管理/////////////////////////////////////////

    /**
     * 获取用户昵称
     * 参数名	必选	类型	说明
     * uid	    是	    String	用户ID
     *
     * @return
     */
    public static Observable<UserNickResult> getUserNick(String uid) {
        GetUserNickBean getUserNickBean = new GetUserNickBean(uid);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getUserNick(timestamp,new HttpUtils<GetUserNickBean>().getBodyToken(getUserNickBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 修改用户昵称
     *
     * @return
     */
    public static Observable<BaseResult> modifyUserNick(String uid, String nickname) {
        ModifyUserNickBean modifyUserNickBean = new ModifyUserNickBean(uid, nickname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .modifyUserNick(timestamp,new HttpUtils<ModifyUserNickBean>().getBodyToken(modifyUserNickBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 用户留言
     * 参数名	必选	类型	说明
     * uid	    是	    String	用户ID
     * suggest	是	    String	留言内容
     *
     * @return
     */
    public static Observable<BaseResult> putMessage(String uid, String suggest) {
        PutMessageBean putMessageBean = new PutMessageBean(uid, suggest);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .putMessage(timestamp,new HttpUtils<PutMessageBean>().getBodyToken(putMessageBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取指定设备的普通管理员
     */
    public static Observable<String> getDeviceGeneralAdministrator(String user_id, String devname) {
        GetDeviceGeneralAdministratorBean getDeviceGeneralAdministratorBean = new GetDeviceGeneralAdministratorBean(user_id, devname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getDeviceGeneralAdministrator(timestamp,new HttpUtils<GetDeviceGeneralAdministratorBean>().getBodyToken(getDeviceGeneralAdministratorBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 删除设备的普通用户
     */
    public static Observable<BaseResult> deleteDeviceNormalUser(String adminid, String dev_username, String devname) {
        DeleteDeviceNormalUserBean deleteDeviceNormalUserBean = new DeleteDeviceNormalUserBean(adminid, dev_username, devname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .deleteDeviceNormalUser(timestamp,new HttpUtils<DeleteDeviceNormalUserBean>().getBodyToken(deleteDeviceNormalUserBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取用户协议版本
     *
     * @return
     */
    public static Observable<UserProtocolVersionResult> getUserProtocolVersion() {
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getUserProtocolVersion()
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取用户协议
     * 参数名	    必选	类型	说明
     * protocolId	是	    String	协议版本ID
     *
     * @return
     */
    public static Observable<UserProtocolResult> getUserProtocolContent(String protocolId) {
        GetUserProtocolContentBean getUserProtocolContentBean = new GetUserProtocolContentBean(protocolId);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getUserProtocolContent(timestamp,new HttpUtils<GetUserProtocolContentBean>().getBodyToken(getUserProtocolContentBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    ////////////////////////////////////////////////门锁管理/////////////////////////////////////////////////////////////

    /**
     * 管理员添加设备
     * 参数名	必选	类型	说明
     * devmac	是	String	设备mac
     * devname	是	String	设备唯一编号
     * user_id	是	String	用户ID
     * password1	是	String	密码1
     * password2	是	String	密码2
     *
     * @return
     */
    public static Observable<BaseResult> addDevice(String devmac, String devname, String user_id, String password1, String password2, String model, String bleVersion, String deviceSn, String functionSet) {
        //(String devmac, String devname, String user_id, String password1, String password2, String model, String bleVersion, String deviceSN, String peripheralId, String softwareVersion, String functionSet)
        AddDeviceBean addDeviceBean = new AddDeviceBean(devmac, devname, user_id, password1, password2, model, bleVersion, deviceSn, "", "", functionSet);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .addDevice(timestamp,new HttpUtils<AddDeviceBean>().getBodyToken(addDeviceBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 查询列表设备列表
     * 参数名	必选	类型	说明
     * user_id	是	    String	用户ID
     *
     * @return
     */
    public static Observable<GetDeviceResult> getDevices(String user_id) {
        GetDevicesBean getDevicesBean = new GetDevicesBean(user_id);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getDevices(timestamp,new HttpUtils<GetDevicesBean>().getBodyToken(getDevicesBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 删除设备
     * 参数名	必选	类型	说明
     * adminid	是	    String	用户ID
     * devname	是	    String	设备唯一编号
     *
     * @return
     */
    public static Observable<BaseResult> deleteDevice(String adminid, String devname) {
        DeleteDeviceBean deleteDeviceBean = new DeleteDeviceBean(adminid, devname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .deleteDevice(timestamp,new HttpUtils<DeleteDeviceBean>().getBodyToken(deleteDeviceBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取操作记录
     * 参数名	    必选	类型	说明
     * device_name	是	    String	设备SN
     * pagenum      页数
     *
     * @return
     */
    public static Observable<OperationRecordResult> getOperationRecord(String device_name, int pagenum) {
        GetOperationRecordBean getOperationRecordBean = new GetOperationRecordBean(device_name, pagenum);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getOperationRecord(timestamp,new HttpUtils<GetOperationRecordBean>().getBodyToken(getOperationRecordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 修改门锁昵称
     * 参数名	必选	类型	说明
     * devname	是	    String	设备唯一编号
     * user_id	是	    String	用户ID
     * nickName	是	    String	设备昵称
     *
     * @return
     */
    public static Observable<BaseResult> modifyLockNick(String devname, String user_id, String lockNickName) {
        ModifyLockNickBean modifyLockNickBean = new ModifyLockNickBean(devname, user_id, lockNickName);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .modifyLockNick(timestamp,new HttpUtils<ModifyLockNickBean>().getBodyToken(modifyLockNickBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }


    /**
     * 根据SN获取蓝牙密码
     * 参数名	必选	类型	说明
     * SN	    是	    String	设备唯一编号
     *
     * @return
     */
    public static Observable<GetPwdBySnResult> getPwdBySn(String SN) {
        GetPwdBySNBean getPwdBySNBean = new GetPwdBySNBean(SN);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getpwdBySN(timestamp,new HttpUtils<GetPwdBySNBean>().getBodyToken(getPwdBySNBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }


    /**
     * 三方重置门锁   解绑门锁
     * 参数名	必选	类型	说明
     * devname	是	    String	设备SN
     * adminid	是	    String	管理员用户ID
     *
     * @return
     */
    public static Observable<BaseResult> resetDevice(String adminid, String devname) {
        ResetDeviceBean resetDeviceBean = new ResetDeviceBean(adminid, devname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .resetDevice(timestamp,new HttpUtils<ResetDeviceBean>().getBodyToken(resetDeviceBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 门锁密钥添加
     * 参数名	    必选	类型	说明
     * uid	        是	    String	管理员用户ID
     * devName	    是	    String	设备唯一编号
     * pwdType	    是	    int 	密钥类型：1密码 2临时密码 3指纹密码 4卡片密码
     * num	        是	    String	密钥编号
     * nickName	    是	    String	密钥昵称
     * type 	    否	    int 	密钥周期类型：1永久 2时间段 3周期 4 24小时
     * startTime	否	    timestamp	时间段密钥开始时间
     * endTime	    否	    timestamp	时间段密钥结束时间
     * items	    否	    list	周期密码星期几
     *
     * @return
     */
    public static Observable<BaseResult> addPassword(
            String uid, String devName, List<AddPasswordBean.Password> pwdList) {
        AddPasswordBean addPasswordBean = new AddPasswordBean(uid, devName, pwdList);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .addPassword(timestamp,new HttpUtils<AddPasswordBean>().getBodyToken(addPasswordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 删除秘钥
     * 参数名	必选	类型	说明
     * uid	    是	    String	管理员用户ID
     * devName	是	    String	设备唯一编号
     * pwdType	是	    String	密钥类型：1永久密码 2临时密码 3指纹密码 4卡片密码
     * num	    是	    String	密钥编号
     *
     * @return
     */
    public static Observable<BaseResult> deletePassword(String uid, String devName, List<DeletePasswordBean.DeletePassword> pwdList) {
        DeletePasswordBean deletePasswordBean = new DeletePasswordBean(uid, devName, pwdList);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .deletePassword(timestamp,new HttpUtils<DeletePasswordBean>().getBodyToken(deletePasswordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 修改秘钥昵称
     * 参数名	必选	类型	说明
     * uid	    是	    String	管理员密钥昵称
     * devName	是	    String	设备密钥昵称
     * pwdType	是	    String	密钥类型：1永久密码 2临时密码 3指纹密码 4密钥昵称
     * num	    是	    String	密钥昵称
     * nickName	是	    String	密钥昵称
     *
     * @return
     */
    public static Observable<BaseResult> modifyPasswordNick(String uid, String devName, int pwdType, String num, String nickName) {
//        String uid, String devName, int pwdType, String num, String nickName
        ModifyPasswordNickBean modifyPasswordNickBean = new ModifyPasswordNickBean(uid, devName, pwdType, num, nickName);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .modifyPasswordNick(timestamp,new HttpUtils<ModifyPasswordNickBean>().getBodyToken(modifyPasswordNickBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 查询秘钥列表
     * 参数名	必选	类型	说明
     * uid	    是	    String	管理员用户ID
     * devName	是	    String	设备唯一编号
     * pwdType	是	    String	密钥类型：0所有密码 1永久密码 2临时密码 3指纹密码 4卡片密码
     *
     * @return
     */
    public static Observable<GetPasswordResult> getPasswords(String uid, String devName, int pwdType) {
        GetPasswordBean getPasswordBean = new GetPasswordBean(uid, devName, pwdType);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getPasswords(timestamp,new HttpUtils<GetPasswordBean>().getBodyToken(getPasswordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 查询单个秘钥
     * 参数名	必选	类型	说明
     * uid	是	String	管理员用户ID
     * devName	是	String	设备唯一编号
     * pwdType	是	String	密钥类型：1永久密码 2临时密码 3指纹密码 4卡片密码
     * num	是	String	密钥编号
     * nickName	是	String	密钥昵称
     *
     * @return
     */

    public static Observable<SinglePasswordResult> getSinglePassword(String uid, String devName, int pwdType, String num) {
        GetSinglePasswordBean getSinglePasswordBean = new GetSinglePasswordBean(uid, devName, pwdType, num);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getSinglePassword(timestamp,new HttpUtils<GetSinglePasswordBean>().getBodyToken(getSinglePasswordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 检查锁是否绑定
     */
    public static Observable<CheckBindResult> checkLockBind(String deviceName) {
        CheckBind checkBind = new CheckBind();
        checkBind.setUser_id(MyApplication.getInstance().getUid());
        checkBind.setDevname(deviceName);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .checkLockBind(timestamp,new HttpUtils<CheckBind>().getBodyToken(checkBind,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    ////////////////////////////////////////////用户管理////////////////////////////////////////////////

    /**
     * 添加普通用户
     *
     * @param admin_id        管理员用户ID
     * @param device_username 要分享的用户账号（手机号）
     * @param devicemac
     * @param devname
     * @param end_time
     * @param lockNickName    门锁昵称
     * @param open_purview    类型：3永久
     * @param start_time
     * @param items
     * @return
     */
    public static Observable<BaseResult>
    addUser(String admin_id, String device_username, String devicemac, String devname, String end_time, String lockNickName,
            String open_purview, String start_time, List<String> items) {
        AddUserBean addUserBean = new AddUserBean(admin_id, device_username, devicemac, devname, end_time, lockNickName, open_purview, start_time, items);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .addUser(timestamp,new HttpUtils<AddUserBean>().getBodyToken(addUserBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 更改普通用户昵称
     *
     * @param ndId         用户-设备关联ID
     * @param userNickName 用户昵称
     * @return
     */
    public static Observable<BaseResult>
    modifyCommonUserNickname(String ndId, String userNickName) {
        ModifyCommonUserNicknameBean modifyCommonUserNicknameBean = new ModifyCommonUserNicknameBean(ndId, userNickName);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .modifyCommonUserNickname(timestamp,new HttpUtils<ModifyCommonUserNicknameBean>().getBodyToken(modifyCommonUserNicknameBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 删除普通用户
     * 参数名	        必选	类型	说明
     * admin_id	        是	    String	管理员用户ID
     * device_username	是	    String	要分享的用户账号（手机号）
     * devname	        是	    String	设备SN
     *
     * @return
     */
    public static Observable<BaseResult> deleteUser(String dev_username, String adminid, String devname) {
        DeleteUserBean deleteUserBean = new DeleteUserBean(dev_username, adminid, devname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .deleteUser(timestamp,new HttpUtils<DeleteUserBean>().getBodyToken(deleteUserBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 查询普通用户列表
     * @return
     */
    public static Observable<BaseResult> searchUser(String devname, String user_id) {
        SearchUSerBean searchUSerBean = new SearchUSerBean(devname, user_id);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .searchUser(timestamp,new HttpUtils<SearchUSerBean>().getBodyToken(searchUSerBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }


    //////////////////////////////////////开门记录///////////////////////////////////////////////

    /**
     * 上传秘钥开门记录
     * 参数名	        必选	类型	说明
     * device_name	    是	    String	设备SN
     * device_nickname	是	    String	门锁昵称
     * user_id	        是	    String	上传用户ID
     * openLockList	是	    JsonArray
     * open_time	    是	    String	开门时间
     * open_type	    是	    String	开门类型
     * user_num	    是	    String	密钥编号
     * nickName	    是	    String	密钥昵称
     *
     * @return
     */
    public static Observable<BaseResult> uploadBinRecord(String device_name, String device_nickname,
                                                         String user_id, List<UploadBinRecordBean.OpenLockRecordBle> openLockList) {
        UploadBinRecordBean uploadBinRecordBean = new UploadBinRecordBean(device_name, device_nickname, user_id, openLockList);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .uploadBinRecord(timestamp,new HttpUtils<UploadBinRecordBean>().getBodyToken(uploadBinRecordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 上传App开门记录
     * 参数名	    必选	类型	说明
     * devname	    是	    String	设备唯一编号
     * is_admin	    是	    String	是否是管理员：0否 1是
     * open_type	是	    String	开门类型：200
     * user_id	    是	    String	用户ID
     * nickName	    是	    String	用户昵称
     *
     * @return
     */
    public static Observable<BaseResult> uploadAppRecord(String devname, String is_admin, String open_type, String user_id, String nickName) {
        UploadAppRecordBean uploadAppRecordBean = new UploadAppRecordBean(devname, is_admin, open_type, user_id, nickName);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .uploadAppRecord(timestamp,new HttpUtils<UploadAppRecordBean>().getBodyToken(uploadAppRecordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 获取开门记录
     * 参数名	    必选	类型	说明
     * device_name	是	    String	设备SN
     * user_id	    是	    String	用户ID
     * open_type	否	    String	开门类型：null(查询所有) "200"(APP开锁) "密码" "指纹" "卡片"
     *
     * @return
     */
    public static Observable<LockRecordResult> getLockRecord(String device_name, String user_id, String openType, String pagenum) {
        GetLockRecordBean getLockRecordBean = new GetLockRecordBean(device_name, user_id, openType, pagenum);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getLockRecord(timestamp,new HttpUtils<GetLockRecordBean>().getBodyToken(getLockRecordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 获取预警记录
     * 参数名	必选	类型	说明
     * devName	是	    String	设备唯一编号
     * pageNum	是	    int	    第几页
     *
     * @return
     */
    public static Observable<GetWarringRecordResult> getWarringRecord(String devName, int pageNum) {
        GetWarringRecordBean getWarringRecordBean = new GetWarringRecordBean(devName, pageNum);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getWarringRecord(timestamp,new HttpUtils<GetWarringRecordBean>().getBodyToken(getWarringRecordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 发送短信验证码
     * 参数名	必选	类型	说明
     * tel	    是	    String	手机号码
     * code	    是	    String	区号：86中国
     *
     * @return
     */
    public static Observable<BaseResult> sendMessage(String tel, String code) {
        SendMessageBean sendMessageBean = new SendMessageBean(tel, code);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .sendMessage(timestamp,new HttpUtils<SendMessageBean>().getBodyNoToken(sendMessageBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 发送邮箱验证码
     * 参数名	必选	类型	说明
     * email	    是	    String	邮箱
     *
     * @return
     */
    public static Observable<BaseResult> sendEmailYZM(String email) {
        SendEmailBean sendEmailBean = new SendEmailBean(email);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .sendEamilYZM(timestamp,new HttpUtils<SendEmailBean>().getBodyNoToken(sendEmailBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 上传头像
     * uid  用户唯一标识
     * path 图片路径
     */

    public static Observable<BaseResult> uploadPic(String uid, String path) {
        File file = new File(path);
        RequestBody uidBody = RequestBody.create(MediaType.parse("multipart/form-data"), uid);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class).getPicturesBean(uidBody, filePart)
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 下载头像
     */
    public static Observable<ResponseBody> downloadUserHead(String uid) {
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .downloadUserHead(uid)
                .subscribeOn(Schedulers.io());
//                .compose(RxjavaHelper.observeOnMainThread())
//                .subscribe();

    }

    /**
     * 常见问题列表
     */
    public static Observable<String> getFaqList(int languageType) {
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getFAQList(languageType)
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 系统消息
     */
    public static Observable<String> getMessageList(String uid, int page) {
        GetMessageBean bean = new GetMessageBean(uid, page);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getMessageList(timestamp,new HttpUtils<GetMessageBean>().getBodyToken(bean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 删除系统消息
     */
    public static Observable<DeleteMessageResult> deleteMessage(String uid, String mid) {
        DeleteMessageBean messageBean = new DeleteMessageBean(uid, mid);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .deleteMessage(timestamp,new HttpUtils<DeleteMessageBean>().getBodyToken(messageBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 查询帮助日志
     */
    public static Observable<GetHelpLogResult> getHelpLog(String uid, int page) {
        GetHelpLogBean helpLogBean = new GetHelpLogBean(uid, page);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getHelpLog(timestamp,new HttpUtils<GetHelpLogBean>().getBodyToken(helpLogBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 请求App版本
     */
    @Deprecated
    public static Observable<VersionBean> getAppVersion() {
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getAppVersion()
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 上报预警记录
     * 参数名	    必选	类型	说明
     * devName	    是	    String	设备唯一编号
     * warningList	是	    JsonArray
     * warningType	是	    int	预警类型：1低电量 2钥匙开门 3验证错误 4防撬提醒 5即时性推送消息
     * warningTime	是	    timestamp（s）	预警时间
     * content	    否	    String	预警内容
     *
     * @return
     */
    public static Observable<BaseResult> uploadWarringRecord(String devName, List<WarringRecord> warningList) {
        UploadWarringRecordBean uploadWarringRecordBean = new UploadWarringRecordBean(devName, warningList);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .uploadWarringRecord(timestamp,new HttpUtils<UploadWarringRecordBean>().getBodyToken(uploadWarringRecordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    public static Observable<BaseResult> openLockAuth(String devname, String is_admin, String open_type, String user_id) {
        OpenLockAuth openLockAuth = new OpenLockAuth(devname, is_admin, open_type, user_id);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .openLockAuth(timestamp,new HttpUtils<OpenLockAuth>().getBodyToken(openLockAuth,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    public static Observable<BaseResult> uploadPushId(String uid, String jpushId, int type) {
        PushBean pushBean = new PushBean(uid, jpushId, type);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .uploadPushId(timestamp,new HttpUtils<PushBean>().getBodyToken(pushBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * upload phone message
     *
     * @param uid
     * @param account
     * @param model        手机型号  SM-C7000
     * @param manufacturer 手机厂商 samsung
     * @param version      Android系统版本号 android8.0
     * @return
     */
    public static Observable<BaseResult> uploadPushPhoneMsg(String uid, String account, String model, String manufacturer, String version) {
        PhoneMessage phoneMessage = new PhoneMessage(uid, account, model, manufacturer, version);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .uploadPushMsg(timestamp,new HttpUtils<PhoneMessage>().getBodyToken(phoneMessage,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    public static Observable<SwitchStatusResult> getPushSwitch(String uid) {
        PushSwitch pushSwitch = new PushSwitch(uid);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getPushSwitch(timestamp,new HttpUtils<PushSwitch>().getBodyToken(pushSwitch,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    public static Observable<SwitchStatusResult> updatePushSwitch(String uid, boolean openlockPushSwitch) {
        PushSwitchBean pushSwitchBean = new PushSwitchBean(uid, openlockPushSwitch);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .updatePushSwitch(timestamp,new HttpUtils<PushSwitchBean>().getBodyToken(pushSwitchBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * {
     * "devName":"BT01191910010",
     * "operationList":[
     * {
     * "uid":"5c6fb4d014fd214910b33e80",
     * "eventType":4,
     * "eventSource":3,
     * "eventCode":3,
     * "userNum":4,
     * "eventTime":"2019-07-04 12:00:03"
     * }
     * ]
     * }
     */
    public static Observable<BaseResult> uploadOperationRecord(String device_name, List<UploadOperationRecordBean.OperationListBean> operationListBeanList) {
        UploadOperationRecordBean uploadOperationRecordBean = new UploadOperationRecordBean(device_name, operationListBeanList);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .uploadOperationRecord(timestamp,new HttpUtils<UploadOperationRecordBean>().getBodyToken(uploadOperationRecordBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     * 更新蓝牙的版本信息
     *
     * @param devname         设备名
     * @param user_id
     * @param softwareVersion 蓝牙软件版本号
     * @param deviceSN        蓝牙的SN
     * @return
     */
    public static Observable<BaseResult> updateSoftwareVersion(String devname, String user_id, String softwareVersion, String deviceSN) {
        UpdateSoftwareVersionBean updateSoftwareVersionBean = new UpdateSoftwareVersionBean(devname, user_id, softwareVersion, deviceSN, "");
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .updateSoftwareVersion(timestamp,new HttpUtils<UpdateSoftwareVersionBean>().getBodyToken(updateSoftwareVersionBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 更新蓝牙的版本号
     *
     * @param devname
     * @param user_id
     * @param bleVersion
     * @return
     */
    public static Observable<BaseResult> updateBleVersion(String devname, String user_id, String bleVersion) {
        UpdateBleVersionBean updateSoftwareVersionBean = new UpdateBleVersionBean(devname, user_id, bleVersion);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .updateBleVersion(timestamp,new HttpUtils<UpdateBleVersionBean>().getBodyToken(updateSoftwareVersionBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 更新功能集
     *
     * @param devname
     * @param user_id
     * @param functionSet
     * @return
     */
    public static Observable<BaseResult> modifyFunctionSet(String devname, String user_id, String functionSet) {
        ModifyFunctionSetBean updateSoftwareVersionBean = new ModifyFunctionSetBean(devname, user_id, functionSet);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .modifyFunctionSet(timestamp,new HttpUtils<ModifyFunctionSetBean>().getBodyToken(updateSoftwareVersionBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * @param customer   是	int	客户：1凯迪仕 2小凯 3桔子物联 4飞利浦
     * @param deviceName 是	String	设备唯一编号
     * @param version    否	String	当前版本号
     * @param type       1主模块 2算法模块 3相机模块（空：默认1） 4协议栈
     * @return
     */
    public static Observable<CheckOTAResult> getOtaInfo(int customer, String deviceName, String version, int type) {
        OTABean helpLogBean = new OTABean(customer, deviceName, version, type);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getOtaInfo(timestamp,new HttpUtils<OTABean>().getBodyToken(helpLogBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /** 多固件检查更新
     * @param customer   是	int	客户：1凯迪仕 2小凯 3桔子物联 4飞利浦
     * @param deviceName 是	String	设备唯一编号
     * @return
     */
    public static Observable<MultiCheckOTAResult> getOtaMultiInfo(int customer, String deviceName, List<HangerMultiOTABean.OTAParams> params,String devtype) {
        HangerMultiOTABean helpLogBean = new HangerMultiOTABean(customer, deviceName, params,devtype);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getOtaMultiCheck(timestamp,new HttpUtils<HangerMultiOTABean>().getBodyToken(helpLogBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /** 多固件检查更新
     * @param customer   是	int	客户：1凯迪仕 2小凯 3桔子物联 4飞利浦
     * @param deviceName 是	String	设备唯一编号
     * @return
     */
    public static Observable<MultiCheckOTAResult> getOtaMultiInfo(int customer, String deviceName, List<MultiOTABean.OTAParams> params) {
        MultiOTABean helpLogBean = new MultiOTABean(customer, deviceName, params);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getOtaMultiCheck(timestamp,new HttpUtils<MultiOTABean>().getBodyToken(helpLogBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * @param sn
     * @param version    新版本的版本号
     * @param customer   客户：1凯迪仕 2小凯 3桔子物联 4飞利浦
     * @param resultCode 结果：0升级成功 1升级失败 （可自定义其他错误码）
     * @param devNum     模块：1主模块 2算法模块 3相机模块（空：默认1）
     * @return
     */
    public static Observable<BaseResult> uploadOtaResult(String sn, String version, int customer, String resultCode, int devNum) {
        UploadOtaResultBean uploadOtaResultBean = new UploadOtaResultBean(sn, version, customer, resultCode, devNum);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .uploadOtaResult(timestamp,new HttpUtils<UploadOtaResultBean>().getBodyToken(uploadOtaResultBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }


    ////////////////////////////////////////////           WiFi锁api功能            ///////////////////////////////////////////////

    /**
     * 绑定WiFi锁
     *
     * @param wifiSN       是	String	wifi模块SN
     * @param lockNickName 否	String	门锁昵称
     * @param uid          是	String	用户ID
     * @return
     */
    public static Observable<BaseResult> wifiLockBind(String wifiSN, String lockNickName, String uid, String randomCode, String wifiName,int func, int distributionNetwork) {
        WiFiLockBindBean wiFiLockBindBean = new WiFiLockBindBean(wifiSN, lockNickName, uid, randomCode, wifiName,func, distributionNetwork);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockBind(timestamp,new HttpUtils<WiFiLockBindBean>().getBodyToken(wiFiLockBindBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 解绑WiFi锁
     *
     * @param wifiSN
     * @param uid
     * @return
     */
    public static Observable<BaseResult> wifiLockUnbind(String wifiSN, String uid) {
        WiFiLockWifiSNAndUid wiFiLockUnbind = new WiFiLockWifiSNAndUid(wifiSN, uid);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockUnbind(timestamp,new HttpUtils<WiFiLockWifiSNAndUid>().getBodyToken(wiFiLockUnbind,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 修改WiFi锁昵称
     *
     * @param wifiSN
     * @param uid
     * @param lockNickname
     * @return
     */
    public static Observable<BaseResult> wifiLockUpdateNickname(String wifiSN, String uid, String lockNickname) {
        WiFiLockUpdateNickNameBean wiFiLockUpdateNickNameBean = new WiFiLockUpdateNickNameBean(wifiSN, uid, lockNickname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockUpdateNickname(timestamp,new HttpUtils<WiFiLockUpdateNickNameBean>().getBodyToken(wiFiLockUpdateNickNameBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 更改推送开关
     *
     * @param wifiSN
     * @param uid
     * @param switchX 推送开关： 1(0默认开启)开启推送 2关闭推送
     * @return
     */
    public static Observable<BaseResult> wifiLockUpdatePush(String wifiSN, String uid, int switchX) {
        WifiLockUpdatePushSwitchBean wifiLockUpdatePushSwitchBean = new WifiLockUpdatePushSwitchBean(wifiSN, uid, switchX);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockUpdatePush(timestamp,new HttpUtils<WifiLockUpdatePushSwitchBean>().getBodyToken(wifiLockUpdatePushSwitchBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * @param wifiSN
     * @param uid
     * @return
     */
    public static Observable<WifiLockGetPasswordListResult> wifiLockGetPwdList(String wifiSN, String uid) {
        WiFiLockWifiSNAndUid wiFiLockUnbind = new WiFiLockWifiSNAndUid(wifiSN, uid);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockGetPwdList(timestamp,new HttpUtils<WiFiLockWifiSNAndUid>().getBodyToken(wiFiLockUnbind,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 修改wifi密码昵称
     *
     * @param uid      是	String	管理员用户ID
     * @param wifiSN   是	String	设备唯一编号
     * @param pwdType  是	String	密钥类型：1密码 2指纹密码 3卡片密码
     * @param num      是	int	密钥编号
     * @param nickName 是	String	密钥昵称
     * @return
     */
    public static Observable<BaseResult> wifiLockUpdatePwdNickName(String uid, String wifiSN, int pwdType, int num, String nickName) {
        WifiLockUpdatePwdNickBean wifiLockUpdatePwdNickBean = new WifiLockUpdatePwdNickBean(uid, wifiSN, pwdType, num, nickName);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockUpdatePwdNickName(timestamp,new HttpUtils<WifiLockUpdatePwdNickBean>().getBodyToken(wifiLockUpdatePwdNickBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 修改wifi密码昵称
     *
     * @param uid      是	String	管理员用户ID
     * @param wifiSN   是	String	设备唯一编号
     * @param pwdType  是	String	密钥类型：1密码 2指纹密码 3卡片密码 4面容识别
     * @param num      是	int	密钥编号
     * @param nickName 是	String	密钥昵称
     * @return
     */
    public static Observable<BaseResult> wifiLockUpdatePwdNickName(String uid, String wifiSN, int pwdType, int num, String nickName,String userNickName) {
        WifiLockUpdatePwdNickBean wifiLockUpdatePwdNickBean = new WifiLockUpdatePwdNickBean(uid, wifiSN, pwdType, num, nickName,userNickName);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockUpdatePwdNickName(timestamp,new HttpUtils<WifiLockUpdatePwdNickBean>().getBodyToken(wifiLockUpdatePwdNickBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 分享设备
     *
     * @param wifiSN       是	String	wifi模块SN
     * @param uid          是	String	用户ID
     * @param uname        是	String	分享账号
     * @param userNickname 是	String	分享用户昵称
     * @return
     */
    public static Observable<BaseResult> wifiLockAddShareUser(String wifiSN, String uid, String uname, String userNickname, String adminNickname) {
        WifiLockShareBean wifiLockShareBean = new WifiLockShareBean(wifiSN, uid, uname, userNickname, adminNickname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockShare(timestamp,new HttpUtils<WifiLockShareBean>().getBodyToken(wifiLockShareBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * wifi锁删除授权用户
     *
     * @param shareId 是	String	用户设备关联ID
     * @param uid     是	String	用户ID
     * @return
     */
    public static Observable<BaseResult> wifiLockDeleteShareUser(String shareId, String uid, String adminNickname) {
        WifiLockDeleteShareBean wifiLockDeleteShareBean = new WifiLockDeleteShareBean(shareId, uid, adminNickname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockDeleteShareUser(timestamp,new HttpUtils<WifiLockDeleteShareBean>().getBodyToken(wifiLockDeleteShareBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * wifi锁修改授权用户昵称
     *
     * @param shareId
     * @param nickName
     * @return
     */
    public static Observable<BaseResult> wifiLockUpdateShareUserNickname(String shareId, String nickName, String uid) {
        WifiLockUpdateShareNickBean wifiLockUpdateShareNickBean = new WifiLockUpdateShareNickBean(shareId, nickName, uid);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockUpdateShareUserNickname(timestamp,new HttpUtils<WifiLockUpdateShareNickBean>().getBodyToken(wifiLockUpdateShareNickBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * wifi锁授权用户列表
     *
     * @param wifiSN
     * @param uid
     * @return
     */
    public static Observable<WifiLockShareResult> wifiLockGetShareUserList(String wifiSN, String uid) {
        WiFiLockWifiSNAndUid wiFiLockWifiSNAndUid = new WiFiLockWifiSNAndUid(wifiSN, uid);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockGetShareUserList(timestamp,new HttpUtils<WiFiLockWifiSNAndUid>().getBodyToken(wiFiLockWifiSNAndUid,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取wifi锁操作记录
     *
     * @param wifiSN
     * @param page
     * @return
     */
    public static Observable<GetWifiLockOperationRecordResult> wifiLockGetOperationList(String wifiSN, int page) {
        WifiLockRecordBean wiFiLockWifiSNAndUid = new WifiLockRecordBean(wifiSN, page);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockGetOperationList(timestamp,new HttpUtils<WifiLockRecordBean>().getBodyToken(wiFiLockWifiSNAndUid,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取wifi锁操作筛选记录
     *
     * @param wifiSN
     * @param page
     * @return
     */
    public static Observable<GetWifiLockOperationScreenedRecordResult> wifiLockGetOperationFilterList(String wifiSN, int page, long startTime, long endTime) {
        WifiLockRecordScreenedBean wiFiLockWifiSNAndUid = new WifiLockRecordScreenedBean(wifiSN, page,startTime,endTime);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockGetOperationFilterList(timestamp,new HttpUtils<WifiLockRecordScreenedBean>().getBodyToken(wiFiLockWifiSNAndUid,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 获取wifi报警记录
     */

    public static Observable<GetWifiLockAlarmRecordResult> wifiLockGetAlarmList(String wifiSN, int page) {
        WifiLockRecordBean wiFiLockWifiSNAndUid = new WifiLockRecordBean(wifiSN, page);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockGetAlarmList(timestamp,new HttpUtils<WifiLockRecordBean>().getBodyToken(wiFiLockWifiSNAndUid,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 开锁次数
     */

    public static Observable<GetOpenCountResult> wifiLockGetOpenCount(String wifiSN) {
        GetOpenCountBean getOpenCountBean = new GetOpenCountBean(wifiSN);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockGetOpenCount(timestamp,new HttpUtils<GetOpenCountBean>().getBodyToken(getOpenCountBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * @param uid
     * @param wifiSN
     * @param randomCode
     * @return
     */
    public static Observable<BaseResult> wifiLockUpdateInfo(String uid, String wifiSN, String randomCode, String wifiName,int func) {
        WifiLockUpdateInfoBean updateInfoBean = new WifiLockUpdateInfoBean(uid, wifiSN, randomCode, wifiName,func);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockUpdateInfo(timestamp,new HttpUtils<WifiLockUpdateInfoBean>().getBodyToken(updateInfoBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }



    /**
     * @return
     */
    public static Observable<BaseResult> wifiLockUploadOta( CheckOTAResult.UpdateFileInfo updateFileInfo,String wifiSN) {
        UploadOtaBean uploadOtaBean = new UploadOtaBean(  wifiSN, updateFileInfo.getFileLen(), updateFileInfo .getFileUrl(),
                updateFileInfo.getFileMd5(),updateFileInfo.getDevNum(), updateFileInfo.getFileVersion());
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiLockUploadOta(timestamp,new HttpUtils<UploadOtaBean>().getBodyToken(uploadOtaBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * @return
     */
    public static Observable<ResponseBody> getQrCodeContent(String url) {
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getQrCodeContent(url)
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * @return
     */
    public static Observable<String> getZIGBEENINFO(String uid,String gwSN,String zigbeeSN) {
        ZigBeenInfo uploadOtaBean = new ZigBeenInfo(  zigbeeSN, uid, gwSN);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .GET_ZIGBEEN_INFO_CONTEXT(timestamp,new HttpUtils<ZigBeenInfo>().getBodyToken(uploadOtaBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 单火开关设备联动开关昵称修改
     *
     * @return
     */
    public static Observable<BaseResult> updateSwitchNickname(ModifySwitchNickBean modifySwitchNickBean) {
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .updateSwitchNickname(timestamp,new HttpUtils<ModifySwitchNickBean>().getBodyToken(modifySwitchNickBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 设置密钥胁迫报警接收账户
     *
     */
    public static Observable<BaseResult> wifiPwdDuressAccount(SettingPwdDuressAccountBean settingPwdDuressAccountBean) {
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiPwdDuressAccount(timestamp,new HttpUtils<SettingPwdDuressAccountBean>().getBodyToken(settingPwdDuressAccountBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 设置密钥胁迫报警开关
     *
     */
    public static Observable<BaseResult> wifiPwdDuressAlarm(SettingPwdDuressAlarmBean settingPwdDuressAlarmBean) {
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiPwdDuressAlarm(timestamp,new HttpUtils<SettingPwdDuressAlarmBean>().getBodyToken(settingPwdDuressAlarmBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 修改胁迫报警开关
     *
     */
    public static Observable<BaseResult> wifiPwdDuressAlarmSwitch(SettingPwdDuressAlarmSwitchBean settingPwdDuressAlarmSwitchBean) {
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiDuressAlarmSwitch(timestamp,new HttpUtils<SettingPwdDuressAlarmSwitchBean>().getBodyToken(settingPwdDuressAlarmSwitchBean,timestamp))
                .compose(RxjavaHelper.observeOnMainThread());
    }

    ////////////////////////////////////////////           WiFi视频锁api功能            ///////////////////////////////////////////////

    /**
     * 绑定视频锁
     */
    public static Observable<WifiLockVideoBindResult> wifiVideoLockBind(WiFiLockVideoBindBean wiFiLockBindBean) {
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiVideoLockBind(timestamp,new HttpUtils<WiFiLockVideoBindBean>().getBodyToken(wiFiLockBindBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  解绑视频锁
     */
    public static Observable<WifiLockVideoBindResult> wifiVideoLockUnbind(String wifiSN, String uid) {
        WiFiLockWifiSNAndUid wiFiLockUnbind = new WiFiLockWifiSNAndUid(wifiSN, uid);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiVideoLockUnbind(timestamp,new HttpUtils<WiFiLockWifiSNAndUid>().getBodyToken(wiFiLockUnbind,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  绑定视频锁失败
     */
    public static Observable<WifiLockVideoBindResult> wifiVideoLockBindFail(String wifiSN, int result) {
        WiFiLockViewBindFailBean wiFiLockUnbind = new WiFiLockViewBindFailBean(wifiSN, result);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiVideoLockBindFail(timestamp,new HttpUtils<WiFiLockViewBindFailBean>().getBodyToken(wiFiLockUnbind,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  更新视频锁
     */
    public static Observable<WifiLockVideoBindResult> wifiVideoLockUpdateBind(WiFiLockVideoUpdateBindBean wiFiLockBindBean) {
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiVideoLockUpdateBind(timestamp,new HttpUtils<WiFiLockVideoUpdateBindBean>().getBodyToken(wiFiLockBindBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  视频锁分页查询报警记录
     */
    public static Observable<GetWifiVideoLockAlarmRecordResult> wifiVideoLockGetAlarmList(String wifiSn, int page) {
        WifiLockRecordBean wiFiLockWifiSNAndUid = new WifiLockRecordBean(wifiSn, page);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiVideoLockGetAlarmList(timestamp,new HttpUtils<WifiLockRecordBean>().getBodyToken(wiFiLockWifiSNAndUid,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  视频锁报警记录筛选查询
     */
    public static Observable<GetWifiVideoLockAlarmScreenedRecordResult> wifiVideoLockGetAlarmFilterList(String wifiSn, int page, long startTime, long endTime) {
        WifiLockRecordScreenedBean wiFiLockWifiSNAndUid = new WifiLockRecordScreenedBean(wifiSn, page , startTime , endTime);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiVideoLockGetAlarmFilterList(timestamp,new HttpUtils<WifiLockRecordScreenedBean>().getBodyToken(wiFiLockWifiSNAndUid,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  视频锁分页查询门铃记录
     */
    public static Observable<GetWifiVideoLockAlarmRecordResult> wifiVideoLockGetDoorbellList(String wifiSn,int page){
        WifiLockRecordBean wiFiLockWifiSNAndUid = new WifiLockRecordBean(wifiSn, page);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiVideoLockGetDoorbellList(timestamp,new HttpUtils<WifiLockRecordBean>().getBodyToken(wiFiLockWifiSNAndUid,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  视频锁分页查询门铃筛选记录
     */
    public static Observable<GetWifiVideoLockDoorbellScreenedRecordResult> wifiVideoLockGetDoorbellFilterList(String wifiSn, int page, long startTime, long endTime){
        WifiLockRecordScreenedBean wiFiLockWifiSNAndUid = new WifiLockRecordScreenedBean(wifiSn, page,startTime,endTime);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiVideoLockGetDoorbellFilterList(timestamp,new HttpUtils<WifiLockRecordScreenedBean>().getBodyToken(wiFiLockWifiSNAndUid,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  查询wifi锁设备列表
     */
    public static Observable<WifiDeviceListResult> wifiDeviceLisst(){
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiDeviceList(timestamp,new HttpUtils<WifiLockDeviceBean>().getBodyToken(new WifiLockDeviceBean(MyApplication.getInstance().getUid()),timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());

    }

    /**
     *  查询设备列表
     */
    public static Observable<AllBindDevices> httpGetAllBindDevices(){
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getAllBindDevices(timestamp,new HttpUtils<HttpAllBindDevicesBean>().getBodyToken(new HttpAllBindDevicesBean(MyApplication.getInstance().getUid(),2),timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());

    }

    ////////////////////////////////////////////           晾衣机api功能            ///////////////////////////////////////////////

    /**
     *  检查晾衣机设备是否被绑定
     */
    public static Observable<ClothesHangerMachineCheckBindingResult> clothesHangerMachineCheckBinding(String wifiSN){
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .clothesHangerMachineCheckBinding(timestamp,new HttpUtils<ClothesHangerMachineDeviceBean>().getBodyToken(new ClothesHangerMachineDeviceBean(wifiSN,MyApplication.getInstance().getUid()),timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *   晾衣机绑定
     */
    public static Observable<ClothesHangerMachineBindResult> clothesHangerMachineBind(String wifiSN){
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .clothesHangerMachineBind(timestamp,new HttpUtils<ClothesHangerMachineDeviceBean>().getBodyToken(new ClothesHangerMachineDeviceBean(wifiSN,MyApplication.getInstance().getUid()),timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  晾衣机解绑
     */
    public static Observable<ClothesHangerMachineUnBindResult> clothesHangerMachineUnBind(String wifiSN){
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .clothesHangerMachineUnbind(timestamp,new HttpUtils<ClothesHangerMachineDeviceBean>().getBodyToken(new ClothesHangerMachineDeviceBean(wifiSN,MyApplication.getInstance().getUid()),timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 修改晾衣机昵称
     */
    public static Observable<BaseResult> hangerUpdateNickname(String wifiSN, String lockNickname) {
        HangerUpdateNickNameBean wiFiLockUpdateNickNameBean = new HangerUpdateNickNameBean(wifiSN, MyApplication.getInstance().getUid(), lockNickname);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .hangerUpdateNickname(timestamp,new HttpUtils<HangerUpdateNickNameBean>().getBodyToken(wiFiLockUpdateNickNameBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  多固件确认升级
     */
    public static Observable<BaseResult> wifiDeviceUploadMultiOta(String wifiSN,List<UpgradeMultiOTABean.UpgradeTaskBean> upgradeTask) {
        UpgradeMultiOTABean wiFiLockUpdateNickNameBean = new UpgradeMultiOTABean(wifiSN, upgradeTask);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiDeviceUploadMultiOta(timestamp,new HttpUtils<UpgradeMultiOTABean>().getBodyToken(wiFiLockUpdateNickNameBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     *  多固件确认升级
     */
    public static Observable<BaseResult> hangerDeviceUploadMultiOta(String wifiSN, String type,List<HangerUpgradeMultiOTABean.UpgradeTaskBean> upgradeTask) {
        HangerUpgradeMultiOTABean wiFiLockUpdateNickNameBean = new HangerUpgradeMultiOTABean(wifiSN, type, upgradeTask);
        String timestamp = System.currentTimeMillis() /1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .wifiDeviceUploadMultiOta(timestamp,new HttpUtils<HangerUpgradeMultiOTABean>().getBodyToken(wiFiLockUpdateNickNameBean,timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取wechat的OpenId
     */
    public static Observable<GetWeChatOpenIdResult> getWeChatOpenId(@NonNull String code) {
        GetWeChatOpenIdBean bean = new GetWeChatOpenIdBean();
        bean.setCode(code);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .getWeChatOpenId(timestamp, new HttpUtils<GetWeChatOpenIdBean>().getBodyNoToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取微信用户绑定的手机号
     */
    public static Observable<GetWeChatUserPhoneResult> getWeChatUserPhone(@NonNull String openId) {
        GetWeChatUserPhoneBean bean = new GetWeChatUserPhoneBean();
        bean.setOpenId(openId);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .getWeChatUserPhone(timestamp, new HttpUtils<GetWeChatUserPhoneBean>().getBodyNoToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 注册微信openid，绑定手机
     */
    public static Observable<RegisterWeChatAndBindPhoneResult> registerWeChatAndBindPhone(@NonNull String openId,
                                                                                          @NonNull String tel,
                                                                                          @NonNull String tokens) {
        RegisterWeChatAndBindPhoneBean bean = new RegisterWeChatAndBindPhoneBean();
        bean.setOpenId(openId);
        bean.setTel(tel);
        bean.setTokens(tokens);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .registerWeChatAndBindPhone(timestamp, new HttpUtils<RegisterWeChatAndBindPhoneBean>().getBodyToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 微信用户一键登录
     */
    public static Observable<WeChatLoginResult> weChatLogin(@NonNull String openId, @NonNull String tel) {
        WeChatLoginBean bean = new WeChatLoginBean();
        bean.setOpenId(openId);
        bean.setTel(tel);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .weChatLogin(timestamp, new HttpUtils<WeChatLoginBean>().getBodyNoToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 手机验证码登录
     */
    public static Observable<WeChatLoginResult> codeLogin(@NonNull String tokens, @NonNull String tel) {
        CodeLoginBean bean = new CodeLoginBean();
        bean.setTokens(tokens);
        bean.setTel(tel);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getNoTokenInstance().create(IXiaoKaiNewService.class)
                .codeLogin(timestamp, new HttpUtils<CodeLoginBean>().getBodyNoToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取门锁当天统计
     */
     public static Observable<GetStatisticsDayResult> getDoorLockDtatisticsDay(@NonNull String uid, @NonNull String wifiSN){
         DoorLockStatisticsBean bean = new DoorLockStatisticsBean();
         bean.setUid(uid);
         bean.setWifiSN(wifiSN);
         String timestamp = System.currentTimeMillis()/1000 + "";
         return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                 .getDoorLockDtatisticsDay(timestamp, new HttpUtils<DoorLockStatisticsBean>().getBodyToken(bean, timestamp))
                 .subscribeOn(Schedulers.io())
                 .compose(RxjavaHelper.observeOnMainThread());
     }

    /**
     * 获取门锁7天统计
     */
    public static Observable<GetStatisticsSevenDayResult> getDoorLockDtatisticsSevenDay(@NonNull String uid, @NonNull String wifiSN){
        DoorLockStatisticsBean bean = new DoorLockStatisticsBean();
        bean.setUid(uid);
        bean.setWifiSN(wifiSN);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getDoorLockDtatisticsSevenDay(timestamp, new HttpUtils<DoorLockStatisticsBean>().getBodyToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 获取门锁当天统计
     */
    public static Observable<GetProductionModelListResult> getProductionModelList(){
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getProductionModelList(timestamp, new HttpUtils<>().getBodyToken(null, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }


    /**
     * 获取设备列表
     */
    public static Observable<AllBindDevices> getAllBindDevice(String uid , int modelSearchType){
        AllBindDeviceBean bean = new AllBindDeviceBean();
        bean.setUid(uid);
        bean.setModelSearchType(modelSearchType);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .getAllBindDevice(timestamp, new HttpUtils<>().getBodyToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 天猫精灵扫码认证
     */
    public static Observable<TmallDeviceListResult> aligenieUserlogin(String uid ,  TmallDeviceBean.Code code){
        TmallDeviceBean bean = new TmallDeviceBean();
        bean.setUid(uid);
        bean.setCode(code);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .aligenieUserlogin(timestamp, new HttpUtils<>().getBodyToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 天猫精灵用户分享设备
     */
    public static Observable<TmallAddDeviceResult> aligenieUserDeviceShare(String uid , String nickName, TmallDeviceListBean tmallDeviceListBean){
        TmallAddDeviceBean bean = new TmallAddDeviceBean();
        bean.setUid(uid);
        bean.setWifiSN(tmallDeviceListBean.getWifiSN());
        bean.setDeviceOpenId(tmallDeviceListBean.getDeviceOpenId());
        bean.setUserOpenId(tmallDeviceListBean.getUserOpenId());
        bean.setNickName(nickName);
        bean.setAligenieDeviceModel(tmallDeviceListBean.getAligenieDeviceModel());
        bean.setAligenieMac(tmallDeviceListBean.getAligenieMac());
        bean.setAligenieSN(tmallDeviceListBean.getAligenieSN());
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .aligenieUserDeviceShare(timestamp, new HttpUtils<>().getBodyToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 查询用户分享猫精app的设备
     */
    public static Observable<TmallQueryDeviceListResult> aligenieUserDeviceShareQuery(String sn, String uid ){
        TmallQueryDeviceBean bean = new TmallQueryDeviceBean();
        bean.setUid(uid);
        bean.setWifiSN(sn);
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .aligenieUserDeviceShareQuery(timestamp, new HttpUtils<>().getBodyToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }

    /**
     * 天猫精灵删除分享设备
     */
    public static Observable<TmallAddDeviceResult> aligenieUserDeviceShareDel(TmallQueryDeviceListResult.TmallQueryDeviceList tmallQueryDeviceList , String uid){
        TmallDelDeviceBean bean = new TmallDelDeviceBean();
        bean.setUid(uid);
        bean.setWifiSN(tmallQueryDeviceList.getWifiSN());
        bean.setDeviceOpenId(tmallQueryDeviceList.getDeviceOpenId());
        bean.setUserOpenId(tmallQueryDeviceList.getUserOpenId());
        String timestamp = System.currentTimeMillis()/1000 + "";
        return RetrofitServiceManager.getInstance().create(IXiaoKaiNewService.class)
                .aligenieUserDeviceShareDel(timestamp, new HttpUtils<>().getBodyToken(bean, timestamp))
                .subscribeOn(Schedulers.io())
                .compose(RxjavaHelper.observeOnMainThread());
    }
}
