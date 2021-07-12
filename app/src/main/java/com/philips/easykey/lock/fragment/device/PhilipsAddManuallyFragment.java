package com.philips.easykey.lock.fragment.device;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.PhilipsAddVideoLockActivity;
import com.philips.easykey.lock.adapter.PhilipsAddDeviceDoorLockAdapter;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetProductionModelListResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.disposables.Disposable;

/**
 * author : Jack
 * time   : 2021/4/26
 * E-mail : wengmaowei@kaadas.com
 * desc   : 手动添加设备
 */
public class PhilipsAddManuallyFragment extends Fragment {

    private RecyclerView rvDoorLock;
    private PhilipsAddDeviceDoorLockAdapter philipsAddDeviceDoorLockAdapter;
    private HashMap<String,Object> hashMap;
    private final ArrayList<HashMap<String,Object>> doorlockModelList = new ArrayList<>();
    public static PhilipsAddManuallyFragment newInstance() {
        return new PhilipsAddManuallyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_manually, container, false);
        addDoorlockModelList();
        rvDoorLock = root.findViewById(R.id.rvDoorLock);
        philipsAddDeviceDoorLockAdapter = new PhilipsAddDeviceDoorLockAdapter(R.layout.philips_item_add_door_lock_list_rv,doorlockModelList);
        rvDoorLock.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvDoorLock.addItemDecoration(new SpacesItemDecoration(0, 0, 0, SizeUtils.dp2px(18)));
        rvDoorLock.setAdapter(philipsAddDeviceDoorLockAdapter);
        philipsAddDeviceDoorLockAdapter.setOnClickListener((v, data) -> {
            Intent wifiIntent = new Intent(getContext(), PhilipsAddVideoLockActivity.class);
            String wifiModelType = "WiFi&VIDEO";
            wifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(wifiIntent);
        });
        return root;
    }

    private void addDoorlockModelList(){
        doorlockModelList.clear();
        hashMap = new HashMap<>();
        hashMap.put("DoorlockName", getResources().getString(R.string.philips_ddl708v_5hw));
        hashMap.put("DoorlockImage", getResources().getDrawable(R.drawable.philips_home_scan_img_lock));
        doorlockModelList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("DoorlockName", getResources().getString(R.string.philips_ddl708vp_5hw));
        hashMap.put("DoorlockImage", getResources().getDrawable(R.drawable.philips_home_scan_img_lock));
        doorlockModelList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("DoorlockName", getResources().getString(R.string.philips_ddl708_5hw));
        hashMap.put("DoorlockImage", getResources().getDrawable(R.drawable.philips_home_scan_img_lock));
        doorlockModelList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("DoorlockName", getResources().getString(R.string.philips_ddl708v_8hw));
        hashMap.put("DoorlockImage", getResources().getDrawable(R.drawable.philips_home_scan_img_lock));
        doorlockModelList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("DoorlockName", getResources().getString(R.string.philips_ddl708vp_8hw));
        hashMap.put("DoorlockImage", getResources().getDrawable(R.drawable.philips_home_scan_img_lock));
        doorlockModelList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("DoorlockName", getResources().getString(R.string.philips_ddl708_8hw));
        hashMap.put("DoorlockImage", getResources().getDrawable(R.drawable.philips_home_scan_img_lock));
        doorlockModelList.add(hashMap);
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
