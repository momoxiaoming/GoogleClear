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
class AntivirusScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_ANTIVIRUS,
            "antivirus",
            idToString(R.string.scenes_phone_sd),
            idToString(R.string.scenes_check_phone_aq),
            "",
            idToString(R.string.scenes_sd_now),
            idToString(R.string.scenes_sd_finish),
            idToString(R.string.scenes_phone_safe),
            "home_antivirus_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_antivirus

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_SHORT_VIDEO_CLEAN)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(idToString(R.string.scenes_phone_no_safe), idToString(R.string.scenes_phone_no_safe_2))
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/antivirus/images",
            "scenes/lottieFiles/antivirus/data.json",
            null,
            LottieFrame(0, 63),
            LottieFrame(63, 93)
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_check_ys)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_check_bd_app)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_check_wl)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_bd_cs), "", taskList)
    }

}