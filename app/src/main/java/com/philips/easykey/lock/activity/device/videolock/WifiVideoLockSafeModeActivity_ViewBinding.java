// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockSafeModeActivity_ViewBinding implements Unbinder {
  private WifiVideoLockSafeModeActivity target;

  private View view2131296334;

  private View view2131297194;

  private View view2131296966;

  @UiThread
  public WifiVideoLockSafeModeActivity_ViewBinding(WifiVideoLockSafeModeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockSafeModeActivity_ViewBinding(final WifiVideoLockSafeModeActivity target,
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
    target.ckNormal = Utils.findRequiredViewAsType(source, R.id.ck_normal, "field 'ckNormal'", CheckBox.class);
    target.ckSafe = Utils.findRequiredViewAsType(source, R.id.ck_safe, "field 'ckSafe'", CheckBox.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi, "field 'avi'", AVLoadingIndicatorView.class);
    target.tvTips = Utils.findRequiredViewAsType(source, R.id.tv_tips, "field 'tvTips'", TextView.class);
    view = Utils.findRequiredView(source, R.id.safe_layout, "method 'onClick'");
    view2131297194 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.normal_layout, "method 'onClick'");
    view2131296966 = view;
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
    WifiVideoLockSafeModeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.ckNormal = null;
    target.ckSafe = null;
    target.avi = null;
    target.tvTips = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297194.setOnClickListener(null);
    view2131297194 = null;
    view2131296966.setOnClickListener(null);
    view2131296966 = null;
  }
}
