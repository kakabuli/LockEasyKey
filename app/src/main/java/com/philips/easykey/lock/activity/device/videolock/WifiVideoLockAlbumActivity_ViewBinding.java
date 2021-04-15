// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockAlbumActivity_ViewBinding implements Unbinder {
  private WifiVideoLockAlbumActivity target;

  private View view2131296334;

  private View view2131297410;

  private View view2131296756;

  @UiThread
  public WifiVideoLockAlbumActivity_ViewBinding(WifiVideoLockAlbumActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockAlbumActivity_ViewBinding(final WifiVideoLockAlbumActivity target,
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
    target.recycleview = Utils.findRequiredViewAsType(source, R.id.recycleview, "field 'recycleview'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.tv_cancel, "field 'tvCancel' and method 'onClick'");
    target.tvCancel = Utils.castView(view, R.id.tv_cancel, "field 'tvCancel'", TextView.class);
    view2131297410 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_myalbum_delete, "field 'ivMyAlbumDelete' and method 'onClick'");
    target.ivMyAlbumDelete = Utils.castView(view, R.id.iv_myalbum_delete, "field 'ivMyAlbumDelete'", ImageView.class);
    view2131296756 = view;
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
    WifiVideoLockAlbumActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.recycleview = null;
    target.tvCancel = null;
    target.ivMyAlbumDelete = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297410.setOnClickListener(null);
    view2131297410 = null;
    view2131296756.setOnClickListener(null);
    view2131296756 = null;
  }
}
