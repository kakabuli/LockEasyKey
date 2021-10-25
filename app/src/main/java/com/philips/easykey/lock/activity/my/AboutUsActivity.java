package com.philips.easykey.lock.activity.my;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewNotActivateActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.SharedUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by David on 2019/4/9
 */
public class AboutUsActivity extends BaseAddToApplicationActivity {

    TextView tvContent;
    ImageView ivPhilipsWeiXinQRCode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ivPhilipsWeiXinQRCode = findViewById(R.id.iv_philips_wechat_qr_code);
        tvContent = findViewById(R.id.tv_content);
        tvContent.setText(getString(R.string.philips_contact_us));

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.rl_customer_service_phone).setOnClickListener(v -> SharedUtil.getInstance().callPhone(this, getResources().getString(R.string.Philips_after_sales_number)));

        ivPhilipsWeiXinQRCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                boolean b = saveImageToGallery(AboutUsActivity.this, ivPhilipsWeiXinQRCode);
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
        // 首先保存图片：相册名称
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.app_name);
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = getString(R.string.app_name) + "-" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            //把文件插入到系统图库
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
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
