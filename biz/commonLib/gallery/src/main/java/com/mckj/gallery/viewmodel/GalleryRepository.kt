package com.mckj.gallery.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.mckj.gallery.bean.BucketBean
import com.mckj.gallery.bean.MediaBean
import com.mckj.baselib.util.Log
import com.mckj.gallery.MediaProducer
import com.mckj.gallery.bean.MediaConstant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * @author xx
 * @version 1
 * @createTime 2021/7/23 15:39
 * @desc 图库数据交互层仓库
 */
class GalleryRepository() {

    val mImgLiveData = MutableLiveData<MutableList<MediaBean>>()
    val mVideoLiveData = MutableLiveData<MutableList<MediaBean>>()
    val mediaData = MutableLiveData<MutableList<MediaBean>>()
    val mBucketData = MutableLiveData<MutableList<BucketBean>>()

    suspend fun getImgList(
        context: Context,
        bucketId: String = "",
        page: Int = 1,
        limit: Int = Int.MAX_VALUE
    ) {
        val list = mutableListOf<MediaBean>()
        withContext(Dispatchers.IO) {
            val imgList = MediaProducer.instance.getImgList(context, bucketId, page, limit)
            // TODO: 2021/7/20 处理分页数据
            imgList?.let {
                list.addAll(imgList)
            }
        }
        mImgLiveData.postValue(list)

    }

    suspend fun getVideoList(
        context: Context,
        bucketId: String = "",
        page: Int = 1,
        limit: Int = Int.MAX_VALUE
    ) {
        val list = mutableListOf<MediaBean>()
        withContext(Dispatchers.IO) {
            val videoList = MediaProducer.instance.getVideoList(context, bucketId, page, limit)
            videoList?.let {
                list.addAll(videoList)
            }
        }
        mVideoLiveData.postValue(list)
    }

    suspend fun getMediaData(
        context: Context,
        bucketId: String = Int.MIN_VALUE.toString(),
        page: Int = 1,
        limit: Int = Int.MAX_VALUE,
        requestType: String = MediaConstant.MEDIA_TYPE_ALL
    ) {
        val list = mutableListOf<MediaBean>()
        withContext(Dispatchers.IO) {
            when (requestType) {
                MediaConstant.MEDIA_TYPE_ALL -> {
                    val imgList =
                        MediaProducer.instance.getImgList(context, bucketId, page, limit)
                    val videoList =
                        MediaProducer.instance.getVideoList(context, bucketId, page, limit)
                    imgList?.let {
                        list.addAll(imgList)
                    }
                    videoList?.let {
                        list.addAll(videoList)
                    }
                }
                MediaConstant.MEDIA_TYPE_IMG -> {
                    val imgList =
                        MediaProducer.instance.getImgList(context, bucketId, page, limit)
                    imgList?.let {
                        list.addAll(imgList)
                    }
                }
                MediaConstant.MEDIA_TYPE_VIDEO -> {
                    val videoList =
                        MediaProducer.instance.getVideoList(context, bucketId, page, limit)
                    videoList?.let {
                        list.addAll(videoList)
                    }
                }

                else -> {
                    Log.d(GalleryViewModel.Tag, "getMediaData error  undefine requestType")
                }
            }
        }
        mediaData.postValue(list)
    }

    suspend fun getMediaDataById(context: Context,id:String){
        val list = mutableListOf<MediaBean>()
        var dataList = MediaProducer.instance.getImgListById(context,id)
        if (dataList != null){
            list.addAll(dataList)
            mediaData.postValue(list)
        }else{
            getMediaData(context)
        }
    }

    suspend fun getBuckets(context: Context) {
        val list = mutableListOf<BucketBean>()
        withContext(Dispatchers.IO) {
            val buckets = MediaProducer.instance.getBuckets(context)
            buckets?.let {
                list.addAll(buckets)
            }
        }
        mBucketData.postValue(list)
    }


    suspend fun deleteMedia(context: Context, bean: MediaBean): Int {
        return MediaProducer.instance.deleteNum(context, bean)
    }

    fun insertMedia(context: Context, bean: MediaBean) {

    }
}