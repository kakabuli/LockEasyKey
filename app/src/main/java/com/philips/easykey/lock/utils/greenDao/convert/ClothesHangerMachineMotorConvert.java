package com.philips.easykey.lock.utils.greenDao.convert;

import com.google.gson.Gson;
import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineMotorBean;

import org.greenrobot.greendao.converter.PropertyConverter;


public class ClothesHangerMachineMotorConvert implements PropertyConverter<ClothesHangerMachineMotorBean, String>{


    @Override
    public ClothesHangerMachineMotorBean convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue,ClothesHangerMachineMotorBean.class);

    }

    @Override
    public String convertToDatabaseValue(ClothesHangerMachineMotorBean entityProperty) {
        return new Gson().toJson(entityProperty);
    }

}