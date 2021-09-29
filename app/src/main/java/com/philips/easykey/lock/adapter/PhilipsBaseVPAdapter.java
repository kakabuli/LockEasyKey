package com.philips.easykey.lock.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * author :
 * time   : 2021/4/22
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public abstract class PhilipsBaseVPAdapter<T> extends PagerAdapter {

    private final List<T> mDataList;
    private final SparseArray<View> mViewSparseArray;
    private final LayoutInflater layoutInflater;
    private final int mConvertId;


    public PhilipsBaseVPAdapter(Context context, int convertId, List<T> dataList) {
        this.mDataList = dataList;
        this.mConvertId = convertId;
        layoutInflater = LayoutInflater.from(context);
        mViewSparseArray = new SparseArray<>(dataList.size());
    }

    @Override
    public int getCount() {
        if (mDataList == null) return 0;
        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        View view = getView(container, position);
        container.addView(view);
        bindView(view, mDataList.get(position),position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView(getView(container, position));
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    //将布局添加到mViewSparseArray
    public View getView(ViewGroup container, int position){
        View view = mViewSparseArray.get(position);
        if (view == null){
            view = layoutInflater.inflate(mConvertId, container, false);
            mViewSparseArray.put(position, view);
        }
        return view;
    }

    public abstract void bindView(View view, T data,int position);

}
