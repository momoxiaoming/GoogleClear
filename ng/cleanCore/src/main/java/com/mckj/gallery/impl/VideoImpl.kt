package com.mckj.gallery.impl

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.mckj.gallery.bean.BucketBean
import com.mckj.gallery.bean.MediaBean
import com.mckj.api.util.Utils
import com.mckj.gallery.bean.GalleryConstants
import java.io.File
import java.security.Security


/**
 *@author xx
 *@version 1
 *@createTime 2021/7/20 15:22
 *@desc
 */
open class VideoImpl : IMediaAction<MediaBean> {
    companion object {
        const val Tag = "VideoImpl"
    }

    override fun getMediaList(
        context: Context,
        bucketId: String,
        page: Int,
        limit: Int,
    ): MutableList<MediaBean> {
        val offset = (page - 1) * limit
        val mediaBeanList = mutableListOf<MediaBean>()
        val contentResolver = context.applicationContext.contentResolver
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (bucketId != Int.MIN_VALUE.toString()) {
            selection = MediaStore.Images.Media.BUCKET_ID + "=?"
            selectionArgs = arrayOf(bucketId)
        }
        val projection = getProjection()
        val sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC"
        val uri = getUri()
        try {
            contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use {
                it.moveToFirst()
                do {
                    parse(context, it)?.let { mediaBean ->
                        if ((mediaBean.isImage() || mediaBean.isVideo())
                            && mediaBean.length > 0
                        )
                            mediaBeanList.add(mediaBean)

                    }
                } while (it.moveToNext())
                if (!it.isClosed) {
                    it.close()
                }
            }
        } catch (e: Exception) {
            Log.d(Tag, "getMedia error ${e.message}")
        }
        return mediaBeanList
    }

    /**
     * cursor对象中解析出视频信息
     */
    override fun parse(context: Context, cursor: Cursor): MediaBean? {
        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID))
        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
        val originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
        val bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID))
        val bucketDisplayName =
            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
        val mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))
        val createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
        val length = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
        return MediaBean(
            id = id,
            title = title,
            originalPath = originalPath,
            createData = createDate,
            mimeType = mimeType,
            length = length,
            bucketId = bucketId,
            bucketDisplayName = bucketDisplayName,
        )
    }

    /**
     * 获取图库视频需要的参数集
     */
    override fun getProjection(): Array<String> {
        val projection = ArrayList<String>()
        projection.add(MediaStore.Video.Media._ID)
        projection.add(MediaStore.Video.Media.TITLE)
        projection.add(MediaStore.Video.Media.DATA)
        projection.add(MediaStore.Video.Media.BUCKET_ID)
        projection.add(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
        projection.add(MediaStore.Video.Media.MIME_TYPE)
        projection.add(MediaStore.Video.Media.DATE_ADDED)
        projection.add(MediaStore.Video.Media.DATE_MODIFIED)
        projection.add(MediaStore.Video.Media.SIZE)
        return projection.toTypedArray()
    }

    override fun deleteMedia(
        context: Context,
        bean: MediaBean,
        block: ((status: GalleryConstants.RemoveStatus) -> Unit)?
    ): Int {
        var numImagesRemoved: Int
        if (bean.originalPath.isNullOrEmpty()) return 0
        val contentResolver = context.contentResolver
        val uri =
            ContentUris.withAppendedId(getUri(), bean.id!!)
        val selection: String? = null
        val selectionArgs: Array<String>? = null
        try {
            numImagesRemoved = contentResolver.delete(uri, selection, selectionArgs)
            Log.d(Tag, "移除对象：${bean.originalPath}")
        } catch (e: Exception) {
            if (isAboveQ() && (e is RecoverableSecurityException)) {
                if (context is Activity) {
                    context.startIntentSenderForResult(
                        e.userAction.actionIntent.intentSender,
                        GalleryConstants.REQUEST_CODE_SECURITY,
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                    block?.invoke(GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION)
                }
            } else {
                block?.invoke(GalleryConstants.RemoveStatus.REMOVED)
            }
            numImagesRemoved = 0
            Log.d(Tag, "deleteMedia error ${bean.originalPath}")
            return numImagesRemoved
        }
        block?.invoke(GalleryConstants.RemoveStatus.REMOVED)
        return numImagesRemoved
    }

    override fun insertMedia(context: Context, path: String): Boolean {
        val contentResolver = context.contentResolver
        val file =
            File(Utils.getDCIMPath() + "/" + Utils.getName(path))
        Utils.copyFile(path, file.absolutePath)
        val localContentValues =
            getVideoContentValues(context, file, System.currentTimeMillis())
        val insertUri = contentResolver.insert(
            getUri(),
            localContentValues
        )
        Log.d(Tag, "insertMedia: uri:$insertUri")
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.path),
            arrayOf("video/mp4")
        ) { _, _ -> }

        return true
    }


    private fun getVideoContentValues(
        context: Context?,
        paramFile: File,
        paramLong: Long
    ): ContentValues? {
        val localContentValues = ContentValues()
        localContentValues.put("title", paramFile.name)
        localContentValues.put("_display_name", paramFile.name)
        localContentValues.put("mime_type", "video/mp4")
        localContentValues.put("datetaken", paramLong)
        localContentValues.put("date_modified", paramLong)
        localContentValues.put("date_added", paramLong)
        localContentValues.put("_data", paramFile.absolutePath)
        localContentValues.put("_size", paramFile.length())
        return localContentValues
    }

    override fun getBuckets(context: Context): MutableList<BucketBean>? {
        return null
    }

    private fun getUri(): Uri {
        return if (isAboveQ()) MediaStore.Video.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        ) else MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }

    private fun isAboveQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}