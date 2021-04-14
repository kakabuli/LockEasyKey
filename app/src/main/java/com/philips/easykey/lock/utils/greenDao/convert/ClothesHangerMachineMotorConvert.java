package com.philips.easykey.lock.utils.greenDao.convert;

import com.alibaba.fastjson.JSON;
import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineHangerStateBean;
import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineMotorBean;

import org.greenrobot.greendao.converter.PropertyConverter;


public class ClothesHangerMachineMotorConvert implements PropertyConverter<ClothesHangerMachineMotorBean, String>{


    @Override
    public ClothesHangerMachineMotorBean convertToEntityProperty(String databaseValue) {
        return JSON.parseObject(databaseValue,ClothesHangerMachineMotorBean.class);

    }

    @Override
    public String convertToDatabaseValue(ClothesHangerMachineMotorBean entityProperty) {
        return JSON.toJSONString(entityProperty);
    }

}