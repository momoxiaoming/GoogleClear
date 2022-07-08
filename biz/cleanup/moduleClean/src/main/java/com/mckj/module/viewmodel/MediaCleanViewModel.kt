package com.mckj.module.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.api.entity.AppJunk
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.module.bean.QAuthorBean

class MediaCleanViewModel : AbstractViewModel() {

    var mAppJunk: AppJunk? = null
    var mSortType = MutableLiveData<Int>()

    var mAppJunkLiveData = MutableLiveData<AppJunk>()

    var mType: Int = 0//qq,wx(埋点区分)

    var mActivityResultLiveData = MutableLiveData<QAuthorBean>()

    class MediaCleanViewModelFactory() :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return MediaCleanViewModel() as T
        }
    }

    fun postActivityResult(qAuthorBean: QAuthorBean) {
        mActivityResultLiveData.postValue(qAuthorBean)
    }

}