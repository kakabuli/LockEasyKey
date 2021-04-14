// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockSixthActivity_ViewBinding implements Unbinder {
  private WifiVideoLockSixthActivity target;

  private View view2131296334;

  private View view2131296637;

  private View view2131296763;

  private View view2131296381;

  @UiThread
  public WifiVideoLockSixthActivity_ViewBinding(WifiVideoLockSixthActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockSixthActivity_ViewBinding(final WifiVideoLockSixthActivity target,
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
    view = Utils.findRequiredView(source, R.id.help, "field 'help' and method 'onClick'");
    target.help = Utils.castView(view, R.id.help, "field 'help'", ImageView.class);
    view2131296637 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.apPasswordEdit = Utils.findRequiredViewAsType(source, R.id.ap_password_edit, "field 'apPasswordEdit'", EditText.class);
    view = Utils.findRequiredView(source, R.id.iv_password_status, "field 'ivPasswordStatus' and method 'onClick'");
    target.ivPasswordStatus = Utils.castView(view, R.id.iv_password_status, "field 'ivPasswordStatus'", ImageView.class);
    view2131296763 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.button_next, "method 'onClick'");
    view2131296381 = view;
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
    WifiVideoLockSixthActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.help = null;
    target.apPasswordEdit = null;
    target.ivPasswordStatus = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296637.setOnClickListener(null);
    view2131296637 = null;
    view2131296763.setOnClickListener(null);
    view2131296763 = null;
    view2131296381.setOnClickListener(null);
    view2131296381 = null;
  }
}
