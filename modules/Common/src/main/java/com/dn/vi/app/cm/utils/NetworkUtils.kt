package com.dn.vi.app.cm.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.telephony.TelephonyManager
import androidx.core.content.getSystemService
import java.net.NetworkInterface
import java.util.*

/**
 * Network
 * Created by holmes on 2020/5/19.
 **/
object NetworkUtils {

    /**
     * 是否有网络
     */
    @JvmStatic
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService<ConnectivityManager>() ?: return false
        return cm.activeNetworkInfo?.isConnected ?: false
//        var netType = 0
//        try {
//            cm.activeNetworkInfo?.let { net ->
//                val type = net.type
//                netType = when (type) {
//                    ConnectivityManager.TYPE_WIFI -> {
//                        type
//                    }
//                    ConnectivityManager.TYPE_MOBILE -> {
//                        type
//                    }
//                    else -> {
//                        -1
//                    }
//                }
//            }
//        } catch (e: Exception) {
//        }
//        return netType != 0
    }

    const val NO_NETWORK = 1
    const val NETWORK_2G = 2
    const val NETWORK_3G = 3
    const val NETWORK_4G = 4
    const val NETWORK_5G = 5
    const val NETWORK_WIFI = 6
    const val NETWORK_UNKNOWN = 7


    fun getNetworkType(context: Context): Int {
        if (!isConnected(context)) {
            return NO_NETWORK;
        }
        if (isWifiConnected(context)) {
            return NETWORK_WIFI
        }
        val tm: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return when (tm.networkType) {
            TelephonyManager.NETWORK_TYPE_GPRS,
            TelephonyManager.NETWORK_TYPE_CDMA,
            TelephonyManager.NETWORK_TYPE_EDGE,
            TelephonyManager.NETWORK_TYPE_1xRTT,
            TelephonyManager.NETWORK_TYPE_IDEN -> NETWORK_2G

            TelephonyManager.NETWORK_TYPE_EVDO_A,
            TelephonyManager.NETWORK_TYPE_UMTS,
            TelephonyManager.NETWORK_TYPE_EVDO_0,
            TelephonyManager.NETWORK_TYPE_HSDPA,
            TelephonyManager.NETWORK_TYPE_HSUPA,
            TelephonyManager.NETWORK_TYPE_HSPA,
            TelephonyManager.NETWORK_TYPE_EVDO_B,
            TelephonyManager.NETWORK_TYPE_EHRPD,
            TelephonyManager.NETWORK_TYPE_HSPAP -> NETWORK_3G

            TelephonyManager.NETWORK_TYPE_LTE -> NETWORK_4G

            TelephonyManager.NETWORK_TYPE_NR -> NETWORK_5G
            else -> NETWORK_UNKNOWN
        }
    }

    /**
     * 获取连接网络信息
     */
    fun getNetworkInfo(context: Context): NetworkInfo? {
        return getConnectivityManager(context).activeNetworkInfo
    }

    /**
     * 是否网络连接
     */
    fun isConnected(context: Context): Boolean {
        try {
            val connectivityManager = getConnectivityManager(context)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo != null && networkInfo.isConnected
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 是否wifi连接
     *
     * @return true or false
     */
    fun isWifiConnected(context: Context): Boolean {
        try {
            val connectivityManager = getConnectivityManager(context)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            } else {
                val info = connectivityManager.activeNetworkInfo
                info != null && info.isConnected && info.type == ConnectivityManager.TYPE_WIFI
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 是否为流量连接
     */
    fun isMobileData(context: Context): Boolean {
        try {
            val connectivityManager = getConnectivityManager(context)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
            } else {
                val info = connectivityManager.activeNetworkInfo
                info != null && info.isConnected && info.type == ConnectivityManager.TYPE_MOBILE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * ip地址转换
     */
    fun intToIp(ip: Int): String {
        return (ip and 0xFF).toString() + "." +
                (ip shr 8 and 0xFF) + "." +
                (ip shr 16 and 0xFF) + "." +
                (ip shr 24 and 0xFF)
    }

    fun getConnectivityManager(context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }


    @SuppressWarnings("all")
    fun getWIFISSID(context: Context): String {
        if (!isWifiConnected(context)) {
            return ""
        }
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?
        if (wifiManager != null) {
            try {
                val info = wifiManager.connectionInfo
                if (info != null) {
                    val listOfConfigurations = wifiManager.configuredNetworks
                    if (listOfConfigurations != null) {
                        for (index in listOfConfigurations.indices) {
                            val configuration = listOfConfigurations[index]
                            if (configuration != null && configuration.networkId == info.networkId) {
                                val ssid = configuration.SSID
                                if (ssid != null) {
                                    return ssid.replace("\"", "")
                                }
                            }
                        }
                    }
                }
            } catch (e: Throwable) {
                //
            }
        }
        return ""
    }

    fun isVpnConnected(): Boolean {
        return try {
            val niList: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
                ?: return false
            val it: Iterator<NetworkInterface> = Collections.list(niList).iterator()
            while (it.hasNext()) {
                val intf: NetworkInterface = it.next()
                if (intf.isUp && intf.interfaceAddresses.size !== 0) {
                    if ("tun0" == intf.name || "ppp0" == intf.name) {
                        return true
                    }
                }
            }
            false
        } catch (e: Throwable) {
            e.printStackTrace()
            false
        }
    }
}