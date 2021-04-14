package com.philips.easykey.lock.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.videolock.WifiVideoLockAlbumDetailActivity;
import com.philips.easykey.lock.activity.device.wifilock.videolock.WifiVideoLockPreViewActivity;
import com.philips.easykey.lock.bean.FileBean;
import com.philips.easykey.lock.bean.FileItemBean;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.StringUtil;

import java.util.List;

import la.xiong.androidquick.tool.SizeUtils;


public class MyAlbumAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> {
    private Context context;

    public MyAlbumAdapter(List<FileBean> data, Context context) {
        super(R.layout.item_my_ablum_layout, data);
        this.context = context;
    }

    public onMyAlbumItemClickListener listener;

    public interface onMyAlbumItemClickListener{
        void onMyAlbumItemClick(MyAlbumItemAdapter adapter,List<FileItemBean> data,int position);
    }

    public void setOnMyAlbumItemClickListener(onMyAlbumItemClickListener listener){
        this.listener = listener;
    }
    @Override
    protected void convert(BaseViewHolder helper, FileBean item) {

         helper.setText(R.id.tv_date,item.getDate());

        RecyclerView recyclerView = helper.getView(R.id.recycleview);
        List<FileItemBean> data = item.getItem();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        if(item.isFirst()){
            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    super.onDraw(c, parent, state);
                }

                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                    if(parent.getChildAdapterPosition(view) % 3 != 0){
                        outRect.left = SizeUtils.dp2px(5);
                    }
                    if(parent.getChildAdapterPosition(view) >2){
                        outRect.top = SizeUtils.dp2px(5);
                    }
                }
            });
            item.setFirst(false);
        }
        recyclerView.setLayoutManager(gridLayoutManager);
        LogUtils.e("MyAlbumAdapter");
        MyAlbumItemAdapter adapter = new MyAlbumItemAdapter(data,context);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e(data.get(position).getPath());
                if(listener != null){
                    listener.onMyAlbumItemClick((MyAlbumItemAdapter)adapter,(List<FileItemBean>)adapter.getData(),position);
                }
            }
        });
        adapter.notifyDataSetChanged();
    }

}
