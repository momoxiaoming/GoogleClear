package com.mckj.api.client.task.mediaTask

import com.dn.vi.app.base.app.AppMod
import com.mckj.api.client.JunkConstants
import com.mckj.api.client.task.base.BaseTask
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkDescription
import com.mckj.api.entity.JunkInfo
import com.mckj.gallery.impl.PictureImpl
import io.reactivex.rxjava3.functions.Consumer

/**
 * @author xx
 * @version 1
 * @createTime 2021/10/28 17:36
 * @desc
 */
class WechatImgTask : BaseTask() {
    override fun getName(): String {
        return "微信图片缓存"
    }

    override fun scan(consumer: Consumer<AppJunk>): Boolean {
        val imgJunk = AppJunk(
            type = JunkConstants.AppCacheType.IMG_CACHE,
            appName = "微信",
            packageName = "com.tencent.mm",
            icon = null,
            junkDescription = JunkDescription(title = "微信图片缓存", description = "微信图片缓存")
        )
        val imgList = PictureImpl().getMediaListByName(
            AppMod.app,
            name = "WeiXin",
            page = 1,
            limit = Int.MAX_VALUE
        )
        var totalSize = 0L
        val list = mutableListOf<JunkInfo>()
        imgList?.forEach {
            list.add(
                JunkInfo(
                    junkType = JunkConstants.JunkType.OTHER,
                    fileType = JunkConstants.JunkFileType.IMG,
                    name = "微信图片缓存",
                    description = "微信图片缓存",
                    createTime = it.createData ?: 0,
                    junkSize = it.length,
                    path = it.originalPath ?: "",
                    mediaBean = it
                )
            )
            totalSize += it.length
        }
        imgJunk.junks = list
        imgJunk.junkSize = totalSize
        consumer.accept(imgJunk)
        return true
    }

    override fun clean(appjunk: AppJunk): Boolean {
        return true
    }

    override fun stop() {

    }
}