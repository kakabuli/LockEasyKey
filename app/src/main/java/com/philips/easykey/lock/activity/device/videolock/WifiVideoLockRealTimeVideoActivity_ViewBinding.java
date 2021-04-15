// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockRealTimeVideoActivity_ViewBinding implements Unbinder {
  private WifiVideoLockRealTimeVideoActivity target;

  private View view2131296792;

  private View view2131296334;

  private View view2131296785;

  private View view2131296755;

  private View view2131296703;

  private View view2131296778;

  @UiThread
  public WifiVideoLockRealTimeVideoActivity_ViewBinding(WifiVideoLockRealTimeVideoActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockRealTimeVideoActivity_ViewBinding(final WifiVideoLockRealTimeVideoActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.iv_setting, "field 'ivSetting' and method 'onViewClicked'");
    target.ivSetting = Utils.castView(view, R.id.iv_setting, "field 'ivSetting'", ImageView.class);
    view2131296792 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.back, "field 'back' and method 'onViewClicked'");
    target.back = Utils.castView(view, R.id.back, "field 'back'", ImageView.class);
    view2131296334 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mSufaceView = Utils.findRequiredViewAsType(source, R.id.surface_view, "field 'mSufaceView'", SurfaceView.class);
    target.tvTemporaryPassword = Utils.findRequiredViewAsType(source, R.id.tv_temporary_password, "field 'tvTemporaryPassword'", TextView.class);
    target.llyTemporaryPassword = Utils.findRequiredViewAsType(source, R.id.lly_temporary_password, "field 'llyTemporaryPassword'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.iv_screenshot, "method 'onViewClicked'");
    view2131296785 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_mute, "method 'onViewClicked'");
    view2131296755 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_calling, "method 'onViewClicked'");
    view2131296703 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_recoring, "method 'onViewClicked'");
    view2131296778 = view;
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
    WifiVideoLockRealTimeVideoActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivSetting = null;
    target.back = null;
    target.mSufaceView = null;
    target.tvTemporaryPassword = null;
    target.llyTemporaryPassword = null;

    view2131296792.setOnClickListener(null);
    view2131296792 = null;
    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296785.setOnClickListener(null);
    view2131296785 = null;
    view2131296755.setOnClickListener(null);
    view2131296755 = null;
    view2131296703.setOnClickListener(null);
    view2131296703 = null;
    view2131296778.setOnClickListener(null);
    view2131296778 = null;
  }
}
