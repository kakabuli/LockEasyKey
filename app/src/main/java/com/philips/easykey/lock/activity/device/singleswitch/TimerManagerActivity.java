package com.philips.easykey.lock.activity.device.singleswitch;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.SingleSwitchTimerAdapter;
import com.philips.easykey.lock.bean.SingleSwitchTimerShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimerManagerActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.iv_ad_timer)
    ImageView ivAdTimer;
    @BindView(R.id.button_next)
    Button buttonNext;
    @BindView(R.id.rv_timer_list)
    SwipeMenuRecyclerView rvTimerList;
    @BindView(R.id.ll_no_data)
    LinearLayout llNoData;
    private List<SingleSwitchTimerShowBean> singleSwitchTimerShowBeans = new ArrayList<>();
    private SingleSwitchTimerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_manager);
        ButterKnife.bind(this);
        for (int i = 0; i < 10; i++) {
            singleSwitchTimerShowBeans.add(new SingleSwitchTimerShowBean("16:00 - 20:00", getString(R.string.philips_activity_timer_manager_1),
                    getString(R.string.philips_activity_timer_manager_2), i % 2 == 0));
        }

        if (singleSwitchTimerShowBeans!=null&& singleSwitchTimerShowBeans.size()>0) {
            llNoData.setVisibility(View.GONE);
            rvTimerList.setVisibility(View.VISIBLE);
        }else {
            llNoData.setVisibility(View.VISIBLE);
            rvTimerList.setVisibility(View.GONE);
        }
        initRecycleView();
    }


    public void initRecycleView(){
        adapter = new SingleSwitchTimerAdapter(singleSwitchTimerShowBeans);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                SingleSwitchTimerShowBean switchTimerShowBean = (SingleSwitchTimerShowBean) adapter.getData().get(position);
            }
        });
        rvTimerList.setSwipeMenuCreator(swipeMenuCreator);
        rvTimerList.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                int adapterPosition = menuBridge.getAdapterPosition();
                menuBridge.closeMenu();
                ToastUtils.showShort(getString(R.string.philips_activity_timer_manager_3,adapterPosition+""));
            }
        });
        rvTimerList.setLayoutManager(new LinearLayoutManager(this));
        rvTimerList.setAdapter(adapter);

    }
    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

                 SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext())
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText(getResources().getString(R.string.delete))
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

        }
    };

}
