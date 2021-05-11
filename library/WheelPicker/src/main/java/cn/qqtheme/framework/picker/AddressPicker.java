package cn.qqtheme.framework.picker;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.util.LogUtils;
import cn.qqtheme.framework.widget.WheelView;

/**
 * 地址选择器（包括省级、地级、县级），地址数据见demo项目assets目录下。
 * “assets/city.json”转换自国家统计局（http://www.stats.gov.cn/tjsj/tjbz/xzqhdm）
 *
 * @author 李玉江[QQ:1032694760]
 * @see Province
 * @see City
 * @see County
 * @since 2015/12/15, 2016/12/18
 */
public class AddressPicker extends LinkagePicker<Province, City, County> {
    private OnAddressPickListener mOnAddressPickListener;
    private OnWheelListener mOnWheelListener;
    //只显示地市及区县
    private boolean mHideProvince = false;
    //只显示省份及地市
    private boolean mHideCounty = false;
    //省市区数据
    private final ArrayList<Province> mProvinces;

    public AddressPicker(Activity activity, ArrayList<Province> provinces) {
        super(activity, new AddressProvider(provinces));
        this.mProvinces = provinces;
    }

    /**
     * 设置默认选中的省市县
     */
    public void setSelectedItem(Province province, City city, County county) {
        super.setSelectedItem(province, city, county);
    }

    public void setSelectedItem(String province, String city, String county) {
        setSelectedItem(new Province(province), new City(city), new County(county));
    }

    public Province getSelectedProvince() {
        return mProvinces.get(selectedFirstIndex);
    }

    public City getSelectedCity() {
        List<City> cities = getSelectedProvince().getCities();
        if (cities.size() == 0) {
            return null;//可能没有第二级数据
        }
        return cities.get(selectedSecondIndex);
    }

    public County getSelectedCounty() {
        City selectedCity = getSelectedCity();
        if (selectedCity == null) {
            return null;
        }
        List<County> counties = selectedCity.getCounties();
        if (counties.size() == 0) {
            return null;//可能没有第三级数据
        }
        return counties.get(selectedThirdIndex);
    }

    /**
     * 隐藏省级行政区，只显示地市级和区县级。
     * 设置为true的话，地址数据中只需要某个省份的即可
     * 参见demo中的“assets/city2.json”
     */
    public void setHideProvince(boolean hideProvince) {
        this.mHideProvince = hideProvince;
    }

    /**
     * 隐藏县级行政区，只显示省级和市级。
     * 设置为true的话，hideProvince将强制为false
     * 数据源依然使用demo中的“assets/city.json” 仅在逻辑上隐藏县级选择框，实际项目中应该去掉县级数据。
     */
    public void setHideCounty(boolean hideCounty) {
        this.mHideCounty = hideCounty;
    }

