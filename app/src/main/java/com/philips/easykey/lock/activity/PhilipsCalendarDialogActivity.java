package com.philips.easykey.lock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.bean.DateBean;
import com.bigkoo.pickerview.listener.OnPagerChangeListener;
import com.bigkoo.pickerview.listener.OnSingleChooseListener;
import com.bigkoo.pickerview.utils.CalendarUtil;
import com.bigkoo.pickerview.view.CalendarView;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.DeviceSelectAdapter;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

import java.util.ArrayList;
import java.util.List;


public class PhilipsCalendarDialogActivity extends BaseAddToApplicationActivity {

    private int RESULT_OK = 100;
    private int RESULT_CLOSE = 101;
    private CalendarView calendarView;
    private TextView  title;
    private RelativeLayout mRldialog;

    private int[] cDate = CalendarUtil.getCurrentDate();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.philips_calendar_dialog);

        mRldialog = findViewById(R.id.rl_dialog);
        calendarView = findViewById(R.id.calendar);
        title = findViewById(R.id.title);

        calendarView.setInitDate(cDate[0] + "." + cDate[1])
                .setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init();

        title.setText(cDate[0] + "年" + cDate[1] + "月");

        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                title.setText(date[0] + "年" + date[1] + "月");
            }
        });

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                title.setText(date.getSolar()[0] + "年" + date.getSolar()[1] + "月");
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("date", date.getSolar()[0] + "年" + date.getSolar()[1] + "月" + date.getSolar()[2]);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    public void lastMonth(View view) {
        calendarView.lastMonth();
    }

    public void nextMonth(View view) {
        calendarView.nextMonth();
    }

    public void dismiss(View view){
        finish();
        setResult(RESULT_CLOSE);
    }

}
