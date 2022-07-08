package com.mckj.module.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.module.bean.GroupJunkInfo
import com.mckj.module.bean.UIDealData

/**
 * @author leix
 * @version 1
 * @createTime 2021/8/31 11:38
 * @desc
 */
class TencentCleanViewModel() : AbstractViewModel() {

    companion object {
        const val Tag = "TencentCleanViewModel"
    }

    var mUiDealDataLiveData = MutableLiveData<UIDealData>()

    var mUiDealData = UIDealData()

    /**
     * 更新选中的垃圾大小
     */
    fun updateSelectedJunkSize(isSelected: Boolean, appJunk: GroupJunkInfo) {
        if (isSelected) {
            mUiDealData.selectedSize += appJunk.junkSize
            if (mUiDealData.selectedJunkList?.contains(appJunk) == true) {
                mUiDealData.selectedJunkList?.remove(appJunk)
            }
            mUiDealData.selectedJunkList?.add(appJunk)
        } else {
            mUiDealData.selectedSize -= appJunk.junkSize
            if (mUiDealData.selectedJunkList?.contains(appJunk) == true) {
                mUiDealData.selectedJunkList?.remove(appJunk)
            }
        }
        mUiDealDataLiveData.postValue(mUiDealData)
    }

    fun updateCacheSize(groupJunks: ArrayList<GroupJunkInfo>) {
        mUiDealData.selectedJunkList = mutableListOf()
        mUiDealData.totalSize = 0L
        mUiDealData.selectedSize = 0L
        groupJunks.forEach {
            mUiDealData.totalSize += it.junkSize
            mUiDealData.selectedSize += it.junkSize
        }
        mUiDealData.selectedJunkList?.clear()
        mUiDealData.selectedJunkList?.addAll(groupJunks)
        mUiDealDataLiveData.postValue(mUiDealData)
    }


    class TencentCleanViewModelFactory() :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return TencentCleanViewModel() as T
        }
    }

}