    /**
     * 设置滑动监听器
     */
    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.mOnWheelListener = onWheelListener;
    }

    public void setOnAddressPickListener(OnAddressPickListener listener) {
        this.mOnAddressPickListener = listener;
    }

    @Override
    protected View makeCenterView() {
        if (null == provider) {
            throw new IllegalArgumentException("please set address provider before make view");
        }
        float provinceWeight = firstColumnWeight;
        float cityWeight = secondColumnWeight;
        float countyWeight = thirdColumnWeight;
        if (mHideCounty) {
            mHideProvince = false;
        }
        if (mHideProvince) {
            provinceWeight = 0;
            cityWeight = firstColumnWeight;
            countyWeight = secondColumnWeight;
        }
        dividerConfig.setRatio(WheelView.DividerConfig.FILL);

        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        final WheelView provinceView = createWheelView();
        provinceView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, provinceWeight));
        layout.addView(provinceView);
        if (mHideProvince) {
            provinceView.setVisibility(View.GONE);
        }

        final WheelView cityView = createWheelView();
        cityView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, cityWeight));
        layout.addView(cityView);

        final WheelView countyView = createWheelView();
        countyView.setLayoutParams(new LinearLayout.LayoutParams(0, WRAP_CONTENT, countyWeight));
        layout.addView(countyView);
        if (mHideCounty) {
            countyView.setVisibility(View.GONE);
        }

        provinceView.setItems(provider.initFirstData(), selectedFirstIndex);
        provinceView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedFirstIndex = index;
                selectedFirstItem = getSelectedProvince();
                if (mOnWheelListener != null) {
                    mOnWheelListener.onProvinceWheeled(selectedFirstIndex, selectedFirstItem);
                }
                LogUtils.verbose(this, "change cities after province wheeled: index=" + index);
                selectedSecondIndex = 0;//重置地级索引
                selectedThirdIndex = 0;//重置县级索引
                //根据省份获取地市
                //noinspection unchecked
                List<City> cities = provider.linkageSecondData(selectedFirstIndex);
                if (cities.size() > 0) {
                    selectedSecondItem = cities.get(selectedSecondIndex);
                    cityView.setItems(cities, selectedSecondIndex);
                } else {
                    selectedSecondItem = null;
                    cityView.setItems(new ArrayList<String>());
                }
                //根据地市获取区县
                //noinspection unchecked
                List<County> counties = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
                if (counties.size() > 0) {
                    selectedThirdItem = counties.get(selectedThirdIndex);
                    countyView.setItems(counties, selectedThirdIndex);
                } else {
                    selectedThirdItem = null;
                    countyView.setItems(new ArrayList<String>());
                }
            }
        });

        cityView.setItems(provider.linkageSecondData(selectedFirstIndex), selectedSecondIndex);
        cityView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedSecondIndex = index;
                selectedSecondItem = getSelectedCity();
                if (mOnWheelListener != null) {
                    mOnWheelListener.onCityWheeled(selectedSecondIndex, selectedSecondItem);
                }
                LogUtils.verbose(this, "change counties after city wheeled: index=" + index);
                selectedThirdIndex = 0;//重置县级索引
                //根据地市获取区县
                //noinspection unchecked
                List<County> counties = provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex);
                if (counties.size() > 0) {
                    selectedThirdItem = counties.get(selectedThirdIndex);
                    //若不是用户手动滚动，说明联动需要指定默认项
                    countyView.setItems(counties, selectedThirdIndex);
                } else {
                    selectedThirdItem = null;
                    countyView.setItems(new ArrayList<String>());
                }
            }
        });

        countyView.setItems(provider.linkageThirdData(selectedFirstIndex, selectedSecondIndex), selectedThirdIndex);
        countyView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedThirdIndex = index;
                selectedThirdItem = getSelectedCounty();
                if (mOnWheelListener != null) {
                    mOnWheelListener.onCountyWheeled(selectedThirdIndex, selectedThirdItem);
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (mOnAddressPickListener != null) {
            Province province = getSelectedProvince();
            City city = getSelectedCity();
            County county = null;
            if (!mHideCounty) {
                county = getSelectedCounty();
            }
            mOnAddressPickListener.onAddressPicked(province, city, county);
        }
    }

    /**
     * 地址选择回调
     */
    public interface OnAddressPickListener {

        void onAddressPicked(Province province, City city, County county);

    }

    /**
     * 滑动回调
     */
    public interface OnWheelListener {

        void onProvinceWheeled(int index, Province province);

        void onCityWheeled(int index, City city);

        void onCountyWheeled(int index, County county);

    }

    /**
     * 地址提供者
     */
    private static class AddressProvider implements Provider<Province, City, County> {
        private final List<Province> mProvinces1 = new ArrayList<>();
        private final List<List<City>> mSecondList = new ArrayList<>();
        private final List<List<List<County>>> mThirdList = new ArrayList<>();

        AddressProvider(List<Province> provinces) {
            parseData(provinces);
        }

        @Override
        public boolean isOnlyTwo() {
            return false;
        }

        @Override
        public List<Province> initFirstData() {
            return mProvinces1;
        }

        @Override
        public List<City> linkageSecondData(int firstIndex) {
            if (mSecondList.size() <= firstIndex) {
                return new ArrayList<>();
            }
            return mSecondList.get(firstIndex);
        }

        @Override
        public List<County> linkageThirdData(int firstIndex, int secondIndex) {
            if (mThirdList.size() <= firstIndex) {
                return new ArrayList<>();
            }
            List<List<County>> lists = mThirdList.get(firstIndex);
            if (lists.size() <= secondIndex) {
                return new ArrayList<>();
            }
            return lists.get(secondIndex);
        }

        private void parseData(List<Province> data) {
            int provinceSize = data.size();
            //添加省
            for (int x = 0; x < provinceSize; x++) {
                Province pro = data.get(x);
                mProvinces1.add(pro);
                List<City> cities = pro.getCities();
                List<City> xCities = new ArrayList<>();
                List<List<County>> xCounties = new ArrayList<>();
                int citySize = cities.size();
                //添加地市
                for (int y = 0; y < citySize; y++) {
                    City cit = cities.get(y);
                    cit.setProvinceId(pro.getAreaId());
                    xCities.add(cit);
                    List<County> counties = cit.getCounties();
                    ArrayList<County> yCounties = new ArrayList<>();
                    int countySize = counties.size();
                    //添加区县
                    for (int z = 0; z < countySize; z++) {
                        County cou = counties.get(z);
                        cou.setCityId(cit.getAreaId());
                        yCounties.add(cou);
                    }
                    xCounties.add(yCounties);
                }
                mSecondList.add(xCities);
                mThirdList.add(xCounties);
            }
        }

    }

}
