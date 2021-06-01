package com.philips.easykey.lock.activity.device.gatewaylock.card;

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
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


/**
 * Created by David
 */
public class GatewayDoorCardManagerDetailActivity extends BaseAddToApplicationActivity implements View.OnClickListener {


    ImageView ivBack;
    TextView tvContent;
    TextView tvNumber;
    Button btnDelete;
    TextView tvName;
    ImageView ivEditor;
    TextView tvTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_card_manager_detail);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        tvNumber = findViewById(R.id.tv_number);
        btnDelete = findViewById(R.id.btn_delete);
        tvName = findViewById(R.id.tv_name);
        ivEditor = findViewById(R.id.iv_editor);
        tvTime = findViewById(R.id.tv_time);

        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.door_card_detail));
        ivEditor.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:

                finish();
                break;
            case R.id.iv_editor:
                //弹出编辑框
                View mView = LayoutInflater.from(this).inflate(R.layout.have_edit_dialog, null);
                TextView tvTitle = mView.findViewById(R.id.tv_title);
                EditText editText = mView.findViewById(R.id.et_name);
                //获取卡昵称判断
               /* if (card.getNickName()!=null){
                    editText.setText(card.getNickName());
                    editText.setSelection(card.getNickName().length());
                }*/
                TextView tv_cancel = mView.findViewById(R.id.tv_left);
                TextView tv_query = mView.findViewById(R.id.tv_right);
                AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
                tvTitle.setText(getString(R.string.input_door_card_name));
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
                        //TODO 获取卡昵称判断
                      /*  if (StringUtil.judgeNicknameWhetherSame(card.getNickName(),name)){
                            ToastUtils.showShort(R.string.nickname_not_modify);
                            alertDialog.dismiss();
                            return;
                        }*/
                        //todo 上传昵称
                        alertDialog.dismiss();
                    }
                });
                break;
            case R.id.btn_delete:
                //删除
                if (NetUtil.isNetworkAvailable()) {
                    AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.hint), getString(R.string.confirm_delete_door_card), getString(R.string.philips_cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
                        @Override
                        public void left() {

                        }

                        @Override
                        public void right() {
                            //todo 删除
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(String toString) {

                        }
                    });
                } else {
                    ToastUtils.showShort(getString(R.string.network_exception));
                }
                break;
        }
    }
}
