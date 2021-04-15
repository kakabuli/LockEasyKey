// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockCameraVersionActivity_ViewBinding implements Unbinder {
  private WifiVideoLockCameraVersionActivity target;

  private View view2131296334;

  private View view2131297083;

  private View view2131297128;

  private View view2131297167;

  @UiThread
  public WifiVideoLockCameraVersionActivity_ViewBinding(WifiVideoLockCameraVersionActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockCameraVersionActivity_ViewBinding(final WifiVideoLockCameraVersionActivity target,
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
    target.tvSerialNumber = Utils.findRequiredViewAsType(source, R.id.tv_serial_number, "field 'tvSerialNumber'", TextView.class);
    target.tvLockFirwareNumber = Utils.findRequiredViewAsType(source, R.id.tv_lock_firware_number, "field 'tvLockFirwareNumber'", TextView.class);
    target.tvLockWifiFirwareNumber = Utils.findRequiredViewAsType(source, R.id.tv_lock_wifi_firware_number, "field 'tvLockWifiFirwareNumber'", TextView.class);
    target.tvChildSystemFirwareNumber = Utils.findRequiredViewAsType(source, R.id.tv_child_system_firware_number, "field 'tvChildSystemFirwareNumber'", TextView.class);
    target.tvHardwareVersion = Utils.findRequiredViewAsType(source, R.id.tv_hardware_version, "field 'tvHardwareVersion'", TextView.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi, "field 'avi'", AVLoadingIndicatorView.class);
    target.ivChildSystemFirwareNumber = Utils.findRequiredViewAsType(source, R.id.iv_child_system_firware_number, "field 'ivChildSystemFirwareNumber'", ImageView.class);
    target.ivLockWifiFirwareNumber = Utils.findRequiredViewAsType(source, R.id.iv_lock_wifi_firware_number, "field 'ivLockWifiFirwareNumber'", ImageView.class);
    target.ivLockFirwareNimner = Utils.findRequiredViewAsType(source, R.id.iv_lock_firware_number, "field 'ivLockFirwareNimner'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.rl_child_system_firware_number, "method 'onClick'");
    view2131297083 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_lock_wifi_firware_number, "method 'onClick'");
    view2131297128 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_tv_lock_firware_number, "method 'onClick'");
    view2131297167 = view;
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
    WifiVideoLockCameraVersionActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.tvSerialNumber = null;
    target.tvLockFirwareNumber = null;
    target.tvLockWifiFirwareNumber = null;
    target.tvChildSystemFirwareNumber = null;
    target.tvHardwareVersion = null;
    target.avi = null;
    target.ivChildSystemFirwareNumber = null;
    target.ivLockWifiFirwareNumber = null;
    target.ivLockFirwareNimner = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297083.setOnClickListener(null);
    view2131297083 = null;
    view2131297128.setOnClickListener(null);
    view2131297128 = null;
    view2131297167.setOnClickListener(null);
    view2131297167 = null;
  }
}
