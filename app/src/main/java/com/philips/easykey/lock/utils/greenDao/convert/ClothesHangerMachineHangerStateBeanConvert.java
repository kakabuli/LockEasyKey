package com.philips.easykey.lock.utils.greenDao.convert;

import com.alibaba.fastjson.JSON;
import com.philips.easykey.lock.publiclibrary.bean.ClothesHangerMachineHangerStateBean;

import org.greenrobot.greendao.converter.PropertyConverter;


public class ClothesHangerMachineHangerStateBeanConvert implements PropertyConverter<ClothesHangerMachineHangerStateBean, String>{


    @Override
    public ClothesHangerMachineHangerStateBean convertToEntityProperty(String databaseValue) {
        return JSON.parseObject(databaseValue,ClothesHangerMachineHangerStateBean.class);

    }

    @Override
    public String convertToDatabaseValue(ClothesHangerMachineHangerStateBean entityProperty) {
        return JSON.toJSONString(entityProperty);
    }

}