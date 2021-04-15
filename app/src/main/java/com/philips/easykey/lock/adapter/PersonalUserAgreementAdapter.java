package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PersonalUserAgreementBean;


import java.util.List;

public class PersonalUserAgreementAdapter extends BaseQuickAdapter<PersonalUserAgreementBean, BaseViewHolder> {
    public PersonalUserAgreementAdapter(int layoutResId, @Nullable List<PersonalUserAgreementBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PersonalUserAgreementBean item) {
        //用户协议标题
        TextView title = helper.getView(R.id.agreement_title);
        if (!item.getFlag()) {
            title.setVisibility(View.VISIBLE);
            title.setText(item.getTitle());
        } else {
            title.setVisibility(View.GONE);
        }

        //用户协议内容
        TextView content = helper.getView(R.id.agreement_content);
        content.setText(item.getContent());

    }


}
