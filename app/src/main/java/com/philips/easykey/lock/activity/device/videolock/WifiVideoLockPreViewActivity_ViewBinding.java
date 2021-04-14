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
import com.bm.library.PhotoView;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockPreViewActivity_ViewBinding implements Unbinder {
  private WifiVideoLockPreViewActivity target;

  private View view2131296334;

  private View view2131296756;

  @UiThread
  public WifiVideoLockPreViewActivity_ViewBinding(WifiVideoLockPreViewActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockPreViewActivity_ViewBinding(final WifiVideoLockPreViewActivity target,
      View source) {
    this.target = target;

    View view;
    target.preview_img = Utils.findRequiredViewAsType(source, R.id.preview_img, "field 'preview_img'", PhotoView.class);
    view = Utils.findRequiredView(source, R.id.back, "field 'iv_back' and method 'onViewClicked'");
    target.iv_back = Utils.castView(view, R.id.back, "field 'iv_back'", ImageView.class);
    view2131296334 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
    view = Utils.findRequiredView(source, R.id.iv_myalbum_delete, "method 'onViewClicked'");
    view2131296756 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockPreViewActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.preview_img = null;
    target.iv_back = null;
    target.tvName = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296756.setOnClickListener(null);
    view2131296756 = null;
  }
}
