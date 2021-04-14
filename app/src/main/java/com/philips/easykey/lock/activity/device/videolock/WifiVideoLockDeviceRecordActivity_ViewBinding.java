// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockDeviceRecordActivity_ViewBinding implements Unbinder {
  private WifiVideoLockDeviceRecordActivity target;

  private View view2131296334;

  private View view2131296767;

  private View view2131296764;

  @UiThread
  public WifiVideoLockDeviceRecordActivity_ViewBinding(WifiVideoLockDeviceRecordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockDeviceRecordActivity_ViewBinding(final WifiVideoLockDeviceRecordActivity target,
      View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.back, "field 'back' and method 'onViewClicked'");
    target.back = Utils.castView(view, R.id.back, "field 'back'", ImageView.class);
    view2131296334 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.durationSeekBar = Utils.findRequiredViewAsType(source, R.id.duration_seek_bar, "field 'durationSeekBar'", SeekBar.class);
    target.surfaceView = Utils.findRequiredViewAsType(source, R.id.video_surface, "field 'surfaceView'", SurfaceView.class);
    target.llyBootomBar = Utils.findRequiredViewAsType(source, R.id.lly_bottom_bar, "field 'llyBootomBar'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.iv_play_start, "field 'ivPlayStart' and method 'onViewClicked'");
    target.ivPlayStart = Utils.castView(view, R.id.iv_play_start, "field 'ivPlayStart'", ImageView.class);
    view2131296767 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_pause, "field 'ivPause' and method 'onViewClicked'");
    target.ivPause = Utils.castView(view, R.id.iv_pause, "field 'ivPause'", ImageView.class);
    view2131296764 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvDuration = Utils.findRequiredViewAsType(source, R.id.tv_duration, "field 'tvDuration'", TextView.class);
    target.tvTime = Utils.findRequiredViewAsType(source, R.id.tv_time, "field 'tvTime'", TextView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockDeviceRecordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.durationSeekBar = null;
    target.surfaceView = null;
    target.llyBootomBar = null;
    target.ivPlayStart = null;
    target.ivPause = null;
    target.tvDuration = null;
    target.tvTime = null;
    target.tvName = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296767.setOnClickListener(null);
    view2131296767 = null;
    view2131296764.setOnClickListener(null);
    view2131296764 = null;
  }
}
