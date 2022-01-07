package com.dn.baselib.util

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import com.dn.openlib.utils.Log
import com.dn.vi.app.base.app.AppMod
import java.lang.reflect.InvocationTargetException


/**
 * Describe:进程工具类
 *
 * Created By yangb on 2020/9/9
 */

object ProcessUtil {

    private const val TAG = "ProcessUtil"

    /**
     * 获取所有后台进程
     */
    fun getAllBackgroundProcess(): List<String> {
        val activityManager =
            AppMod.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        val infoList = activityManager?.runningAppProcesses ?: emptyList()
        val set = mutableSetOf<String>()
        for (info in infoList) {
            Log.i(TAG, "getAllBackgroundProcess: ${info.processName}")
            for (pkg in info.pkgList) {
                Log.i(TAG, "getAllBackgroundProcess: $pkg")
                set.add(pkg)
            }
        }
        return set.toList()
    }

    /**
     * 获取所有运行的app信息
     *
     * @return List<PackageInfo>
     */
    fun getRunningApps(): List<PackageInfo>? {
        val packageManager: PackageManager = AppMod.app.packageManager ?: return null
        val installedPackages = packageManager.getInstalledPackages(0)
        val result = mutableListOf<PackageInfo>()
        for (pi in installedPackages) {
            val flag = pi.applicationInfo.flags
            if ((flag and ApplicationInfo.FLAG_SYSTEM == 0)//非系统应用
                && ((flag and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 0)  ////升级后也不是系统应用
                && ((flag and ApplicationInfo.FLAG_STOPPED) == 0)//未处于停止状态
                && (pi.packageName != AppMod.app.packageName) //排除自己
            ) {
                result.add(pi)
            }
        }
        return result
    }

    /**
     * 清除内存返回优化百分比
     */
    fun killAllProcessesToPercent(): Float {
        val beforeMemory = getAvailMemory()
        killBackgroundProcesses()
        val afterMemory = getAvailMemory()
        return (afterMemory - beforeMemory) / beforeMemory
    }

    /**
     * 清除内存返回优化内存长度
     */
    fun killAllProcessesToMemory(): Float {
        val beforeMemory = getAvailMemory()
        killBackgroundProcesses()
        val afterMemory = getAvailMemory()
        return beforeMemory - afterMemory
    }

    /**
     * 清除其它正在运行的App
     */
    fun killBackgroundProcesses(): Int {
        val default = 3
        try {
            val activityManager = getActivityManager()
            val appList = getRunningApps() ?: return default
            for (info in appList) {
                activityManager.killBackgroundProcesses(info.packageName)
            }
            return appList.size
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return default
    }

    /**
     * 杀死进程
     */
    fun killBackgroundProcesses(packageName: String) {
        try {
            val activityManager = getActivityManager()
            activityManager.killBackgroundProcesses(packageName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 利用反射强行停止,需要系统签名
     */
    private fun forceStopProgress(pkgName: String?) {
        try {
            val activityManager = getActivityManager()
            val forceStopPackage =
                activityManager.javaClass.getDeclaredMethod("forceStopPackage", String::class.java)
            forceStopPackage.isAccessible = true
            forceStopPackage.invoke(activityManager, pkgName)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    /**
     * 判断本应用是否已经位于最前端
     *
     * @return 本应用已经位于最前端时，返回 true；否则返回 false
     */
    fun isRunningForeground(): Boolean {
        val context = AppMod.app
        val activityManager = getActivityManager()
        val appProcessInfoList = activityManager.runningAppProcesses ?: return false
        for (appProcessInfo in appProcessInfoList) {
            if (appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName == context.applicationInfo.processName) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 将本应用置顶到最前端
     * 当本应用位于后台时，则将它切换到最前端
     */
    fun setTopApp(): Boolean {
        var result = false
        do {
            if (isRunningForeground()) {
                break
            }
            val pkgName = AppMod.app.packageName ?: break
            val activityManager = getActivityManager()
            val taskInfoList = activityManager.getRunningTasks(100)
            var id = -1
            for (info in taskInfoList) {
                val packageName = info.topActivity?.packageName ?: break
                Log.i(TAG, "setTopApp: packageName:$packageName")
                if (packageName == pkgName) {
                    id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        info.taskId
                    } else {
                        info.id
                    }
                    break
                }
            }
            Log.i(TAG, "setTopApp: id:$id")
            if (id == -1) {
                break
            }
            activityManager.moveTaskToFront(id, ActivityManager.MOVE_TASK_WITH_HOME)
            result = true
        } while (false)
        return result
    }

    /**
     * 抢占任务
     *
     * 将本应用置顶到最前端
     * 当本应用位于后台时，则将它切换到最前端
     */
    fun kotsTask(): Boolean {
        var result = false
        do {
            val pkgName = AppMod.app.packageName ?: break
            val activityManager = getActivityManager()
            val taskInfoList = activityManager.getRunningTasks(100)
            var id = -1
            for (info in taskInfoList) {
                val packageName = info.topActivity?.packageName ?: break
                Log.i(TAG, "kotsService: packageName:$packageName")
                if (packageName == pkgName) {
                    id = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        info.taskId
                    } else {
                        info.id
                    }
                    break
                }
            }
            Log.i(TAG, "kotsService: id:$id")
            if (id == -1) {
                break
            }
            activityManager.moveTaskToFront(id, ActivityManager.MOVE_TASK_WITH_HOME)
            result = true
        } while (false)
        return result
    }


    /**
     * 获取手机可用内存（单位M）
     */
    private fun getAvailMemory(): Float {
        val activityManager = getActivityManager()
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo.availMem / (1024f * 1024)
    }

    /**
     * 获取手机可用内存信息
     */
    fun getMemory(): ActivityManager.MemoryInfo {
        val activityManager = getActivityManager()
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        return memoryInfo
    }

    /**
     * 是否为主进程
     */
    fun isMainProcess(): Boolean {
        val mainProcessName = getMainProcessName()
        if (mainProcessName != null) {
            return AppMod.app.packageName == mainProcessName
        }
        return !AppMod.app.packageName.contains(":")
    }

    /**
     * 获取主进程名
     */
    fun getMainProcessName(): String? {
        val pid = android.os.Process.myPid()
        var processName: String? = null
        val activityManager = getActivityManager()
        val processList = activityManager.runningAppProcesses ?: return processName
        for (process in processList) {
            if (process.pid == pid) {
                processName = process.processName
            }
        }
        return processName
    }

    /**
     * 获取ActivityManager
     */
    fun getActivityManager(): ActivityManager {
        return AppMod.app.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

}