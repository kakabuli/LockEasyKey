// Generated code from Butter Knife. Do not modify!
package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.philips.easykey.lock.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WifiVideoLockWanderingJudgeTimeActivity_ViewBinding implements Unbinder {
  private WifiVideoLockWanderingJudgeTimeActivity target;

  private View view2131296334;

  private View view2131297121;

  private View view2131297120;

  private View view2131297119;

  private View view2131297118;

  private View view2131297117;

  private View view2131297116;

  @UiThread
  public WifiVideoLockWanderingJudgeTimeActivity_ViewBinding(WifiVideoLockWanderingJudgeTimeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WifiVideoLockWanderingJudgeTimeActivity_ViewBinding(final WifiVideoLockWanderingJudgeTimeActivity target,
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
    target.ivJudgeTime1 = Utils.findRequiredViewAsType(source, R.id.iv_judge_time_1, "field 'ivJudgeTime1'", CheckBox.class);
    target.ivJudgeTime2 = Utils.findRequiredViewAsType(source, R.id.iv_judge_time_2, "field 'ivJudgeTime2'", CheckBox.class);
    target.ivJudgeTime3 = Utils.findRequiredViewAsType(source, R.id.iv_judge_time_3, "field 'ivJudgeTime3'", CheckBox.class);
    target.ivJudgeTime4 = Utils.findRequiredViewAsType(source, R.id.iv_judge_time_4, "field 'ivJudgeTime4'", CheckBox.class);
    target.ivJudgeTime5 = Utils.findRequiredViewAsType(source, R.id.iv_judge_time_5, "field 'ivJudgeTime5'", CheckBox.class);
    target.ivJudgeTime6 = Utils.findRequiredViewAsType(source, R.id.iv_judge_time_6, "field 'ivJudgeTime6'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.rl_judge_time_6, "method 'onClick'");
    view2131297121 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_judge_time_5, "method 'onClick'");
    view2131297120 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_judge_time_4, "method 'onClick'");
    view2131297119 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_judge_time_3, "method 'onClick'");
    view2131297118 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_judge_time_2, "method 'onClick'");
    view2131297117 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_judge_time_1, "method 'onClick'");
    view2131297116 = view;
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
    WifiVideoLockWanderingJudgeTimeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.back = null;
    target.ivJudgeTime1 = null;
    target.ivJudgeTime2 = null;
    target.ivJudgeTime3 = null;
    target.ivJudgeTime4 = null;
    target.ivJudgeTime5 = null;
    target.ivJudgeTime6 = null;

    view2131296334.setOnClickListener(null);
    view2131296334 = null;
    view2131297121.setOnClickListener(null);
    view2131297121 = null;
    view2131297120.setOnClickListener(null);
    view2131297120 = null;
    view2131297119.setOnClickListener(null);
    view2131297119 = null;
    view2131297118.setOnClickListener(null);
    view2131297118 = null;
    view2131297117.setOnClickListener(null);
    view2131297117 = null;
    view2131297116.setOnClickListener(null);
    view2131297116 = null;
  }
}
