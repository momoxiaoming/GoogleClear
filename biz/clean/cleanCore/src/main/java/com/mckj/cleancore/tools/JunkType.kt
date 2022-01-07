package com.mckj.cleancore.tools

/**
 * Describe:
 *
 * Created By yangb on 2021/5/13
 */
object JunkType {

    /**
     * 应用缓存
     */
    const val APP_CACHE: Int = 1 shl 0

    /**
     * 安装包文件
     */
    const val APK_FILE: Int = 1 shl 1

    /**
     * 卸载残留
     */
    const val UNINSTALL_RESIDUE: Int = 1 shl 2

    /**
     * 专清-微信
     */
    const val WECHAT: Int = 1 shl 3

    /**
     * 专清-QQ
     */
    const val QQ: Int = 1 shl 4

    /**
     * 内存缓存
     */
    const val RAM_CACHE: Int = 1 shl 5

    /**
     * 广告垃圾
     */
    const val AD_CACHE: Int = 1 shl 6



    /**
     * 自动扫描
     */
    const val AUTO_SCAN = APP_CACHE or AD_CACHE or UNINSTALL_RESIDUE

}