package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class WifiLockAddNewModifyPasswordDisconnectActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    TextView notice;
    ImageView ivAnim;
    TextView buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_modify_password_disconnect);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        ivAnim = findViewById(R.id.iv_anim);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> startActivity(new Intent(this,WifiLockHelpActivity.class)));
        buttonNext.setOnClickListener(v -> startActivity(new Intent(this,WifiLockAddNewScanActivity.class)));

        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();
    }

}
