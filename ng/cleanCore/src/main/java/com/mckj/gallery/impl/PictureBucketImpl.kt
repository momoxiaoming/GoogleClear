package com.mckj.gallery.impl

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.mckj.gallery.bean.BucketBean
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.gallery.bean.MediaBean

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/22 15:26
 * @desc
 */
class PictureBucketImpl : IMediaAction<BucketBean> {
    override fun getMediaList(
        context: Context,
        bucketId: String,
        page: Int,
        limit: Int
    ): MutableList<MediaBean>? {
        return null
    }


    override fun insertMedia(context: Context, path: String):Boolean {
        return true
    }

    override fun parse(context: Context, cursor: Cursor): BucketBean? {
        val bean = BucketBean()
        val bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
        val bucketDisplayName =
            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
        val bucketKey = MediaStore.Images.Media.BUCKET_ID
        val cover = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        bean.bucketId = bucketId
        bean.bucketName = bucketDisplayName
        bean.cover = cover
        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getProjection(),
            "$bucketKey=?", arrayOf(bucketId), null
        )?.use {
            bean.counts = it.count
            if (!it.isClosed) it.close()
        }
        return bean
    }

    override fun getProjection(): Array<String> {
        val projection = ArrayList<String>()
        projection.add(MediaStore.Images.Media.BUCKET_ID)
        projection.add(MediaStore.Images.Media.DATA)
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        projection.add(MediaStore.Images.Media.ORIENTATION)
        return projection.toTypedArray()
    }

    override fun getBuckets(context: Context): MutableList<BucketBean>? {
        val bucketList = mutableListOf<BucketBean>()
        val projection = getProjection()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val allBucketBean = BucketBean()
        allBucketBean.bucketName = "所有图片"
        allBucketBean.bucketId = Integer.MIN_VALUE.toString()
        bucketList.add(allBucketBean)
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
                        if (!bucketList.contains(bucket)) {
                            if (allBucketBean.cover.isNullOrEmpty()) {
                                allBucketBean.cover = bucket.cover
                            }
                            bucketList.add(bucket)
                        }
                    }
                } while (it.moveToNext())
            }
        } catch (e: Exception) {
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