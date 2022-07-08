package com.mckj.gallery.impl

import android.content.Context
import android.database.Cursor
import com.mckj.gallery.bean.BucketBean
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.gallery.bean.MediaBean

/**
 *@author leix
 *@version 1
 *@createTime 2021/7/20 15:22
 *@desc 定义用户和媒体库的交互能力
 */
interface IMediaAction<T> {

    fun getMediaList(
        context: Context,
        bucketId: String = "",
        page: Int = 1,
        limit: Int = 24,
    ): MutableList<MediaBean>?

    fun deleteMedia(
        context: Context,
        bean: MediaBean,
        block: ((status: GalleryConstants.RemoveStatus) -> Unit)? = null
    ): Int

    fun insertMedia(context: Context, path: String): Boolean

    fun parse(context: Context, cursor: Cursor): T?

    fun getProjection(): Array<String>

    fun getBuckets(context: Context): MutableList<BucketBean>?
}