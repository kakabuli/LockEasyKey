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
import com.philips.easykey.lock.utils.KeyConstants;

public class WifiLockChangeAdminPasswordActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    TextView notice;
    TextView alreadyModify;
    ImageView ivAnim;

    int times;
    private boolean video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_change_admin_password);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        alreadyModify = findViewById(R.id.already_modify);
        ivAnim = findViewById(R.id.iv_anim);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiLockHelpActivity.class)));
        alreadyModify.setOnClickListener(v -> {
            if(video){
                finish();
            }else{
                Intent wifiIntent = new Intent(this, WifiLockHasModifyPasswordAndDisconnectActivity.class);
//                wifiIntent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES, times);
                startActivity(wifiIntent);
            }
        });

        Intent intent = getIntent();
        times = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES, 1);
        video = intent.getBooleanExtra("video",false);
        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();
    }

}
