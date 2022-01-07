package com.mckj.sceneslib.ui.scenes.model.cooldown

import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.entity.ScenesTask
import com.mckj.sceneslib.entity.ScenesTaskData
import com.dn.baselib.base.databinding.AbstractViewModel
import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class CoolDownViewModel : AbstractViewModel() {

    companion object {
        const val TAG = "CoolDownViewModel"
    }

    /**
     * 温度
     */
    val mTemperatureLiveData = MutableLiveData<Int>()

    init {
        mTemperatureLiveData.value = (24..48).random()
    }

    fun getTaskData(): ScenesTaskData? {
        val temperature = mTemperatureLiveData.value ?: 0
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_scanning_sjyj)) {
            delay(700L)
            mTemperatureLiveData.value = (temperature * 0.95f).toInt()
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_close_hot_app)) {
            delay(700L)
            mTemperatureLiveData.value = (temperature * 0.90f).toInt()
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_temp_jw)) {
            delay(700L)
            mTemperatureLiveData.value = (temperature * 0.85f).toInt()
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_sjjw_ing), "", taskList)
    }

}