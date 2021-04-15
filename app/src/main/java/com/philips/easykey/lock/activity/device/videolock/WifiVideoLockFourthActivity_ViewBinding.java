// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.DropEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockFourthActivity_ViewBinding implements Unbinder {
  private WifiVideoLockFourthActivity target;

  private View view2131296334;

  private View view2131296637;

  private View view2131296719;

  private View view2131296457;

  private View view2131297570;

  @UiThread
  public WifiVideoLockFourthActivity_ViewBinding(WifiVideoLockFourthActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockFourthActivity_ViewBinding(final WifiVideoLockFourthActivity target,
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
    target.headTitle = Utils.findRequiredViewAsType(source, R.id.head_title, "field 'headTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.help, "field 'help' and method 'onClick'");
    target.help = Utils.castView(view, R.id.help, "field 'help'", ImageView.class);
    view2131296637 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.apSsidText = Utils.findRequiredViewAsType(source, R.id.ap_ssid_text, "field 'apSsidText'", DropEditText.class);
    target.apPasswordEdit = Utils.findRequiredViewAsType(source, R.id.ap_password_edit, "field 'apPasswordEdit'", EditText.class);
    view = Utils.findRequiredView(source, R.id.iv_eye, "field 'ivEye' and method 'onClick'");
    target.ivEye = Utils.castView(view, R.id.iv_eye, "field 'ivEye'", ImageView.class);
    view2131296719 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.confirm_btn, "field 'confirmBtn' and method 'onClick'");
    target.confirmBtn = Utils.castView(view, R.id.confirm_btn, "field 'confirmBtn'", TextView.class);
    view2131296457 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_support_list, "field 'tvSupportList' and method 'onClick'");
    target.tvSupportList = Utils.castView(view, R.id.tv_support_list, "field 'tvSupportList'", TextView.class);
    view2131297570 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.head = Utils.findRequiredViewAsType(source, R.id.head, "field 'head'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WifiVideoLockFourthActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.headTitle = null;
    target.help = null;
    target.apSsidText = null;
    target.apPasswordEdit = null;
    target.ivEye = null;
    target.confirmBtn = null;
    target.tvSupportList = null;
    target.head = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131296637.setOnClickListener(null);
    view2131296637 = null;
    view2131296719.setOnClickListener(null);
    view2131296719 = null;
    view2131296457.setOnClickListener(null);
    view2131296457 = null;
    view2131297570.setOnClickListener(null);
    view2131297570 = null;
  }
}
