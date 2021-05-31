package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class WifiLockOldUserFirstActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    TextView notice;
    ImageView ivAnim;
    TextView buttonNext;

    private String wifiModelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_old_user_first);
        Intent intent = getIntent();
        wifiModelType = intent.getStringExtra("wifiModelType");

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        ivAnim = findViewById(R.id.iv_anim);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> startActivity(new Intent(this,WifiLockHelpActivity.class)));
        buttonNext.setOnClickListener(v -> {
            //                startActivity(new Intent(this,WifiLockOldUserSecondActivity.class));
            Intent wifiIntent = new Intent(this, WifiLockOldUserSecondActivity.class);
            wifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(wifiIntent);
        });

        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();
    }

}
