package com.philips.easykey.lock.utils.greenDao.convert;

import com.alibaba.fastjson.JSON;
import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineLightingBean;
import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineMotorBean;

import org.greenrobot.greendao.converter.PropertyConverter;


public class ClothesHangerMachineLightConvert implements PropertyConverter<ClothesHangerMachineLightingBean, String>{


    @Override
    public ClothesHangerMachineLightingBean convertToEntityProperty(String databaseValue) {
        return JSON.parseObject(databaseValue,ClothesHangerMachineLightingBean.class);

    }

    @Override
    public String convertToDatabaseValue(ClothesHangerMachineLightingBean entityProperty) {
        return JSON.toJSONString(entityProperty);
    }

}