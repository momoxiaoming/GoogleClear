package com.mckj.gallery.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mckj.gallery.bean.MediaBean
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.gallery.bean.MediaConstant
import kotlinx.coroutines.launch

/**
 *@author xx
 *@version 1
 *@createTime 2021/7/20
 *@desc
 */
class GalleryViewModel(private val repository: GalleryRepository) : AbstractViewModel() {

    companion object {
        const val Tag = "GalleryViewModel"
    }

    val mImgLiveData = repository.mImgLiveData
    val mVideoLiveData = repository.mVideoLiveData
    val mediaData = repository.mediaData
    val mBucketData = repository.mBucketData

    fun getImgList(
        context: Context,
        bucketId: String = "",
        page: Int = 1,
        limit: Int = Int.MAX_VALUE
    ) {
        viewModelScope.launch {
            repository.getImgList(context, bucketId, page, limit)
        }
    }

    fun getVideoList(
        context: Context,
        bucketId: String = "",
        page: Int = 1,
        limit: Int = Int.MAX_VALUE
    ) {
        viewModelScope.launch {
            repository.getVideoList(context, bucketId, page, limit)
        }
    }

    fun getMediaData(
        context: Context,
        bucketId: String = Int.MIN_VALUE.toString(),
        page: Int = 1,
        limit: Int = Int.MAX_VALUE,
        requestType: String = MediaConstant.MEDIA_TYPE_ALL
    ) {
        viewModelScope.launch {
            repository.getMediaData(context, bucketId, page, limit, requestType)
        }
    }

    fun getBuckets(context: Context) {
        viewModelScope.launch {
            repository.getBuckets(context)
        }
    }

    fun useOutSideData(list: MutableList<MediaBean>) {
        mediaData.postValue(list)
    }


    class GalleryViewModelFactory(private val repository: GalleryRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return GalleryViewModel(repository) as T
        }

    }

}