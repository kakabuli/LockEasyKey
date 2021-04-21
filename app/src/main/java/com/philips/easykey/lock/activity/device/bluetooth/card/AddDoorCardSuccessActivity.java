package com.philips.easykey.lock.activity.device.bluetooth.card;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.ShiXiaoNameAdapter;
import com.philips.easykey.lock.bean.ShiXiaoNameBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.ble.AddFingerSuccessPresenter;
import com.philips.easykey.lock.mvp.view.IAddFingerSuccessView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David
 */
public class AddDoorCardSuccessActivity extends BaseActivity<IAddFingerSuccessView, AddFingerSuccessPresenter<IAddFingerSuccessView>>
        implements View.OnClickListener, IAddFingerSuccessView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_success_page_number)
    TextView tvSuccessPageNumber;
    @BindView(R.id.et_door_card_name)
    EditText etDoorCardName;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.btn_save)
    Button btnSave;
    List<ShiXiaoNameBean> list = new ArrayList<>();
    ShiXiaoNameAdapter shiXiaoNameAdapter;
    private BleLockInfo bleLockInfo;
    private int userNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_door_card_success);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        tvContent.setText(getString(R.string.add_door_card));
        initRecycleview();
        bleLockInfo = MyApplication.getInstance().getBleService().getBleLockInfo();
        userNum = getIntent().getIntExtra(KeyConstants.USER_NUM, 0);
        tvSuccessPageNumber.setText("" + userNum+getString(R.string.card_add_success));
        initMonitor();
    }
    private void initMonitor() {
        etDoorCardName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                shiXiaoNameAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    protected AddFingerSuccessPresenter<IAddFingerSuccessView> createPresent() {
        return new AddFingerSuccessPresenter<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
                String strDoorCardName = etDoorCardName.getText().toString();
                if (!StringUtil.nicknameJudge(strDoorCardName)) {
                    ToastUtils.showShort(R.string.nickname_verify_error);
                    return;
                }
                showLoading(getString(R.string.is_savving));
                if (bleLockInfo == null) {
                    return;
                }
                mPresenter.uploadPasswordNickToServer(4, bleLockInfo.getServerLockInfo().getLockName(), strDoorCardName
                        , userNum > 9 ? "" + userNum : "0" + userNum);
                break;
        }
    }

    private void initRecycleview() {
        list.add(new ShiXiaoNameBean(getString(R.string.father), false));
        list.add(new ShiXiaoNameBean(getString(R.string.mother), false));
        list.add(new ShiXiaoNameBean(getString(R.string.elder_brother), false));
        list.add(new ShiXiaoNameBean(getString(R.string.small_di_di), false));
        list.add(new ShiXiaoNameBean(getString(R.string.elder_sister), false));
        list.add(new ShiXiaoNameBean(getString(R.string.rests), false));


        shiXiaoNameAdapter = new ShiXiaoNameAdapter(list);
        recycleview.setLayoutManager(new GridLayoutManager(this, 6));
        recycleview.setAdapter(shiXiaoNameAdapter);
        shiXiaoNameAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                ShiXiaoNameBean shiXiaoNameBean = list.get(position);
                String name = shiXiaoNameBean.getName();
                etDoorCardName.setText(name);
                etDoorCardName.setSelection(name.length());
                list.get(position).setSelected(true);
                shiXiaoNameAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onUploadSuccess() {
        //保存成功.
        hiddenLoading();
        Intent intent = new Intent(this, DoorCardManagerActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onUploadFailed(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void onUploadFailedServer(BaseResult result) {
        hiddenLoading();
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
    }
}
