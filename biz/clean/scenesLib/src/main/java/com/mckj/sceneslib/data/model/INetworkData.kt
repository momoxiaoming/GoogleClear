package com.mckj.sceneslib.data.model

import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.WifiInfo

/**
 * Describe:
 *
 * Created By yangb on 2020/12/16
 */
interface INetworkData {

    /**
     * 获取网络连接信息
     */
    suspend fun getConnectInfo(): ConnectInfo

    /**
     * 获取wifi扫描数据
     */
    suspend fun getWifiInfoList(): List<WifiInfo>?

}