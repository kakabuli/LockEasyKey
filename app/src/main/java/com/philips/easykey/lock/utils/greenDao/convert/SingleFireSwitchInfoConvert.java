package com.philips.easykey.lock.utils.greenDao.convert;

import com.google.gson.Gson;
import com.philips.easykey.lock.publiclibrary.bean.SingleFireSwitchInfo;
import org.greenrobot.greendao.converter.PropertyConverter;


/**
 * Created by hushucong
 * on 2020/6/11
 */
public class SingleFireSwitchInfoConvert implements PropertyConverter<SingleFireSwitchInfo, String>{

    @Override
    public SingleFireSwitchInfo convertToEntityProperty(String databaseValue) {
        return new Gson().fromJson(databaseValue,SingleFireSwitchInfo.class);
    }

    @Override
    public String convertToDatabaseValue(SingleFireSwitchInfo entityProperty) {
        return new Gson().toJson(entityProperty);
    }


}