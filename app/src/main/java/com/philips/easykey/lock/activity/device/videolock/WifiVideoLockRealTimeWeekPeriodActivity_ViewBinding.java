// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockRealTimeWeekPeriodActivity_ViewBinding implements Unbinder {
  private WifiVideoLockRealTimeWeekPeriodActivity target;

  private View view2131296334;

  private View view2131296811;

  private View view2131297108;

  private View view2131297109;

  private View view2131297110;

  private View view2131297111;

  private View view2131297112;

  private View view2131297113;

  private View view2131297114;

  private View view2131297115;

  @UiThread
  public WifiVideoLockRealTimeWeekPeriodActivity_ViewBinding(WifiVideoLockRealTimeWeekPeriodActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockRealTimeWeekPeriodActivity_ViewBinding(final WifiVideoLockRealTimeWeekPeriodActivity target,
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
    view = Utils.findRequiredView(source, R.id.iv_week_0, "field 'ivWeek0' and method 'onClick'");
    target.ivWeek0 = Utils.castView(view, R.id.iv_week_0, "field 'ivWeek0'", CheckBox.class);
    view2131296811 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.ivWeek1 = Utils.findRequiredViewAsType(source, R.id.iv_week_1, "field 'ivWeek1'", CheckBox.class);
    target.ivWeek2 = Utils.findRequiredViewAsType(source, R.id.iv_week_2, "field 'ivWeek2'", CheckBox.class);
    target.ivWeek3 = Utils.findRequiredViewAsType(source, R.id.iv_week_3, "field 'ivWeek3'", CheckBox.class);
    target.ivWeek4 = Utils.findRequiredViewAsType(source, R.id.iv_week_4, "field 'ivWeek4'", CheckBox.class);
    target.ivWeek5 = Utils.findRequiredViewAsType(source, R.id.iv_week_5, "field 'ivWeek5'", CheckBox.class);
    target.ivWeek6 = Utils.findRequiredViewAsType(source, R.id.iv_week_6, "field 'ivWeek6'", CheckBox.class);
    target.ivWeek7 = Utils.findRequiredViewAsType(source, R.id.iv_week_7, "field 'ivWeek7'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.rl_iv_week_0, "method 'onClick'");
    view2131297108 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_iv_week_1, "method 'onClick'");
    view2131297109 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_iv_week_2, "method 'onClick'");
    view2131297110 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_iv_week_3, "method 'onClick'");
    view2131297111 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_iv_week_4, "method 'onClick'");
    view2131297112 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_iv_week_5, "method 'onClick'");
    view2131297113 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_iv_week_6, "method 'onClick'");
    view2131297114 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_iv_week_7, "method 'onClick'");
    view2131297115 = view;
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
    WifiVideoLockRealTimeWeekPeriodActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.ivWeek0 = null;
    target.ivWeek1 = null;
    target.ivWeek2 = null;
    target.ivWeek3 = null;
    target.ivWeek4 = null;
    target.ivWeek5 = null;
    target.ivWeek6 = null;
    target.ivWeek7 = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296811.setOnClickListener(null);
    view2131296811 = null;
    view2131297108.setOnClickListener(null);
    view2131297108 = null;
    view2131297109.setOnClickListener(null);
    view2131297109 = null;
    view2131297110.setOnClickListener(null);
    view2131297110 = null;
    view2131297111.setOnClickListener(null);
    view2131297111 = null;
    view2131297112.setOnClickListener(null);
    view2131297112 = null;
    view2131297113.setOnClickListener(null);
    view2131297113 = null;
    view2131297114.setOnClickListener(null);
    view2131297114 = null;
    view2131297115.setOnClickListener(null);
    view2131297115 = null;
  }
}
