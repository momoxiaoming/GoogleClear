package com.mckj.gallery.impl

import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.mckj.api.util.Utils
import com.mckj.gallery.bean.BucketBean
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.gallery.bean.MediaBean
import java.io.File
import kotlin.collections.ArrayList


/**
 *@author leix
 *@version 1
 *@createTime 2021/7/20 15:22
 *@desc
 */
class PictureImpl : IMediaAction<MediaBean> {
    companion object {
        const val Tag = "PictureImpl"
    }

    override fun getMediaList(
        context: Context,
        bucketId: String,
        page: Int,
        limit: Int,
    ): MutableList<MediaBean>? {
        val mediaBeanList = mutableListOf<MediaBean>()
        val uri = getUri()
        val projection = getProjection()
        var selection: String? = null
        var selectionArgs: Array<String>? = null
        if (bucketId != Int.MIN_VALUE.toString()) {
            selection = MediaStore.Images.Media.BUCKET_ID + "=?"
            selectionArgs = arrayOf(bucketId)
        }
        val sortOrder = MediaStore.Images.Media._ID + " ASC"
        try {
            val cursor = context.applicationContext.contentResolver.query(
                uri, projection, selection, selectionArgs, sortOrder
            )
            cursor?.let {
                while (cursor.moveToNext()) {
                    parse(context, cursor)?.let { mediaBean ->
                        if ((!mediaBean.isImage() || !mediaBean.isVideo())
                            && mediaBean.length > 0
                        )
                            mediaBeanList.add(mediaBean)
                    }
                }
                cursor.close()
            }
        } catch (e: Exception) {
            Log.d(Tag, "getMedia error ${e.message}")
        }
        return mediaBeanList
    }

