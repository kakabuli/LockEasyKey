package com.philips.easykey.lock.activity.my;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;

public class PersonalMessageDetailActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    TextView tvTitle;
    TextView tvTime;
    TextView tvDetail;
    private Long time;
    private String title;
    private String content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_message_detail);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        tvTitle = findViewById(R.id.tv_title);
        tvTime = findViewById(R.id.tv_time);
        tvDetail = findViewById(R.id.tv_detail);

        Intent messageIntent = getIntent();
        time = messageIntent.getLongExtra(KeyConstants.MESSAGE_DETAIL_TIME, 0);
        title = messageIntent.getStringExtra(KeyConstants.MESSAGE_DETAIL_TITLE);
        content = messageIntent.getStringExtra(KeyConstants.MESSAGE_DETAIL_CONTENT);
        initView();
        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.message_detail));
    }

    private void initView() {
        String messageTime = DateUtils.secondToDate(time);
        tvTime.setText(messageTime);
        if (title != null) {
            tvTitle.setText(title);
        }
        if (content != null) {
            tvDetail.setText(content);
        }

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
