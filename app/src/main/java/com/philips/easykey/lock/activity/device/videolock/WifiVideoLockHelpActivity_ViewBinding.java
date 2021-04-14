// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockHelpActivity_ViewBinding implements Unbinder {
  private WifiVideoLockHelpActivity target;

  private View view2131296334;

  private View view2131297123;

  private View view2131297152;

  @UiThread
  public WifiVideoLockHelpActivity_ViewBinding(WifiVideoLockHelpActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockHelpActivity_ViewBinding(final WifiVideoLockHelpActivity target,
      View source) {
    this.target = target;

    View view;
    target.vLeft = Utils.findRequiredView(source, R.id.v_left, "field 'vLeft'");
    target.vRight = Utils.findRequiredView(source, R.id.v_right, "field 'vRight'");
    target.content = Utils.findRequiredViewAsType(source, R.id.content, "field 'content'", FrameLayout.class);
    target.tvLeft = Utils.findRequiredViewAsType(source, R.id.tv_left, "field 'tvLeft'", TextView.class);
    target.tvRight = Utils.findRequiredViewAsType(source, R.id.tv_right, "field 'tvRight'", TextView.class);
    view = Utils.findRequiredView(source, R.id.back, "method 'onClick'");
    view2131296334 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_left, "method 'onClick'");
    view2131297123 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_right, "method 'onClick'");
    view2131297152 = view;
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
    WifiVideoLockHelpActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.vLeft = null;
    target.vRight = null;
    target.content = null;
    target.tvLeft = null;
    target.tvRight = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297123.setOnClickListener(null);
    view2131297123 = null;
    view2131297152.setOnClickListener(null);
    view2131297152 = null;
  }
}
