package com.mckj.gallery.impl

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.mckj.gallery.bean.BucketBean
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.gallery.bean.MediaBean

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/22 15:26
 * @desc
 */
class VideoBucketImpl : IMediaAction<BucketBean> {
    override fun getMediaList(
        context: Context,
        bucketId: String,
        page: Int,
        limit: Int
    ): MutableList<MediaBean>? {
        return null
    }


    override fun insertMedia(context: Context, path: String): Boolean {
        return true
    }

    override fun parse(context: Context, cursor: Cursor): BucketBean? {
        val bean = BucketBean()
        val bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
        val bucketDisplayName =
            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
        val bucketKey = MediaStore.Video.Media.BUCKET_ID
        val cover = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        bean.bucketId = bucketId
        bean.bucketName = bucketDisplayName
        bean.cover = cover
        bean.mimeType = "video/mp4"
        context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI, getProjection(),
            "$bucketKey=?", arrayOf(bucketId), null
        )?.use {
            bean.counts = it.count
            if (!it.isClosed) it.close()
        }
        return bean
    }

    override fun getProjection(): Array<String> {
        val projection = ArrayList<String>()
        projection.add(MediaStore.Video.Media.BUCKET_ID)
        projection.add(MediaStore.Video.Media.DATA)
        projection.add(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        return projection.toTypedArray()
    }

    override fun getBuckets(context: Context): MutableList<BucketBean>? {
        val bucketList = mutableListOf<BucketBean>()
        val projection = getProjection()
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val allVideoBucketBean = BucketBean()
        //由于业务需求，将媒体库中的视频文件归为所有视频1个bucket
        allVideoBucketBean.mimeType = "video/mp4"
        allVideoBucketBean.bucketName = "所有视频"
        allVideoBucketBean.bucketId = Int.MIN_VALUE.toString()

        try {
            context.contentResolver.query(
                uri,
                projection,
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC "
            )?.use {
                it.moveToFirst()
                do {
                    parse(context, it)?.let { bucket ->
                        if (allVideoBucketBean.cover.isNullOrEmpty()) {
                            allVideoBucketBean.cover = bucket.cover
                            bucketList.add(allVideoBucketBean)
                        }
                    }
                } while (it.moveToNext())
            }
        } catch (e: Exception) {
            if (allVideoBucketBean.cover.isNullOrEmpty()) {
                bucketList.add(allVideoBucketBean)
            }
            Log.d(PictureImpl.Tag, "getMedia error ${e.message}")
        }
        return bucketList
    }

    override fun deleteMedia(
        context: Context,
        bean: MediaBean,
        block: ((status: GalleryConstants.RemoveStatus) -> Unit)?
    ): Int {
        return 0
    }
}