package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.RotateTransformation;

import java.util.List;

import com.blankj.utilcode.util.SizeUtils;

public class PhilipsWifiVideoLockAlarmIAdapter extends BaseQuickAdapter<WifiVideoLockAlarmRecord, BaseViewHolder> {
    List<WifiVideoLockAlarmRecord> data;

    private VideoRecordCallBackLinstener mListener;
    public interface VideoRecordCallBackLinstener{
        void onVideoRecordCallBackLinstener(WifiVideoLockAlarmRecord record);
    }

    public void setVideoRecordCallBackLinstener(VideoRecordCallBackLinstener listener){
        this.mListener = listener;
    }

    public PhilipsWifiVideoLockAlarmIAdapter(@Nullable List<WifiVideoLockAlarmRecord> data, VideoRecordCallBackLinstener listener) {
        super(R.layout.philips_item_wifi_video_lock_alarm_record,data);
        this.data = data;
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiVideoLockAlarmRecord record) {
        boolean first = record.isFirst();

        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvDayTime = helper.getView(R.id.tv_day_time);
        TextView tvRight = helper.getView(R.id.tv_right);

        long time = Long.parseLong(record.getTime());
        String s = DateUtils.currentLong2HourMin(time * 1000);
        tvTime.setText(TextUtils.isEmpty(s) ? "" : s);
        tvDayTime.setVisibility( first? View.VISIBLE : View.GONE);
        //设置天时间
        String dayTime = record.getDayTime();
        if (!TextUtils.isEmpty(dayTime)) {
            if (dayTime.equals(DateUtils.getCurrentYMD())) {
                dayTime = tvDayTime.getContext().getString(R.string.today);
            }
            tvDayTime.setText(dayTime+"");

        }


        ImageView ivContent = helper.getView(R.id.iv_content);
        ImageView ivPlay = helper.getView(R.id.iv_paly);

        RelativeLayout rlPic = helper.getView(R.id.rl_pic);

        if(record.getThumbUrl() == null && !record.isThumbState()){
            if(record.getFileName() == null || record.getFileName().isEmpty()){
                rlPic.setVisibility(View.GONE);
            }else{
                rlPic.setVisibility(View.VISIBLE);
            }
        }else{
            rlPic.setVisibility(View.VISIBLE);
        }

        switch (record.getType()){
            case 0x10://您的智能门锁低电量，请及时更换
                tvRight.setText(Html.fromHtml("<font color='#666666'>"+ tvRight.getContext().getString(R.string.wifi_lock_alarm_low_power)
                        + "</font>" + "<br><font color='#999999'>"+ tvRight.getContext().getString(R.string.philips_wifi_video_lock_alarm_1) +"</font>"));
                rlPic.setVisibility(View.GONE);
                break;
            case 0x20:// 您的门锁有故障，请注意
                tvRight.setText(tvRight.getContext().getString(R.string.wifi_lock_alarm_problem));
                break;
            case 0x03:// 门锁错误验证多次，门锁系统锁定90秒
                tvRight.setText(tvRight.getContext().getString(R.string.wifi_lock_alarm_lock_5min_1));
                break;
            case 0x08:// 门锁正在被机械方式开启，请回家或联系保安查看
                tvRight.setText(tvDayTime.getContext().getString(R.string.wifi_lock_alarm_opens));
                break;
            case 0x04:// 已监测到您的门锁被撬，请联系家人或小区保安
                tvRight.setText(Html.fromHtml("<font color='#666666'>"+ tvDayTime.getContext().getString(R.string.pick_proof)
                        +"</font>" + "<br><font color='#999999'>"+ tvRight.getContext().getString(R.string.philips_wifi_video_lock_alarm_2) +"</font>"));
                break;
            case 0x70:// 徘徊报警
                tvRight.setText(tvDayTime.getContext().getText(R.string.wandering_alarm));
                break;
            case 0x02:// 有人使用劫持密码开启门锁，赶紧联系或报警
                tvRight.setText(tvDayTime.getContext().getString(R.string.wifi_lock_alarm_hijack));
                break;
            case 0x01:// 锁定报警
                tvRight.setText(tvRight.getContext().getString(R.string.lock_alarm));
                break;
            case 0x40:// 布防报警
                tvRight.setText(Html.fromHtml("<font color='#666666'>"+ tvDayTime.getContext().getString(R.string.warring_defence)
                        +"</font>" + "<br><font color='#999999'>"+ tvRight.getContext().getString(R.string.philips_wifi_video_lock_alarm_3) +"</font>"));
                break;
        }

        if(record.getThumbUrl()!=null && !record.getThumbUrl().isEmpty()){
            Glide.with(ivContent.getContext()).load(record.getThumbUrl())
                    .apply(new RequestOptions().error(R.mipmap.img_video_lock_default).placeholder(R.mipmap.img_video_lock_default).dontAnimate()
                            .transform(new RotateTransformation(90f))).into(ivContent);
        }else{
            Glide.with(ivContent.getContext()).load(R.mipmap.img_video_lock_default).into(ivContent);
        }

        if(record.isThumbState() && record.getFileName() != null){
            ivPlay.setVisibility(View.VISIBLE);
        }else{
            ivPlay.setVisibility(View.GONE);
        }

        ivContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(record.getFileDate() != null && !record.getFileDate().isEmpty()){
                    if(mListener != null){
                        mListener.onVideoRecordCallBackLinstener(record);
                    }
                    /*
                    Intent intent = new Intent(mContext, WifiLockVideoDeviceRecordActivity.class);
                    intent.putExtra(KeyConstants.WIFI_SN,record.getWifiSN());
                    intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_ALARM_RECORD_DATA,record);
                    mContext.startActivity(intent);*/
                }
            }
        });


    }
}
