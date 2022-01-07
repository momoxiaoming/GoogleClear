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
class NetworkSpeedScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_NETWORK_SPEED,
            "network_speed",
            idToString(R.string.scenes_net_speed),
            idToString(R.string.scenes_net_c),
            "",
            idToString(R.string.scenes_now_yh),
            idToString(R.string.scenes_now_yh_finish),
            "${idToString(R.string.scenes_wljs)}${(10..20).random()}%",
            "home_network_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_network_speed

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_SIGNAL_SPEED, ScenesType.TYPE_PHONE_SPEED)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(idToString(R.string.scenes_net_need_yh), idToString(R.string.scenes_net_fxxl))
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/network_speed/images",
            "scenes/lottieFiles/network_speed/data.json",
            LottieFrame(0, 40),
            LottieFrame(40, 120),
            LottieFrame(120, 151)
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_yhlyq)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_czsw)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_yhwlpz)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_net_speed_ing), "", taskList)
    }

}