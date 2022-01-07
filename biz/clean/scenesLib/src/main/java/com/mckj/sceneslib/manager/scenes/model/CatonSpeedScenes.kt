package com.mckj.sceneslib.manager.scenes.model

import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.cleancore.manager.junk.JunkManager
import com.mckj.cleancore.tools.JunkType
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class CatonSpeedScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_CATON_SPEED,
            "caton_speed",
            idToString(R.string.scenes_kd_yh),
            idToString(R.string.scenes_phone_perfor),
            "",
            idToString(R.string.scenes_clean_now),
            idToString(R.string.scenes_clean_finish),
            idToString(R.string.scenes_phone_yh_ok2),
            "home_caton_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_caton_speed

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_ANTIVIRUS)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(idToString(R.string.scenes_back_app), idToString(R.string.scenes_phone_xn))
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/caton_speed/images",
            "scenes/lottieFiles/caton_speed/data.json",
            LottieFrame(0, 40),
            LottieFrame(40, 80),
            LottieFrame(80, 94)
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_yhsjcl)) {
            JunkManager.getInstance().scanAndClean(JunkType.UNINSTALL_RESIDUE)
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_phone_ram_ing)) {
            JunkManager.getInstance().scanAndClean(JunkType.APP_CACHE)
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_close_bat_app_ing)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_km_ing), "", taskList)
    }

}