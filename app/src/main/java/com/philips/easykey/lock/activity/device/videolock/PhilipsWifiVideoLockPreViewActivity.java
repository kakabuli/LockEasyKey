package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;

import java.io.File;


public class PhilipsWifiVideoLockPreViewActivity extends BaseAddToApplicationActivity {

    PhotoView preview_img;
    ImageView iv_back;
    TextView tvName;

    private String stringExtra;

    /*@Override
    protected MyAlbumPlayerPresenter<IMyAlbumPlayerView> createPresent() {
        return new MyAlbumPlayerPresenter<>();
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_video_preview);

        preview_img = findViewById(R.id.preview_img);
        iv_back = findViewById(R.id.back);
        tvName = findViewById(R.id.tv_name);

        iv_back.setOnClickListener(v -> finish());
        findViewById(R.id.iv_myalbum_delete).setOnClickListener(v -> {
            if(!stringExtra.isEmpty() && new File(stringExtra).exists()){
                showDeleteDialog(stringExtra);
            }
        });

        stringExtra = getIntent().getStringExtra(KeyConstants.VIDEO_PIC_PATH);
        String name = getIntent().getStringExtra("NAME");

        tvName.setText(name);
        // 启用图片缩放功能
        preview_img.enable();

        //.apply(new RequestOptions().transform(new RotateTransformation(90)))
        Glide.with(this).load(stringExtra).into(preview_img);
    }

    private void showDeleteDialog(String filepath) {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(
                PhilipsWifiVideoLockPreViewActivity.this
                , getString(R.string.activity_wifi_video_preview_delete),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#0066A1", "#FFFFFF", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        Intent intent = new Intent(PhilipsWifiVideoLockPreViewActivity.this, PhilipsWifiVideoLockAlbumActivity.class);
                        intent.putExtra(KeyConstants.VIDEO_PIC_PATH,stringExtra);
                        intent.putExtra("NAME",tvName.getText().toString());
                        setResult(RESULT_OK,intent);
                        finish();
                        if(!filepath.isEmpty()){
                            if(new File(filepath).exists()){
                                new File(filepath).delete();
                            }
                        }
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }
}
