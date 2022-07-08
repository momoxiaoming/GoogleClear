package com.mckj.sceneslib.manager.network.ap

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.os.Build
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.log.VLog
import kotlinx.android.parcel.Parcelize


/**
 * AP 相关的控制
 * Created by holmes on 2021/4/23.
 **/
object ApManager {

    const val EXTRA_AP_INFO = "ap:info"

    const val STATE_AP_OFF = 0
    const val STATE_AP_ON = 1

    val log: VLog.Logger
        get() = VLog.scoped("apm")

    /**
     * AP基本信息
     */
    @Parcelize
    data class ApInfo(
        val ssid: String,
        val passwd: String
    ) : android.os.Parcelable {

        fun createWifiConfiguration(): WifiConfiguration {
            val config = WifiConfiguration()
            config.SSID = ssid
            config.preSharedKey = passwd
            config.status = WifiConfiguration.Status.ENABLED
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN)
            return config
        }

    }

    private val context: Context
        get() = AppMod.app

    private val wm: WifiManager by lazy {
        context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    private val controller: ApController by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ApControllerO()
        } else {
            ApControllerOld()
        }
    }

    fun getApState(): Int {
        return controller.getApState()
    }

    fun closeAp() {
        log.i("close ap")
        controller.closeAp()
    }

    fun openAp(apInfo: ApInfo): ApInfo {
        disableWifi()

        log.i("open ap. {${apInfo.ssid}, ${apInfo.passwd}}")
        return controller.openAp(apInfo)
    }

    private fun isApOn(): Boolean {
        return getApState() == STATE_AP_ON
    }

    fun disableWifi() {
        if (wm.isWifiEnabled) {
            wm.setWifiEnabled(false)
        }
        log.i("disable wifi error")
    }


}