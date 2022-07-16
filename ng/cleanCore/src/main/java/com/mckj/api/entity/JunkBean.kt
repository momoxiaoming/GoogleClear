package com.mckj.api.entity

import android.graphics.drawable.Drawable
import android.net.Uri
import com.mckj.api.client.JunkConstants
import com.mckj.gallery.bean.MediaBean

/**
 * @author xx
 * @version 1
 * @createTime 2021/9/28 17:21
 * @desc
 */

//Junk附加信息:标题，简介
data class JunkDescription(var title: String = "", val description: String = "")

data class MediaEntity(
    val lastModify: Long = 0,
    val path: String = "",
    val length: Long = 0,
    var desc: String = "",
    var name: String = "",
    var uri: Uri? = null
)

//单个缓存垃圾，垃圾的最小颗粒
data class JunkInfo(
    var parent: String = "",
    var junkType: Int = 0,
    var fileType: Int = 0,
    var name: String = "",
    var description: String = "",
    var createTime: Long = 0,
    var junkSize: Long = 0,
    var path: String = "",
    var mediaBean: MediaBean? = null,//媒体资源的话附加媒体信息,
    var tag: Int = -1,//朔源标记位
    var uri: Uri? = null,
    var uriStr:String = ""
) {
    override fun toString(): String {
        return "\n parent:$parent" +
                "\n junkType:$junkType" +
                "\n fileType:$fileType" +
                "\n name:$name" +
                "\n description:$description" +
                "\n junkSize:$junkSize" +
                "\n path:$path"
    }
}

//应用颗粒级别的缓存，以应用为1个颗粒
data class AppJunk(
    var type: Int? = JunkConstants.Session.APP_CACHE,//@Linked JunkConstants.AppCacheType
    var appName: String = "",
    var packageName: String = "",
    var icon: Drawable? = null,
    var junkSize: Long = 0,
    var junkDescription: JunkDescription? = null,
    var junks: MutableList<JunkInfo>? = null,//应用内的缓存列表
    var tag: Int = -1//朔源标记位
) {
    override fun toString(): String {
        return "\n type:$type" +
                "\n appName:$appName" +
                "\n packageName:$packageName" +
                "\n junkSize:$junkSize" +
                "\n junks:${junks?.size}"
    }
}

//扫描的全部缓存垃圾
data class CacheJunk(
    var junkDescription: JunkDescription? = null,
    var junkSize: Long = 0,
    var appJunks: MutableList<AppJunk>? = null
)

data class QDirBean(
    val disPlay: String,
    val lastModify: Long,
    val size: Long,
    val documentId: String,
    val list: MutableList<QFileBean>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as QDirBean
        if (documentId == other.documentId) return true
        return true
    }
}

data class QFileBean(
    val disPlay: String,
    val lastModify: Long,
    val size: Long,
    val documentId: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as QDirBean
        if (documentId == other.documentId) return true
        return true
    }
}

//扫描信息
data class ScanBean(var status: Int = JunkConstants.ScanStatus.START, var junk: CacheJunk? = null)