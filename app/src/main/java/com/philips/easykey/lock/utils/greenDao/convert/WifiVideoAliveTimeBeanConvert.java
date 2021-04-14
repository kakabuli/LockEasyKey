package com.philips.easykey.lock.utils.greenDao.convert;

import com.alibaba.fastjson.JSON;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAliveTimeBean;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockSetPirBean;

import org.greenrobot.greendao.converter.PropertyConverter;


public class WifiVideoAliveTimeBeanConvert implements PropertyConverter<WifiVideoLockAliveTimeBean, String>{


    @Override
    public WifiVideoLockAliveTimeBean convertToEntityProperty(String databaseValue) {
        return JSON.parseObject(databaseValue,WifiVideoLockAliveTimeBean.class);

    }

    @Override
    public String convertToDatabaseValue(WifiVideoLockAliveTimeBean entityProperty) {
        return JSON.toJSONString(entityProperty);
    }

}