    fun getMediaListByName(
        context: Context,
        name: String,
        page: Int,
        limit: Int,
    ): MutableList<MediaBean>? {
        val offset = (page - 1) * limit
        val mediaBeanList = mutableListOf<MediaBean>()
        val contentResolver = context.contentResolver
        val selection: String?
        val selectionArgs: Array<String>?
        selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "=?"
        selectionArgs = arrayOf(name)
        val sortOrder = MediaStore.Images.Media._ID + " ASC"
        val projection = getProjection()
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
                        if ((!mediaBean.isImage() || !mediaBean.isVideo())
                            && mediaBean.length > 0
                        )
                            mediaBeanList.add(mediaBean)
                    }
                } while (it.moveToNext())
                if (it != null && !it.isClosed) {
                    it.close()
                }
            }
        } catch (e: Exception) {
            Log.d(Tag, "getMedia error ${e.message}")
        }
        return mediaBeanList
    }

    fun getMediaListById(
        context: Context,
        id: String,
    ): MutableList<MediaBean>? {
        val mediaBeanList = mutableListOf<MediaBean>()
        val contentResolver = context.contentResolver
        val selection: String?
        val selectionArgs: Array<String>?
        selection = MediaStore.Images.Media._ID + ">=?"
        selectionArgs = arrayOf(id)
        val sortOrder = MediaStore.Images.Media._ID + " ASC"
        val projection = getProjection()
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
                        if ((!mediaBean.isImage() || !mediaBean.isVideo())
                            && mediaBean.length > 0
                        )
                            mediaBeanList.add(mediaBean)
                    }
                } while (it.moveToNext())
                if (it != null && !it.isClosed) {
                    it.close()
                }
            }
        } catch (e: Exception) {
            Log.d(Tag, "getMedia error ${e.message}")
        }
        return mediaBeanList
    }

    override fun getProjection(): Array<String> {
        val projection = ArrayList<String>()
        projection.add(MediaStore.Images.Media._ID)
        projection.add(MediaStore.Images.Media.TITLE)
        projection.add(MediaStore.Images.Media.DATA)
        projection.add(MediaStore.Images.Media.MIME_TYPE)
        projection.add(MediaStore.Images.Media.DATE_ADDED)
        projection.add(MediaStore.Images.Media.DATE_MODIFIED)
        projection.add(MediaStore.Images.Media.SIZE)
        projection.add(MediaStore.Images.Media.BUCKET_ID)
        projection.add(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        projection.add(MediaStore.Images.Media.ORIENTATION)
        projection.add(MediaStore.Images.Media.WIDTH)
        projection.add(MediaStore.Images.Media.HEIGHT)
        return projection.toTypedArray()
    }


    /**
     * cursor对象中解析出图片信息
     */
    override fun parse(context: Context, cursor: Cursor): MediaBean? {
        val length = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE))
        val originalPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
        if (originalPath.isNullOrEmpty() || length < 0 || !File(originalPath).exists()) return null

        val id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE))
        val bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID))
        val bucketDisplayName =
            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
        val mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE))
        val createDate = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED))
        val width = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH))
        val height = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT))
        if (width == 0L || height == 0L) {
            return null
        }
        return MediaBean(
            id = id,
            title = title,
            originalPath = originalPath,
            createData = createDate * 1000,
            mimeType = mimeType,
            length = length,
            bucketId = bucketId,
            bucketDisplayName = bucketDisplayName
        )
    }

    override fun deleteMedia(
        context: Context,
        bean: MediaBean,
        block: ((status: GalleryConstants.RemoveStatus) -> Unit)?
    ): Int {
        var numImagesRemoved: Int
        if (bean.originalPath.isNullOrEmpty()) return 0
        val contentResolver = context.applicationContext.contentResolver
        val uri =
            ContentUris.withAppendedId(getUri(), bean.id!!)
        val selection: String? = null
        val selectionArgs: Array<String>? = null
        try {
            numImagesRemoved = contentResolver.delete(uri, selection, selectionArgs)
            Log.d(Tag, "移除对象：${bean.originalPath}")
        } catch (e: Exception) {
            if (isAboveQ() && e is RecoverableSecurityException) {
                if (context is Activity) {
                    val bundle = Bundle()
                    bundle.putString("uri", bean.originalPath)
                    context.startIntentSenderForResult(
                        e.userAction.actionIntent.intentSender,
                        GalleryConstants.REQUEST_CODE_SECURITY,
                        null,
                        0,
                        0,
                        0,
                        bundle
                    )
                    block?.invoke(GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION)
                }
            }
            numImagesRemoved = 0
            Log.d(Tag, "deleteMedia error ${bean.originalPath}")
            return numImagesRemoved
        }
        block?.invoke(GalleryConstants.RemoveStatus.REMOVED)
        return numImagesRemoved
    }


    fun deleteBatchMedia(
        context: Context,
        list: List<MediaBean>,
        block: ((status: GalleryConstants.RemoveStatus) -> Unit)?
    ) {
        val contentResolver = context.applicationContext.contentResolver
        var uriList = mutableListOf<Uri>()
        list.forEach {bean ->
            uriList.add(ContentUris.withAppendedId(getUri(), bean.id!!))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("RecycledJob", "安卓11 deleteBatchMedia: ")
            //安卓11批量删除 需要权限
            val editPendingIntent = MediaStore.createDeleteRequest(contentResolver,uriList)
            if (context is Activity) {
                context.startIntentSenderForResult(editPendingIntent.intentSender,
                    GalleryConstants.REQUEST_CODE_SECURITY,
                    null,
                    0,
                    0,
                    0,
                    null)
            }
        }else{
            Log.d("RecycledJob", "安卓11以下 deleteBatchMedia: ${uriList}")
            list.forEach { bean ->
                try {
                    contentResolver.delete(ContentUris.withAppendedId(getUri(), bean.id!!), null, null)
                }catch (e:Exception){
                    if (isAboveQ() && e is RecoverableSecurityException) {
                        if (context is Activity) {
                            val bundle = Bundle()
                            bundle.putString("uri", bean.originalPath)
                            context.startIntentSenderForResult(
                                e.userAction.actionIntent.intentSender,
                                GalleryConstants.REQUEST_CODE_SECURITY,
                                null,
                                0,
                                0,
                                0,
                                bundle
                            )
                            block?.invoke(GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION)
                        }
                    }
                }
            }
            block?.invoke(GalleryConstants.RemoveStatus.REMOVED)
        }


    }

    override fun insertMedia(context: Context, path: String): Boolean {
        val contentResolver = context.contentResolver
        val file =
            File(Utils.getDCIMPath() + "/" + Utils.getName(path))
        Utils.copyFile(path, file.absolutePath)
        val contentValues = getImageContentValues(
            context,
            file,
            System.currentTimeMillis()
        )
        contentResolver.insert(getUri(), contentValues)
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.path),
            arrayOf("image/jpeg")
        ) { _, _ -> }
        return true
    }

    private fun getImageContentValues(
        paramContext: Context?,
        paramFile: File,
        paramLong: Long
    ): ContentValues {
        val localContentValues = ContentValues()
        localContentValues.put("title", paramFile.name)
        localContentValues.put("_display_name", paramFile.name)
        localContentValues.put("mime_type", "image/jpeg")
        localContentValues.put("datetaken", paramLong)
        localContentValues.put("date_modified", paramLong)
        localContentValues.put("date_added", paramLong)
        localContentValues.put("orientation", Integer.valueOf(0))
        localContentValues.put("_data", paramFile.absolutePath)
        localContentValues.put("_size", paramFile.length())
        return localContentValues
    }

    override fun getBuckets(context: Context): MutableList<BucketBean>? {
        return null
    }

    private fun getUri(): Uri {
        return if (isAboveQ()) MediaStore.Images.Media.getContentUri(
            MediaStore.VOLUME_EXTERNAL
        ) else MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    private fun isAboveQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}