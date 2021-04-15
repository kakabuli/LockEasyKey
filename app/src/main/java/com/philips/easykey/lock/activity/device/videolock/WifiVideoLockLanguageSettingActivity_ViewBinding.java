// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.AVLoadingIndicatorView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockLanguageSettingActivity_ViewBinding implements Unbinder {
  private WifiVideoLockLanguageSettingActivity target;

  private View view2131296334;

  private View view2131297679;

  private View view2131296545;

  @UiThread
  public WifiVideoLockLanguageSettingActivity_ViewBinding(WifiVideoLockLanguageSettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockLanguageSettingActivity_ViewBinding(final WifiVideoLockLanguageSettingActivity target,
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
    target.zhImg = Utils.findRequiredViewAsType(source, R.id.zh_img, "field 'zhImg'", CheckBox.class);
    target.enImg = Utils.findRequiredViewAsType(source, R.id.en_img, "field 'enImg'", CheckBox.class);
    target.avi = Utils.findRequiredViewAsType(source, R.id.avi, "field 'avi'", AVLoadingIndicatorView.class);
    target.tvTips = Utils.findRequiredViewAsType(source, R.id.tv_tips, "field 'tvTips'", TextView.class);
    view = Utils.findRequiredView(source, R.id.zh_layout, "method 'onClick'");
    view2131297679 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.en_layout, "method 'onClick'");
    view2131296545 = view;
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
    WifiVideoLockLanguageSettingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.zhImg = null;
    target.enImg = null;
    target.avi = null;
    target.tvTips = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297679.setOnClickListener(null);
    view2131297679 = null;
    view2131296545.setOnClickListener(null);
    view2131296545 = null;
  }
}
