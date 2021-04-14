package com.philips.easykey.lock.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.NetworkSpecifier;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.PatternMatcher;

import java.util.ArrayList;
import java.util.List;

public class WifiUtils {
    private static WifiUtils utils = null;

    public WifiUtils(Context context){
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static WifiUtils getInstance(Context context){
        if (utils == null){
            synchronized (WifiUtils.class){
                if (utils == null){
                    utils = new WifiUtils(context);
                }
            }
        }
        return utils;
    }

    public boolean startScan() {
        return wifiManager.startScan();
    }

    private WifiManager wifiManager;
    private ConnectivityManager connectivityManager;

    /**
     * wifi是否打开
     * @return
     */
    public boolean isWifiEnable(){
        boolean isEnable = false;
        if (wifiManager != null){
            if (wifiManager.isWifiEnabled()){
                isEnable = true;
            }
        }
        return isEnable;
    }

    /**
     * 打开WiFi
     */
    public void openWifi(){
        if (wifiManager != null && !isWifiEnable()){
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 关闭WiFi
     */
    public void closeWifi(){
        if (wifiManager != null && isWifiEnable()){
            wifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 获取WiFi列表
     * @return
     */
    public List<ScanResult> getWifiList(){
        List<ScanResult> resultList = new ArrayList<>();
        if (wifiManager != null && isWifiEnable()){
            resultList.addAll(wifiManager.getScanResults());
        }
        return resultList;
    }

    /**
     * 有密码连接
     * @param ssid
     * @param pws
     */
    public void connectWifiPws(String ssid, String pws){
        LogUtils.e("连接wifi   " +ssid);
        if (android.os.Build.VERSION.SDK_INT >= /*android.os.Build.VERSION_CODES.Q*/50) {
            NetworkSpecifier specifier = new WifiNetworkSpecifier.Builder()
                    .setSsidPattern(new PatternMatcher(ssid, PatternMatcher.PATTERN_PREFIX))
                    .setWpa2Passphrase(pws)
                    .build();

            NetworkRequest request = new NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                    .setNetworkSpecifier(specifier)
                    .build();

            ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    connectivityManager.bindProcessToNetwork(network);

                }

                @Override
                public void onUnavailable() {

                }
            };
            connectivityManager.requestNetwork(request, networkCallback);

            return ;
        }else{
            wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
            int netId = wifiManager.addNetwork(getWifiConfig(ssid, pws, true));
            wifiManager.enableNetwork(netId, true);
        }
    }


    /**
     * 关闭当前wifi
     */
    public void disableWiFi( ){
        LogUtils.e("连接wifi   " );
        wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
    }

    /**
     * 无密码连接
     * @param ssid
     */
    public void connectWifiNoPws(String ssid){
        LogUtils.e("连接wifi   " +ssid);
        wifiManager.disableNetwork(wifiManager.getConnectionInfo().getNetworkId());
        int netId = wifiManager.addNetwork(getWifiConfig(ssid, "", false));
        wifiManager.enableNetwork(netId, true);
    }

    /**
     * wifi设置
     * @param ssid
     * @param pws
     * @param isHasPws
     */
    private WifiConfiguration getWifiConfig(String ssid, String pws, boolean isHasPws){
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";

        WifiConfiguration tempConfig = isExist(ssid);
        if(tempConfig != null) {
            wifiManager.removeNetwork(tempConfig.networkId);
        }
        if (isHasPws){
            config.preSharedKey = "\""+pws+"\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }else {
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        return config;
    }

    /**
     * 得到配置好的网络连接
     * @param ssid
     * @return
     */
    private WifiConfiguration isExist(String ssid) {
        List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\""+ssid+"\"")) {
                return config;
            }
        }
        return null;
    }
}