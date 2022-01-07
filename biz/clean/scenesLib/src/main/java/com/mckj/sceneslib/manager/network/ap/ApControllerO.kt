package com.mckj.sceneslib.manager.network.ap

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiConfiguration
import android.os.Handler
import com.android.dx.stock.ProxyBuilder
import com.dn.vi.app.cm.log.VLog
import java.io.File
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method


/**
 * 高于android 8的ap控制
 * (参考)[https://github.com/aegis1980/WifiHotSpot/blob/master/app/src/main/java/com/fitc/wifihotspot/MyOreoWifiManager.java]
 *
 * Created by holmes on 2021/4/23.
 **/
open class ApControllerO : ApControllerOld() {

    override val log: VLog.Logger
        get() = VLog.scoped("apm:o")

    private val cm: ConnectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun getApState(): Int {
        if (isTetherActive()) {
            return ApManager.STATE_AP_ON
        }
        return ApManager.STATE_AP_OFF
    }

    override fun openAp(apInfo: ApManager.ApInfo): ApManager.ApInfo {
        log.i("open ap. {${apInfo.ssid}, ${apInfo.passwd}}")

        val configuration = apInfo.createWifiConfiguration()
        try {
            if (getApState() == ApManager.STATE_AP_ON) {
                ApManager.disableWifi()
                // closeAp()
            }
        } catch (e: Exception) {
            log.printErrStackTrace(e, "close ap error")
        }

        //配置AP
        try {
            val setConfiguration = wm.javaClass.getMethod(
                "setWifiApConfiguration",
                WifiConfiguration::class.java
            )
            setConfiguration.invoke(wm, configuration)
        } catch (e: Exception) {
            log.printErrStackTrace(e, "set wifi ap configuration error")
        }

        val started = startTethering(object : com.mckj.sceneslib.manager.network.ap.MyOnStartTetheringCallback() {
            override fun onTetheringStarted() {
                log.i("tethering started")
            }

            override fun onTetheringFailed() {
                log.w("tethering failed")
            }

        })
        if (started) {
            log.i("tethering pre-start")
        } else {
            log.i("tethering pre-start fail")
        }

        val newApInfo = try {
            val btDevice = BluetoothAdapter.getDefaultAdapter()
            val deviceName = btDevice.name
            if (!deviceName.isNullOrEmpty()) {
                ApManager.ApInfo(deviceName, "")
            } else {
                apInfo
            }
        } catch (e: Exception) {
            log.printErrStackTrace(e, "get device name error")
            apInfo
        }
        return newApInfo
    }

    override fun closeAp() {
        log.i("close ap")
        try {
            stopTethering()
        } catch (e: Exception) {
            log.printErrStackTrace(e, "close ap error")
        }
    }


    /**
     * Checks where tethering is on.
     * This is determined by the getTetheredIfaces() method,
     * that will return an empty array if not devices are tethered
     *
     * @return true if a tethered device is found, false if not found
     */
    open fun isTetherActive(): Boolean {
        try {
            val method: Method =
                cm.javaClass.getDeclaredMethod("getTetheredIfaces")
            if (method == null) {
                log.e("getTetheredIfaces is null")
            } else {
                val res: Array<String>? = method.invoke(cm) as? Array<String>
                log.d("getTetheredIfaces invoked")
                log.d(res.toString())
                if ((res?.size ?: 0) > 0) {
                    return true
                }
            }
        } catch (e: java.lang.Exception) {
            log.printErrStackTrace(e, "Error in getTetheredIfaces")
        }
        return false
    }

    /**
     * This enables tethering using the ssid/password defined in Settings App>Hotspot & tethering
     * Does not require app to have system/privileged access
     * Credit: Vishal Sharma - https://stackoverflow.com/a/52219887
     */
    open fun startTethering(callback: com.mckj.sceneslib.manager.network.ap.MyOnStartTetheringCallback): Boolean {

        // On Pie if we try to start tethering while it is already on, it will
        // be disabled. This is needed when startTethering() is called programmatically.
        if (isTetherActive()) {
            log.d("Tether already active, returning")
            return false
        }
        val outputDir: File = context.getCodeCacheDir()
        val proxy: Any
        proxy = try {
            ProxyBuilder.forClass(OnStartTetheringCallbackClass())
                .dexCache(outputDir).handler(object : InvocationHandler {
                    @Throws(Throwable::class)
                    override fun invoke(proxy: Any?, method: Method, args: Array<Any?>?): Any? {
                        when (method.name) {
                            "onTetheringStarted" -> callback.onTetheringStarted()
                            "onTetheringFailed" -> callback.onTetheringFailed()
                            else -> ProxyBuilder.callSuper(proxy, method, args)
                        }
                        return null
                    }
                }).build()
        } catch (e: java.lang.Exception) {
            log.printErrStackTrace(e, "Error in enableTethering ProxyBuilder")
            return false
        }
        var method: Method? = null
        try {
            method = cm.javaClass.getDeclaredMethod(
                "startTethering",
                Int::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType, OnStartTetheringCallbackClass(),
                Handler::class.java
            )
            if (method == null) {
                log.e("startTetheringMethod is null")
            } else {
                method.invoke(
                    cm,
                    ConnectivityManager.TYPE_MOBILE,
                    false,
                    proxy,
                    null
                )
                log.d("startTethering invoked")
            }
            return true
        } catch (e: java.lang.Exception) {
            log.printErrStackTrace(e, "Error in enableTethering")
        }
        return false
    }

    open fun stopTethering() {
        try {
            val method: Method = cm.javaClass.getDeclaredMethod(
                "stopTethering",
                Int::class.javaPrimitiveType
            )
            if (method == null) {
                log.e("stopTetheringMethod is null")
            } else {
                method.invoke(cm, ConnectivityManager.TYPE_MOBILE)
                log.d("stopTethering invoked")
            }
        } catch (e: java.lang.Exception) {
            log.printErrStackTrace(e, "stopTethering error: $e")
            e.printStackTrace()
        }
    }

    private fun OnStartTetheringCallbackClass(): Class<*>? {
        try {
            return Class.forName("android.net.ConnectivityManager\$OnStartTetheringCallback")
        } catch (e: ClassNotFoundException) {
            log.printErrStackTrace(e, "OnStartTetheringCallbackClass error: $e")
        }
        return null
    }


}