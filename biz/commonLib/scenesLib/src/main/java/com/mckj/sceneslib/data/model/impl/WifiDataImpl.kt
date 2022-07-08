package com.mckj.sceneslib.data.model.impl

import android.annotation.SuppressLint
import android.net.wifi.ScanResult
import android.text.TextUtils
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.sceneslib.data.model.IWifiData
import com.mckj.sceneslib.util.MacUtil
import com.mckj.sceneslib.util.NetUtil
import com.mckj.baselib.util.NetworkUtil
import com.mckj.baselib.util.WifiUtil
import com.mckj.sceneslib.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Describe:
 *
 * Created By yangb on 2020/10/14
 */

class WifiDataImpl : IWifiData {

    companion object {
        const val TAG = "WifiDataImpl"

        private val INSTANCE by lazy { WifiDataImpl() }

        fun getInstance(): WifiDataImpl = INSTANCE

    }

    @SuppressLint("HardwareIds")
    override suspend fun getConnectInfo(): ConnectInfo {
        Log.i(TAG, "getConnectInfo: ")
        var name = ""
        var level = 0
        var ipText = ""
        var speedText = ""
        var mac = ""
        var networkType =  ConnectInfo.NetworkType.NOT_CONNECTED
         return withContext(Dispatchers.IO) {
            try {
                networkType = when (NetworkUtil.getNetworkType()) {
                    NetworkUtil.NO_NETWORK -> {
                        name = "未连接"
                        ConnectInfo.NetworkType.NOT_CONNECTED
                    }
                    NetworkUtil.NETWORK_WIFI -> {
                        val info = WifiUtil.getConnectionInfo()
                        if (info != null) {
                            name = WifiUtil.parseSSID(info.ssid ?: "")
                            if (name == "<unknown ssid>") {
                                name = "WiFi"
                            }
                            level = WifiUtil.getWifiSignalLevel(info.rssi)
                            ipText = NetworkUtil.intToIp(info.ipAddress)
                            speedText = "${info.linkSpeed}${android.net.wifi.WifiInfo.LINK_SPEED_UNITS}"
                            mac = MacUtil.getAddressMac() ?: info.macAddress
                        }
                        ConnectInfo.NetworkType.WIFI
                    }
                    NetworkUtil.NETWORK_2G -> {
                        name = "移动网络2G"
                        ConnectInfo.NetworkType.MOBILE_2G
                    }
                    NetworkUtil.NETWORK_3G -> {
                        name = "移动网络3G"
                        ConnectInfo.NetworkType.MOBILE_3G
                    }
                    NetworkUtil.NETWORK_4G -> {
                        name = "移动网络4G"
                        ConnectInfo.NetworkType.MOBILE_4G
                    }
                    NetworkUtil.NETWORK_5G -> {
                        name = "移动网络5G"
                        ConnectInfo.NetworkType.MOBILE_5G
                    }
                    else -> {
                        name = "其它网络"
                        ConnectInfo.NetworkType.UNKNOWN
                    }

                }
            }catch (e:Exception){

            }

            ConnectInfo(networkType, name, ipText, speedText, mac, level)
        }
    }

    override suspend fun startScan(): Boolean {
        Log.i(TAG, "startScan: ")
        return withContext(Dispatchers.IO) {
            WifiUtil.startScan()
        }
    }

    override suspend fun getScanResult(): List<ScanResult>? {
        Log.i(TAG, "getScanResult: ")
        return withContext(Dispatchers.IO) {
            WifiUtil.getScanResult()
        }
    }

    override suspend fun disconnect(): Boolean {
        Log.i(TAG, "disconnect: ")
        return withContext(Dispatchers.IO) {
            WifiUtil.disconnect()
        }
    }

    override suspend fun removeConnect(wifiInfo: WifiInfo): Boolean {
        Log.i(TAG, "removeConnect: ")
        return withContext(Dispatchers.IO) {
            WifiUtil.disconnect()
            WifiUtil.removeConnect(wifiInfo.ssid)
        }
    }

    override suspend fun connect(wifiInfo: WifiInfo, password: String?): Boolean {
        Log.i(TAG, "connect: ")
        disconnect()
        return withContext(Dispatchers.IO) {
            if (TextUtils.isEmpty(password)) {
                WifiUtil.connect(wifiInfo.ssid)
            } else {
                WifiUtil.connect(wifiInfo.ssid, password, wifiInfo.getWifiEncryptType())
            }
        }
    }

    override suspend fun pingEnable(): Boolean {
        return withContext(Dispatchers.IO) {
            NetUtil.pingEnable()
        }
    }

    override suspend fun reportFishing(wifiInfo: WifiInfo): Boolean {
        return withContext(Dispatchers.IO) {
            NetUtil.pingEnable()
            NetUtil.dnsEnable()
        }
    }

}