package com.philips.easykey.lock.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.MessageFragmentPresenter;
import com.philips.easykey.lock.mvp.view.IMessageView;
import com.philips.easykey.lock.widget.BadgeNumberView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PhilipsMessageFragment extends BaseFragment<IMessageView, MessageFragmentPresenter<IMessageView>> implements IMessageView {

    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.tv_door_lock_msg)
    TextView tvDoorLockMsg;
    @BindView(R.id.v_door_lock_msg)
    View vDoorLockMsg;
    @BindView(R.id.tv_system_msg)
    TextView tvSystemMsg;
    @BindView(R.id.v_system_msg)
    View vSystemMsg;
    @BindView(R.id.vp_message)
    ViewPager vpMessage;
    @BindView(R.id.rl_door_lock_msg)
    RelativeLayout rlDoorLockMsg;
    @BindView(R.id.badge_number_view)
    BadgeNumberView badgeNumberView;
    @BindView(R.id.rl_system_msg)
    RelativeLayout rlSystemMsg;
    private View mView;
    private Unbinder unbinder;
    private FragmentPagerAdapter adapter;
    private List<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.philips_fragment_message, container, false);
        }
        unbinder = ButterKnife.bind(this, mView);
        initView();
        initFragment();
        return mView;
    }

    @Override
    protected MessageFragmentPresenter<IMessageView> createPresent() {
        return new MessageFragmentPresenter<>();
    }

    private void initView() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(titleBar.getLayoutParams());
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        titleBar.setLayoutParams(lp);
        badgeNumberView.setText(32);
        setTvDoorLockMsgAndTvSystemMsgColor(true);
        vpMessage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setTvDoorLockMsgAndTvSystemMsgColor(true);
                } else if (position == 1) {
                    setTvDoorLockMsgAndTvSystemMsgColor(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initFragment() {
        PhilipsDoorLockMessageFragment doorLockMsgFragment = new PhilipsDoorLockMessageFragment();
        PhilipsSystemMessageFragment systemMessageFragment = new PhilipsSystemMessageFragment();
        fragments.add(doorLockMsgFragment);
        fragments.add(systemMessageFragment);
        if (adapter == null) {
            adapter = new FragmentPagerAdapter(getChildFragmentManager()) {
                @Override
                public Fragment getItem(int i) {
                    return fragments.get(i);
                }

                @Override
                public long getItemId(int position) {
                    int hashCode = fragments.get(position).hashCode();
                    return hashCode;
                }

                @Override
                public int getItemPosition(@NonNull Object object) {
                    return POSITION_NONE;
                }

                @Override
                public int getCount() {
                    return fragments.size();
                }
            };
            vpMessage.setAdapter(adapter);
        }
    }

    /**
     * @param clickedTvDoorLockMsg 表示是否选中门锁消息
     */
    private void setTvDoorLockMsgAndTvSystemMsgColor(boolean clickedTvDoorLockMsg) {
        if (clickedTvDoorLockMsg) {
            tvDoorLockMsg.setEnabled(true);
            tvSystemMsg.setEnabled(false);
            vDoorLockMsg.setVisibility(View.VISIBLE);
            vSystemMsg.setVisibility(View.GONE);
        } else {
            tvDoorLockMsg.setEnabled(false);
            tvSystemMsg.setEnabled(true);
            vDoorLockMsg.setVisibility(View.GONE);
            vSystemMsg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_door_lock_msg, R.id.rl_system_msg})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_door_lock_msg:
                setTvDoorLockMsgAndTvSystemMsgColor(true);
                if (fragments.size() > 0) {
                    vpMessage.setCurrentItem(0);
                }
                break;
            case R.id.rl_system_msg:
                setTvDoorLockMsgAndTvSystemMsgColor(false);
                if (fragments.size() > 1) {
                    vpMessage.setCurrentItem(1);
                }
                break;
        }
    }
}
