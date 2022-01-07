package com.mckj.cleancore.manager.junk

import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.cleancore.db.entity.JunkDbEntity
import com.mckj.cleancore.entity.AppJunkEntity
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.entity.JunkDetailEntity
import com.mckj.cleancore.util.Log
import io.reactivex.rxjava3.functions.Consumer
import java.io.File

/**
 * Describe:
 *
 * Created By yangb on 2021/5/17
 */
object AppJunkHelper {

    const val TAG = "AppJunkHelper"

    /**
     * 获取Map
     */
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

    /**
     * 获取Map
     */
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

    /**
     * 解析垃圾路径
     *
     * @param type 清理类型{@link JunkType}
     * @param map 数据集合 key-包名 value-路径集合
     * @param consumer 回调
     */
    fun parseAppJunk(
        type: Int,
        map: Map<String, MutableList<JunkDbEntity>>,
        consumer: Consumer<IJunkEntity>
    ) {
        val root = FileUtil.getSDPath()
        if (root.isEmpty()) {
            Log.e(TAG, "parseAppJunk error: root is null")
            return
        }
        //app为单位扫描
        for ((key, value) in map) {
            val details = mutableListOf<JunkDetailEntity>()
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
                val count = FileUtil.getLength(cacheFile)
                if (count <= 0) {
                    return@forEach
                }
                Log.i(TAG, "parseAppJunk: type:$type, path:${cacheFile.path}")
                details.add(getJunkDetailEntity(junkDbEntity, cacheFile.path, count))
                size += count
            }
            if (size <= 0) {
                continue
            }
            val icon = AppUtil.getAppIcon(AppMod.app, key)
            consumer.accept(AppJunkEntity(type, name, "缓存文件", key, icon, details, size))
        }
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
            junkType = com.mckj.cleancore.data.Constants.JUNK_CATE_CACHE,
            fileType = com.mckj.cleancore.data.Constants.MEDIA_TYPE_DEFAULT,
            desc = "cache",
            filePath = FileUtil.getAppCache(packageName),
            rootPath = "",
            strategy = 1
        )
    }

    private fun getCacheJunkDbEntity(packageName: String): JunkDbEntity {
        val appName = AppUtil.getAppName(AppMod.app, packageName)?.toString()
        return JunkDbEntity(
            0L,
            packageName = packageName,
            appName = appName,
            junkType = com.mckj.cleancore.data.Constants.JUNK_CATE_CACHE,
            fileType = com.mckj.cleancore.data.Constants.MEDIA_TYPE_DEFAULT,
            desc = "cache",
            filePath = FileUtil.getAppCache(packageName),
            rootPath = "",
            strategy = 1
        )
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
            val cache = FileUtil.getAppCache(packageName)
            val userPath = JunkHelper.getUsePath(entity)
            if (userPath?.contains(cache, true) == true) {
                result = true
            }
        } while (false)
        return result
    }

    private fun getJunkDetailEntity(
        entity: JunkDbEntity,
        validPath: String,
        size: Long
    ): JunkDetailEntity {
        val category = when (entity.junkType) {
            com.mckj.cleancore.data.Constants.JUNK_CATE_AD -> JunkDetailEntity.CATEGORY_AD_CACHE
            else -> JunkDetailEntity.CATEGORY_NORMAL_CACHE
        }
        val type = when (entity.fileType) {
            com.mckj.cleancore.data.Constants.MEDIA_TYPE_IMAGE -> JunkDetailEntity.TYPE_IMAGE
            com.mckj.cleancore.data.Constants.MEDIA_TYPE_AUDIO -> JunkDetailEntity.TYPE_AUDIO
            com.mckj.cleancore.data.Constants.MEDIA_TYPE_VIDEO -> JunkDetailEntity.TYPE_VIDEO
            else -> JunkDetailEntity.TYPE_NORMAL
        }
        val desc = entity.desc ?: ""
        return JunkDetailEntity(category, type, desc, validPath, size)
    }

}