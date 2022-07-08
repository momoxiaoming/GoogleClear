package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.app.AppOpsManager
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.Context.NETWORK_STATS_SERVICE
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.TrafficStats
import android.os.Build
import android.os.Process
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.dn.vi.app.base.app.AppMod
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NetAppInfo
import kotlinx.coroutines.*
import java.util.*


/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.util.main.network
 * @data  2022/3/28 10:34
 */

object CheckNetWorkHelper {

    @RequiresApi(Build.VERSION_CODES.M)
    private var networkStatsManager =
        AppMod.app.getSystemService(NETWORK_STATS_SERVICE) as NetworkStatsManager

    //工具包 10888
    @RequiresApi(Build.VERSION_CODES.M)
    fun getBytesValue(uid: Int): Array<Long> {
        val currentNetwork = checkNetwork()
        var rxBytes = 0L
        var txBytes = 0L
        return if (currentNetwork == NETWORK_NONE) {
            arrayOf(0, 0)
        } else {
            val newWorkStates = networkStatsManager.querySummary(
                currentNetwork,
                null,
                0,
                System.currentTimeMillis(),
            )
            do {
                val bucket = NetworkStats.Bucket()
                newWorkStates.getNextBucket(bucket)
                if (bucket.uid == uid) {
                    rxBytes += bucket.rxBytes
                    txBytes += bucket.txBytes

                }
            } while (newWorkStates.hasNextBucket())
            arrayOf(rxBytes, txBytes)
        }
    }

    /**
     * 获取当天某个app接收流量
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun getPackageTxBytesMobile(uid: Int = 10932): Array<Long> {
        val currentNetwork = checkNetwork()
        var rxBytes = 0L
        var txBytes = 0L
        return if (currentNetwork == NETWORK_NONE) {
            arrayOf(0, 0)
        } else {
            val newWorkStates = networkStatsManager.querySummary(
                currentNetwork,
                null,
                0,
                System.currentTimeMillis(),
            )
            do {
                val bucket = NetworkStats.Bucket()
                newWorkStates.getNextBucket(bucket)
                if (bucket.uid == uid) {
                    rxBytes += bucket.rxBytes
                    txBytes += bucket.txBytes
                }
            } while (newWorkStates.hasNextBucket())
            arrayOf(rxBytes, txBytes)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    fun getAllMonthMobile(): Array<Long> {
        val currentNetwork = checkNetwork()
        return if (currentNetwork == NETWORK_NONE) {
            arrayOf(0, 0)
        } else {
            val newWorkStates = networkStatsManager.querySummaryForUser(
                currentNetwork,
                null,
                getTimesMorning(),
                System.currentTimeMillis()
            )
            val totalRxBytes = newWorkStates.rxBytes
            val totalTxBytes = newWorkStates.txBytes
            arrayOf(totalRxBytes, totalTxBytes)
        }
    }


    private val connectivityManager =
        AppMod.app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkNetwork(): Int {
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return when {
            networkCapabilities == null -> {
                NETWORK_NONE
            }
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                NetworkCapabilities.TRANSPORT_CELLULAR
            }
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                NetworkCapabilities.TRANSPORT_WIFI
            }
            else -> {
                NETWORK_NONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun showCurrentNetWork() = when (checkNetwork()) {
        NETWORK_NONE -> "未知网络"
        NetworkCapabilities.TRANSPORT_CELLULAR -> "移动网络"
        NetworkCapabilities.TRANSPORT_WIFI -> "WIFI网络"
        else -> "未知网络"
    }

    private const val NETWORK_NONE = -1

    //上传的流量
    fun getAppTxBytes(uid: Int) = TrafficStats.getUidTxBytes(uid)

    //下载的流量 byte
    fun getAppRxBytes(uid: Int) = TrafficStats.getUidRxBytes(uid)

    //手机全部网络接口 包括wifi，3g、2g上传的总流量
    fun getTotalTxBytes() = TrafficStats.getTotalTxBytes()

    //手机全部网络接口 包括wifi，3g、2g下载的总流量
    fun getTotalRxBytes() = TrafficStats.getTotalRxBytes()


    fun hasPermissionToReadNetworkStats(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager?
        val mode = appOps?.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), context.packageName
        )
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true
        }
        return false
    }

    // 打开“有权查看使用情况的应用”页面
    fun requestReadNetworkIntent() = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)


    private val currentAllApp = arrayListOf<NetAppInfo>()
    private val job = Job()
    private val scope = CoroutineScope(job)


    fun initAllAppData() {
        currentAllApp.clear()
        scope.launch {
            val installedAppInfoList = withContext(Dispatchers.Default) {
                getInstalledAppInfoList(
                    AppMod.app,
                    containSystemApp = true
                )
            }
            currentAllApp.addAll(installedAppInfoList)
        }
    }

    fun getCurrentAppData() = currentAllApp


    /**
     * 获取手机已安装应用信息
     *
     * @param context 上下文
     * @param containSelf 是否包含自己应用
     * @param containSystemApp 是否包含系统应用
     */

    fun getInstalledAppInfoList(
        context: Context,
        containSelf: Boolean = false,
        containSystemApp: Boolean = false,
    ): MutableList<NetAppInfo> {
        val infoList = arrayListOf<NetAppInfo>()
        val pm = context.packageManager
        val installedPackages = pm.getInstalledPackages(0)
        for (pi in installedPackages) {
            val applicationInfo = pi?.applicationInfo ?: continue
            if (!containSelf && pi.packageName == context.packageName) {
                continue
            }
            if (((ApplicationInfo.FLAG_SYSTEM and applicationInfo.flags != 0) and !containSystemApp)) {
                continue
            }
            val appName = pi.applicationInfo.loadLabel(pm).toString()
            val uid = pi.applicationInfo.uid
            val loadIcon = pi.applicationInfo.loadIcon(pm)
            infoList.add(
                NetAppInfo(
                    appName,
                    loadIcon,
                    uid
                )
            )
        }
        return infoList
    }


    fun renderFileSize(srcSize: Long): String {
        val kb = 1024f
        val mb = 1024 * 1024.0
        val gb = 1024 * 1024 * 1024.0
        return if (srcSize > 0) {
            if (srcSize > gb) {
                "${formatNum((srcSize / gb).toFloat())}GB/s"
            } else {
                if (srcSize > mb) {
                    "${formatNum((srcSize / mb).toFloat())}MB/s"
                } else {
                    if (srcSize > kb) {
                        "${formatNum((srcSize / kb))}KB/s"
                    } else {
                        "${srcSize}B/s"
                    }
                }
            }
        } else {
            "0B/s"
        }
    }

    fun formatNum(num: Float) = String.format("%.1f", num)


    /**
     * 获取当天的零点时间
     */
    private fun getTimesMorning(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return (cal.timeInMillis)
    }

    /**
     * 获取本月第一天零点的时间
     */
    private fun getTimesMonthMorning(): Long {
        val cal = Calendar.getInstance()
        cal.set(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONDAY),
            cal.get(Calendar.DAY_OF_MONTH),
            0,
            0,
            0
        )
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
        return cal.timeInMillis
    }
}


