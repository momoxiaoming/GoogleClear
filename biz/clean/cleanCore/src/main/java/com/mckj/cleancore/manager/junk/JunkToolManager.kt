package com.mckj.cleancore.manager.junk

import com.mckj.cleancore.tools.*
import com.mckj.cleancore.tools.qq.QQCacheTool
import com.mckj.cleancore.tools.apk.ApkFileTool
import com.mckj.cleancore.tools.cache.AppCacheTool
import com.mckj.cleancore.tools.ram.RamCacheTool
import com.mckj.cleancore.tools.uninstall.UninstallResidueTool
import com.mckj.cleancore.tools.wechat.WechatCacheTool

/**
 * Describe:垃圾工具管理
 *
 * Created By yangb on 2021/3/2
 */
class JunkToolManager {

    companion object {

        const val TAG = "JunkToolManager"

        private val INSTANCE by lazy { JunkToolManager() }

        fun getInstance(): JunkToolManager = INSTANCE

    }

    /**
     * 垃圾管理， key-工具类型
     */
    private val junkToolMap = mutableMapOf<Int, AbsJunkTool>()

    fun register(type: Int, junkTool: AbsJunkTool) {
        junkToolMap[type] = junkTool
    }

    fun unregister(type: Int) {
        junkToolMap.remove(type)
    }

    /**
     * 获取工具
     */
    fun getTool(type: Int): AbsJunkTool? {
        return junkToolMap[type]
    }

    /**
     * 获取所有工具
     */
    fun getAllTools(): List<AbsJunkTool> {
        return junkToolMap.map { entry ->
            entry.value
        }
    }

    /**
     * 获取工具列表
     *
     * @param type 工具类型
     */
    fun getToolsByType(type: Int): List<AbsJunkTool> {
        val list = mutableListOf<AbsJunkTool>()
        for ((key, value) in junkToolMap) {
            if (type and key != 0) {
                list.add(value)
            }
        }
        return list
    }

    init {
        //安装包文件管理
        register(JunkType.APK_FILE, ApkFileTool())
        //残留清理
        register(JunkType.UNINSTALL_RESIDUE, UninstallResidueTool())
        //应用缓存
        register(JunkType.APP_CACHE, AppCacheTool())
        //qq清理
        register(JunkType.QQ, QQCacheTool())
        //微信清理
        register(JunkType.WECHAT, WechatCacheTool())
        //内存缓存
        register(JunkType.RAM_CACHE, RamCacheTool())
    }

}