// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockWanderingAlarmActivity_ViewBinding implements Unbinder {
  private WifiVideoLockWanderingAlarmActivity target;

  private View view2131296334;

  private View view2131297177;

  private View view2131297175;

  private View view2131296810;

  private View view2131297174;

  @UiThread
  public WifiVideoLockWanderingAlarmActivity_ViewBinding(WifiVideoLockWanderingAlarmActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockWanderingAlarmActivity_ViewBinding(final WifiVideoLockWanderingAlarmActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.back, "field 'back' and method 'onClick'");
    target.back = Utils.castView(view, R.id.back, "field 'back'", ImageView.class);
    view2131296334 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_wandering_pir_sensitivity, "field 'rlWanderingPIRSensitivity' and method 'onClick'");
    target.rlWanderingPIRSensitivity = Utils.castView(view, R.id.rl_wandering_pir_sensitivity, "field 'rlWanderingPIRSensitivity'", RelativeLayout.class);
    view2131297177 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_wandering_judge_time, "field 'rlWanderingJudgeTime' and method 'onClick'");
    target.rlWanderingJudgeTime = Utils.castView(view, R.id.rl_wandering_judge_time, "field 'rlWanderingJudgeTime'", RelativeLayout.class);
    view2131297175 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_wandering_alarm, "field 'ivWanderingAlarm' and method 'onClick'");
    target.ivWanderingAlarm = Utils.castView(view, R.id.iv_wandering_alarm, "field 'ivWanderingAlarm'", ImageView.class);
    view2131296810 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvWanderingPirSensitivityRight = Utils.findRequiredViewAsType(source, R.id.tv_wandering_pir_sensitivity_right, "field 'tvWanderingPirSensitivityRight'", TextView.class);
    target.tvWanderingJudgeTimeRight = Utils.findRequiredViewAsType(source, R.id.tv_wandering_judge_time_right, "field 'tvWanderingJudgeTimeRight'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rl_wandering_alarm, "field 'rlWanderingAlarm' and method 'onClick'");
    target.rlWanderingAlarm = Utils.castView(view, R.id.rl_wandering_alarm, "field 'rlWanderingAlarm'", RelativeLayout.class);
    view2131297174 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi, "field 'avi'", AVLoadingIndicatorView.class);
    target.tvTips = Utils.findRequiredViewAsType(source, R.id.tv_tips, "field 'tvTips'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockWanderingAlarmActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.rlWanderingPIRSensitivity = null;
    target.rlWanderingJudgeTime = null;
    target.ivWanderingAlarm = null;
    target.tvWanderingPirSensitivityRight = null;
    target.tvWanderingJudgeTimeRight = null;
    target.rlWanderingAlarm = null;
    target.avi = null;
    target.tvTips = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297177.setOnClickListener(null);
    view2131297177 = null;
    view2131297175.setOnClickListener(null);
    view2131297175 = null;
    view2131296810.setOnClickListener(null);
    view2131296810 = null;
    view2131297174.setOnClickListener(null);
    view2131297174 = null;
  }
}
