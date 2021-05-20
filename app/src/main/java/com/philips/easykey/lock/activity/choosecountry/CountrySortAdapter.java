/*      						
 * Copyright 2010 Beijing XinWei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015年3月21日	| DuanBoKan 	| 	create the file
 */

package com.philips.easykey.lock.activity.choosecountry;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;


import com.philips.easykey.lock.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 *
 * 国家码选择
 *
 * <p>
 * 类详细描述
 * </p>
 *
 * @author duanbokan
 *
 */

public class CountrySortAdapter extends BaseAdapter implements SectionIndexer
{
	
	private List<CountrySortModel> mList;
	
	private final Context mContext;
	
	/***
	 * 初始化
	 */
	public CountrySortAdapter(Context mContext, List<CountrySortModel> list) {
		this.mContext = mContext;
		if (list == null) {
			this.mList = new ArrayList<>();
		} else {
			this.mList = list;
		}
	}
	
	@Override
	public int getCount()
	{
		return this.mList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder viewHolder;
		final CountrySortModel mContent = mList.get(position);
		
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.coogame_country_item, null);
			viewHolder.mCountrySortName = view.findViewById(R.id.country_catalog);
			viewHolder.mCountryName = view.findViewById(R.id.country_name);
			viewHolder.mCountryNumber = view.findViewById(R.id.country_number);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);
		
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.mCountrySortName.setVisibility(View.VISIBLE);
			viewHolder.mCountrySortName.setText(mContent.sortLetters);
		} else {
			viewHolder.mCountrySortName.setVisibility(View.GONE);
		}
		
		viewHolder.mCountryName.setText(this.mList.get(position).countryName);
		viewHolder.mCountryNumber.setText(this.mList.get(position).countryNumber);
		
		return view;
	}
	
	@Override
	public int getPositionForSection(int section) {
		if (section != 42) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = mList.get(i).sortLetters;
				char firstChar = sortStr.toUpperCase(Locale.CHINESE).charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
		} else {
			return 0;
		}
		return -1;
	}
	
	@Override
	public int getSectionForPosition(int position) {
		return mList.get(position).sortLetters.charAt(0);
	}
	
	@Override
	public Object[] getSections()
	{
		return null;
	}
	
	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 */
	public void updateListView(List<CountrySortModel> list) {
		if (list == null) {
			this.mList = new ArrayList<>();
		} else {
			this.mList = list;
		}
		notifyDataSetChanged();
	}
	
	public static class ViewHolder {
		// 国家码简拼所属的字母范围
		public TextView mCountrySortName;
		// 国家名
		public TextView mCountryName;
		// 代码
		public TextView mCountryNumber;
		
	}
	
}
