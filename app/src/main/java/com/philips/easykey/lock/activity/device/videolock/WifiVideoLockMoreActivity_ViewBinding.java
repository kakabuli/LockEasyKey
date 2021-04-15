// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockMoreActivity_ViewBinding implements Unbinder {
  private WifiVideoLockMoreActivity target;

  @UiThread
  public WifiVideoLockMoreActivity_ViewBinding(WifiVideoLockMoreActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockMoreActivity_ViewBinding(WifiVideoLockMoreActivity target, View source) {
    this.target = target;

    target.rlDeviceName = Utils.findRequiredViewAsType(source, R.id.rl_device_name, "field 'rlDeviceName'", RelativeLayout.class);
    target.ivMessageFree = Utils.findRequiredViewAsType(source, R.id.iv_message_free, "field 'ivMessageFree'", ImageView.class);
    target.rlMessageFree = Utils.findRequiredViewAsType(source, R.id.rl_message_free, "field 'rlMessageFree'", RelativeLayout.class);
    target.rlSafeMode = Utils.findRequiredViewAsType(source, R.id.rl_safe_mode, "field 'rlSafeMode'", RelativeLayout.class);
    target.ivSafeMode = Utils.findRequiredViewAsType(source, R.id.iv_safe_mode, "field 'ivSafeMode'", TextView.class);
    target.rlAm = Utils.findRequiredViewAsType(source, R.id.rl_am, "field 'rlAm'", RelativeLayout.class);
    target.ivAm = Utils.findRequiredViewAsType(source, R.id.iv_am, "field 'ivAm'", TextView.class);
    target.rlPowerSave = Utils.findRequiredViewAsType(source, R.id.rl_powerSave, "field 'rlPowerSave'", RelativeLayout.class);
    target.ivPowerSave = Utils.findRequiredViewAsType(source, R.id.iv_powerSave, "field 'ivPowerSave'", TextView.class);
    target.rlDoorLockLanguageSwitch = Utils.findRequiredViewAsType(source, R.id.rl_door_lock_language_switch, "field 'rlDoorLockLanguageSwitch'", RelativeLayout.class);
    target.ivSilentMode = Utils.findRequiredViewAsType(source, R.id.iv_silent_mode, "field 'ivSilentMode'", ImageView.class);
    target.rlSilentMode = Utils.findRequiredViewAsType(source, R.id.rl_silent_mode, "field 'rlSilentMode'", RelativeLayout.class);
    target.rlDeviceInformation = Utils.findRequiredViewAsType(source, R.id.rl_device_information, "field 'rlDeviceInformation'", RelativeLayout.class);
    target.rlCheckFirmwareUpdate = Utils.findRequiredViewAsType(source, R.id.rl_check_firmware_update, "field 'rlCheckFirmwareUpdate'", RelativeLayout.class);
    target.btnDelete = Utils.findRequiredViewAsType(source, R.id.btn_delete, "field 'btnDelete'", Button.class);
    target.tvDeviceName = Utils.findRequiredViewAsType(source, R.id.tv_device_name, "field 'tvDeviceName'", TextView.class);
    target.rlCheckFaceOta = Utils.findRequiredViewAsType(source, R.id.rl_check_face_ota, "field 'rlCheckFaceOta'", RelativeLayout.class);
    target.back = Utils.findRequiredViewAsType(source, R.id.back, "field 'back'", ImageView.class);
    target.tvLanguage = Utils.findRequiredViewAsType(source, R.id.tv_language, "field 'tvLanguage'", TextView.class);
    target.headTitle = Utils.findRequiredViewAsType(source, R.id.head_title, "field 'headTitle'", TextView.class);
    target.wifiName = Utils.findRequiredViewAsType(source, R.id.wifi_name, "field 'wifiName'", TextView.class);
    target.rlWifiName = Utils.findRequiredViewAsType(source, R.id.rl_wifi_name, "field 'rlWifiName'", RelativeLayout.class);
    target.rlMessagePush = Utils.findRequiredViewAsType(source, R.id.rl_message_push, "field 'rlMessagePush'", RelativeLayout.class);
    target.rlWanderingAlarm = Utils.findRequiredViewAsType(source, R.id.rl_wandering_alarm, "field 'rlWanderingAlarm'", RelativeLayout.class);
    target.tvWanderingAlarmRight = Utils.findRequiredViewAsType(source, R.id.tv_wandering_alarm_right, "field 'tvWanderingAlarmRight'", TextView.class);
    target.rlRealTimeVideo = Utils.findRequiredViewAsType(source, R.id.rl_real_time_video, "field 'rlRealTimeVideo'", RelativeLayout.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi, "field 'avi'", AVLoadingIndicatorView.class);
    target.tvTips = Utils.findRequiredViewAsType(source, R.id.tv_tips, "field 'tvTips'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockMoreActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rlDeviceName = null;
    target.ivMessageFree = null;
    target.rlMessageFree = null;
    target.rlSafeMode = null;
    target.ivSafeMode = null;
    target.rlAm = null;
    target.ivAm = null;
    target.rlPowerSave = null;
    target.ivPowerSave = null;
    target.rlDoorLockLanguageSwitch = null;
    target.ivSilentMode = null;
    target.rlSilentMode = null;
    target.rlDeviceInformation = null;
    target.rlCheckFirmwareUpdate = null;
    target.btnDelete = null;
    target.tvDeviceName = null;
    target.rlCheckFaceOta = null;
    target.back = null;
    target.tvLanguage = null;
    target.headTitle = null;
    target.wifiName = null;
    target.rlWifiName = null;
    target.rlMessagePush = null;
    target.rlWanderingAlarm = null;
    target.tvWanderingAlarmRight = null;
    target.rlRealTimeVideo = null;
    target.avi = null;
    target.tvTips = null;
  }
}
