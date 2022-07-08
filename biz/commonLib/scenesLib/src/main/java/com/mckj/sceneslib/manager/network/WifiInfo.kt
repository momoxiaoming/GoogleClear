package com.mckj.sceneslib.manager.network

import android.os.Parcelable
import com.mckj.baselib.util.WifiUtil
import kotlinx.android.parcel.Parcelize

/**
 * Describe:WifiInfo
 *
 * Created By yangb on 2020/10/16
 */
@Parcelize
data class WifiInfo(
    val ssid: String,
    val bssid: String,
    val level: Int,
    var isConnect: Boolean = false,//是否连接
    var isExists: Boolean = false,//是否保存过
    val capabilities: String //加密类型
) : Parcelable {

    companion object {

        //信号等级1（强）
        const val SIGNAL_LEVEL_1 = 1

        //信号等级1（较强）
        const val SIGNAL_LEVEL_2 = 2

        //信号等级1（弱）
        const val SIGNAL_LEVEL_3 = 3

        //信号等级1（较弱）
        const val SIGNAL_LEVEL_4 = 4

        //信号等级1（微弱）
        const val SIGNAL_LEVEL_5 = 5
    }

    /**
     * 获取信号等级
     */
    fun getSignalLevel(): Int {
        return when {
            level > -70 -> {
                SIGNAL_LEVEL_1
            }
            level > -80 -> {
                SIGNAL_LEVEL_2
            }
            level > -90 -> {
                SIGNAL_LEVEL_3
            }
            level > -100 -> {
                SIGNAL_LEVEL_4
            }
            else -> {
                SIGNAL_LEVEL_5
            }
        }
    }

    /**
     * 获取信号强度描述
     */
    fun getSignalLevelText(): String {
        return when (getSignalLevel()) {
            SIGNAL_LEVEL_1 -> {
                "强"
            }
            SIGNAL_LEVEL_2 -> {
                "较强"
            }
            SIGNAL_LEVEL_3 -> {
                "弱"
            }
            SIGNAL_LEVEL_4 -> {
                "较弱"
            }
            else -> {
                "微弱"
            }
        }
    }

    /**
     * 获取加密类型
     */
    fun getWifiEncryptType(): Int {
        return when {
            capabilities.contains("WEP") -> {
                WifiUtil.WIFI_CIPHER_WEP
            }
            capabilities.contains("WPA2") -> {
                WifiUtil.WIFI_CIPHER_WPA2
            }
            capabilities.contains("WPA") -> {
                WifiUtil.WIFI_CIPHER_WPA
            }
            else -> {
                WifiUtil.WIFI_CIPHER_NO_PASS
            }
        }
    }

    /**
     * 是否需要输入密码
     */
    fun isInputNeedPassword(): Boolean {
        return getWifiEncryptType() != WifiUtil.WIFI_CIPHER_NO_PASS
    }

    /**
     * 获取加密类型描述
     */
    fun getWifiEncryptTypeText(): String {
        val build = StringBuilder()
        if (capabilities.contains("WEP")) {
            build.append("WEP")
        }
        if (capabilities.contains("WPA")) {
            if (build.isNotEmpty()) {
                build.append("/")
            }
            build.append("WPA")
        }
        if (capabilities.contains("WPA2")) {
            if (build.isNotEmpty()) {
                build.append("/")
            }
            build.append("WPA2")
        }
        return build.toString()
    }

}