// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockCallingActivity_ViewBinding implements Unbinder {
  private WifiVideoLockCallingActivity target;

  private View view2131296696;

  private View view2131296779;

  private View view2131296792;

  private View view2131296334;

  private View view2131296785;

  private View view2131296755;

  private View view2131296703;

  private View view2131296778;

  private View view2131296693;

  private View view2131296774;

  private View view2131296918;

  private View view2131296802;

  @UiThread
  public WifiVideoLockCallingActivity_ViewBinding(WifiVideoLockCallingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockCallingActivity_ViewBinding(final WifiVideoLockCallingActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.iv_answer_icon, "field 'ivAnswerIcon' and method 'onViewClicked'");
    target.ivAnswerIcon = Utils.castView(view, R.id.iv_answer_icon, "field 'ivAnswerIcon'", ImageView.class);
    view2131296696 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_refuse_icon, "field 'ivRefuseIcon' and method 'onViewClicked'");
    target.ivRefuseIcon = Utils.castView(view, R.id.iv_refuse_icon, "field 'ivRefuseIcon'", ImageView.class);
    view2131296779 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.ivRefuseIcon1 = Utils.findRequiredViewAsType(source, R.id.iv_refuse_icon_1, "field 'ivRefuseIcon1'", ImageView.class);
    target.tvAnswer = Utils.findRequiredViewAsType(source, R.id.tv_answer, "field 'tvAnswer'", TextView.class);
    target.tvRefuse1 = Utils.findRequiredViewAsType(source, R.id.tv_refuse_1, "field 'tvRefuse1'", TextView.class);
    target.tvRefuse = Utils.findRequiredViewAsType(source, R.id.tv_refuse, "field 'tvRefuse'", TextView.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi, "field 'avi'", AVLoadingIndicatorView.class);
    target.tvTips = Utils.findRequiredViewAsType(source, R.id.tv_tips, "field 'tvTips'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_setting, "field 'ivSetting' and method 'onViewClicked'");
    target.ivSetting = Utils.castView(view, R.id.iv_setting, "field 'ivSetting'", ImageView.class);
    view2131296792 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.back, "field 'back' and method 'onViewClicked'");
    target.back = Utils.castView(view, R.id.back, "field 'back'", ImageView.class);
    view2131296334 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mSufaceView = Utils.findRequiredViewAsType(source, R.id.surface_view, "field 'mSufaceView'", SurfaceView.class);
    target.tvTemporaryPassword = Utils.findRequiredViewAsType(source, R.id.tv_temporary_password, "field 'tvTemporaryPassword'", TextView.class);
    target.llyTemporaryPassword = Utils.findRequiredViewAsType(source, R.id.lly_temporary_password, "field 'llyTemporaryPassword'", LinearLayout.class);
    target.rlVideoLayout = Utils.findRequiredViewAsType(source, R.id.rl_video_layout, "field 'rlVideoLayout'", RelativeLayout.class);
    target.rlMarkLayout = Utils.findRequiredViewAsType(source, R.id.rl_mark_layout, "field 'rlMarkLayout'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.iv_screenshot, "field 'ivScreenshot' and method 'onViewClicked'");
    target.ivScreenshot = Utils.castView(view, R.id.iv_screenshot, "field 'ivScreenshot'", ImageView.class);
    view2131296785 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_mute, "field 'ivMute' and method 'onViewClicked'");
    target.ivMute = Utils.castView(view, R.id.iv_mute, "field 'ivMute'", ImageView.class);
    view2131296755 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_calling, "field 'ivCalling' and method 'onViewClicked'");
    target.ivCalling = Utils.castView(view, R.id.iv_calling, "field 'ivCalling'", ImageView.class);
    view2131296703 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_recoring, "field 'ivRecoring' and method 'onViewClicked'");
    target.ivRecoring = Utils.castView(view, R.id.iv_recoring, "field 'ivRecoring'", ImageView.class);
    view2131296778 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_album, "field 'ivAlbum' and method 'onViewClicked'");
    target.ivAlbum = Utils.castView(view, R.id.iv_album, "field 'ivAlbum'", ImageView.class);
    view2131296693 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.llyRecord = Utils.findRequiredViewAsType(source, R.id.lly_record, "field 'llyRecord'", RelativeLayout.class);
    target.ivScreenshotBitmap = Utils.findRequiredViewAsType(source, R.id.iv_screenshot_bitmap, "field 'ivScreenshotBitmap'", ImageView.class);
    target.ivRecordSpot = Utils.findRequiredViewAsType(source, R.id.iv_record_spot, "field 'ivRecordSpot'", ImageView.class);
    target.tvTime = Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_real_time_refuse_icon, "field 'ivRealTimeRefuseIcon' and method 'onViewClicked'");
    target.ivRealTimeRefuseIcon = Utils.castView(view, R.id.iv_real_time_refuse_icon, "field 'ivRealTimeRefuseIcon'", ImageView.class);
    view2131296774 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.rlRealTime = Utils.findRequiredViewAsType(source, R.id.rl_real_time, "field 'rlRealTime'", RelativeLayout.class);
    target.rlCallingTime = Utils.findRequiredViewAsType(source, R.id.rl_calling_time, "field 'rlCallingTime'", RelativeLayout.class);
    target.ivHeadPic = Utils.findRequiredViewAsType(source, R.id.iv_head_pic, "field 'ivHeadPic'", ImageView.class);
    target.ivBigHeadPic = Utils.findRequiredViewAsType(source, R.id.iv_big_head_pic, "field 'ivBigHeadPic'", ImageView.class);
    target.tvHeadPic = Utils.findRequiredViewAsType(source, R.id.tv_head_pic, "field 'tvHeadPic'", TextView.class);
    target.tvBigHeadPic = Utils.findRequiredViewAsType(source, R.id.tv_big_head_pic, "field 'tvBigHeadPic'", TextView.class);
    target.tvVideoTimeStamp = Utils.findRequiredViewAsType(source, R.id.tv_video_timestamp, "field 'tvVideoTimeStamp'", TextView.class);
    target.tvCallingTips = Utils.findRequiredViewAsType(source, R.id.tv_calling_tip, "field 'tvCallingTips'", TextView.class);
    target.tvDoorbell = Utils.findRequiredViewAsType(source, R.id.tv_doorbell, "field 'tvDoorbell'", TextView.class);
    target.ivCache = Utils.findRequiredViewAsType(source, R.id.iv_cache, "field 'ivCache'", ImageView.class);
    target.rlTitleBar = Utils.findRequiredViewAsType(source, R.id.rl_title_bar, "field 'rlTitleBar'", RelativeLayout.class);
    target.titleBar = Utils.findRequiredViewAsType(source, R.id.title_bar, "field 'titleBar'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.mark_back, "method 'onViewClicked'");
    view2131296918 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_temporary_pwd, "method 'onViewClicked'");
    view2131296802 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockCallingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivAnswerIcon = null;
    target.ivRefuseIcon = null;
    target.ivRefuseIcon1 = null;
    target.tvAnswer = null;
    target.tvRefuse1 = null;
    target.tvRefuse = null;
    target.avi = null;
    target.tvTips = null;
    target.ivSetting = null;
    target.back = null;
    target.mSufaceView = null;
    target.tvTemporaryPassword = null;
    target.llyTemporaryPassword = null;
    target.rlVideoLayout = null;
    target.rlMarkLayout = null;
    target.ivScreenshot = null;
    target.ivMute = null;
    target.ivCalling = null;
    target.ivRecoring = null;
    target.ivAlbum = null;
    target.llyRecord = null;
    target.ivScreenshotBitmap = null;
    target.ivRecordSpot = null;
    target.tvTime = null;
    target.ivRealTimeRefuseIcon = null;
    target.rlRealTime = null;
    target.rlCallingTime = null;
    target.ivHeadPic = null;
    target.ivBigHeadPic = null;
    target.tvHeadPic = null;
    target.tvBigHeadPic = null;
    target.tvVideoTimeStamp = null;
    target.tvCallingTips = null;
    target.tvDoorbell = null;
    target.ivCache = null;
    target.rlTitleBar = null;
    target.titleBar = null;

    view2131296696.setOnClickListener(null);
    view2131296696 = null;
    view2131296779.setOnClickListener(null);
    view2131296779 = null;
    view2131296792.setOnClickListener(null);
    view2131296792 = null;
    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296785.setOnClickListener(null);
    view2131296785 = null;
    view2131296755.setOnClickListener(null);
    view2131296755 = null;
    view2131296703.setOnClickListener(null);
    view2131296703 = null;
    view2131296778.setOnClickListener(null);
    view2131296778 = null;
    view2131296693.setOnClickListener(null);
    view2131296693 = null;
    view2131296774.setOnClickListener(null);
    view2131296774 = null;
    view2131296918.setOnClickListener(null);
    view2131296918 = null;
    view2131296802.setOnClickListener(null);
    view2131296802 = null;
  }
}
