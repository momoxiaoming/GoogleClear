package com.mckj.sceneslib.manager.network.ap

import android.content.Context
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.log.VLog
import java.lang.reflect.Method

/**
 * 低于android 8的ap控制
 * Created by holmes on 2021/4/23.
 **/
open class ApControllerOld : ApController() {

    open val log: VLog.Logger
        get() = VLog.scoped("apm")

    protected val context: Context
        get() = AppMod.app

    protected val wm: WifiManager by lazy {
        context.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    override fun getApState(): Int {
        val on = try {
            val method: Method = wm.javaClass.getDeclaredMethod("isWifiApEnabled")
            method.isAccessible = true
            (method.invoke(wm) as Boolean)!!
        } catch (ignored: Throwable) {
            false
        }
        return if (on) {
            ApManager.STATE_AP_ON
        } else {
            ApManager.STATE_AP_OFF
        }
    }

    override fun openAp(apInfo: ApManager.ApInfo): ApManager.ApInfo {
        ApManager.log.i("open ap. {${apInfo.ssid}, ${apInfo.passwd}}")

        val configuration = apInfo.createWifiConfiguration()
        try {
            if (getApState() == ApManager.STATE_AP_ON) {
                ApManager.disableWifi()
                closeAp()
            }

            //使用反射开启Wi-Fi热点
            val method = wm.javaClass.getMethod(
                "setWifiApEnabled",
                WifiConfiguration::class.java,
                Boolean::class.javaPrimitiveType
            )
            method.invoke(wm, configuration, true)
        } catch (e: java.lang.Exception) {
            ApManager.log.printErrStackTrace(e, "open ap error")
        }
        return apInfo
    }

    override fun closeAp() {
        ApManager.log.i("close ap")
        try {
            val method: Method = wm.javaClass.getMethod(
                "setWifiApEnabled",
                WifiConfiguration::class.java,
                Boolean::class.javaPrimitiveType
            )
            method.invoke(wm, null, false)
        } catch (e: Exception) {
            ApManager.log.printErrStackTrace(e, "close ap error")
        }
    }

}