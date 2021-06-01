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
import com.philips.easykey.lock.activity.device.videolock.WifiVideoLockFourthActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class WifiLockAddNewThird2Activity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    TextView notice;
    TextView alreadyModify;
    ImageView ivAnim;
    TextView title;


    private String wifiModelType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_third2);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        alreadyModify = findViewById(R.id.already_modify);
        ivAnim = findViewById(R.id.iv_anim);
        title = findViewById(R.id.title);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> {
            if(wifiModelType.contains("VIDEO")){
                startActivity(new Intent(this, WifiVideoLockHelpActivity.class));
            }else{
                startActivity(new Intent(this,WifiLockHelpActivity.class));
            }
        });
        alreadyModify.setOnClickListener(v -> {
            if(wifiModelType.contains("VIDEO")){
                Intent wifiVideoIntent = new Intent(this, WifiVideoLockFourthActivity.class);
                wifiVideoIntent.putExtra("wifiModelType", wifiModelType);
                startActivity(wifiVideoIntent);
            }else{
                //                startActivity(new Intent(this, WifiLockAddNewFifthActivity.class));
                Intent wifiIntent = new Intent(this, WifiLockAddNewFifthActivity.class);
                wifiIntent.putExtra("wifiModelType", wifiModelType);
                startActivity(wifiIntent);
            }
        });

        Intent intent = getIntent();
        wifiModelType = intent.getStringExtra("wifiModelType");
        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();

        if(wifiModelType.contains("VIDEO")){
            head.setText(R.string.modify_admin_pwd);
            title.setText(R.string.modify_admin_pwd);
        }else {
            head.setText(getString(R.string.philips_activity_wifi_lock_add_new_third_1));
            title.setText(R.string.add_lock);
        }
    }

}
