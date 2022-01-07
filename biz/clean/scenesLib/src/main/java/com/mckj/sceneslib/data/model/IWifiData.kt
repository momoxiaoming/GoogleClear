package com.mckj.sceneslib.data.model

import android.net.wifi.ScanResult
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.WifiInfo

/**
 * Describe:wifi连接
 *
 * Created By yangb on 2020/10/14
 */
interface IWifiData {

    /**
     * 获取网络连接信息
     */
    suspend fun getConnectInfo(): ConnectInfo

    /**
     * 扫描Wifi
     */
    suspend fun startScan(): Boolean

    /**
     * 获取wifi扫描数据
     */
    suspend fun getScanResult(): List<ScanResult>?

    /**
     * 断开连接
     */
    suspend fun disconnect(): Boolean

    /**
     * 移除连接
     */
    suspend fun removeConnect(wifiInfo: WifiInfo): Boolean

    /**
     * 连接
     */
    suspend fun connect(wifiInfo: WifiInfo, password: String?): Boolean

    /**
     * 能否ping通，判断有无网络
     */
    suspend fun pingEnable(): Boolean

    /**
     * 举报钓鱼
     */
    suspend fun reportFishing(wifiInfo: WifiInfo): Boolean

}