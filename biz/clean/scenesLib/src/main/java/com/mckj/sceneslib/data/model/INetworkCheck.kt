package com.mckj.sceneslib.data.model

import androidx.core.util.Consumer
import com.mckj.sceneslib.entity.WifiDevice

/**
 * Describe:
 *
 * Created By yangb on 2021/4/23
 */
interface INetworkCheck {

    /**
     * 扫描wifi设备
     */
    suspend fun scanWifiDevice(hostIp: String, consumer: Consumer<WifiDevice>): Boolean

}