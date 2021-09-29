package com.philips.easykey.lock.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.my.AboutUsActivity;
import com.philips.easykey.lock.activity.my.BarCodeActivity;
import com.philips.easykey.lock.activity.my.PersonalFAQActivity;
import com.philips.easykey.lock.activity.my.PersonalSystemSettingActivity;
import com.philips.easykey.lock.activity.my.PhilipsPersonalSecuritySettingActivity;
import com.philips.easykey.lock.activity.my.PhilipsPersonalUpdateHeadDataActivity;
import com.philips.easykey.lock.activity.my.PhilipsUserFeedbackActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.MyFragmentPresenter;
import com.philips.easykey.lock.mvp.view.IMyFragmentView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserNickResult;
import com.philips.easykey.lock.utils.BitmapUtil;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.MMKVUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StatusBarUtils;
import com.philips.easykey.lock.utils.StorageUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.philips.easykey.lock.utils.ftp.GeTui;
import com.philips.easykey.lock.widget.CircleImageView;


import static android.app.Activity.RESULT_OK;


/**
 * 我的Fragment
 *
 * @company kaadas
 * created at 2019/2/25 14:47
 */
public class PhilipsPersonalCenterFragment extends BaseFragment<IMyFragmentView, MyFragmentPresenter<IMyFragmentView>> implements IMyFragmentView {

    RelativeLayout faqLayout;
    RelativeLayout headSecond;
    CircleImageView ivPhoto;
    RelativeLayout rlUserFeedback;
    TextView tvNickName;
    TextView tvNumber;

