// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
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

public class WifiVideoLockRealTimeActivity_ViewBinding implements Unbinder {
  private WifiVideoLockRealTimeActivity target;

  private View view2131296334;

  private View view2131297146;

  private View view2131297170;

  private View view2131297171;

  @UiThread
  public WifiVideoLockRealTimeActivity_ViewBinding(WifiVideoLockRealTimeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockRealTimeActivity_ViewBinding(final WifiVideoLockRealTimeActivity target,
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
    view = Utils.findRequiredView(source, R.id.rl_real_time_period, "field 'rlRealTimePeriod' and method 'onClick'");
    target.rlRealTimePeriod = Utils.castView(view, R.id.rl_real_time_period, "field 'rlRealTimePeriod'", RelativeLayout.class);
    view2131297146 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.ivVideoConnectOpen = Utils.findRequiredViewAsType(source, R.id.iv_video_connect_open, "field 'ivVideoConnectOpen'", ImageView.class);
    target.ivVideoConnectPower = Utils.findRequiredViewAsType(source, R.id.iv_video_connect_power, "field 'ivVideoConnectPower'", ImageView.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi, "field 'avi'", AVLoadingIndicatorView.class);
    target.tvTips = Utils.findRequiredViewAsType(source, R.id.tv_tips, "field 'tvTips'", TextView.class);
    target.tvPeriodConnect = Utils.findRequiredViewAsType(source, R.id.tv_period_connect, "field 'tvPeriodConnect'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rl_video_connect_open, "method 'onClick'");
    view2131297170 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_video_connect_power, "method 'onClick'");
    view2131297171 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockRealTimeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.rlRealTimePeriod = null;
    target.ivVideoConnectOpen = null;
    target.ivVideoConnectPower = null;
    target.avi = null;
    target.tvTips = null;
    target.tvPeriodConnect = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297146.setOnClickListener(null);
    view2131297146 = null;
    view2131297170.setOnClickListener(null);
    view2131297170 = null;
    view2131297171.setOnClickListener(null);
    view2131297171 = null;
  }
}
