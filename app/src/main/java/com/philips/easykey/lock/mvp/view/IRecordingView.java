package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.utils.db.MediaItem;

import java.util.ArrayList;

/**
 * Create By denganzhi  on 2019/5/5
 * Describe
 */

public interface IRecordingView extends IBaseView {


    void  showFetchResult(ArrayList<MediaItem> mediaItemArrayList);


    void deleteResult(Boolean isFlag);


}
