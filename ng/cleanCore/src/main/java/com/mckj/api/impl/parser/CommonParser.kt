package com.mckj.api.impl.parser

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.api.client.JunkConstants
import com.mckj.api.data.Constants
import com.mckj.api.db.entity.JunkDbEntity
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkInfo
import com.mckj.api.manager.junk.JunkHelper
import com.mckj.api.util.FileUtils
import java.io.File
import io.reactivex.rxjava3.functions.Consumer
import java.util.stream.Collector
import java.util.stream.Collectors

/**
 * @author leix
 * @version 1
 * @createTime 2021/12/22 9:24
 * @desc
 */
class CommonParser : AbsParser() {

    companion object {
        const val TAG = "CommonParser"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun parseJunk(
        map: Map<String, MutableList<JunkDbEntity>>,
        consumer: Consumer<AppJunk>,
        type: Int
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
                recursionFile(details, cacheFile, key, junkDbEntity) {
                    size += it
                }
            }
            val icon = AppUtil.getAppIcon(AppMod.app, key)
            val junks = mutableListOf<JunkInfo>()
            junks.addAll(details.distinctBy {
                it.path
            })
            consumer.accept(
                AppJunk(
                    type,
                    appName = name,
                    packageName = key,
                    icon = icon,
                    junkSize = size,
                    junks = junks
                )
            )
        }
    }


    private fun recursionFile(
        junks: MutableList<JunkInfo>,
        file: File,
        key: String,
        entity: JunkDbEntity,
        consume: Consumer<Long>
    ) {
        if (file.isDirectory) {
            val listFiles = file.listFiles()
            if (listFiles.isNullOrEmpty()) {
                junks.add(
                    getJunkDetailEntity(
                        JunkConstants.JunkType.EMPTY_DIR,
                        JunkConstants.JunkFileType.EMPTY_DIR,
                        key,
                        entity.desc,
                        file.name,
                        file.path,
                        file.length()
                    )
                )
                consume.accept(file.length())
            }
            listFiles.forEach {
                if (it.isDirectory) {
                    recursionFile(junks, it, key, entity, consume)
                } else {
                    val count = it.length()
                    consume.accept(count)
                    junks.add(
                        getJunkDetailEntity(
                            entity.junkType ?: 0,
                            entity.fileType ?: 0,
                            key,
                            entity.desc,
                            it.name,
                            it.path,
                            count
                        )
                    )
                }
            }
        }
    }

}