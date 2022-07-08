package com.mckj.api.client.task.mediaTask

import com.mckj.api.client.JunkConstants
import com.mckj.api.client.task.base.BaseTask
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkDescription
import com.mckj.api.entity.JunkInfo
import com.mckj.api.entity.MediaEntity
import com.mckj.api.util.FileUtils
import com.mckj.gallery.bean.MediaBean
import io.reactivex.rxjava3.functions.Consumer
import java.io.File

/**
 * @author leix
 * @version 1
 * @createTime 2021/10/28 17:37
 * @desc
 */
class QQVideoTask : BaseTask() {
    override fun getName(): String {
        return "QQ视频缓存"
    }

    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        val videoJunk = AppJunk(
            type = JunkConstants.AppCacheType.VIDEO_CACHE,
            appName = "QQ",
            packageName = "com.tencent.mobileqq",
            icon = null,
            junkDescription = JunkDescription(title = "QQ视频缓存", description = "QQ视频缓存")
        )
        val videos = getVideoList()
        val list = mutableListOf<JunkInfo>()
        var totalSize = 0L
        videos?.forEach {
            list.add(
                JunkInfo(
                    junkType = JunkConstants.JunkType.OTHER,
                    fileType = JunkConstants.JunkFileType.VIDEO,
                    name = "QQ视频缓存",
                    description = "QQ视频缓存",
                    createTime = it.lastModify,
                    junkSize = it.length,
                    path = it.path,
                    mediaBean = MediaBean(
                        id = getRandomId(),
                        originalPath = it.path,
                        mimeType = "video/mp4"
                    )
                )
            )
            totalSize += it.length
        }
        videoJunk.junkSize = totalSize
        videoJunk.junks = list
        consumer.accept(videoJunk)
        return true
    }

    override fun clean(appjunk: AppJunk): Boolean {
        return true
    }

    override fun stop() {

    }

    private fun getVideoList(): List<MediaEntity>? {
        val videoList = mutableListOf<MediaEntity>()
        val root = FileUtils.getSDPath()
        val shortVideoDir = "$root/Android/data/com.tencent.mobileqq/Tencent/MobileQQ/shortvideo"
        val file = File(shortVideoDir)
        if (file.exists() && file.isDirectory) {
            file.listFiles()?.forEach { it ->
                if (it.isDirectory) {
                    it.listFiles()?.forEach { child ->
                        if (child.isFile && child.name.endsWith(".mp4")) {
                            videoList.add(
                                MediaEntity(
                                    child.lastModified(),
                                    child.absolutePath,
                                    child.length()
                                )
                            )
                        }
                    }
                }
            }
        }
        return videoList
    }

    private fun getRandomId(): Long {
        return (1..Long.MAX_VALUE).random()
    }
}