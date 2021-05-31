package com.philips.easykey.lock.activity.device.singleswitch;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.bluetooth.password.CycleRulesActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.TimeUtil;

import java.util.Arrays;


public class AddTimeActivity extends BaseAddToApplicationActivity {

    ImageView back;
    TextView tvStartTime;
    TextView tvEndTime;
    TextView tvRepeatRule;
    TextView tvAddDevice;
    Button buttonNext;
    public static final int REQUEST_CODE = 100;
    private int[] days;
    private String weekRule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);

        back = findViewById(R.id.back);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        tvRepeatRule = findViewById(R.id.tv_repeat_rule);
        tvAddDevice = findViewById(R.id.tv_add_device);
        buttonNext = findViewById(R.id.button_next);

        tvStartTime.setOnClickListener(v -> {
            TimeUtil.getInstance().getHourMinute(this, new TimeUtil.TimeListener() {
                @Override
                public void time(String hour, String minute) {
                    tvStartTime.setText(hour + ":" + minute);
                }
            });
        });
        tvEndTime.setOnClickListener(v -> {
            TimeUtil.getInstance().getHourMinute(this, new TimeUtil.TimeListener() {
                @Override
                public void time(String hour, String minute) {
                    tvEndTime.setText(hour + ":" + minute);
                }
            });
        });
        tvRepeatRule.setOnClickListener(v -> {
            Intent intent = new Intent(this, CycleRulesActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });

        tvRepeatRule.setText(getString(R.string.no_repeat));
        tvAddDevice.setText(getString(R.string.to_add));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE == requestCode) {
                weekRule = data.getStringExtra(KeyConstants.WEEK_REPEAT_DATA);
                days = data.getIntArrayExtra(KeyConstants.DAY_MASK);
                LogUtils.d("收到的周计划是   " + Arrays.toString(days));
                tvRepeatRule.setText(weekRule);
            }
        }
    }
}
