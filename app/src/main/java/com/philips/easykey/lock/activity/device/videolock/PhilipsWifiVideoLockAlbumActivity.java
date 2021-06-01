package com.philips.easykey.lock.activity.device.videolock;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.MyAlbumAdapter;
import com.philips.easykey.lock.adapter.MyAlbumItemAdapter;
import com.philips.easykey.lock.bean.FileBean;
import com.philips.easykey.lock.bean.FileItemBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import com.philips.easykey.core.tool.FileTool;

public class PhilipsWifiVideoLockAlbumActivity extends BaseAddToApplicationActivity {

    ImageView back;
    RecyclerView recycleview;
    ImageView ivMyAlbumDelete;

    private MyAlbumAdapter adapter;

    private List<FileBean> items = new ArrayList<>();

    private String wifiSn = "";

    private boolean showDeleteItem = false;

    private final int DELETE_MYALBUM_PIC_VIDEO = 10100;

    private Dialog deleteSelectFileItemDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_video_album);

        back = findViewById(R.id.back);
        recycleview = findViewById(R.id.recycleview);
        ivMyAlbumDelete = findViewById(R.id.iv_myalbum_delete);

        back.setOnClickListener(v -> {
            if(showDeleteItem){
                revoke();
            }else{
                finish();
            }
        });
        ivMyAlbumDelete.setOnClickListener(v -> {
            if(showDeleteItem){
                if(isSelectFileItem()){
                    showDeleteSelectFileItemDialog();
                }else{
                    revoke();
                    ToastUtils.showShort(getString(R.string.wifi_video_lock_delete_show_toast) + "");
                }
            }else {
                showDeleteItem = true;
                showDeleteItem(true);
            }
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        String filePath = FileTool.getVideoLockPath(this,wifiSn).getPath();
        LogUtils.d("shulan  filePath-->" + filePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchFiles(filePath);

            }
        }).start();

        initRecycleView();
    }

    private void initRecycleView() {
        adapter = new MyAlbumAdapter(items, PhilipsWifiVideoLockAlbumActivity.this);
        recycleview.setLayoutManager(new LinearLayoutManager(PhilipsWifiVideoLockAlbumActivity.this));
        recycleview.setAdapter(adapter);
        adapter.setOnMyAlbumItemClickListener(new MyAlbumAdapter.onMyAlbumItemClickListener() {
            @Override
            public void onMyAlbumItemClick(MyAlbumItemAdapter adapter, List<FileItemBean> data, int position) {
                if(showDeleteItem){
                    if(data.get(position).isSelect()){
                        data.get(position).setSelect(false);
                    }else{
                        data.get(position).setSelect(true);
                    }
                    adapter.notifyItemChanged(position);
                    return;
                }
                if(data.get(position).getType() == 1){
                    Intent intent = new Intent(PhilipsWifiVideoLockAlbumActivity.this, PhilipsWifiVideoLockPreViewActivity.class);
                    intent.putExtra(KeyConstants.VIDEO_PIC_PATH,data.get(position).getPath());
                    String filename = data.get(position).getName();
                    filename = StringUtil.getFileNameNoEx(filename);
                    try {
                        filename = DateUtils.getStrFromMillisecond2(Long.parseLong(filename));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    intent.putExtra("NAME",filename);
                    startActivityForResult(intent,DELETE_MYALBUM_PIC_VIDEO);
                }else if(data.get(position).getType() == 2){
                    Intent intent = new Intent(PhilipsWifiVideoLockAlbumActivity.this, PhilipsWifiVideoLockAlbumDetailActivity.class);
                    String filename = data.get(position).getName();
                    filename = StringUtil.getFileNameNoEx(filename);
                    try{
                        filename = DateUtils.getStrFromMillisecond2(Long.parseLong(filename));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    intent.putExtra("NAME",filename);
                    intent.putExtra(KeyConstants.VIDEO_PIC_PATH,data.get(position).getPath());
                    startActivityForResult(intent,DELETE_MYALBUM_PIC_VIDEO);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == DELETE_MYALBUM_PIC_VIDEO){
                String stringExtra = data.getStringExtra(KeyConstants.VIDEO_PIC_PATH);
                if(stringExtra.isEmpty()){
                    return;
                }
                String name = "";
                try{
                    name = data.getStringExtra("NAME");
                    name = name.split(" ")[0];
                    name = name.replace("-","/");
                }catch (Exception e){

                }
                if(!name.isEmpty()){
                    deleteFileItem(stringExtra, name);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void deleteFileItem(String stringExtra, String name) {
        try{
            Iterator<FileBean> it = items.iterator();
            boolean flag = false;
            while (it.hasNext()){
                FileBean list = (FileBean)it.next();
                list.setFirst(false);
                if(list.getDate().equals(name)){
                    flag = true;
                    Iterator<FileItemBean> oa = list.getItem().iterator();
                    while (oa.hasNext()){
                        FileItemBean next = oa.next();
                        if(next.getPath().equals(stringExtra)){
                            if(list.getItem().size() == 1){
                                it.remove();
                            }else{
                                oa.remove();
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            if(!flag){
                int[] position = new int[3];
                for(int i = 0 ; i<items.size();i++){
                    items.get(i).setFirst(false);
                    for(int j =0 ; j<items.get(i).getItem().size();j++){
                        if(stringExtra.equals(items.get(i).getItem().get(j).getPath())){
                            position[0] = i;
                            position[1] = j;
                            if(items.get(i).getItem().size() == 1){
                                position[2] = 1;
                            }
                            break;
                        }
                    }
                }
                if(position[2] == 1){
                    items.remove(position[0]);
                }else{
                    items.get(position[0]).getItem().remove(position[1]);
                }
            }
        }catch (Exception e){
        }
    }

    private void showDeleteSelectFileItemDialog() {
        if(deleteSelectFileItemDialog == null){
            deleteSelectFileItemDialog = new Dialog(this, R.style.MyDialog);
        }
        // 获取Dialog布局
        View mView = LayoutInflater.from(this).inflate(R.layout.philips_dialog_bottom_two_button, null);
        Button delete = mView.findViewById(R.id.tv_top);
        Button cancel = mView.findViewById(R.id.tv_bottom);
        deleteSelectFileItemDialog.setContentView(mView);

        Window window = deleteSelectFileItemDialog.getWindow();
        window.setWindowAnimations(R.style.Animation_Bottom_Rising);
        window.setGravity(Gravity.BOTTOM);
        window.setDimAmount(0f);

        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        params.width = (int) width;
        window.setAttributes(params);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                revoke();
                deleteSelectFileItemDialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectFileItem();
                revoke();
                deleteSelectFileItemDialog.dismiss();
            }
        });
        if(!PhilipsWifiVideoLockAlbumActivity.this.isFinishing()){
            deleteSelectFileItemDialog.show();
        }

    }

    private boolean isSelectFileItem(){
        try {
            Iterator<FileBean> it = items.iterator();
            while (it.hasNext()){
                FileBean list = it.next();
                int size = list.getItem().size();
                Iterator<FileItemBean> oa = list.getItem().iterator();
                while (oa.hasNext()){
                    FileItemBean next = oa.next();
                    if(next.isSelect()){
                        return true;
                    }
                    continue;
                }
            }
        }catch (Exception e){

        }
        return false;
    }

    private void revoke() {
        showDeleteItem(false);
        showDeleteItem = false;
    }

    private void deleteSelectFileItem() {
        try {
            Iterator<FileBean> it = items.iterator();
            while (it.hasNext()){
                FileBean list = (FileBean)it.next();
                list.setFirst(false);
                int size = list.getItem().size();
                Iterator<FileItemBean> oa = list.getItem().iterator();
                while (oa.hasNext()){
                    FileItemBean next = oa.next();
                    if(next.isSelect()){
                        File file = new File(next.getPath());
                        if(file.exists()){
                            file.delete();
                        }
                        oa.remove();
                        size--;
                    }
                    continue;
                }
                if(size == 0){
                    it.remove();
                }
                continue;
            }
        }catch (Exception e){

        }
        adapter.notifyDataSetChanged();
    }

    private void showDeleteItem(boolean b) {
        for(FileBean item :items){
            item.setFirst(false);
            for(FileItemBean list : item.getItem()){
                list.setShowDelete(b);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void searchFiles(String path) {
        if(path.isEmpty()){
            return;
        }
        File file = new File(path);
        if(!file.exists()){
            return;
        }
        if(file.isFile()){
            return;
        }
        File[] list = file.listFiles();

        List<FileItemBean> files = new ArrayList<>();
        String str = "";
        for(int i = 0 ; i < list.length;i++){
            if(list[i].isFile() && !list[i].isHidden()){
                str = list[i].getName().substring(list[i].getName().length() - 3 ,list[i].getName().length());
                if(str.equals("mp4")){
                    files.add(new FileItemBean(list[i].getName(),list[i].getPath(),list[i].toURI(),
                            list[i].lastModified(),"mp4",2));
                }else if(str.equals("jpg")){
                    files.add(new FileItemBean(list[i].getName(),list[i].getPath(),list[i].toURI(),
                            list[i].lastModified(),"jpg",1));
                }else if(str.equals("png")){
                    files.add(new FileItemBean(list[i].getName(),list[i].getPath(),list[i].toURI(),
                            list[i].lastModified(),"png",1));
                }
            }

        }

        Collections.sort(files, new Comparator<FileItemBean>() {
            @Override
            public int compare(FileItemBean o1, FileItemBean o2) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
                Date date1 = new Date(o1.getLastModified());
                Date date2 = new Date(o2.getLastModified());
                if(date1.before(date2)){
                    return 1;
                }else{
                    return -1;
                }
            }
        });

        for(FileItemBean item : files){
            LogUtils.d(item.toString());
        }

        int times = 0;
        String str1 = "";
        String str2 = "";
        int temp = 0;
        for(int j = 0 ; j <files.size() ; j++){
            str1 = DateUtils.formatYearMonthDay(files.get(j).getLastModified());
            if(j+1 < files.size()){
                str2 = DateUtils.formatYearMonthDay(files.get(j+1).getLastModified());
            }else{
                str2 = "";
            }

            if(!str1.equals(str2)){
                items.add(new FileBean(str1,files.subList(temp,j+1)));
                temp = j + 1;
                times++;
            }
        }

        for(FileBean i : items){
            LogUtils.d(i.toString());
        }
    }
}
