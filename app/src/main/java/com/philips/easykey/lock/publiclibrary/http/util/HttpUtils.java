package com.philips.easykey.lock.publiclibrary.http.util;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.philips.easykey.lock.BuildConfig;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.rxutils.TimeOutException;
import com.philips.easykey.lock.utils.AES;
import com.blankj.utilcode.util.LogUtils;


import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.RequestBody;
import retrofit2.HttpException;

public class HttpUtils<T> {

    public RequestBody getBodyToken(T t,String timestamp) {
        String obj = new Gson().toJson(t);
        String contentType = "application/json; charset=utf-8";
        if(Integer.parseInt(BuildConfig.HTTP_VERSION) > 0){
            if(BuildConfig.DEBUG)
            LogUtils.d("shulan getBodyToken http body 加密前--->" + obj);
            if(BuildConfig.DEBUG)
            LogUtils.d("shulan getBodyToken timestamp--->" + timestamp);
            try {
                if (!TextUtils.isEmpty(MyApplication.getInstance().getToken())) {
                    obj = AES.Encrypt(obj,AES.keyForToken(MyApplication.getInstance().getToken(),AES.key,timestamp));
                }else{
                    obj = AES.Encrypt(obj,AES.keyNoToken(AES.key,timestamp));
                }
                if(!TextUtils.isEmpty(obj))
                    contentType = "text/plain; charset=utf-8";
                if(BuildConfig.DEBUG)
                LogUtils.d("shulan getBodyToken http body 加密后--->" + obj);
                if(BuildConfig.DEBUG)
                LogUtils.d("shulan getBodyToken http body 解密后后--->" + AES.Decrypt(obj,AES.keyForToken(MyApplication.getInstance().getToken(),AES.key,timestamp)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse(contentType), obj);
        return body;
    }

    public RequestBody getBodyNoToken(T t,String timestamp){
        String obj = new Gson().toJson(t);
        String contentType = "application/json; charset=utf-8";
        if(Integer.parseInt(BuildConfig.HTTP_VERSION) > 0){
            if(BuildConfig.DEBUG)
            LogUtils.d("shulan getBodyNoToken http body 加密前--->" + obj);
            if(BuildConfig.DEBUG)
            LogUtils.d("shulan getBodyNoToken timestamp--->" + timestamp);
            try {
                obj = AES.Encrypt(obj,AES.keyNoToken(AES.key,timestamp));
                if(!TextUtils.isEmpty(obj))
                    contentType = "text/plain; charset=utf-8";
                if(BuildConfig.DEBUG)
                LogUtils.d("shulan getBodyNoToken http body 加密后--->" + obj);
                if(BuildConfig.DEBUG)
                LogUtils.d("shulan getBodyNoToken http body 解密后后--->" + AES.Decrypt(obj,AES.keyNoToken(AES.key,timestamp)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse(contentType), obj);
        return body;
    }

    public RequestBody getBody(T t) {
        String obj = new Gson().toJson(t);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), obj);
        return body;
    }


    public static String httpErrorCode(Activity activity, String errorMsg) {
        int code = Integer.parseInt(errorMsg);
        switch (code) {
            case 100:
                errorMsg = activity.getString(R.string.philips_code_account_or_password_empty);
                break;
            case 101:
                errorMsg = activity.getString(R.string.philips_code_account_or_password_error);
                break;
            case 102:
                errorMsg = activity.getString(R.string.philips_code_sn_wrong);
                break;
            case 113:
                errorMsg = activity.getString(R.string.philips_code_update_mission_do_not_exist);
                break;
            case 201:
                errorMsg = activity.getString(R.string.philips_code_not_bind);
                break;
            case 202:
                errorMsg = activity.getString(R.string.philips_code_has_bind);
                break;
            case 204:
                errorMsg = activity.getString(R.string.philips_code_register_account_fail);
                break;
            case 208:
                errorMsg = activity.getString(R.string.philips_code_modify_password_fail);
                break;
            case 210:
                errorMsg = activity.getString(R.string.philips_code_no_result);
                break;
            case 301:
                errorMsg = activity.getString(R.string.philips_code_character_length_exceeds_limit);
                break;
            case 302:
                errorMsg = activity.getString(R.string.philips_code_liu_yan_fail);
                break;
            case 303:
                errorMsg = activity.getString(R.string.philips_code_security_key_full);
                break;
            case 400:
                errorMsg = activity.getString(R.string.philips_code_request_data_format_incorrect);
                break;
            case 401:
                errorMsg = activity.getString(R.string.philips_code_data_parameter_incorrect);
                break;
            case 402:
                errorMsg = activity.getString(R.string.philips_code_to_empower_the_user_not_found);
                break;
            case 403:
                errorMsg = activity.getString(R.string.philips_code_device_not_found);
                break;
            case 404:
                errorMsg = activity.getString(R.string.philips_code_res_not_found);
                break;
            case 405:
                errorMsg = activity.getString(R.string.philips_code_user_repeat_register);
                break;
            case 406:
                errorMsg = activity.getString(R.string.philips_code_not_show_verification);
                break;
            case 407:
                errorMsg = activity.getString(R.string.philips_code_mqtt_server_connect_fail);
                break;
            case 408:
                errorMsg = activity.getString(R.string.philips_code_virification_error);
                break;
            case 409:
                errorMsg = activity.getString(R.string.philips_code_device_repeat_register);
                break;
            case 410:
                errorMsg = activity.getString(R.string.philips_code_user_not_exist);
                break;
            case 411:
                errorMsg = activity.getString(R.string.philips_code_to_empower_the_user_not_found);
                break;
            case 412:
                errorMsg = activity.getString(R.string.philips_code_device_register_fail_repeat_record);
                break;
            case 413:
                errorMsg = activity.getString(R.string.philips_code_image_size_too_large);
                break;
            case 414:
                errorMsg = activity.getString(R.string.philips_code_do_not_register_user_memenet);
                break;
            case 415:
                errorMsg = activity.getString(R.string.philips_code_device_register_fail_duplicate_record);
                break;
            case 416:
                errorMsg=activity.getString(R.string.philips_code_user_exsit);
                break;
            case 419:
                errorMsg = activity.getString(R.string.philips_code_not_peremeter_peremeter_not_match);
                break;
            case 431:
                errorMsg = activity.getString(R.string.philips_code_upload_pushid_fail);
                break;
            case 433:
                errorMsg = activity.getString(R.string.philips_code_not_manager);
                break;
            case 435:
                errorMsg = activity.getString(R.string.philips_code_password_only_number_and_letter_6_15);
                break;
            case 436:
                errorMsg = activity.getString(R.string.philips_code_data_not_map);
                break;
    /*      case 444:
                errorMsg = activity.getString(R.string.not_login);
                break;*/
            case 445:
                errorMsg = activity.getString(R.string.philips_code_invalid_random_code);
                break;
            case 499:
                errorMsg = activity.getString(R.string.philips_code_too_many_request);
                break;
            case 500:
                errorMsg = activity.getString(R.string.philips_code_server_error);
                break;
            case 501:
                errorMsg = activity.getString(R.string.philips_code_business_process_fail);
                break;
            case 509:
                errorMsg = activity.getString(R.string.philips_code_server_process_timeout);
                break;
            case 539:
                errorMsg = activity.getString(R.string.philips_code_api_interface_update_fail);
                break;
            case 567:
                errorMsg = activity.getString(R.string.philips_code_failed_to_register_users_in_batch);
                break;
            case 592:
                errorMsg = activity.getString(R.string.philips_code_sn_password_mac_map_incorrect);
                break;
            case 595:
                errorMsg = activity.getString(R.string.philips_code_information_verification_fail);
                break;
            case 601:
                errorMsg = activity.getString(R.string.philips_code_get_nickname_fail);
                break;
            case 602:
                errorMsg = activity.getString(R.string.philips_code_modify_push_switch_fail);
                break;
            case 603:
                errorMsg = activity.getString(R.string.philips_code_modify_nickname_fail);
                break;
            case 607:
                errorMsg = activity.getString(R.string.philips_code_upload_file_fail);
                break;
            case 704:
                errorMsg = activity.getString(R.string.philips_code_verification_send_too_many);
                break;
            case 711:
                errorMsg = activity.getString(R.string.philips_code_register_memenet_fail);
                break;
            case 780:
                errorMsg = activity.getString(R.string.philips_code_logout_fail);
                break;
            case 781:
                errorMsg = activity.getString(R.string.philips_code_add_device_fail);
                break;
            case 782:
                errorMsg = activity.getString(R.string.philips_code_operation_fail);
                break;
            case 785:
                errorMsg = activity.getString(R.string.philips_code_open_lock_fail);
                break;
            case 801:
                errorMsg = activity.getString(R.string.philips_code_malicious_login);
                break;
            case 803:
                errorMsg = activity.getString(R.string.philips_code_open_lock_fail_not_permission);
                break;
            case 806:
                errorMsg = activity.getString(R.string.philips_code_illegal_data);
                break;
            case 812:
                errorMsg = activity.getString(R.string.philips_code_has_nodification_manager_confirm);
                break;
            case 813:
                errorMsg = activity.getString(R.string.philips_code_has_bind_gateway);
                break;
            case 816:
                errorMsg = activity.getString(R.string.philips_code_approval_fail);
                break;
            case 819:
                errorMsg = activity.getString(R.string.philips_code_get_list_fail);
                break;
            case 820:
                errorMsg = activity.getString(R.string.philips_code_get_gateway_device_list_fail);
                break;
            case 823:
                errorMsg = activity.getString(R.string.philips_code_get_open_lock_record_fail);
                break;
            case 845:
                errorMsg = activity.getString(R.string.philips_code_unbind_fail);
                break;
            case 847:
                errorMsg = activity.getString(R.string.philips_code_not_gateway_manager);
                break;
            case 871:
                errorMsg = activity.getString(R.string.philips_code_bind_gateway_fail);
                break;
            case 946:
                errorMsg = activity.getString(R.string.philips_code_mimi_bind_gateway_fail);
                break;
            case 947:
                errorMsg = activity.getString(R.string.philips_code_approval_mimi_bind_gateway_fail);
                break;
            case 991:
                errorMsg = activity.getString(R.string.philips_code_production_fail);
                break;
            case 998:
                errorMsg = activity.getString(R.string.philips_code_no_permission);
                break;
            case 999:
                errorMsg = activity.getString(R.string.philips_code_resource_not_find);
                break;
        }
        return errorMsg;
    }

    public static String httpProtocolErrorCode(Activity activity, Throwable e) {
        String errorMsg = "";
        if (e instanceof IOException) {
            /** 没有网络 */
            errorMsg = activity.getString(R.string.network_exception);
        } else if (e instanceof OtherException) {
            /** 网络正常，http 请求成功，服务器返回逻辑错误  接口返回的别的状态码处理*/
            OtherException otherException = (OtherException) e;
            if (otherException.getResponse().getCode() == 444) { //Token过期
                LogUtils.d("token过期   " + Thread.currentThread().getName());
                if (MyApplication.getInstance().getMqttService()!=null){
                    MyApplication.getInstance().getMqttService().httpMqttDisconnect();
                }
                MyApplication.getInstance().tokenInvalid(true);
            } else {
                errorMsg = activity.getString(R.string.server_back_exception) + ((OtherException) e).getResponse().toString();
            }

        } else if (e instanceof SocketTimeoutException) { //连接超时
            errorMsg = activity.getString(R.string.connect_out_of_date);
        } else if (e instanceof TimeOutException) { //连接超时
            errorMsg = activity.getString(R.string.tiem_out);
        } else if (e instanceof ConnectException) { //连接异常
            errorMsg = activity.getString(R.string.connect_exception);
        } else if (e instanceof UnknownHostException) { //无法识别主机  网络异常
            errorMsg = activity.getString(R.string.unable_identify_host);
        } else if (e instanceof HttpException) {
            /** 网络异常，http 请求失败，即 http 状态码不在 [200, 300) 之间, such as: "server internal error". 协议异常处理*/
            errorMsg = ((HttpException) e).response().message();
            HttpException httpException = (HttpException) e;
            errorMsg = activity.getString(R.string.http_exception) + httpException.code() + activity.getString(R.string.exception) + httpException.message();
        } else {
            /** 其他未知错误 */
            errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : "unknown error";
        }
        LogUtils.d("  网络请求   " + errorMsg);
        return errorMsg;
    }
}
