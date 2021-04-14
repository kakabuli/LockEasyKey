// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockFirwareNumberActivity_ViewBinding implements Unbinder {
  private WifiVideoLockFirwareNumberActivity target;

  private View view2131296334;

  @UiThread
  public WifiVideoLockFirwareNumberActivity_ViewBinding(WifiVideoLockFirwareNumberActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockFirwareNumberActivity_ViewBinding(final WifiVideoLockFirwareNumberActivity target,
      View source) {
    this.target = target;

    View view;
    target.tvHardwareVersion = Utils.findRequiredViewAsType(source, R.id.tv_hardware_version, "field 'tvHardwareVersion'", TextView.class);
    target.tvHardVersion = Utils.findRequiredViewAsType(source, R.id.tv_hard_version, "field 'tvHardVersion'", TextView.class);
    target.ivHardVersion = Utils.findRequiredViewAsType(source, R.id.iv_hard_version, "field 'ivHardVersion'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.back, "method 'onClick'");
    view2131296334 = view;
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
    WifiVideoLockFirwareNumberActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvHardwareVersion = null;
    target.tvHardVersion = null;
    target.ivHardVersion = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
  }
}
