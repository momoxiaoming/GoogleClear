package com.mckj.sceneslib.ui.scenes.model.cooldown

import androidx.lifecycle.MutableLiveData
import com.mckj.sceneslib.entity.ScenesTask
import com.mckj.sceneslib.entity.ScenesTaskData
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.util.ResourceUtil
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

    private val TASK_TIME_MAX = 8


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
        val startTime = System.currentTimeMillis()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_scanning_phone_hardware)) {
            delay(700L)
            mTemperatureLiveData.value = (temperature * 0.95f).toInt()
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_close_apps_cause_heat)) {
            delay(700L)
            mTemperatureLiveData.value = (temperature * 0.90f).toInt()
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_phone_starts_cool_down)) {
            delay(700L)
            mTemperatureLiveData.value = (temperature * 0.85f).toInt()
            return@ScenesTask true
        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_phone_cooling), "", taskList)
    }

}