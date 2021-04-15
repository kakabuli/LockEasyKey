// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockAddSuccessActivity_ViewBinding implements Unbinder {
  private WifiVideoLockAddSuccessActivity target;

  private View view2131297195;

  private View view2131296334;

  private View view2131297570;

  private View viewSource;

  @UiThread
  public WifiVideoLockAddSuccessActivity_ViewBinding(WifiVideoLockAddSuccessActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockAddSuccessActivity_ViewBinding(final WifiVideoLockAddSuccessActivity target,
      View source) {
    this.target = target;

    View view;
    target.inputName = Utils.findRequiredViewAsType(source, R.id.input_name, "field 'inputName'", EditText.class);
    target.recycler = Utils.findRequiredViewAsType(source, R.id.recycler, "field 'recycler'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.save, "field 'save' and method 'onViewClicked'");
    target.save = Utils.castView(view, R.id.save, "field 'save'", Button.class);
    view2131297195 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.lock = Utils.findRequiredViewAsType(source, R.id.lock, "field 'lock'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.back, "field 'back' and method 'onViewClicked'");
    target.back = Utils.castView(view, R.id.back, "field 'back'", ImageView.class);
    view2131296334 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_support_list, "method 'onViewClicked'");
    view2131297570 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    viewSource = source;
    source.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockAddSuccessActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.inputName = null;
    target.recycler = null;
    target.save = null;
    target.lock = null;
    target.back = null;

    view2131297195.setOnClickListener(null);
    view2131297195 = null;
    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297570.setOnClickListener(null);
    view2131297570 = null;
    viewSource.setOnClickListener(null);
    viewSource = null;
  }
}
