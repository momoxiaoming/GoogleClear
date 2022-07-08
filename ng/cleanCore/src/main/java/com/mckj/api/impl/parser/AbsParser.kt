package com.mckj.api.impl.parser

import android.net.Uri
import android.util.Log
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.api.data.Constants
import com.mckj.api.db.entity.JunkDbEntity
import com.mckj.api.entity.JunkInfo
import com.mckj.api.manager.junk.JunkHelper
import com.mckj.api.util.FileUtils
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author leix
 * @version 1
 * @createTime 2021/12/22 9:27
 * @desc
 */
abstract class AbsParser : IParser {
    val mOptEnable = AtomicBoolean(true)
    val mStopFlag = AtomicBoolean(false)

    fun getAppMap(
        appList: List<String>,
        dbList: List<JunkDbEntity>
    ): MutableMap<String, MutableList<JunkDbEntity>> {
        val map: MutableMap<String, MutableList<JunkDbEntity>> = getAppMap(dbList)
        appList.forEach { packageName ->
            if (map.containsKey(packageName)) {
                return@forEach
            }
            map[packageName] = mutableListOf(getCacheJunkDbEntity(packageName))
        }
        return map
    }

    fun getAppMap(dbList: List<JunkDbEntity>): MutableMap<String, MutableList<JunkDbEntity>> {
        //先把应用垃圾归类
        val appEntityMap = mutableMapOf<String, MutableList<JunkDbEntity>>()
        dbList.forEach { entity ->
            val key = getPackageName(entity)
            var value = appEntityMap[key]
            if (value == null) {
                value = mutableListOf(getCacheJunkDbEntity(entity))
                appEntityMap[key] = value
            }
            if (isFilter(entity)) {
                return@forEach
            }
            value.add(entity)
        }
        return appEntityMap
    }

    private fun isFilter(entity: JunkDbEntity): Boolean {
        var result = false
        do {
            val packageName = getPackageName(entity)
            val cache = FileUtils.getAppCache(packageName)
            val userPath = JunkHelper.getUsePath(entity)
            if (userPath?.contains(cache, true) == true) {
                result = true
            }
        } while (false)
        return result
    }

    private fun getPackageName(entity: JunkDbEntity): String {
        val packageName = entity.packageName
        return if (packageName.isNullOrEmpty() || packageName == "unknow") {
            ""
        } else {
            packageName
        }
    }

    private fun getCacheJunkDbEntity(entity: JunkDbEntity): JunkDbEntity {
        val packageName = getPackageName(entity)
        return JunkDbEntity(
            0,
            packageName = packageName,
            appName = entity.appName,
            junkType = Constants.JUNK_CATE_CACHE,
            fileType = Constants.FILE_TYPE_DEFAULT,
            desc = "cache",
            filePath = FileUtils.getAppCache(packageName),
            rootPath = "",
            strategy = 1
        )
    }

    private fun getCacheJunkDbEntity(packageName: String): JunkDbEntity {
        val appName = AppUtil.getAppName(AppMod.app, packageName).toString()
        return JunkDbEntity(
            0L,
            packageName = packageName,
            appName = appName,
            junkType = Constants.JUNK_CATE_CACHE,
            fileType = Constants.FILE_TYPE_DEFAULT,
            desc = "cache",
            filePath = FileUtils.getAppCache(packageName),
            rootPath = "",
            strategy = 1
        )
    }

    override fun stop() {
        Log.d("leix","停止：")
        mOptEnable.set(false)
        mStopFlag.set(true)
        Log.d("leix","停止后：${mOptEnable.get()}---${mStopFlag.get()}")
    }

    //获取单个垃圾颗粒
    fun getJunkDetailEntity(
        type:Int,
        fileType:Int,
        key: String,
        desc: String?="",
        fileName:String?="",
        validPath: String,
        size: Long,
        uri: Uri? = null
    ): JunkInfo {
        return JunkInfo(
            parent = key,
            junkType = type,
            fileType = fileType,
            name = fileName?:"",
            description = desc?:"",
            createTime = 0,
            junkSize = size,
            path = validPath,
            uri = uri
        )
    }
}