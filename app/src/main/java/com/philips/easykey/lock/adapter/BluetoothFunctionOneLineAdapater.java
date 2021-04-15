package com.philips.easykey.lock.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.BluetoothLockFunctionBean;

import java.util.List;

public class BluetoothFunctionOneLineAdapater extends RecyclerView.Adapter {
   private List<BluetoothLockFunctionBean> data;
    private OnItemClickListener onItemClickListener;

    public BluetoothFunctionOneLineAdapater(List<BluetoothLockFunctionBean> data, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_lock_function, parent, false);
        view.getLayoutParams().height = parent.getHeight() ;
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyHolder holder = (MyHolder) viewHolder;
        holder.iv_icon.setImageResource(data.get(position).getImage());
        holder.tvNamepublic.setText(data.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener!=null){
                    onItemClickListener.onItemClick(position,data.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ImageView iv_icon;

        public TextView tvNamepublic;

        MyHolder(@NonNull View itemView) {
            super(itemView);
            iv_icon = itemView.findViewById(R.id.iv);
            tvNamepublic = itemView.findViewById(R.id.tv_name);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position, BluetoothLockFunctionBean bluetoothLockFunctionBean);
    }
}
