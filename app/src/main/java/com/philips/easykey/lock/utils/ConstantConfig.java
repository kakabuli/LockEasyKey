package com.philips.easykey.lock.utils;

import com.philips.easykey.lock.BuildConfig;

public class ConstantConfig {

    public static final String HTTP_BASE_URL = BuildConfig.HTTP_HOST;//philips正式服务器
    public static final String OTA_INFO_URL = BuildConfig.HTTP_HOST;//philips正式服务器
    public static final String OTA_RESULT_UPLOAD_URL = OTA_INFO_URL + "ota/btResultAdd";  //正式OTA上报服务器
    public static final String MQTT_BASE_URL = BuildConfig.MQTT_HOST;//正式服务器
    public static final String LINPHONE_URL = BuildConfig.SIP_HOST;//正式sip

    public static final String PHILIPS_PRIVACY_POLICY = "https://app.cone-x.com/mobile_h5/Philips_Android_Privacy_Policy_EasyKey+";
    public static final String PHILIPS_TERMS_OF_USE = "https://app.cone-x.com/mobile_h5/Philips_Terms_of_Use";
    public static final String PHILPS_DURRESS_HELP = "https://app.cone-x.com/mobile_h5/Philps_Durress_Help";
    public static final String PHILIPS_UPDATE_ADMIN_PASSWORD = "https://app.cone-x.com/mobile_h5/Philips_Update_Admin_Password";
    public static final String PHILIPS_ADD_SCAN_DEVICE = "https://app.cone-x.com/mobile_h5/Philips_Add_Scan_Device";
    public static final String PHILIPS_FQA = "https://app.cone-x.com/mobile_h5/Philips_FQA/";
}
