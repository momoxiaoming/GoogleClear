package com.mckj.baselib.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.*
import android.os.Build
import android.os.PatternMatcher
import android.provider.Settings
import com.mckj.baselib.entity.Constant
import com.mckj.baselib.helper.getApplicationContext


/**
 * Describe:
 *
 * Created By yangb on 2020/10/14
 */
object WifiUtil {

    const val TAG = "WifiUtil"

    const val WIFI_CIPHER_NO_PASS = 1
    const val WIFI_CIPHER_WEP = 2
    const val WIFI_CIPHER_WPA = 3
    const val WIFI_CIPHER_WPA2 = 4

    /**
     * 返回信号强度
     */
    fun getWifiSignalLevel(rssi: Int): Int {
        return WifiManager.calculateSignalLevel(rssi, 5)
    }

    /**
     * 获取WiFi列表
     *
     * @return List<ScanResult>
     */
    fun getScanResult(): List<ScanResult>? {
        try {
            return getWifiManager().scanResults
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * wifi是否打开
     *
     * @return true or false
     */
    fun isWifiEnable(): Boolean {
        try {
            val wifiManager = getWifiManager()
            return wifiManager.isWifiEnabled
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取wifi状态
     *
     * @return int
     */
    fun getWifiState(): Int {
        try {
            return getWifiManager().wifiState
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return WifiManager.WIFI_STATE_UNKNOWN
    }

    /**
     * 尝试操作wifi开关
     *
     * @param enable true OPEN, false CLOSE
     */
    fun tryWifiEnable(enable: Boolean): Boolean {
        try {
            val state = getWifiState()
            if (enable && (state == WifiManager.WIFI_STATE_ENABLED || state == WifiManager.WIFI_STATE_ENABLING)) {
                return true
            } else if (!enable && (state == WifiManager.WIFI_STATE_DISABLED || state == WifiManager.WIFI_STATE_DISABLING)) {
                return true
            }
            val result = setWifiEnable(enable)
            if (!result) {
                openWifiSetting()
                return false
            }
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun setWifiEnable(enable: Boolean): Boolean {
        try {
            return getWifiManager().setWifiEnabled(enable)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 获取连接的Wifi信息
     *
     * @return WifiInfo
     */
    fun getConnectionInfo(): WifiInfo? {
        try {
            return getWifiManager().connectionInfo
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 扫描wifi
     *
     * @return true or false
     */
    fun startScan(): Boolean {
        try {
            val wifiManager = getWifiManager()
            return wifiManager.startScan()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 检查wifi是否连接.
     */
    fun isConnected(ssid: String): Boolean {
        val wifiManager = getWifiManager()
        if (!wifiManager.isWifiEnabled) {
            return false
        }
        val info = getConnectionInfo() ?: return false
        val connectSsid = parseSSID(info.ssid)
        return connectSsid == ssid
    }

    fun connect(ssid: String): Boolean {
        val wifiConfiguration = getWifiConfiguration(ssid)
        if (wifiConfiguration != null) {
            return connect(wifiConfiguration.networkId)
        } else {
            Log.e(TAG, "connect error: wifiConfiguration is null, ssid:$ssid")
        }
        return false
    }

    /**
     * 连接wifi
     */
    fun connect(ssid: String, password: String?, wifiType: Int): Boolean {
        Log.i(TAG, "connect: ssid:$ssid, password:$password, wifiType:$wifiType")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && Constant.WIFI_SUPPORT_ANDROID_Q) {
            val builder = WifiNetworkSpecifier.Builder()
            if (wifiType != WIFI_CIPHER_NO_PASS) {
                builder.setWpa2Passphrase(password ?: "")
            }
            val specifier = builder
                .setSsidPattern(PatternMatcher(ssid, PatternMatcher.PATTERN_PREFIX))
                .build()

            val request = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .setNetworkSpecifier(specifier)
                .build()
            val connectivityManager = NetworkUtil.getConnectivityManager()

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.i(TAG, "onAvailable: ")
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.i(TAG, "onUnavailable: ")
                }
            }
            connectivityManager.requestNetwork(request, networkCallback)
            return true
        } else {
            val wifiManager = getWifiManager()
            //检查是否配置过这个wifi, 若配置，则移除
            removeConnect(ssid)
            //添加配置
            val networkId: Int = wifiManager.addNetwork(getWifiConfig(ssid, password, wifiType))
            if (networkId == -1) {
                Log.e(TAG, "connect error: networkId == -1")
                return false
            }
            wifiManager.saveConfiguration()
            return connect(networkId)
        }
    }

    /**
     * 连接wifi
     */
    fun connect(networkId: Int): Boolean {
        val wifiManager = getWifiManager()
        val result = wifiManager.enableNetwork(networkId, true)
        wifiManager.reconnect()
        return result
    }

    /**
     * 断开连接
     */
    fun disconnect(): Boolean {
        val wifiManager = getWifiManager()
        return wifiManager.disconnect()
    }

    /**
     * 移除网络
     */

    fun removeConnect(ssid: String): Boolean {
        val wifiConfiguration = getWifiConfiguration(ssid)
        if (wifiConfiguration != null) {
            return removeConnect(wifiConfiguration.networkId)
        } else {
            Log.e(TAG, "removeConnect error: wifiConfiguration is null, ssid:$ssid")
        }
        return false
    }

    /**
     * 移除网络
     */
    fun removeConnect(networkId: Int): Boolean {
        val wifiManager = getWifiManager()
        if (wifiManager.removeNetwork(networkId)) {
            wifiManager.saveConfiguration()
            return true
        } else {
            Log.e(TAG, "removeConnect error: removeNetwork is false")
        }
        return false
    }

    /**
     * 获取已配置的网络
     */
    @SuppressLint("MissingPermission")
    fun getWifiConfiguration(ssid: String): WifiConfiguration? {
        val name = warpSSID(ssid)
        val existingConfigs = getWifiManager().configuredNetworks ?: return null
        for (existingConfig in existingConfigs) {
            if (existingConfig.SSID == name) {
                return existingConfig
            }
        }
        return null
    }

    /**
     * 获取WifiManager
     */
    fun getWifiManager(): WifiManager {
        return getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    /**
     * 打开系统wifi设置页面
     */
    fun openWifiSetting() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplicationContext().startActivity(intent)
    }

    fun warpSSID(ssid: String): String {
        return '"' + ssid + '"'
    }

    fun parseSSID(ssid: String): String {
        if (ssid.startsWith('"') && ssid.endsWith('"')) {
            return ssid.substring(1, ssid.length - 1)
        }
        return ssid
    }

    /**
     * 创建wifi配置
     */
    private fun getWifiConfig(ssid: String, pwd: String?, wifiType: Int): WifiConfiguration? {
        val config = WifiConfiguration()
        config.allowedAuthAlgorithms.clear()
        config.allowedGroupCiphers.clear()
        config.allowedKeyManagement.clear()
        config.allowedPairwiseCiphers.clear()
        config.allowedProtocols.clear()
        config.SSID = "\"" + ssid + "\""
        when (wifiType) {
            WIFI_CIPHER_NO_PASS -> {
                config.wepKeys[0] = "";
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
            }
            WIFI_CIPHER_WEP -> {
                config.hiddenSSID = true;
                config.wepKeys[0] = "\"" + pwd + "\"";
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                config.wepTxKeyIndex = 0;
            }
            WIFI_CIPHER_WPA -> {
                config.preSharedKey = "\"" + pwd + "\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                config.status = WifiConfiguration.Status.ENABLED;
            }
            WIFI_CIPHER_WPA2 -> {
                config.preSharedKey = "\"" + pwd + "\"";
                config.hiddenSSID = true;
                config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                config.status = WifiConfiguration.Status.ENABLED;
            }
        }
        return config
    }

}