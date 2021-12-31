package com.dn.vi.app.base.image.loader

import android.content.pm.ApplicationInfo

/**
 *  用Glide 来加载app的icon
 *
 *  不同的[from*]方法来请求不同的icon来源。
 * Created by holmes on 2020/6/17.
 **/
class AppIconReq {
    companion object {

        /**
         * 加载已安装的包
         */
        @JvmStatic
        fun fromInstallPackage(packageName: String): AppIconReq {
            return AppIconReq().also { it.packageName = packageName }
        }

        /**
         * 加载已安装的包
         */
        @JvmStatic
        fun fromAppInfo(appInfo: ApplicationInfo): AppIconReq {
            return AppIconReq().also { it.appInfo = appInfo }
        }

        /**
         * 加载 apk文件
         */
        @JvmStatic
        fun fromApkFile(filepath: String): AppIconReq {
            return AppIconReq().also { it.apkFilepath = filepath }
        }
    }

    private constructor()

    @Transient
    internal var packageName: String = ""

    @Transient
    internal var appInfo: ApplicationInfo? = null

    @Transient
    internal var apkFilepath: String = ""

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppIconReq

        if (packageName != other.packageName) return false
        if (appInfo != other.appInfo) return false
        if (apkFilepath != other.apkFilepath) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packageName.hashCode()
        result = 31 * result + (appInfo?.hashCode() ?: 0)
        result = 31 * result + apkFilepath.hashCode()
        return result
    }

    override fun toString(): String {
        return "AppIcon(p='$packageName', a=${appInfo?.packageName}, f='$apkFilepath')"
    }


}