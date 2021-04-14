package com.philips.easykey.lock.adapter;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.FileBean;
import com.philips.easykey.lock.bean.FileItemBean;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MyAlbumItemAdapter extends BaseQuickAdapter<FileItemBean, BaseViewHolder> {

    Context context;

    public MyAlbumItemAdapter(List<FileItemBean> data, Context context) {
        super(R.layout.item_my_ablum, data);
        this.context = context;
    }


    @Override
    protected void convert(BaseViewHolder helper, FileItemBean item) {
        LogUtils.e("MyAlbumItemAdapter");
        if(item.getType() == 2 && item.getSuffix().equals("mp4")){
            LogUtils.e("---------" +(item.getPath()));
            LogUtils.e("---------" +getVideoDuration(item.getPath()));

            helper.getView(R.id.duration).setVisibility(View.VISIBLE);
            helper.setText(R.id.duration,getVideoDuration(item.getPath()));
            helper.getView(R.id.iv_icon_background).setVisibility(View.VISIBLE);
            helper.getView(R.id.iv_icon_paly).setVisibility(View.VISIBLE);
        }else{
            helper.getView(R.id.duration).setVisibility(View.GONE);
            helper.getView(R.id.iv_icon_background).setVisibility(View.GONE);
            helper.getView(R.id.iv_icon_paly).setVisibility(View.GONE);
        }
        Glide.with(context).load(item.getPath()).into((ImageView) helper.getView(R.id.iv_pic));

        if(item.isShowDelete()){
            helper.getView(R.id.iv_icon_delete).setVisibility(View.VISIBLE);
            if(item.isSelect()){
                helper.getView(R.id.iv_icon_delete).setSelected(true);
            }else{
                helper.getView(R.id.iv_icon_delete).setSelected(false);
            }
        }else{
            helper.getView(R.id.iv_icon_delete).setVisibility(View.GONE);
            item.setSelect(false);
        }


    }

    //获取视频总时长

    private String getVideoDuration(String path){
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        try {
            media.setDataSource(path);
            String duration = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            media.release();
            int time = Integer.parseInt(duration) /1000;
            return DateUtils.GetMinutes(time);
        }catch (Exception e){

        }

        return "00:00";
    }


}
