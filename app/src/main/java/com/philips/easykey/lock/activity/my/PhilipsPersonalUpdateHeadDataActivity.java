package com.philips.easykey.lock.activity.my;


import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.PersonalDataPresenter;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BitmapUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StorageUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.mvp.view.IPersonalDataView;
import com.philips.easykey.lock.widget.BottomMenuDialog;
import com.philips.easykey.lock.widget.CircleImageView;
import com.philips.easykey.lock.widget.image.GlideEngine;

import java.util.ArrayList;
import java.util.List;


import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class PhilipsPersonalUpdateHeadDataActivity extends BaseActivity<IPersonalDataView, PersonalDataPresenter<IPersonalDataView>> implements IPersonalDataView, View.OnClickListener {


    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    ImageView headPortraitRight;
    CircleImageView ivHead;
    RelativeLayout rlHead;
    ImageView headNameRight;
    TextView headPortraitName;
    RelativeLayout headNicknameLayout;
    TextView headTelNum;
    RelativeLayout headTelNumLayout;
    private BottomMenuDialog.Builder dialogBuilder;
    private BottomMenuDialog bottomMenuDialog;
    public static final int PHOTO_REQUEST_CODE = 101;
    private String photoPath;
    private Bitmap changeBitmap;
    public static final int REQUEST_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_personal_data);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        headPortraitRight = findViewById(R.id.head_portrait_right);
        ivHead = findViewById(R.id.iv_head);
        rlHead = findViewById(R.id.rl_head);
        headNameRight = findViewById(R.id.head_name_right);
        headPortraitName = findViewById(R.id.head_portrait_name);
        headNicknameLayout = findViewById(R.id.head_nickname_layout);
        headTelNum = findViewById(R.id.head_telNum);
        headTelNumLayout = findViewById(R.id.head_telNum_layout);

        headNicknameLayout.setOnClickListener(v -> {
            Intent mHeadNickName = new Intent(this, PersonalUpdateNickNameActivity.class);
            startActivity(mHeadNickName);
        });
        rlHead.setOnClickListener(v -> showHeadDialog());
        findViewById(R.id.bt_sign_out_login).setOnClickListener(v -> loginOut());

        initView();
        getMessage();
        tvContent.setText(getString(R.string.personal_center));
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getMessage();
    }

    private void initView() {
        //获取头像
        String strPhotoPath = (String) SPUtils.get(KeyConstants.HEAD_PATH, "");
        if ("".equals(strPhotoPath)) {
            mPresenter.downloadPicture(MyApplication.getInstance().getUid());
        } else {
            showImage(strPhotoPath);
        }

    }

    private void getMessage() {
        //获取昵称
        String userName = (String) SPUtils.get(SPUtils.USERNAME, "");
        if (!TextUtils.isEmpty(userName)) {
            if(userName.length() > 10){
                headPortraitName.setText(userName.substring(0,10) + "...");
            }else {
                headPortraitName.setText(userName);
            }
        }
        //获取手机号码
        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
        if (!TextUtils.isEmpty(phone)) {
            headTelNum.setText(StringUtil.phoneToHide(phone));
        }
    }

    @Override
    protected PersonalDataPresenter<IPersonalDataView> createPresent() {
        return new PersonalDataPresenter<>();
    }




    //展示头像对话框
    private void showHeadDialog() {
        dialogBuilder = new BottomMenuDialog.Builder(this);
        dialogBuilder.addMenu(R.string.select_photo_album, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlbum();

                if (bottomMenuDialog != null) {
                    bottomMenuDialog.dismiss();
                }


            }
        });
        dialogBuilder.addMenu(R.string.zi_pai, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  自拍
                requestPermission(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                });
                if (bottomMenuDialog != null) {
                    bottomMenuDialog.dismiss();
                }

            }
        });
        bottomMenuDialog = dialogBuilder.create();
        bottomMenuDialog.show();
    }

    private void createAlbum(){
        PictureSelector.create(PhilipsPersonalUpdateHeadDataActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .isCamera(false)
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode( PictureConfig.SINGLE)
                .isPreviewImage(true)
                .isZoomAnim(true)
                .isEnableCrop(true)// 是否裁剪
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .cropImageWideHigh(300,300)
                .withAspectRatio(1, 1)
                .cutOutQuality(100)// 裁剪输出质量 默认100
                .minimumCompressSize(20)// 小于多少kb的图片不压缩
                .forResult(onResultCallbackListener);
    }

    private OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener<LocalMedia>() {
        @Override
        public void onResult(List<LocalMedia> result) {
            if(result.size() > 0){
                photoPath = result.get(0).getCutPath();
                if (null == photoPath || "".equals(photoPath)) {
                    photoPath = "";
                }
                uploadPhoto();
            }else {
                ToastUtils.showShort(R.string.no_data);
            }
        }

        @Override
        public void onCancel() {

        }
    };

    private void showChangeNumberDialog(){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                this
                , getString(R.string.philips_activity_personal_change_tel_number),
                getString(R.string.philips_cancel), getString(R.string.philips_dialog_change), "#A4A4A4", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        Intent mUpdateNumber = new Intent(PhilipsPersonalUpdateHeadDataActivity.this, PhilipsPersonalUpdateNumberActivity.class);
                        startActivity(mUpdateNumber);

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void loginOut() {
        AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.hint), getString(R.string.confirm_log_out), getString(R.string.philips_cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
            @Override
            public void left() {

            }

            @Override
            public void right() {
                mPresenter.loginOut();
                showLoading(getString(R.string.is_login_out));
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(String toString) {

            }
        });
    }

    //请求权限
    public void requestPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  //版本号大鱼23
            //判断是否已经赋予权限   没有权限  赋值权限
            permissions = checkPermission(permissions);
            if (permissions.length == 0) {
                PictureSelector.create(PhilipsPersonalUpdateHeadDataActivity.this)
                        .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                        .isEnableCrop(true)// 是否裁剪
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .cropImageWideHigh(300,300)
                        .withAspectRatio(1, 1)
                        .cutOutQuality(100)// 裁剪输出质量 默认100
                        .forResult(onResultCallbackListener);
            } else {
                //如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(this, checkPermission(permissions), REQUEST_PERMISSION_REQUEST_CODE);
            }
        }
    }

    //检查权限
    public String[] checkPermission(String[] permissions) {
        List<String> noGrantedPermission = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED) {
                noGrantedPermission.add(permission);
            }
        }
        String[] permission = new String[noGrantedPermission.size()];

        return noGrantedPermission.toArray(permission);
    }

    //展示图片
    private void showImage(String photoPath) {
        if ("".equals(photoPath)) {
            ivHead.setImageDrawable(getResources().getDrawable(R.drawable.philips_mine_img_profile));
        } else {
            int degree = BitmapUtil.readPictureDegree(photoPath);
            changeBitmap = BitmapUtil.ratio(photoPath, 720, 720);
            /**
             * 把图片旋转为正的方向
             */
            if (changeBitmap!=null){
                Bitmap newbitmap = BitmapUtil.rotaingImageView(degree, changeBitmap);
                ivHead.setImageBitmap(newbitmap);
            }
        }
    }

    //上传到服务器
    private void uploadPhoto() {
        LogUtils.d("davi photoPath " + photoPath);
        String uid = MyApplication.getInstance().getUid();
        mPresenter.uploadPicture(uid, photoPath);
    }

    //权限申请结果
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
//                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(getActivity(), getString(R.string.not_allow_permission_warring), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            PictureSelector.create(PhilipsPersonalUpdateHeadDataActivity.this)
                    .openCamera(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                    .isEnableCrop(true)// 是否裁剪
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .cropImageWideHigh(300,300)
                    .withAspectRatio(1, 1)
                    .cutOutQuality(100)// 裁剪输出质量 默认100
                    .forResult(onResultCallbackListener);
        }
    }

    @Override
    public void photoUploadSuccess() {
        mPresenter.downloadPicture(MyApplication.getInstance().getUid());
    }

    @Override
    public void photoUploadFail(BaseResult baseResult) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, baseResult.getCode()));
    }

    @Override
    public void photoUploadError(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.upload_hear) + HttpUtils.httpProtocolErrorCode(this, throwable));
    }


    @Override
    public void downloadPhoto(Bitmap bitmap) {
        StorageUtil.getInstance().saveServerPhoto(bitmap);
        ivHead.setImageBitmap(bitmap);
    }

    @Override
    public void downloadPhotoError(Throwable e) {
//        ToastUtils.showShort( HttpUtils.httpProtocolErrorCode(this,e));
    }

    @Override
    public void onLoginOutSuccess() {
        hiddenLoading();
        //退出mqtt
        if (MyApplication.getInstance().getMqttService()!=null){
            MyApplication.getInstance().getMqttService().httpMqttDisconnect();
        }
        MyApplication.getInstance().tokenInvalid(false);
    }

    @Override
    public void onLoginOutFailed(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.philips_code_logout_fail) + HttpUtils.httpProtocolErrorCode(this, throwable));
        LogUtils.d("退出失败  " + throwable.getMessage());
    }

    @Override
    public void onLoginOutFailedServer(BaseResult result) {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.philips_code_logout_fail) + HttpUtils.httpErrorCode(this, result.getCode()));
        LogUtils.d("退出失败  " + result.getMsg());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}