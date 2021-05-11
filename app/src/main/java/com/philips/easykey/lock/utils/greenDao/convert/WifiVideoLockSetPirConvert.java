package com.philips.easykey.lock.utils.greenDao.convert;

import com.google.gson.Gson;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockSetPirBean;

import org.greenrobot.greendao.converter.PropertyConverter;


public class WifiVideoLockSetPirConvert implements PropertyConverter<WifiVideoLockSetPirBean, String>{


    @Override
    public WifiVideoLockSetPirBean convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue,WifiVideoLockSetPirBean.class);
    }

    @Override
    public String convertToDatabaseValue(WifiVideoLockSetPirBean entityProperty) {
        return new Gson().toJson(entityProperty);
    }

}