    private View mPersonlCenterView;
    private Bitmap changeBitmap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mPersonlCenterView == null) {
            mPersonlCenterView = inflater.inflate(R.layout.philips_fragment_my, container, false);
        }

        faqLayout = mPersonlCenterView.findViewById(R.id.faq_layout);
        headSecond = mPersonlCenterView.findViewById(R.id.head_second);
        ivPhoto = mPersonlCenterView.findViewById(R.id.iv_photo);
        rlUserFeedback = mPersonlCenterView.findViewById(R.id.rl_user_feedback);
        tvNickName = mPersonlCenterView.findViewById(R.id.tv_nick_name);
        tvNumber = mPersonlCenterView.findViewById(R.id.tv_number);

        headSecond.setOnClickListener(v -> {
            Intent updateHeadData = new Intent(getActivity(), PhilipsPersonalUpdateHeadDataActivity.class);
            startActivity(updateHeadData);
        });
        mPersonlCenterView.findViewById(R.id.system_setting).setOnClickListener(v -> {
            Intent mSercurityIntent = new Intent(getActivity(), PhilipsPersonalSecuritySettingActivity.class);
            startActivity(mSercurityIntent);
        });
        rlUserFeedback.setOnClickListener(v -> {
            Intent mUserFeedbackintent = new Intent(getActivity(), PhilipsUserFeedbackActivity.class);
            startActivity(mUserFeedbackintent);
        });
        faqLayout.setOnClickListener(v -> {
            Intent mFaq = new Intent(getActivity(), PersonalFAQActivity.class);
            startActivity(mFaq);
        });
        mPersonlCenterView.findViewById(R.id.about_app).setOnClickListener(v -> {
            Intent mSystemSetting = new Intent(getActivity(), PersonalSystemSettingActivity.class);
            startActivity(mSystemSetting);
        });
        mPersonlCenterView.findViewById(R.id.contact_us).setOnClickListener(v -> {
            Intent aboutIntent = new Intent(getActivity(), AboutUsActivity.class);
            startActivity(aboutIntent);
        });

        return mPersonlCenterView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("PersonalCenter---onResume");
        initView();
    }

    private void initView() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(headSecond.getLayoutParams());
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        headSecond.setLayoutParams(lp);
        //用户名
        String userName = (String) SPUtils.get(SPUtils.USERNAME, "");
        if (!TextUtils.isEmpty(userName)) {
            tvNickName.setText(userName);
        } else {
            //获取昵称
            String uid = MMKVUtils.getStringMMKV(SPUtils.UID);
            mPresenter.getUserName(uid);
        }

        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
        if (!TextUtils.isEmpty(phone)) {
            tvNumber.setText(getString(R.string.personal_number) + "：" + StringUtil.phoneToHide(phone));
        }

        String photoPath = (String) SPUtils.get(KeyConstants.HEAD_PATH, "");
        if ("".equals(photoPath)) {
            mPresenter.downloadPicture(MyApplication.getInstance().getUid());
        } else {
            showImage(photoPath);
        }
    }

    @Override
    protected MyFragmentPresenter<IMyFragmentView> createPresent() {
        return new MyFragmentPresenter<>();
    }

    private void showImage(String photoPath) {
        if ("".equals(photoPath)) {
            ivPhoto.setImageDrawable(getResources().getDrawable(R.drawable.philips_mine_img_profile));
        } else {
            int degree = BitmapUtil.readPictureDegree(photoPath);
            changeBitmap = BitmapUtil.ratio(photoPath, 720, 720);
            /**
             * 把图片旋转为正的方向
             */
            if (changeBitmap != null) {
                Bitmap newbitmap = BitmapUtil.rotaingImageView(degree, changeBitmap);
                ivPhoto.setImageBitmap(newbitmap);
                ivPhoto.setBackgroundResource(R.drawable.head_circle_bj);
            }
        }
    }

    @Override
    public void downloadPhoto(Bitmap bitmap) {
        ivPhoto.setImageBitmap(bitmap);
        ivPhoto.setBackgroundResource(R.drawable.head_circle_bj);
        StorageUtil.getInstance().saveServerPhoto(bitmap);
    }

    @Override
    public void downloadPhotoError(Throwable e) {
//        ToastUtils.showShort( HttpUtils.httpProtocolErrorCode(getActivity(),e));

    }

    @Override
    public void getNicknameSuccess(UserNickResult userNickResult) {
        String nickName = userNickResult.getData().getNickName();
        SPUtils.put(SPUtils.USERNAME, nickName);
        tvNickName.setText(nickName);
    }

    @Override
    public void getNicknameFail(BaseResult baseResult) {
        String account = (String) SPUtils.get(SPUtils.PHONEN, "");
        tvNickName.setText(account);
    }

    @Override
    public void getNicknameError(Throwable throwable) {
        String account = (String) SPUtils.get(SPUtils.PHONEN, "");
        tvNickName.setText(account);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case KeyConstants.SCANPRODUCT_REQUEST_CODE:
                    String result = data.getStringExtra(Constants.SCAN_QR_CODE_RESULT);
                    LogUtils.d(result+"     产品激活");
                    if(result.contains(" ")){
                        result=result.replace(" ","%20");
                    }
                    String bar_url = "http://s.kaadas.com:8989/extFun/regWeb.asp?uiFrm=2&id=" + result + "&telnum=";
//                    +"&telnum=18988780718&mail=8618988780718&nickname=8618988780718";

                    //获取手机号码
                    String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
                    if (!TextUtils.isEmpty(phone)) {
                        bar_url = bar_url + phone + "&mail=";
                    }
                    String userName = (String) SPUtils.get(SPUtils.USERNAME, "");
                    if (!TextUtils.isEmpty(userName)) {
                        bar_url = bar_url + userName + "&nickname=" + userName;
                    }
                    Log.e(GeTui.VideoLog, "finally->result:" + bar_url);
                    Intent intent = new Intent(getActivity(), BarCodeActivity.class);
                    intent.putExtra(KeyConstants.BAR_CODE, bar_url);
                    startActivity(intent);

                    //     String bar_url="http://s.kaadas.com:8989/extFun/regWeb.asp?uiFrm=2&id=SN-GW01183810798%20MAC-90:F2:78:70:0F:33&telnum=18988780718&mail=8618988780718&nickname=8618988780718";

                    break;
            }

        }

    }
}
