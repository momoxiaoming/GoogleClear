package com.mckj.gallery.viewmodel

import androidx.lifecycle.LiveData
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.datebase.dao.GalleryDao
import com.mckj.gallery.datebase.entity.RecycledBean

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/23 15:39
 * @desc 数据交互层仓库
 */
class RecycleRepository(private val mDao: GalleryDao) {

    val allData: LiveData<List<RecycledBean>> = mDao.getRecycledLiveData()

    val imgData: LiveData<List<RecycledBean>> =

        mDao.getRecycledDataWithMimeType(MediaConstant.MEDIA_TYPE_IMG)

    val videoData: LiveData<List<RecycledBean>> =
        mDao.getRecycledDataWithMimeType(MediaConstant.MEDIA_TYPE_VIDEO)

  fun insert(bean: RecycledBean): Boolean {
        val insert = mDao.insert(bean)
        return insert > 0
    }

    suspend fun deleteAll() {
        mDao.deleteAll()
    }

     fun deleteById(bean: RecycledBean) {
        mDao.deleteById(bean.id)
    }

    fun queryRecycledData(): List<RecycledBean> {
        return mDao.getRecycledData()
    }

    //单纯触发请求
    fun queryRecycledData(mimeType: String) {
        mDao.getRecycledDataWithMimeType(mimeType)
    }
}