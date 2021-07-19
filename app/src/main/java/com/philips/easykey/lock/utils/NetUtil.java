package com.philips.easykey.lock.utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.philips.easykey.lock.MyApplication;

import java.util.List;


public class NetUtil {

	/**
	 * 检测当的网络是否可用
	 *
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable() {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) MyApplication.getInstance()
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 当前网络是连接的
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						// 当前所连接的网络可用
						return true;
					}
				}
			}
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * 判断网络是否连接
	 *
	 * @param
	 * @return
	 */

	public static  boolean isNetworkConnected() {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) MyApplication.getInstance()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			return mNetworkInfo.isAvailable();
		}
	 	return false;
}


	public static boolean isWifiEnabled(Context context) {
		ConnectivityManager mgrConn = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		TelephonyManager mgrTel = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
				.getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
				.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
	}

	public static boolean isWifi() {
		ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info= connectivityManager.getActiveNetworkInfo();
		if (info!= null && info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	public static String getWifiName(){
		WifiManager wifiMgr = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiMgr.getConnectionInfo();
		String wifiId = info != null ? info.getSSID() : null;
		return wifiId;
	}

	public static boolean isWifi24G(Context context) {
		int freq = 0;
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
			freq = wifiInfo.getFrequency();
		} else {
			String ssid = wifiInfo.getSSID();
			if (ssid != null && ssid.length() > 2) {
				String ssidTemp = ssid.substring(1, ssid.length() - 1);
				List<ScanResult> scanResults = wifiManager.getScanResults();
				for (ScanResult scanResult : scanResults) {
					if (scanResult.SSID.equals(ssidTemp)) {
						freq = scanResult.frequency;
						break;
					}
				}
			}
		}
		return freq > 2400 && freq < 2500;
	}

	public static boolean isWifi5G(Context context) {
		int freq = 0;
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.LOLLIPOP) {
			freq = wifiInfo.getFrequency();
		} else {
			String ssid = wifiInfo.getSSID();
			if (ssid != null && ssid.length() > 2) {
				String ssidTemp = ssid.substring(1, ssid.length() - 1);
				List<ScanResult> scanResults = wifiManager.getScanResults();
				for (ScanResult scanResult : scanResults) {
					if (scanResult.SSID.equals(ssidTemp)) {
						freq = scanResult.frequency;
						break;
					}
				}
			}
		}
		return freq > 4900 && freq < 5900;
	}

}
