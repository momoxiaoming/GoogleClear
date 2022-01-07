package com.mckj.sceneslib.ui.scenes.model.junk

import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.entity.ScenesTask
import com.mckj.sceneslib.entity.ScenesTaskData
import com.mckj.sceneslib.manager.AutoScanManager
import com.mckj.cleancore.entity.IJunkEntity
import com.mckj.cleancore.manager.junk.JunkManager
import com.mckj.cleancore.tools.JunkType
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class JunkViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "JunkViewModel"
    }

    /**
     * 垃圾总大小
     */
    private var mTotalSize = 0L

    /**
     * 垃圾大小
     */
    val mJunkSizeLiveData = MutableLiveData<Long>()

    var junkList: List<IJunkEntity>? = null

    /**
     * 累计垃圾大小
     * @param list List<IJunkEntity>?
     */
    fun initJunkData(list: List<IJunkEntity>?) {
        junkList = list
        mTotalSize = 0L
        junkList?.forEach {
            mTotalSize += it.getJunkSize()
        }
        mJunkSizeLiveData.value = mTotalSize
    }

    fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_app_hcql)) {
            //应用缓存
            val appCacheList = getJunkList(JunkType.APP_CACHE)
            JunkManager.getInstance().clean(appCacheList) { entity ->
                removeJunkEntity(entity)
            }
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_ad_hcql)) {
            //广告垃圾
            val adCacheList = getJunkList(JunkType.AD_CACHE)
            JunkManager.getInstance().clean(adCacheList) { entity ->
                removeJunkEntity(entity)
            }
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_ljql)) {
            //残留垃圾
            val residueList = getJunkList(JunkType.UNINSTALL_RESIDUE)
            JunkManager.getInstance().clean(residueList) { entity ->
                removeJunkEntity(entity)
            }
            delay(700L)
            AutoScanManager.getInstance().resetData()
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_clean_junk_ing), "", taskList)
    }

    private fun getJunkList(type: Int): List<IJunkEntity> {
        val list = mutableListOf<IJunkEntity>()
        junkList?.forEach { entity ->
            if (type and entity.getJunkType() != 0) {
                list.add(entity)
            }
        }
        return list
    }

    /**
     * 移除清理扫描数据
     */
    private fun removeJunkEntity(IJunkEntity: IJunkEntity) {
        var value = mJunkSizeLiveData.value ?: 0
        value -= IJunkEntity.getJunkSize()
        if (value < 0) {
            value = 0
        }
        mJunkSizeLiveData.postValue(value)
    }

}