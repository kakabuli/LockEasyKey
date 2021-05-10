package com.philips.easykey.lock.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhilipsVideoLockWarningInformAdapter extends BaseQuickAdapter<WifiVideoLockAlarmRecord, BaseViewHolder> {

    private VideoLockWarningCallBackLinstener mListener;

    public interface VideoLockWarningCallBackLinstener{
        void onVideoLockWarningCallBackLinstener(WifiVideoLockAlarmRecord record);
    }

    public PhilipsVideoLockWarningInformAdapter(@Nullable List<WifiVideoLockAlarmRecord> data, VideoLockWarningCallBackLinstener listener) {
        super(R.layout.philips_item_video_lock_warn_inform, data);
        this.mListener = listener;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, WifiVideoLockAlarmRecord wifiVideoLockAlarmRecord) {
        ImageView ivContent = baseViewHolder.getView(R.id.iv_content);
        //Glide.with(getContext()).load(R.mipmap.img_video_lock_default).into(ivContent);
        ivContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wifiVideoLockAlarmRecord.getFileDate() != null && !wifiVideoLockAlarmRecord.getFileDate().isEmpty()){
                    if(mListener != null){
                        mListener.onVideoLockWarningCallBackLinstener(wifiVideoLockAlarmRecord);
                    }
                }
            }
        });
    }
}
