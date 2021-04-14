package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.videolock.WifiVideoLockHelpActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.activity.device.wifilock.videolock.WifiVideoLockFourthActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiLockAddNewThird2Activity extends BaseAddToApplicationActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help)
    ImageView help;
    @BindView(R.id.head)
    TextView head;
    @BindView(R.id.notice)
    TextView notice;
    @BindView(R.id.already_modify)
    TextView alreadyModify;
    @BindView(R.id.iv_anim)
    ImageView ivAnim;
    @BindView(R.id.title)
    TextView title;


    private String wifiModelType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_third2);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        wifiModelType = intent.getStringExtra("wifiModelType");
        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();

        if(wifiModelType.contains("VIDEO")){
            head.setText(R.string.modify_admin_pwd);
            title.setText(R.string.modify_admin_pwd);
        }else {
            head.setText("第四步：" + getString(R.string.modify_admin_pwd));
            title.setText(R.string.add_lock);
        }
    }

    @OnClick({R.id.back, R.id.help, R.id.already_modify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.help:
                if(wifiModelType.contains("VIDEO")){
                    startActivity(new Intent(this, WifiVideoLockHelpActivity.class));
                }else{
                    startActivity(new Intent(this,WifiLockHelpActivity.class));
                }
                break;
            case R.id.already_modify:
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

                break;
        }
    }
}
