package com.philips.easykey.lock.mvp.view.gatewaylockview;

import com.philips.easykey.lock.utils.greenDao.bean.GatewayPasswordPlanBean;

import java.util.List;

public interface IGatewayLockPasswordManagerView extends IGatewayLockPasswordView  {
    /**
     * 正在同步密码计划
     */
    void isSyncPlan();


}
