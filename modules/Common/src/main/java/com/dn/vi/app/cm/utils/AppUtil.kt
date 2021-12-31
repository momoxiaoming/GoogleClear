package com.dn.vi.app.cm.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build


/**
 * Describe:
 *
 * Created By yangb on 2021/3/2
 */
object AppUtil {

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
    ): List<PackageInfo> {
        val list = mutableListOf<PackageInfo>()
        val pm: PackageManager = context.packageManager
        val installedPackages = pm.getInstalledPackages(0)
        for (pi in installedPackages) {
            val applicationInfo = pi?.applicationInfo ?: continue
            if (!containSelf && pi.packageName == context.packageName) {
                continue
            }
            if (!containSystemApp && ApplicationInfo.FLAG_SYSTEM and applicationInfo.flags != 0) {
                continue
            }
            list.add(pi)
        }
        return list
    }

    /**
     * Return the application's icon.
     *
     * @param packageName The name of the package.
     * @return the application's icon
     */
    fun getAppIcon(context: Context, packageName: String): Drawable? {
        return try {
            val pm: PackageManager = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            getAppIcon(context, pi)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getAppIcon(context: Context, packageInfo: PackageInfo): Drawable? {
        return try {
            val pm: PackageManager = context.packageManager
            return packageInfo.applicationInfo.loadIcon(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getAppName(context: Context): CharSequence {
        return getAppName(context, context.packageName)
    }

    fun getAppName(context: Context, packageName: String): CharSequence {
        return try {
            val pm: PackageManager = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            getAppName(context, pi)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    fun getAppName(context: Context, packageInfo: PackageInfo): CharSequence {
        return try {
            val pm: PackageManager = context.packageManager
            packageInfo.applicationInfo.loadLabel(pm)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    fun getAppVersionCode(context: Context): Long {
        return getAppVersionCode(context, context.packageName)
    }

    fun getAppVersionCode(context: Context, packageName: String): Long {
        return try {
            val pm: PackageManager = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            getAppVersionCode(context, pi)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            0L
        }
    }

    /**
     * 获取app version
     */
    fun getAppVersionCode(context: Context, packageInfo: PackageInfo): Long {
        return if (Build.VERSION.SDK_INT >= 28) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }
    }

    fun getAppVersion(context: Context): String {
        return getAppVersion(context, context.packageName)
    }

    fun getAppVersion(context: Context, packageName: String): String {
        return try {
            val pm: PackageManager = context.packageManager
            val pi = pm.getPackageInfo(packageName, 0)
            getAppVersion(context, pi)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 获取app version
     */
    fun getAppVersion(context: Context, packageInfo: PackageInfo): String {
        return packageInfo.versionName
    }

    /**
     * 解析apk信息
     */
    fun parseApk(context: Context, path: String): PackageInfo? {
        val isFile = FileUtil.isFile(path)
        if (!isFile) {
            return null
        }
        return try {
            val pm = context.packageManager ?: return null
            pm.getPackageArchiveInfo(path, 0)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 是否安装
     */
    fun isInstall(context: Context, packageName: String): Boolean {
        return try {
            val pm = context.packageManager
            pm.getApplicationInfo(packageName, 0).enabled
        } catch (e: Exception) {
            false
        }
    }

}