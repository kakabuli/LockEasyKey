package com.philips.easykey.lock.publiclibrary.http.result;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductionModelListResult extends BaseResult{

    /**
     {
     "code": "200",
     "msg": "成功",
     "nowTime": 1587954859,
     "data": {
     "productInfoList": [
     {
     "_id": "5ea5561094479c2be05627c7",
     "developmentModel": "G8012",
     "productModel": "G8012",
     "snHead": "R60",
     "customerType": 1,
     "deviceType": 6,
     "distributionNetwork": 2,
     "productType": 1,
     "adminUrl": "http://globalapp.juziwulian.com/deviceModelFiles/1587893734892/android_admin_xxx.png",
     "deviceListUrl": "http://globalapp.juziwulian.com/deviceModelFiles/1587893735042/android_device_list_xxx.png",
     "authUrl": "http://globalapp.juziwulian.com/deviceModelFiles/1587893743099/android_auth_xxx.png",
     "adminUrl@1x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893743422/ios_admin_xxx@1x.png",
     "deviceListUrl@1x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893747664/ios_device_list_xxx@1x.png",
     "authUrl@1x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893747598/ios_auth_xxx@1x.png",
     "adminUrl@2x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893750680/ios_admin_xxx@2x.png",
     "deviceListUrl@2x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893753749/ios_device_list_xxx@2x.png",
     "authUrl@2x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893757889/ios_auth_xxx@2x.png",
     "adminUrl@3x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893763437/ios_admin_xxx@3x.png",
     "deviceListUrl@3x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893763865/ios_device_list_xxx@3x.png",
     "authUrl@3x": "http://globalapp.juziwulian.com/deviceModelFiles/1587893767392/ios_auth_xxx@3x.png",
     "createTime": "2020-04-26 09:36:16.194"
     }
     ]
     }
     }
     */

    private int nowTime;

    private DataBean data;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setNowTime(int nowTime){
        this.nowTime = nowTime;
    }
    public int getNowTime(){
        return this.nowTime;
    }
    public void setData(DataBean data){
        this.data = data;
    }
    public DataBean getData(){
        return this.data;
    }

    public static class DataBean {
        private List<ProductInfoList> productInfoList;

        public void setProductInfoList(List<ProductInfoList> productInfoList){
            this.productInfoList = productInfoList;
        }
        public List<ProductInfoList> getProductInfoList(){
            return this.productInfoList;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "productInfoList=" + productInfoList +
                    '}';
        }
    }

    public class ProductInfoList
    {
        /*private String _id;

        private String developmentModel;

        private String productModel;

        private String snHead;

        private int customerType;

        private int deviceType;

        private int distributionNetwork;

        private int productType;

        private String adminUrl;

        private String deviceListUrl;

        private String authUrl;

        @SerializedName("adminUrl@1x")
        private String adminUrl1x;
        @SerializedName("deviceListUrl@1x")
        private String deviceListUrl1x;
        @SerializedName("authUrl@1x")
        private String authUrl1x;
        @SerializedName("authUrl@2x")
        private String adminUrl2x;
        @SerializedName("deviceListUrl@2x")
        private String deviceListUrl2x;
        @SerializedName("authUrl@2x")
        private String authUrl2x;
        @SerializedName("authUrl@3x")
        private String adminUrl3x;
        @SerializedName("deviceListUrl@3x")
        private String deviceListUrl3x;
        @SerializedName("authUrl@3x")
        private String authUrl3x;

        private String createTime;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getDevelopmentModel() {
            return developmentModel;
        }

        public void setDevelopmentModel(String developmentModel) {
            this.developmentModel = developmentModel;
        }

        public String getProductModel() {
            return productModel;
        }

        public void setProductModel(String productModel) {
            this.productModel = productModel;
        }

        public String getSnHead() {
            return snHead;
        }

        public void setSnHead(String snHead) {
            this.snHead = snHead;
        }

        public int getCustomerType() {
            return customerType;
        }

        public void setCustomerType(int customerType) {
            this.customerType = customerType;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public int getDistributionNetwork() {
            return distributionNetwork;
        }

        public void setDistributionNetwork(int distributionNetwork) {
            this.distributionNetwork = distributionNetwork;
        }

        public int getProductType() {
            return productType;
        }

        public void setProductType(int productType) {
            this.productType = productType;
        }

        public String getAdminUrl() {
            return adminUrl;
        }

        public void setAdminUrl(String adminUrl) {
            this.adminUrl = adminUrl;
        }

        public String getDeviceListUrl() {
            return deviceListUrl;
        }

        public void setDeviceListUrl(String deviceListUrl) {
            this.deviceListUrl = deviceListUrl;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }

        public String getAdminUrl1x() {
            return adminUrl1x;
        }

        public void setAdminUrl1x(String adminUrl1x) {
            this.adminUrl1x = adminUrl1x;
        }

        public String getDeviceListUrl1x() {
            return deviceListUrl1x;
        }

        public void setDeviceListUrl1x(String deviceListUrl1x) {
            this.deviceListUrl1x = deviceListUrl1x;
        }

        public String getAuthUrl1x() {
            return authUrl1x;
        }

        public void setAuthUrl1x(String authUrl1x) {
            this.authUrl1x = authUrl1x;
        }

        public String getAdminUrl2x() {
            return adminUrl2x;
        }

        public void setAdminUrl2x(String adminUrl2x) {
            this.adminUrl2x = adminUrl2x;
        }

        public String getDeviceListUrl2x() {
            return deviceListUrl2x;
        }

        public void setDeviceListUrl2x(String deviceListUrl2x) {
            this.deviceListUrl2x = deviceListUrl2x;
        }

        public String getAuthUrl2x() {
            return authUrl2x;
        }

        public void setAuthUrl2x(String authUrl2x) {
            this.authUrl2x = authUrl2x;
        }

        public String getAdminUrl3x() {
            return adminUrl3x;
        }

        public void setAdminUrl3x(String adminUrl3x) {
            this.adminUrl3x = adminUrl3x;
        }

        public String getDeviceListUrl3x() {
            return deviceListUrl3x;
        }

        public void setDeviceListUrl3x(String deviceListUrl3x) {
            this.deviceListUrl3x = deviceListUrl3x;
        }

        public String getAuthUrl3x() {
            return authUrl3x;
        }

        public void setAuthUrl3x(String authUrl3x) {
            this.authUrl3x = authUrl3x;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        @Override
        public String toString() {
            return "ProductInfoList{" +
                    "_id='" + _id + '\'' +
                    ", developmentModel='" + developmentModel + '\'' +
                    ", productModel='" + productModel + '\'' +
                    ", snHead='" + snHead + '\'' +
                    ", customerType=" + customerType +
                    ", deviceType=" + deviceType +
                    ", distributionNetwork=" + distributionNetwork +
                    ", productType=" + productType +
                    ", adminUrl='" + adminUrl + '\'' +
                    ", deviceListUrl='" + deviceListUrl + '\'' +
                    ", authUrl='" + authUrl + '\'' +
                    ", adminUrl1x='" + adminUrl1x + '\'' +
                    ", deviceListUrl1x='" + deviceListUrl1x + '\'' +
                    ", authUrl1x='" + authUrl1x + '\'' +
                    ", adminUrl2x='" + adminUrl2x + '\'' +
                    ", deviceListUrl2x='" + deviceListUrl2x + '\'' +
                    ", authUrl2x='" + authUrl2x + '\'' +
                    ", adminUrl3x='" + adminUrl3x + '\'' +
                    ", deviceListUrl3x='" + deviceListUrl3x + '\'' +
                    ", authUrl3x='" + authUrl3x + '\'' +
                    ", createTime='" + createTime + '\'' +
                    '}';
        }*/
    }

    @Override
    public String toString() {
        return "GetProductionModelListResult{" +
                "nowTime=" + nowTime +
                ", data=" + data +
                '}';
    }
}
