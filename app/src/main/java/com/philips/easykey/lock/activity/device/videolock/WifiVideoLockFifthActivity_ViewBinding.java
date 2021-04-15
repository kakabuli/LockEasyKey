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
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockFifthActivity_ViewBinding implements Unbinder {
  private WifiVideoLockFifthActivity target;

  private View view2131296334;

  private View view2131296637;

  private View view2131296909;

  private View view2131296905;

  @UiThread
  public WifiVideoLockFifthActivity_ViewBinding(WifiVideoLockFifthActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockFifthActivity_ViewBinding(final WifiVideoLockFifthActivity target,
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
    view = Utils.findRequiredView(source, R.id.lock_not_activated, "field 'tvFail' and method 'onClick'");
    target.tvFail = Utils.castView(view, R.id.lock_not_activated, "field 'tvFail'", TextView.class);
    view2131296909 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.lock_activated, "field 'tvNext' and method 'onClick'");
    target.tvNext = Utils.castView(view, R.id.lock_activated, "field 'tvNext'", TextView.class);
    view2131296905 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.ivQrcode = Utils.findRequiredViewAsType(source, R.id.iv_qrcode, "field 'ivQrcode'", ImageView.class);
    target.head = Utils.findRequiredViewAsType(source, R.id.head, "field 'head'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockFifthActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.help = null;
    target.tvFail = null;
    target.tvNext = null;
    target.ivQrcode = null;
    target.head = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296637.setOnClickListener(null);
    view2131296637 = null;
    view2131296909.setOnClickListener(null);
    view2131296909 = null;
    view2131296905.setOnClickListener(null);
    view2131296905 = null;
  }
}
