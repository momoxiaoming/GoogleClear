package com.mckj.gallery.viewmodel

import androidx.lifecycle.*
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.datebase.entity.RecycledBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * @author leix
 * @version 1
 * @createTime 2021/7/23 15:13
 * @desc 回收站
 */
class RecycledViewModel(private val repository: RecycleRepository) : AbstractViewModel() {
    //所有被回收的资源
    val mRecycledData = repository.allData
    val mRecycleImgData = repository.imgData
    val mRecycledVideoData = repository.videoData

    fun insert(bean: RecycledBean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(bean)
        }
    }

    fun deleteAll(bean: RecycledBean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    suspend fun deleteById(bean: RecycledBean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteById(bean)
        }
    }

    fun getRecycledMimeTypeData(mimeType: String = MediaConstant.MEDIA_TYPE_IMG): LiveData<List<RecycledBean>> {
        return when (mimeType) {
            MediaConstant.MEDIA_TYPE_VIDEO -> mRecycledVideoData
            else -> mRecycleImgData
        }
    }

    class RecycledViewModelFactory(private val repository: RecycleRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return RecycledViewModel(repository) as T
        }
    }
}