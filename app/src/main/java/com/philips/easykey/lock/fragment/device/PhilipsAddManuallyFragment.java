package com.philips.easykey.lock.fragment.device;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.PhilipsAddVideoLockActivity;
import com.philips.easykey.lock.adapter.PhilipsAddDeviceDoorLockAdapter;
import com.philips.easykey.lock.adapter.listener.PhilipsAddDeviceDoorLockFuzzMatchAdapter;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.bean.deviceAdd.PhilipsAddManuallyDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetProductionModelListResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.widget.SpacesItemDecoration;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * author : Jack
 * time   : 2021/4/26
 * E-mail : wengmaowei@kaadas.com
 * desc   : 手动添加设备
 */
public class PhilipsAddManuallyFragment extends Fragment {

    private RecyclerView mRvDoorLock;
    private RecyclerView mRvDoorLockNameList;
    private EditText mEtSearch;
    private TextView mTvSmartLockStr;
    private TextView mTvSmartLockStr3;
    private View mView;
    private View mView1;
    private Button mBtnSearch;
    private PhilipsAddDeviceDoorLockAdapter mPhilipsAddDeviceDoorLockAdapter;
    private PhilipsAddDeviceDoorLockFuzzMatchAdapter mPhilipsAddDeviceDoorLockFuzzMatchAdapter;
    private PhilipsAddManuallyDeviceBean mPhilipsAddManuallyDeviceBean ;
    private ArrayList<PhilipsAddManuallyDeviceBean> doorlockModelList = new ArrayList<>();;
    public static PhilipsAddManuallyFragment newInstance() {
        return new PhilipsAddManuallyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_manually, container, false);
        addDoorlockModelList();
        mRvDoorLock = root.findViewById(R.id.rvDoorLock);
        mRvDoorLockNameList = root.findViewById(R.id.rvDoorLockNameList);
        mEtSearch = root.findViewById(R.id.etSearch);
        mTvSmartLockStr = root.findViewById(R.id.tvSmartLockStr);
        mTvSmartLockStr3 = root.findViewById(R.id.tvSmartLockStr3);
        mView = root.findViewById(R.id.view);
        mView1 = root.findViewById(R.id.view1);
        mBtnSearch = root.findViewById(R.id.btnSearch);
        mBtnSearch.setOnClickListener(v -> searchDevice());

        mPhilipsAddDeviceDoorLockAdapter = new PhilipsAddDeviceDoorLockAdapter(R.layout.philips_item_add_door_lock_list_rv,doorlockModelList);
        mRvDoorLock.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRvDoorLock.addItemDecoration(new SpacesItemDecoration(0, 0, 0, SizeUtils.dp2px(18)));
        mRvDoorLock.setAdapter(mPhilipsAddDeviceDoorLockAdapter);
        mPhilipsAddDeviceDoorLockAdapter.setOnClickListener((v, data) -> {
            Intent wifiIntent = new Intent(getContext(), PhilipsAddVideoLockActivity.class);
            String wifiModelType = "WiFi&VIDEO";
            wifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(wifiIntent);
            getActivity().finish();

        });

