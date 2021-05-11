package com.philips.easykey.lock.utils.greenDao.convert;

import com.google.gson.Gson;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAliveTimeBean;

import org.greenrobot.greendao.converter.PropertyConverter;


public class WifiVideoAliveTimeBeanConvert implements PropertyConverter<WifiVideoLockAliveTimeBean, String>{


    @Override
    public WifiVideoLockAliveTimeBean convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue,WifiVideoLockAliveTimeBean.class);
    }

    @Override
    public String convertToDatabaseValue(WifiVideoLockAliveTimeBean entityProperty) {
        return new Gson().toJson(entityProperty);
    }

}