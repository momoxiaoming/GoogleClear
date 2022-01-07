package com.mckj.sceneslib.manager.scenes.model

import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
open class PowerSaveScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_POWER_SAVE,
            "power_save",
            idToString(R.string.scenes_super_bat),
            idToString(R.string.scenes_clean_bat_app),
            "",
            idToString(R.string.scenes_now_yh),
            idToString(R.string.scenes_now_yh_finish),
            idToString(R.string.scenes_close_bat_app),
            "home_savepower_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_power_speed

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(idToString(R.string.scenes_back_app), idToString(R.string.scenes_hdgk))
    }

    override fun getGuideTypes(): List<Int>? {
        return  arrayListOf(ScenesType.TYPE_ANTIVIRUS)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/power_save/images",
            "scenes/lottieFiles/power_save/data.json",
            null,
            LottieFrame(0, 68),
            LottieFrame(68, 114)
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_now_fx_bat)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_close_bat_ing)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_close_bat_app_ing)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_bat_yh), "", taskList)
    }

}