        mPhilipsAddDeviceDoorLockFuzzMatchAdapter = new PhilipsAddDeviceDoorLockFuzzMatchAdapter(R.layout.philips_item_add_door_lock_fuzzmatch_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        mRvDoorLockNameList.setLayoutManager(linearLayoutManager);
        //设置recycleView item之间的分割线
        mRvDoorLockNameList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity()).color(Color.parseColor("#EDEDED")).size(3).build());
        mRvDoorLockNameList.setAdapter(mPhilipsAddDeviceDoorLockFuzzMatchAdapter);
        mPhilipsAddDeviceDoorLockFuzzMatchAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent wifiIntent = new Intent(getContext(), PhilipsAddVideoLockActivity.class);
                String wifiModelType = "WiFi&VIDEO";
                wifiIntent.putExtra("wifiModelType", wifiModelType);
                startActivity(wifiIntent);
                getActivity().finish();
            }
        });
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(s)){
                    ArrayList<PhilipsAddManuallyDeviceBean> listPhilipsAddManuallyDeviceBean = searchItems(s + "");
                    if(listPhilipsAddManuallyDeviceBean.size() > 0){
                        mPhilipsAddDeviceDoorLockFuzzMatchAdapter.setList(listPhilipsAddManuallyDeviceBean);
                        mRvDoorLock.setVisibility(View.GONE);
                        mTvSmartLockStr.setVisibility(View.GONE);
                        mTvSmartLockStr3.setVisibility(View.GONE);
                        mView.setVisibility(View.GONE);
                        mView1.setVisibility(View.GONE);
                        mRvDoorLockNameList.setVisibility(View.VISIBLE);
                    }else {
                        mRvDoorLock.setVisibility(View.VISIBLE);
                        mTvSmartLockStr.setVisibility(View.VISIBLE);
                        mTvSmartLockStr3.setVisibility(View.VISIBLE);
                        mView.setVisibility(View.VISIBLE);
                        mView1.setVisibility(View.VISIBLE);
                        mRvDoorLockNameList.setVisibility(View.GONE);
                    }
                }
                else {
                    mRvDoorLock.setVisibility(View.VISIBLE);
                    mRvDoorLock.setVisibility(View.VISIBLE);
                    mTvSmartLockStr.setVisibility(View.VISIBLE);
                    mTvSmartLockStr3.setVisibility(View.VISIBLE);
                    mView.setVisibility(View.VISIBLE);
                    mView1.setVisibility(View.VISIBLE);
                    mRvDoorLockNameList.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return root;
    }

    public ArrayList<PhilipsAddManuallyDeviceBean> searchItems(String name) {
        ArrayList<PhilipsAddManuallyDeviceBean> mSearchList = new ArrayList<>();
        for (int i = 0; i < doorlockModelList.size(); i++) {
            int index = doorlockModelList.get(i).getName().toUpperCase().indexOf(name.toUpperCase());
            // 存在匹配的数据
            if (index != -1) {
                mSearchList.add(doorlockModelList.get(i));
            }
        }
        return mSearchList;
    }

    private void searchDevice(){
        String name = mEtSearch.getText().toString().trim();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showShort(getResources().getString(R.string.philips_please_input_the_correct_lock_model));
        }else {
            boolean isSearchSuccess = false;
            for (int i = 0; i < doorlockModelList.size(); i++) {
                if(TextUtils.equals(doorlockModelList.get(i).getName(),name)){
                    isSearchSuccess = true;
                }
            }
            if(!isSearchSuccess)ToastUtils.showShort(getResources().getString(R.string.philips_the_model_is_not_matched));
        }
    }

    private void addDoorlockModelList(){
        doorlockModelList.clear();
        mPhilipsAddManuallyDeviceBean = new PhilipsAddManuallyDeviceBean();
        mPhilipsAddManuallyDeviceBean.setName(getResources().getString(R.string.philips_ddl708v_5hw));
        mPhilipsAddManuallyDeviceBean.setId(R.drawable.philips_home_scan_img_lock);
        doorlockModelList.add(mPhilipsAddManuallyDeviceBean);

        mPhilipsAddManuallyDeviceBean = new PhilipsAddManuallyDeviceBean();
        mPhilipsAddManuallyDeviceBean.setName(getResources().getString(R.string.philips_ddl708vp_5hw));
        mPhilipsAddManuallyDeviceBean.setId(R.drawable.philips_home_scan_img_lock);
        doorlockModelList.add(mPhilipsAddManuallyDeviceBean);

        mPhilipsAddManuallyDeviceBean = new PhilipsAddManuallyDeviceBean();
        mPhilipsAddManuallyDeviceBean.setName(getResources().getString(R.string.philips_ddl708_5hw));
        mPhilipsAddManuallyDeviceBean.setId(R.drawable.philips_home_scan_img_lock);
        doorlockModelList.add(mPhilipsAddManuallyDeviceBean);

        mPhilipsAddManuallyDeviceBean = new PhilipsAddManuallyDeviceBean();
        mPhilipsAddManuallyDeviceBean.setName(getResources().getString(R.string.philips_ddl708v_8hw));
        mPhilipsAddManuallyDeviceBean.setId(R.drawable.philips_home_scan_img_lock);
        doorlockModelList.add(mPhilipsAddManuallyDeviceBean);

        mPhilipsAddManuallyDeviceBean = new PhilipsAddManuallyDeviceBean();
        mPhilipsAddManuallyDeviceBean.setName(getResources().getString(R.string.philips_ddl708vp_8hw));
        mPhilipsAddManuallyDeviceBean.setId(R.drawable.philips_home_scan_img_lock);
        doorlockModelList.add(mPhilipsAddManuallyDeviceBean);

        mPhilipsAddManuallyDeviceBean = new PhilipsAddManuallyDeviceBean();
        mPhilipsAddManuallyDeviceBean.setName(getResources().getString(R.string.philips_ddl708_8hw));
        mPhilipsAddManuallyDeviceBean.setId(R.drawable.philips_home_scan_img_lock);
        doorlockModelList.add(mPhilipsAddManuallyDeviceBean);
    }

    private void getProductionModelList(){
        XiaokaiNewServiceImp.getProductionModelList()
                .subscribe(new BaseObserver<GetProductionModelListResult>() {
                    @Override
                    public void onSuccess(GetProductionModelListResult getProductionModelListResult) {
                        LogUtils.d("getProductionModelList onSuccess " + getProductionModelListResult.toString());
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        LogUtils.d("getProductionModelList onAckErrorCode " + baseResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        LogUtils.d("getProductionModelList onFailed " + throwable.toString());
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

}
