package com.philips.easykey.lock.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.login.PhilipsLoginActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

/**
 * author :
 * time   : 2021/6/10
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mApi;
    private MyHandler mMyHandler;

    private final String APP_ID = "wx2424a66f6c8a94df";
    private final String APP_SECRET = "e8195f689fd5d0b409935ed32487642d";

    private static class MyHandler extends Handler {
        private final WeakReference<WXEntryActivity> wxEntryActivityWeakReference;

        public MyHandler(WXEntryActivity wxEntryActivity){
            wxEntryActivityWeakReference = new WeakReference<>(wxEntryActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            int tag = msg.what;
            if (tag == NetworkUtil.GET_TOKEN) {
                Bundle data = msg.getData();
                JSONObject json;
                try {
                    json = new JSONObject(data.getString("result"));
                    String openId, accessToken, refreshToken, scope;
                    openId = json.getString("openid");
                    accessToken = json.getString("access_token");
                    refreshToken = json.getString("refresh_token");
                    scope = json.getString("scope");
                    LogUtils.d(data.getString("result"));
                    Intent intent = new Intent(wxEntryActivityWeakReference.get(), PhilipsLoginActivity.class);
                    intent.putExtra("openId", openId);
                    intent.putExtra("accessToken", accessToken);
                    intent.putExtra("refreshToken", refreshToken);
                    intent.putExtra("scope", scope);
                    wxEntryActivityWeakReference.get().startActivity(intent);
                } catch (JSONException e) {
                    LogUtils.e(e.getMessage());
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        mMyHandler = new MyHandler(this);

        try {
            Intent intent = getIntent();
            mApi.handleIntent(intent, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        if (req.getType() == ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX) {
            goToGetMsg();
        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        int result;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.philips_errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.philips_errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.philips_errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.philips_errcode_unsupported;
                break;
            default:
                result = R.string.philips_errcode_unknown;
                break;
        }

        ToastUtils.showShort(getString(result) + ", type=" + resp.getType());
        LogUtils.d("type=" + resp.getType() + "openId=" + resp.openId);

        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            SendAuth.Resp authResp = (SendAuth.Resp)resp;
            String state = authResp.state;
            final String code = authResp.code;
            NetworkUtil.sendWxAPI(mMyHandler, String.format("https://api.weixin.qq.com/sns/oauth2/access_token?" +
                            "appid=%s&secret=%s&code=%s&grant_type=authorization_code", APP_ID, APP_SECRET, code),
                    NetworkUtil.GET_TOKEN);
        }
        finish();
    }

    private void goToGetMsg() {
//        Intent intent = new Intent(this, GetFromWXActivity.class);
//        intent.putExtras(getIntent());
//        startActivity(intent);
//        finish();
    }


}
