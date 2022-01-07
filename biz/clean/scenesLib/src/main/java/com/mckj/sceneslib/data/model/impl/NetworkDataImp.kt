package com.mckj.sceneslib.data.model.impl

import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.sceneslib.data.model.INetworkData

/**
 * Describe:
 *
 * Created By yangb on 2020/12/16
 */
class NetworkDataImp : INetworkData {

    override suspend fun getConnectInfo(): ConnectInfo {
        return WifiDataImpl.getInstance().getConnectInfo()
    }

    override suspend fun getWifiInfoList(): List<WifiInfo>? {
        val scanResultList = WifiDataImpl.getInstance().getScanResult() ?: return null
        val list = mutableListOf<WifiInfo>()
        val nameSet = mutableSetOf<String>()
        scanResultList
            .sortedByDescending { it.level }
            .filter {
                val result = it.SSID != null && it.SSID.isNotEmpty() && !nameSet.contains(it.SSID)
                nameSet.add(it.SSID)
                result
            }
            .forEach {
                list.add(WifiInfo(it.SSID, it.BSSID, it.level, capabilities = it.capabilities))
            }
        return list
    }
}