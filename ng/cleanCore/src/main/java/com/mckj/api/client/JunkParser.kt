package com.mckj.api.client

import android.util.Log
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkInfo
import com.mckj.api.data.Constants
import com.mckj.api.db.entity.JunkDbEntity
import com.mckj.api.manager.junk.JunkHelper
import com.mckj.api.util.FileUtils
import io.reactivex.rxjava3.functions.Consumer
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/28 17:48
 * @desc
 */
object JunkParser {

    const val TAG = "JunkParser"

    /**
     * 是否允许操作
     */
    private val mOptEnable = AtomicBoolean(true)

    private val mStopFlag = AtomicBoolean(false)

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


    private fun getAppMap(dbList: List<JunkDbEntity>): MutableMap<String, MutableList<JunkDbEntity>> {
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


    /**
     * 是否过滤
     *
     * @return true过滤  false不过滤
     *
     */
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

    //解析应用缓存列表
    /**
     * @param type
     * @param map 数据库配置的应用缓存列表
     *
     */
    fun parseAppJunk(
        map: Map<String, MutableList<JunkDbEntity>>,
        tag: Int = -1,
        consumer: Consumer<AppJunk>
    ) {
        if (mStopFlag.get()) {
            mStopFlag.set(false)
            return
        }
        mOptEnable.set(true)
        val root = FileUtils.getSDPath()
        if (root.isEmpty()) {
            Log.e(TAG, "parseAppJunk error: root is null")
            return
        }
        //app为单位扫描
        for ((key, value) in map) {
            if (!mOptEnable.get()) {
                return
            }
            val details = mutableListOf<JunkInfo>()
            var name = ""
            var size = 0L
            value.forEach { junkDbEntity ->
                if (name.isEmpty()) {
                    name = junkDbEntity.appName ?: ""
                }
                //获取可用路径
                val path = JunkHelper.getUsePath(junkDbEntity)
                if (path.isNullOrEmpty()) {
                    return@forEach
                }
                val cacheFile = File(root, path)
                if (!cacheFile.exists()) {
                    return@forEach
                }
                val count = FileUtils.getLength(cacheFile)
                if (count <= 0) {
                    return@forEach
                }
                details.add(getJunkDetailEntity(key, junkDbEntity, cacheFile.path, count, tag))
                size += count
            }
            val icon = AppUtil.getAppIcon(AppMod.app, key)
            consumer.accept(
                AppJunk(
                    appName = name,
                    packageName = key,
                    icon = icon,
                    junkSize = size,
                    junks = details,
                    tag = tag
                )
            )
        }
    }




    fun stop() {
        mOptEnable.set(false)
        mStopFlag.set(true)
    }

    //获取单个垃圾颗粒
    private fun getJunkDetailEntity(
        key: String,
        entity: JunkDbEntity,
        validPath: String,
        size: Long,
        tag: Int
    ): JunkInfo {
        return JunkInfo(
            parent = key,
            junkType = entity.junkType ?: 0,
            fileType = entity.fileType ?: 0,
            name = "",
            description = entity.desc ?: "",
            createTime = 0,
            junkSize = size,
            path = validPath,
            tag = tag
        )
    }
}