package com.philips.easykey.lock.popup;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.PhilipsWifiListAdapter;

import java.util.List;

import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;
import razerdp.util.animation.TranslationConfig;

/**
 * author : Jack
 * time   : 2021/5/7
 * E-mail : wengmaowei@kaadas.com
 * desc   : wifi列表弹窗
 */
public class PhilipsWifiListPopup extends BasePopupWindow {

    private PhilipsWifiListAdapter mWifiSnListAdapter;

    public PhilipsWifiListPopup(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        View rootView = createPopupById(R.layout.philips_popup_wifi_list);
        RecyclerView rvWifiSnList = rootView.findViewById(R.id.rvWifiSnList);
        mWifiSnListAdapter = new PhilipsWifiListAdapter(R.layout.philips_item_wifi_list_rv);
        rvWifiSnList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvWifiSnList.setAdapter(mWifiSnListAdapter);
        return rootView;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        if(mWifiSnListAdapter != null) {
            mWifiSnListAdapter.setOnItemClickListener(onItemClickListener);
        }
    }

    public void updateWifiList(List<String> wifiList) {
        mWifiSnListAdapter.setList(wifiList);
    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withTranslation(TranslationConfig.FROM_TOP)
                .toShow();
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.BOTTOM_TO_TOP)
                .toDismiss();
    }
}
