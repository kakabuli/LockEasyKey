package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.videolock.WifiVideoLockHelpActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class WifiLockAddNewSecondActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    TextView notice;
    ImageView ivAnim;
    TextView lockActivated;
    TextView lockNotActivated;

    private String wifiModelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_second);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        ivAnim = findViewById(R.id.iv_anim);
        lockActivated = findViewById(R.id.lock_activated);
        lockNotActivated = findViewById(R.id.lock_not_activated);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> {
            if(wifiModelType.contains("VIDEO")){
                startActivity(new Intent(this, WifiVideoLockHelpActivity.class));
            }else{
                startActivity(new Intent(this,WifiLockHelpActivity.class));
            }
        });
        lockActivated.setOnClickListener(v -> {
            //startActivity(new Intent(this,WifiLockAddNewThirdActivity.class));
            Intent wifiIntent = new Intent(this, WifiLockAddNewThirdActivity.class);
            wifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(wifiIntent);
        });
        lockNotActivated.setOnClickListener(v -> {
//                startActivity(new Intent(this,WifiLockAddNewNotActivateActivity.class));
            Intent UnActivatedWifiIntent = new Intent(this, WifiLockAddNewNotActivateActivity.class);
            UnActivatedWifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(UnActivatedWifiIntent);
        });

        Intent intent = getIntent();
        wifiModelType = intent.getStringExtra("wifiModelType");
        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();
    }

}
