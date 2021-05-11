package com.philips.easykey.lock.utils.greenDao.convert;

import com.google.gson.Gson;
import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineLightingBean;

import org.greenrobot.greendao.converter.PropertyConverter;


public class ClothesHangerMachineLightConvert implements PropertyConverter<ClothesHangerMachineLightingBean, String>{


    @Override
    public ClothesHangerMachineLightingBean convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue,ClothesHangerMachineLightingBean.class);

    }

    @Override
    public String convertToDatabaseValue(ClothesHangerMachineLightingBean entityProperty) {
        return new Gson().toJson(entityProperty);
    }

}