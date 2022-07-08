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
import java.util.regex.Pattern

/**
 * @author leix
 * @version 1
 * @createTime 2021/10/28 17:37
 * @desc
 */
class WechatVideoTask : BaseTask() {
    override fun getName(): String {
        return "微信视频缓存"
    }

    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        val videoJunk = AppJunk(
            type = JunkConstants.AppCacheType.VIDEO_CACHE,
            appName = "微信",
            packageName = "com.tencent.mm",
            icon = null,
            junkDescription = JunkDescription(title = "微信视频缓存", description = "微信视频缓存")
        )
        val videos = getVideo()
        val list = mutableListOf<JunkInfo>()
        var totalSize = 0L
        videos.forEach {
            list.add(
                JunkInfo(
                    junkType = JunkConstants.JunkType.OTHER,
                    fileType = JunkConstants.JunkFileType.VIDEO,
                    name = "微信视频缓存",
                    description = "微信视频缓存",
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

    /**
     * 获取微信视频缓存列表
     */
    private fun getVideo(): List<MediaEntity> {
        val videoList = mutableListOf<MediaEntity>()
        val wechatUserDir = getWechatUserDir()
        wechatUserDir?.forEach {
            val userFile = File(it)
            if (userFile.exists() && userFile.canRead() && userFile.isDirectory) {
                userFile.listFiles()?.forEach { child ->
                    if (child.isDirectory && child.name == "video") {
                        child.listFiles()?.forEach { videoFile ->
                            if (videoFile.isFile && (videoFile.absolutePath.endsWith(".mp4",false)
                                        || videoFile.absolutePath.endsWith(".mp4", false)
                                        || videoFile.absolutePath.equals("3gp", false)
                                        || videoFile.absolutePath.equals("rmvb", false)
                                        || videoFile.absolutePath.equals("avi", false)
                                        || videoFile.absolutePath.equals("flv", false)
                                        || videoFile.absolutePath.equals("mpg", false)
                                        )
                            ) {
                                videoList.add(
                                    MediaEntity(
                                        videoFile.lastModified(),
                                        videoFile.absolutePath,
                                        videoFile.length()
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
        return videoList
    }

    private fun getWechatUserDir(): List<String>? {
        val userDirList = mutableListOf<String>()
        val root = FileUtils.getSDPath()
        val microMsg = "$root/Android/data/com.tencent.mm/MicroMsg"
        val microMsgFile = File(microMsg)
        if (!microMsgFile.exists()) return null
        microMsgFile.listFiles()?.forEach {
            if (isMd5(key = it.name) && it.exists() && it.isDirectory) {
                userDirList.add("$microMsg/${it.name}")
            }
        }
        return userDirList
    }


    private fun isMd5(key: String): Boolean {
        val pattern = "[0-9a-fA-F]{32}"
        return Pattern.matches(pattern, key)
    }

    private fun getRandomId(): Long {
        return (1..Long.MAX_VALUE).random()
    }
}