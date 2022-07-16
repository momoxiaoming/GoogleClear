package com.mckj.gallery.bean

import android.text.TextUtils

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/22 15:23
 * @desc
 */
data class BucketBean(
    var bucketId: String = "",
    var bucketName: String = "",
    var counts: Int = 0,
    var cover: String = "",
    var mimeType: String = ""
) {
    override fun equals(o: Any?): Boolean {
        if (o == null || o !is BucketBean) {
            return false
        }
        return TextUtils.equals(o.bucketId, bucketId)
    }
    fun isVideo(): Boolean {
        return mimeType.startsWith("video")
    }

    fun isImage(): Boolean {
        return mimeType.startsWith("image")
    }
}