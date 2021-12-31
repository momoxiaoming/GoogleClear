package com.dn.vi.app.cm.utils

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

/**
 * Describe:
 *
 * Created By yangb on 2021/10/23
 */
object ApkUtil {

    fun getPackageInfo(context: Context, path: String): PackageInfo? {
        return try {
            val pm: PackageManager = context.packageManager
            pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getAppInfo(context: Context, path: String): ApplicationInfo? {
        return try {
            val pi = getPackageInfo(context, path) ?: return null
            val appInfo = pi.applicationInfo
            appInfo.sourceDir = path
            appInfo.publicSourceDir = path
            appInfo
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }

    fun getAppIcon(context: Context, path: String): Drawable? {
        val appInfo = getAppInfo(context, path) ?: return null
        return getAppIcon(context, appInfo)
    }

    fun getAppIcon(context: Context, appInfo: ApplicationInfo): Drawable? {
        return context.packageManager.getApplicationIcon(appInfo)
    }

    fun getAppName(context: Context, path: String): String {
        val appInfo = getAppInfo(context, path) ?: return ""
        return getAppName(context, appInfo)
    }

    fun getAppName(context: Context, appInfo: ApplicationInfo): String {
        return context.packageManager.getApplicationLabel(appInfo).toString()
    }

    fun getAppPackageName(context: Context, path: String): String {
        val appInfo = getAppInfo(context, path) ?: return ""
        return getAppPackageName(context, appInfo)
    }

    fun getAppPackageName(context: Context, appInfo: ApplicationInfo): String {
        return appInfo.packageName ?: ""
    }

    fun getAppVersionName(context: Context, path: String): String {
        val packageInfo = getPackageInfo(context, path) ?: return ""
        return packageInfo.versionName
    }

}