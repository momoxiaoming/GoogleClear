package com.mckj.module.manager

import androidx.lifecycle.MutableLiveData
import com.mckj.api.entity.JunkInfo


/**
 * @author xx
 * @version 1
 * @createTime 2021/9/14 14:32
 * @desc
 */
class CleanManager {
    companion object {
        const val Tag = "CleanManager"
        val instance by lazy(LazyThreadSafetyMode.NONE) {
            CleanManager()
        }
    }

    val mSelectedLiveData = MutableLiveData<List<JunkInfo>>()//回收资源被选中列表
    val mSelectedList = ArrayList<JunkInfo>()


    fun selectedJunk(bean: JunkInfo) {
        if (!mSelectedList.contains(bean)) {
            mSelectedList.add(bean)
        } else {
            mSelectedList.remove(bean)
        }
        mSelectedLiveData.postValue(mSelectedList)
    }


    fun removeJunk(bean: JunkInfo) {
        if (mSelectedList.contains(bean)) {
            mSelectedList.remove(bean)
        }
        mSelectedLiveData.postValue(mSelectedList)
    }

    fun selectedAllJunk(beans: List<JunkInfo>, isCleared: Boolean) {
        if (isCleared) {
            mSelectedList.removeAll(beans)
        }
        for (bean in beans) {
            if (!mSelectedList.contains(bean)) {
                mSelectedList.add(bean)
            }
        }
        mSelectedLiveData.postValue(mSelectedList)
    }

    fun clearSelectedData() {
        mSelectedList.clear()
        mSelectedLiveData.postValue(mSelectedList)
    }

    fun clearSelectedData(list: MutableList<JunkInfo>) {
        mSelectedList.removeAll(list)
        mSelectedLiveData.postValue(mSelectedList)
    }
}