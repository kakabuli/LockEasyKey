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
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockRealTimePeriodActivity_ViewBinding implements Unbinder {
  private WifiVideoLockRealTimePeriodActivity target;

  private View view2131296334;

  private View view2131297148;

  private View view2131297147;

  @UiThread
  public WifiVideoLockRealTimePeriodActivity_ViewBinding(WifiVideoLockRealTimePeriodActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockRealTimePeriodActivity_ViewBinding(final WifiVideoLockRealTimePeriodActivity target,
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
    view = Utils.findRequiredView(source, R.id.rl_real_time_setting, "field 'rlRealTimeSetting' and method 'onClick'");
    target.rlRealTimeSetting = Utils.castView(view, R.id.rl_real_time_setting, "field 'rlRealTimeSetting'", RelativeLayout.class);
    view2131297148 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_real_time_rule_repet, "field 'rlRealTimeRuleRepet' and method 'onClick'");
    target.rlRealTimeRuleRepet = Utils.castView(view, R.id.rl_real_time_rule_repet, "field 'rlRealTimeRuleRepet'", RelativeLayout.class);
    view2131297147 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvVideoTimeSetting = Utils.findRequiredViewAsType(source, R.id.tv_video_time_setting, "field 'tvVideoTimeSetting'", TextView.class);
    target.tvPeriodConnect = Utils.findRequiredViewAsType(source, R.id.tv_period_connect, "field 'tvPeriodConnect'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockRealTimePeriodActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.rlRealTimeSetting = null;
    target.rlRealTimeRuleRepet = null;
    target.tvVideoTimeSetting = null;
    target.tvPeriodConnect = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297148.setOnClickListener(null);
    view2131297148 = null;
    view2131297147.setOnClickListener(null);
    view2131297147 = null;
  }
}
