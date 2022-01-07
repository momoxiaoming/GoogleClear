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
 * Created By yangb on 2021/3/24
 */
class SingleSpeedScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_SIGNAL_SPEED,
            "single_speed",
            idToString(R.string.scenes_xhyh),
            idToString(R.string.scenes_ts50),
            "",
            idToString(R.string.scenes_now_yh),
            idToString(R.string.scenes_now_yh_finish),
            idToString(R.string.scenes_yh_ok),
            "home_signal_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_signal_speed

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_PHONE_SPEED)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(idToString(R.string.scenes_net_need_yh), idToString(R.string.scenes_net_need_yh2))
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/signal_speed/images",
            "scenes/lottieFiles/signal_speed/data.json",
            null,
            LottieFrame(0, 35),
            null
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_check_wifi_state)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_check_wifi_state2)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_check_wifi_state3)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_check_wifi_state4), "", taskList)
    }
}