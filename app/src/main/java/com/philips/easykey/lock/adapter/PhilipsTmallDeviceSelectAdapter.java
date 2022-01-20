package com.philips.easykey.lock.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.SevendayDataStatisticsBean;
import com.philips.easykey.lock.publiclibrary.bean.TmallDeviceListBean;
import com.philips.easykey.lock.publiclibrary.http.result.TmallDeviceListResult;
import com.philips.easykey.lock.widget.CurveChart;

import org.jetbrains.annotations.NotNull;

public class PhilipsTmallDeviceSelectAdapter extends BaseQuickAdapter<TmallDeviceListBean, BaseViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PhilipsTmallDeviceSelectAdapter() {
        super(R.layout.philips_item_tmall_select_device);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, TmallDeviceListBean tmallDeviceList) {
        TextView tvDeviceName = baseViewHolder.getView(R.id.tvDeviceName);
        TextView tvBinding = baseViewHolder.getView(R.id.tvBinding);
        CheckBox ck = baseViewHolder.getView(R.id.ck);
        if(tmallDeviceList.getLockNickname().length() > 10){
            tvDeviceName.setText(tmallDeviceList.getLockNickname().substring(0,10) + "...");
        }else {
            tvDeviceName.setText(tmallDeviceList.getLockNickname());
        }
        if(tmallDeviceList.getIsBind() == 0){
            tvBinding.setVisibility(View.GONE);
            ck.setVisibility(View.VISIBLE);
            ck.setChecked(tmallDeviceList.isSelece());
        }else if(tmallDeviceList.getIsBind() == 1){
            tvBinding.setVisibility(View.VISIBLE);
            ck.setVisibility(View.GONE);
        }
        baseViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tmallDeviceList.getIsBind() == 1)return;
                if (listener != null) {
                    listener.onItemClick(baseViewHolder.getLayoutPosition());
                }
            }
        });
    }

}
