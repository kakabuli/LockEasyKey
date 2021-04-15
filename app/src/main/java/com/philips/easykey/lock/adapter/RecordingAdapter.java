package com.philips.easykey.lock.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.SPUtils2;
import com.philips.easykey.lock.utils.db.MediaItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class RecordingAdapter extends BaseQuickAdapter<MediaItem, BaseViewHolder> {

    SimpleDateFormat showFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//UTC时间格式
   // SimpleDateFormat showFormater = new SimpleDateFormat("HH:mm:ss");//UTC时间格式
    public RecordingAdapter() {
        super(R.layout.recording_single_entry);
    }

    Context mContext=null;

    public RecordingAdapter(Context context, @Nullable List<MediaItem> data) {
        super(R.layout.recording_single_entry,data);
		this.mContext=context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MediaItem item) {

        long currentTime= Long.parseLong(item.getCreateTime());
        String imgPath = item.getPath();

        String key=item.getName()+ Constants.RECORDINGTAG;
        String name= (String) SPUtils2.get(mContext,key,"");
		helper.setText(R.id.pir_img_time, showFormater.format(new Date(currentTime)));


        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.pre_video_image)       //加载成功之前占位图
                .error(R.mipmap.pre_video_image)          //加载错误之后的错误图
                .diskCacheStrategy(DiskCacheStrategy.NONE);


        Glide.with(mContext).load(imgPath)
                .apply(options)
                .into((ImageView) helper.getView(R.id.pir_img_icon));


//        Glide.with(mContext).load(imgPath)
//				.error(R.mipmap.pre_video_image)
//				.diskCacheStrategy(DiskCacheStrategy.NONE)
//				.placeholder(R.mipmap.pre_video_image)
//				.into((ImageView) helper.getView(R.id.pir_img_icon));


                 if(item.getMediaType()==2){  //图片
                     helper.getView(R.id.recording_play_icon).setVisibility(View.GONE);
                 }else{
                     helper.getView(R.id.recording_play_icon).setVisibility(View.VISIBLE);
                 }


        if(!TextUtils.isEmpty(name)){
            helper.getView(R.id.pir_history_tv_cicle).setVisibility(View.INVISIBLE);
        }else {
            helper.getView(R.id.pir_history_tv_cicle).setVisibility(View.VISIBLE);
        }

		// helper.addOnClickListener(R.id.pir_img_rl);

		//String result=(String) SPUtils2.get(MyApplication.getInstance(),item,"");

	//	String imgPath=(String) SPUtils2.get(MyApplication.getInstance(),item+ PirConst.IMG_DOWNLOAD_SUC,"");
		//Log.e(PirConst.VideoLog,"item:"+item+ PirConst.IMG_DOWNLOAD_SUC+"  PirHistoryAdapter:"+imgPath);



//		if(!TextUtils.isEmpty(result)){
//		   helper.getView(R.id.pir_history_tv_cicle).setVisibility(View.INVISIBLE);
//		}else {
//			helper.getView(R.id.pir_history_tv_cicle).setVisibility(View.VISIBLE);
//		}

     //   helper.addOnClickListener(R.id.img_pir_del);
     
    }
}
