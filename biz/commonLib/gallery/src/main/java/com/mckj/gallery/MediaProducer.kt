package com.mckj.gallery

import android.content.Context
import com.mckj.gallery.bean.BucketBean
import com.mckj.gallery.bean.MediaBean
import com.mckj.gallery.impl.PictureBucketImpl
import com.mckj.gallery.impl.PictureImpl
import com.mckj.gallery.impl.VideoBucketImpl
import com.mckj.gallery.impl.VideoImpl

/**
 *@author leix
 *@version 1
 *@createTime 2021/7/19 12:22
 *@desc
 */
class MediaProducer {

    companion object {
        const val Tag = "MediaProducer"
        val instance by lazy(LazyThreadSafetyMode.NONE) {
            MediaProducer()
        }
    }

    private var pictureImpl: PictureImpl = PictureImpl()
    private var videoImpl: VideoImpl = VideoImpl()
    private var picBucketImpl: PictureBucketImpl = PictureBucketImpl()
    private var videoBucketImpl: VideoBucketImpl = VideoBucketImpl()

    /**
     * 获取图库图片
     * @param bucketId 文件夹ID，默认全部
     * @param page 当前查询页码,默认1
     * @param limit 每页请求个数，默认一页拉取24个
     */
    fun getImgList(
        context: Context, bucketId: String = "",
        page: Int = 1,
        limit: Int = Int.MAX_VALUE,
    ): MutableList<MediaBean>? {
        return pictureImpl.getMediaList(context, bucketId, page, limit)
    }

    /**
     * 获取id之后的图片数据
     * @param id 图片的id
     */
    fun getImgListById(context: Context,id:String): MutableList<MediaBean>?{
        return pictureImpl.getMediaListById(context,id)
    }

    /**
     * 获取图库视频
     * @param bucketId 文件夹ID，默认全部
     * @param page 当前查询页码,默认1
     * @param limit 每页请求个数，默认一页拉取24个
     */
    suspend fun getVideoList(
        context: Context, bucketId: String = "",
        page: Int = 1,
        limit: Int = 24,
    ): MutableList<MediaBean>? {
        return videoImpl.getMediaList(context, bucketId, page, limit)
    }

    /**
     * 获取相册的全部文件夹
     */
    suspend fun getBuckets(context: Context): MutableList<BucketBean>? {
        val picBuckets = picBucketImpl.getBuckets(context)
        val videoBuckets = videoBucketImpl.getBuckets(context)
        val list = mutableListOf<BucketBean>()
        picBuckets?.let {
            list.addAll(it)
        }
        videoBuckets?.let {
            list.addAll(it)
        }
        return list
    }


    suspend fun deleteNum(context: Context, mediaBean: MediaBean): Int {
        if (mediaBean.isImage())
            return pictureImpl.deleteMedia(context, mediaBean)
        if (mediaBean.isVideo())
            return videoImpl.deleteMedia(context, mediaBean)
        return 0
    }

}