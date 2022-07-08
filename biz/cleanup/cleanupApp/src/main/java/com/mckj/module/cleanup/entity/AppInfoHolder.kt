package com.mckj.module.cleanup.entity

import android.Manifest
import android.annotation.TargetApi
import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import okio.utf8Size
import java.security.MessageDigest
import android.content.Context.USAGE_STATS_SERVICE
import android.os.Process
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.mckj.module.cleanup.util.DateTools
import com.mckj.module.cleanup.util.Log

import java.util.*


/**
 * author: zpwang
 */
object AppInfoHolder {

    private enum class ApplicationType {
        AllApplication, NonSystemApplication, SystemApplication
    }

    private val appMap = mutableMapOf<String, ApplicationLocal>()

    var appUsageList: List<UsageStats> = listOf()

    private var isResume: Boolean = false

    val map: MutableMap<String, Long> = mutableMapOf()

    private val TAG = "AppInfoHolder"

    fun init(context: Context) {
        appMap.clear()
        val packageInfoList =
            context.packageManager.getInstalledPackages(PackageManager.GET_SIGNATURES)
        for (packageInfo in packageInfoList) {
            val applicationInfo = packageInfo.applicationInfo
            val application = ApplicationLocal(
                packageName = packageInfo.packageName,
                name = applicationInfo.loadLabel(context.packageManager).toString(),
                icon = applicationInfo.loadIcon(context.packageManager),
                noUseTime = packageInfo.packageName,
                appSize = applicationInfo.dataDir.utf8Size(),
                firstInstallTime = packageInfo.firstInstallTime,
                lastUpdateTime = packageInfo.lastUpdateTime,
                isSystemApp = isSystemApplication(packageInfo),
                sourceDir = applicationInfo.sourceDir,
                dataDir = applicationInfo.dataDir,
            )
            val uid = applicationInfo.uid
            val storageUuid = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationInfo.storageUuid
            } else {
                UUID.fromString("41217664-9172-527a-b3d5-edabb50a7d69")
            }
            application.uid = uid
            application.storageUuid = storageUuid

            appMap[application.name] = application
        }
    }

    private fun getSignValidString(signatures: ByteArray): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(signatures)
        return toHexString(messageDigest.digest())
    }

    private fun toHexString(keyData: ByteArray): String {
        val strBuilder = StringBuilder(keyData.size * 2)
        for (keyDatum in keyData) {
            var hexStr = Integer.toString(keyDatum.toInt() and 255, 16)
            if (hexStr.length == 1) {
                hexStr = "0$hexStr"
            }
            strBuilder.append(hexStr)
        }
        return strBuilder.toString()
    }

    /**
     * 获取设备的应用信息
     */
    private fun getApplicationInfo(
        context: Context,
        applicationType: ApplicationType
    ): MutableList<ApplicationLocal> {
        if (appMap.isEmpty()) {
            init(context)
        }
        val applicationList = mutableListOf<ApplicationLocal>()
        when (applicationType) {
            ApplicationType.AllApplication -> {
                applicationList.addAll(appMap.values)
            }
            ApplicationType.SystemApplication -> {
                applicationList.addAll(appMap.filter { entry -> entry.value.isSystemApp }.values)
            }
            ApplicationType.NonSystemApplication -> {
                applicationList.addAll(appMap.filter { entry -> !entry.value.isSystemApp }.values)
            }
        }
        return applicationList
    }

    /**
     * 判断是否是系统应用
     */
    private fun isSystemApplication(packageInfo: PackageInfo): Boolean {
        return packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    /**
     * 获取设备所有的应用
     */
    fun getAllApplication(context: Context): MutableList<ApplicationLocal> {
        return getApplicationInfo(
            context,
            ApplicationType.AllApplication
        )
    }

    /**
     * 获取设备所有的系统应用
     */
    fun getAllSystemApplication(context: Context): List<ApplicationLocal> {
        return getApplicationInfo(
            context,
            ApplicationType.SystemApplication
        )
    }

    /**
     * 获取设备所有的非系统应用
     */
    fun getAllNonSystemApplication(context: Context): MutableList<ApplicationLocal> {
        //每次进来将map清空，以便重新获取应用信息
        return getApplicationInfo(
            context,
            ApplicationType.NonSystemApplication
        )
    }

    //和获取上次使用时间相关的Usage
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getAppUsageLists(context: Context): MutableList<UsageStats> {

        val usageStatsManager: UsageStatsManager =
            context.getSystemService(USAGE_STATS_SERVICE) as UsageStatsManager

        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -1)
        val usageStatsList: MutableList<UsageStats> = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_YEARLY,
            System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 3,
            System.currentTimeMillis()
        )

        for (i in usageStatsList) {
            Log.i("appUsage", " listItem: " + i.packageName)
        }

        val usageStatsSet: MutableList<UsageStats> = mutableListOf()
        val mapUse: MutableMap<String, Boolean> = mutableMapOf()
//        Collections.sort(usageStatsList, ComparatorList())

        for (i in usageStatsList) {
            mapUse[i.packageName] = false
        }

        for (i in usageStatsList) {
            if (mapUse[i.packageName] == false) {
                usageStatsSet.add(i)
                mapUse[i.packageName] = true
            }
        }
        return usageStatsSet
    }

//    //使用时间排序器
//    class ComparatorList : Comparator<UsageStats> {
//        override fun compare(t1: UsageStats, t2: UsageStats): Int {
//            return (t2.lastTimeUsed - t1.lastTimeUsed).toInt()
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun initTimeList(context: Context) {
        appUsageList = getAppUsageLists(context)
        for (i in appUsageList) {
            map[i.packageName] = i.lastTimeUsed
            Log.i(
                "appNoUseTime", "packageName: ${i.packageName}, " +
                        "lastUseTimeMills:" + "${i.lastTimeUsed}"
            )
        }
    }

    //获得非系统应用上次使用时间
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun getNonSystemApplicationTime(context: Context, mPackageName: String): Long? {

        if (map.isEmpty()) {
            initTimeList(context)
        }
        return map[mPackageName]
    }

    //格式化时间
    fun getAppNoUseTimeFormat(lastUseTimeMills: Long): String {
        return DateTools.getDateDiff(lastUseTimeMills)
    }

    //判断是否具备访问app使用情况的权限
    @TargetApi(Build.VERSION_CODES.M)
    fun isGrantedUsagePremission(@NonNull context: Context): Boolean {
        val granted: Boolean
        val appOpsManager: AppOpsManager =
            context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode: Int = appOpsManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        granted = if (mode == AppOpsManager.MODE_DEFAULT) {
            context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }
        return granted
    }

}