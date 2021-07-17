package com.philips.easykey.lock.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.utils.RotateTransformation;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhilipsVideoLockWarningInformAdapter extends BaseQuickAdapter<WifiVideoLockAlarmRecord, BaseViewHolder> {

    private VideoLockWarningCallBackLinstener mListener;

    public interface VideoLockWarningCallBackLinstener{
        void onVideoLockWarningCallBackLinstener(WifiVideoLockAlarmRecord record);
    }

    public PhilipsVideoLockWarningInformAdapter( VideoLockWarningCallBackLinstener listener) {
        super(R.layout.philips_item_video_lock_warn_inform);
        this.mListener = listener;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, WifiVideoLockAlarmRecord record) {
        ImageView ivContent = baseViewHolder.getView(R.id.iv_content);
        ImageView ivPlay = baseViewHolder.getView(R.id.iv_paly);
        RelativeLayout rlPic = baseViewHolder.getView(R.id.rl_pic);
        if(record.getThumbUrl() == null && !record.isThumbState()){
            if(record.getFileName() == null || record.getFileName().isEmpty()){
                rlPic.setVisibility(View.GONE);
            }else{
                rlPic.setVisibility(View.VISIBLE);
            }
        }else{
            rlPic.setVisibility(View.VISIBLE);
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
        rlPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(record.getFileDate() != null && !record.getFileDate().isEmpty()){
                    if(mListener != null){
                        mListener.onVideoLockWarningCallBackLinstener(record);
                    }
                }
            }
        });
    }
}
