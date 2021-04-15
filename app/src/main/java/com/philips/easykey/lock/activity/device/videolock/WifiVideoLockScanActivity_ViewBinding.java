// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.WifiCircleProgress;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockScanActivity_ViewBinding implements Unbinder {
  private WifiVideoLockScanActivity target;

  private View view2131296334;

  private View view2131296637;

  private View view2131296432;

  @UiThread
  public WifiVideoLockScanActivity_ViewBinding(WifiVideoLockScanActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockScanActivity_ViewBinding(final WifiVideoLockScanActivity target,
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
    view = Utils.findRequiredView(source, R.id.circle_progress_bar2, "field 'circleProgressBar2' and method 'onClick'");
    target.circleProgressBar2 = Utils.castView(view, R.id.circle_progress_bar2, "field 'circleProgressBar2'", WifiCircleProgress.class);
    view2131296432 = view;
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
    WifiVideoLockScanActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.help = null;
    target.circleProgressBar2 = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296637.setOnClickListener(null);
    view2131296637 = null;
    view2131296432.setOnClickListener(null);
    view2131296432 = null;
  }
}
