package com.philips.easykey.lock.activity.choosecountry;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * Created by ty on 2017/6/3.
 */
public class CountryActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    private List<CountrySortModel> mAllCountryList;
    private EditText mCountryEdtSearch;
    private ListView mCountryLvCountryList;
    private ImageView mCountryIvClearText;
    private CountrySortAdapter mAdapter;
    private SideBar mSideBar;
    private TextView dialog;
    private CountryComparator mPinyinComparator;
    private GetCountryNameSort mCountryChangeUtil;
    private CharacterParserUtil mCharacterParserUtil;
    public ImageView mIvHeadLeft;//结束
    public TextView mTvHeadTxt;
    private String[] mCountryList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_country_choose);
        mIvHeadLeft = findViewById(R.id.iv_head_left);
        mTvHeadTxt = findViewById(R.id.tv_head_txt);

        mIvHeadLeft.setOnClickListener(this);
        init();
        setListener();
        getCountryList();

    }


    /**
     * 初始化界面
     */
    private void init() {
        mCountryEdtSearch = findViewById(R.id.country_et_search);
        mCountryLvCountryList = findViewById(R.id.country_lv_list);
        mCountryIvClearText = findViewById(R.id.country_iv_cleartext);
        dialog = findViewById(R.id.country_dialog);
        mSideBar = findViewById(R.id.country_sidebar);
        mSideBar.setTextView(dialog);
        mAllCountryList = new ArrayList<>();
        mPinyinComparator = new CountryComparator();
        mCountryChangeUtil = new GetCountryNameSort();
        mCharacterParserUtil = new CharacterParserUtil();
        // 将联系人进行排序，按照A~Z的顺序
        Collections.sort(mAllCountryList, mPinyinComparator);
        mAdapter = new CountrySortAdapter(this, mAllCountryList);
        mCountryLvCountryList.setAdapter(mAdapter);
    }


    /****
     * 添加监听
     */
    private void setListener() {
        mCountryIvClearText.setOnClickListener(v -> {
            mCountryEdtSearch.setText("");
            Collections.sort(mAllCountryList, mPinyinComparator);
            mAdapter.updateListView(mAllCountryList);
        });
        mCountryEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void afterTextChanged(Editable s) {
                String searchContent = mCountryEdtSearch.getText().toString();
                if (searchContent.equals("")) {
                    mCountryIvClearText.setVisibility(View.INVISIBLE);
                } else {
                    mCountryIvClearText.setVisibility(View.VISIBLE);
                }
                if (searchContent.length() > 0) {
                    // 按照输入内容进行匹配
                    ArrayList<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) mCountryChangeUtil.search(searchContent, mAllCountryList);
                    mAdapter.updateListView(fileterList);
                } else {
                    mAdapter.updateListView(mAllCountryList);
                }
                mCountryLvCountryList.setSelection(0);
            }
        });
        // 右侧sideBar监听
        mSideBar.setOnTouchingLetterChangedListener(s -> {
            // 该字母首次出现的位置
            int position = mAdapter.getPositionForSection(s.charAt(0));
            if (position != -1) {
                mCountryLvCountryList.setSelection(position);
            }
        });
        mCountryLvCountryList.setOnItemClickListener((adapterView, view, position, arg3) -> {
            String countryName;
            String countryNumber;
            String searchContent = mCountryEdtSearch.getText().toString();
            if (searchContent.length() > 0) {
                // 按照输入内容进行匹配
                ArrayList<CountrySortModel> fileterList = (ArrayList<CountrySortModel>) mCountryChangeUtil.search(searchContent, mAllCountryList);
                countryName = fileterList.get(position).countryName;
                countryNumber = fileterList.get(position).countryNumber;
            } else {
                // 点击后返回
                countryName = mAllCountryList.get(position).countryName;
                countryNumber = mAllCountryList.get(position).countryNumber;
            }
            Intent intent = new Intent();
            intent.putExtra("countryName", countryName);
            intent.putExtra("countryNumber", countryNumber);
            setResult(RESULT_OK, intent);
            LogUtils.d("countryName: + " + countryName + "countryNumber: " + countryNumber);
            finish();
        });
    }


    /**
     * 获取国家列表
     */
    private void getCountryList() {
        boolean checklag = (Boolean) SPUtils.get("checklag", false);
        boolean isChina;
        if (checklag) {
            String language = (String) SPUtils.get("lag", "");
            // 设置应用语言类型
            if (language.equals("zh")) {
                mCountryList = getResources().getStringArray(R.array.country_code_list_ch);
            } else if (language.equals("tw")) {
                mCountryList = getResources().getStringArray(R.array.country_code_list_tw);
            } else {
                mCountryList = getResources().getStringArray(R.array.country_code_list_en);
            }
        } else {
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.equals("zh")) {
                mCountryList = getResources().getStringArray(R.array.country_code_list_ch);
            } else if (language.equals("tw")) {
                mCountryList = getResources().getStringArray(R.array.country_code_list_tw);
            } else {
                mCountryList = getResources().getStringArray(R.array.country_code_list_en);
            }
        }
        for (int i = 0, length = mCountryList.length; i < length; i++) {
            String[] country = mCountryList[i].split("\\*");
            String countryName = country[0];
            String countryNumber = country[1];
            String countrySortKey = mCharacterParserUtil.getSelling(countryName);
            CountrySortModel countrySortModel = new CountrySortModel(countryName, countryNumber, countrySortKey);
            String sortLetter = mCountryChangeUtil.getSortLetterBySortKey(countrySortKey);
            if (sortLetter == null) {
                sortLetter = mCountryChangeUtil.getSortLetterBySortKey(countryName);
            }
            countrySortModel.sortLetters = sortLetter;
            mAllCountryList.add(countrySortModel);
        }
        Collections.sort(mAllCountryList, mPinyinComparator);
        mAdapter.updateListView(mAllCountryList);
        LogUtils.d("length " + mAllCountryList.size());
    }


    public void checkLag() {
        boolean checklag = (Boolean) SPUtils.get("checklag", false);
        boolean isChina;
        if (checklag) {
            String language = (String) SPUtils.get("lag", "");
            // 设置应用语言类型
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            if (language.equals("zh")) {
                config.locale = Locale.SIMPLIFIED_CHINESE;
            } else if (language.equals("tw")) {
                config.locale = Locale.TAIWAN;
            } else {
                config.locale = Locale.ENGLISH;
            }
            resources.updateConfiguration(config, dm);
        } else {
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if (language.equals("zh")) {
                isChina = true;
                config.locale = Locale.SIMPLIFIED_CHINESE;
            } else if (language.equals("tw")) {
                config.locale = Locale.TAIWAN;
            } else {
                isChina = false;
                config.locale = Locale.ENGLISH;
            }
            resources.updateConfiguration(config, dm);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_left:
                finish();
                break;
        }
    }
}
