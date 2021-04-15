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

public class WifiVideoLockScanFailedActivity_ViewBinding implements Unbinder {
  private WifiVideoLockScanFailedActivity target;

  private View view2131296334;

  private View view2131296637;

  private View view2131296969;

  private View view2131297035;

  private View view2131296394;

  @UiThread
  public WifiVideoLockScanFailedActivity_ViewBinding(WifiVideoLockScanFailedActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockScanFailedActivity_ViewBinding(final WifiVideoLockScanFailedActivity target,
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
    view = Utils.findRequiredView(source, R.id.notice, "field 'notice' and method 'onClick'");
    target.notice = Utils.castView(view, R.id.notice, "field 'notice'", TextView.class);
    view2131296969 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.re_scan, "method 'onClick'");
    view2131297035 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.cancel, "method 'onClick'");
    view2131296394 = view;
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
    WifiVideoLockScanFailedActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.help = null;
    target.notice = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296637.setOnClickListener(null);
    view2131296637 = null;
    view2131296969.setOnClickListener(null);
    view2131296969 = null;
    view2131297035.setOnClickListener(null);
    view2131297035 = null;
    view2131296394.setOnClickListener(null);
    view2131296394 = null;
  }
}
