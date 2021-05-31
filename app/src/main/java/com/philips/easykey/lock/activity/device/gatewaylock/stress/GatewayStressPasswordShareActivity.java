package com.philips.easykey.lock.activity.device.gatewaylock.stress;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


/**
 * Created by David
 */
public class GatewayStressPasswordShareActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    TextView tvNumber;
    Button btnDelete;
    TextView tvName;
    TextView tvTime;
    TextView tvShortMessage;
    TextView tvWeiXin;
    TextView tvCopy;
    ImageView ivEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_stress_password_share);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        tvNumber = findViewById(R.id.tv_number);
        btnDelete = findViewById(R.id.btn_delete);
        tvName = findViewById(R.id.tv_name);
        tvTime = findViewById(R.id.tv_time);
        tvShortMessage = findViewById(R.id.tv_short_message);
        tvWeiXin = findViewById(R.id.tv_wei_xin);
        tvCopy = findViewById(R.id.tv_copy);
        ivEditor = findViewById(R.id.iv_editor);

        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.password_detail));
        ivEditor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_editor:
                View mView = LayoutInflater.from(this).inflate(R.layout.have_edit_dialog, null);
                TextView tvTitle = mView.findViewById(R.id.tv_title);
                EditText editText = mView.findViewById(R.id.et_name);
                //TODO 获取到密码设置
              /*  if (password.getNickName()!=null){
                    editText.setText(password.getNickName());
                    editText.setSelection(password.getNickName().length());
                }*/
                TextView tv_cancel = mView.findViewById(R.id.tv_left);
                TextView tv_query = mView.findViewById(R.id.tv_right);
                AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
                tvTitle.setText(getString(R.string.please_input_password_name));
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                tv_query.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editText.getText().toString().trim();
                        if (!StringUtil.nicknameJudge(name)) {
                            ToastUtils.showShort(R.string.nickname_verify_error);
                            return;
                        }
                        //todo 获取到密码对比
                  /*      if (StringUtil.judgeNicknameWhetherSame(password.getNickName(),name)){
                            ToastUtils.showShort(R.string.nickname_not_modify);
                            alertDialog.dismiss();
                            return;
                        }*/
                        //todo 更新昵称
                        alertDialog.dismiss();
                    }
                });
                break;
        }
    }
}
