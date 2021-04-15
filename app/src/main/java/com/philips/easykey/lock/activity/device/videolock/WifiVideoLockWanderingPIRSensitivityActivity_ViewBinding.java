// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockWanderingPIRSensitivityActivity_ViewBinding implements Unbinder {
  private WifiVideoLockWanderingPIRSensitivityActivity target;

  private View view2131296334;

  private View view2131297156;

  private View view2131297157;

  private View view2131297158;

  @UiThread
  public WifiVideoLockWanderingPIRSensitivityActivity_ViewBinding(WifiVideoLockWanderingPIRSensitivityActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockWanderingPIRSensitivityActivity_ViewBinding(final WifiVideoLockWanderingPIRSensitivityActivity target,
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
    target.ivSensitivity1 = Utils.findRequiredViewAsType(source, R.id.iv_sensitivity_1, "field 'ivSensitivity1'", CheckBox.class);
    target.ivSensitivity2 = Utils.findRequiredViewAsType(source, R.id.iv_sensitivity_2, "field 'ivSensitivity2'", CheckBox.class);
    target.ivSensitivity3 = Utils.findRequiredViewAsType(source, R.id.iv_sensitivity_3, "field 'ivSensitivity3'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.rl_sensitivity_1, "method 'onClick'");
    view2131297156 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_sensitivity_2, "method 'onClick'");
    view2131297157 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_sensitivity_3, "method 'onClick'");
    view2131297158 = view;
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
    WifiVideoLockWanderingPIRSensitivityActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.ivSensitivity1 = null;
    target.ivSensitivity2 = null;
    target.ivSensitivity3 = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297156.setOnClickListener(null);
    view2131297156 = null;
    view2131297157.setOnClickListener(null);
    view2131297157 = null;
    view2131297158.setOnClickListener(null);
    view2131297158 = null;
  }
}
