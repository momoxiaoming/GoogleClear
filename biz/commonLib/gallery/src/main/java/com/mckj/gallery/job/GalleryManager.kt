package com.mckj.gallery.job

import androidx.lifecycle.MutableLiveData
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.job.recycled.CleanRecycledDataJodCreate


/**
 * @author xx
 * @version 1
 * @createTime 2021/7/26 14:32
 * @desc
 */
class GalleryManager {
    companion object {
        const val Tag = "GalleryManager"
        val instance by lazy(LazyThreadSafetyMode.NONE) {
            GalleryManager()
        }
    }

    val mSelectedData = MutableLiveData<List<RecycledBean>>()//回收资源被选中列表

    fun checkChangeRecycledBean(bean: RecycledBean) {
        val list = mutableListOf<RecycledBean>()
        val data = mSelectedData.value
        data?.apply {
            list.addAll(this)
        }
        if (!list.contains(bean)) {
            list.add(bean)
        } else {
            list.remove(bean)
        }
        mSelectedData.postValue(list)
    }


    fun removeRecycledBean(bean: RecycledBean) {
        val list = mutableListOf<RecycledBean>()
        val data = mSelectedData.value
        data?.apply {
            list.addAll(this)
        }
        if (list.contains(bean)) {
            list.remove(bean)
        }
        mSelectedData.postValue(list)
    }

    fun selectedAllRecycledBean(beans: List<RecycledBean>, isCleared: Boolean) {
        val list = mutableListOf<RecycledBean>()
        val data = mSelectedData.value
        data?.apply {
            list.addAll(this)
        }
        if (isCleared) {
            list.removeAll(beans)
        } else {
            for (bean in beans) {
                if (!list.contains(bean)) {
                    list.add(bean)
                }
            }
        }
        mSelectedData.postValue(list)
    }

    fun clearSelectedData() {
        mSelectedData.postValue(mutableListOf())
    }

    fun checkRecycledData() {
        //检查回收站的资源
        JobChain.newInstance().addJob(CleanRecycledDataJodCreate().create())
    }
}