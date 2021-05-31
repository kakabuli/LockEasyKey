package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WifiLockAddNewNotActivateActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    TextView notice;
    ImageView ivWeiXinCode;
    TextView lockActivated;

    private String wifiModelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_not_activate);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        ivWeiXinCode = findViewById(R.id.iv_wei_xin_code);
        lockActivated = findViewById(R.id.lock_activated);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> startActivity(new Intent(WifiLockAddNewNotActivateActivity.this,WifiLockHelpActivity.class)));
        lockActivated.setOnClickListener(v -> {
            Intent wifiIntent = new Intent(this, WifiLockAddNewThirdActivity.class);
            wifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(wifiIntent);
        });

        Intent intent = getIntent();
        wifiModelType = intent.getStringExtra("wifiModelType");
        ivWeiXinCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean b = saveImageToGallery(WifiLockAddNewNotActivateActivity.this, ivWeiXinCode);
                if (b) {
                    ToastUtils.showShort(getString(R.string.save_success));
                } else {
                    ToastUtils.showShort(getString(R.string.save_failed));
                }
                return false;
            }
        });
    }


    //保存文件到指定路径
    public boolean saveImageToGallery(Context context, ImageView imageView) {
        BitmapDrawable bmpDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bmp = bmpDrawable.getBitmap();
        if (bmp == null) {
            ToastUtils.showShort(getString(R.string.save_failed));
            return false;
        }
        // 首先保存图片
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "dearxy";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
