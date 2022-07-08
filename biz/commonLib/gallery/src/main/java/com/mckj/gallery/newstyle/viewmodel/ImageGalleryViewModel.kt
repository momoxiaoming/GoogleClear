package com.mckj.gallery.newstyle.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.gallery.bean.MediaBean
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.newstyle.local.LocalDataSource
import com.mckj.gallery.viewmodel.GalleryRepository
import kotlinx.coroutines.launch

class ImageGalleryViewModel(private val repository: GalleryRepository) : AbstractViewModel() {

    val mediaData = repository.mediaData
    val totalData = MutableLiveData<Long>()

    val localRepository by lazy {
        LocalDataSource
    }

    init {
        totalData.postValue(0)
    }

    fun getMediaData(
        context: Context,
        bucketId: String = Int.MIN_VALUE.toString(),
        page: Int = 1,
        limit: Int = Int.MAX_VALUE,
        requestType: String = MediaConstant.MEDIA_TYPE_ALL
    ) {

        checkLocal({
            getMediaDataById(context,it.id.toString())
        }) {
            viewModelScope.launch {
                repository.getMediaData(context, bucketId, page, limit, requestType)
            }
        }
    }

    fun getMediaDataById(context: Context,id:String){
        viewModelScope.launch {
            repository.getMediaDataById(context,id)
        }
    }

    private fun checkLocal(result:(media:MediaBean)->Unit,queryAll:()->Unit){
        var data = localRepository.getGalleryHis()
        if (data?.id != null){
            result(data)
        }else{
            queryAll()
        }
    }

    /**
     * 保存清理图片信息
     */
    fun saveLocalGalleryData(media: MediaBean){
        localRepository.saveGalleryHis(media)
    }

    fun cleanLocalGallery() = localRepository.deleteGallery()

    fun cleanTotalStorage(count:Long){
        var total = totalData.value!!.plus(count)
        totalData.postValue(total)
    }

    fun setTotalStorage(count:Long){
        totalData.postValue(count)
    }

    fun saveCleanSize(size:Long){
        var oldSize = getCleanSize()
        localRepository.saveCleanSize(size + oldSize)
    }

    fun getCleanSize() = localRepository.getCleanSize()

    class GalleryViewModelFactory(private val repository: GalleryRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ImageGalleryViewModel(repository) as T
        }
    }
}