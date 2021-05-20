package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.RotateTransformation;

import java.util.List;

public class PhilipsWifiLockVistorIAdapter extends BaseQuickAdapter<WifiVideoLockAlarmRecord, BaseViewHolder> {
    List<WifiVideoLockAlarmRecord> data;

    private VideoRecordCallBackLinstener mListener;
    public interface VideoRecordCallBackLinstener{
        void onVideoRecordCallBackLinstener(WifiVideoLockAlarmRecord record);
    }

    public void setVideoRecordCallBackLinstener(VideoRecordCallBackLinstener listener){
        this.mListener = listener;
    }

    public PhilipsWifiLockVistorIAdapter(@Nullable List<WifiVideoLockAlarmRecord> data, VideoRecordCallBackLinstener listener) {
        super(R.layout.philips_item_wifi_lock_vistor_record,data);
        this.data = data;
        this.mListener = listener;
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiVideoLockAlarmRecord record) {
        boolean first = record.isFirst();
        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvDayTime = helper.getView(R.id.tv_day_time);

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

        TextView tvRight = helper.getView(R.id.tv_right);
        String content = BleUtil.getVideoAlarmByType(record.getType(), tvRight.getContext());
        tvRight.setText(content + "");

        ivContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(record.getFileDate() != null && !record.getFileDate().isEmpty()){

                    if(mListener != null){
                        mListener.onVideoRecordCallBackLinstener(record);
                    }
                }
            }
        });
    }


}
