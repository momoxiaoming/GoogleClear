package com.mckj.baselib.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.telephony.TelephonyManager
import com.mckj.baselib.helper.getApplicationContext

/**
 * Describe:
 *
 * Created By yangb on 2020/10/14
 */
object NetworkUtil {

    const val NO_NETWORK = 1
    const val NETWORK_2G = 2
    const val NETWORK_3G = 3
    const val NETWORK_4G = 4
    const val NETWORK_5G = 5
    const val NETWORK_WIFI = 6
    const val NETWORK_UNKNOWN = 7


    fun getNetworkType(): Int {
        try {
            if (!isConnected()) {
                return NO_NETWORK
            }
            if (isWifiConnected()) {
                return NETWORK_WIFI
            }
            val tm: TelephonyManager =
                getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
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
        } catch (e: Exception) {
            return NETWORK_UNKNOWN
        }
        NETWORK_UNKNOWN
    }

    /**
     * 获取连接网络信息
     */
    fun getNetworkInfo(): NetworkInfo? {
        return getConnectivityManager().activeNetworkInfo
    }

    /**
     * 是否网络连接
     */
    fun isConnected(): Boolean {
        try {
            val connectivityManager = getConnectivityManager()
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
    fun isWifiConnected(): Boolean {
        try {
            val connectivityManager = getConnectivityManager()
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
    fun isMobileData(): Boolean {
        try {
            val connectivityManager = getConnectivityManager()
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

    fun getConnectivityManager(): ConnectivityManager {
        return getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

}