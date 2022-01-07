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
class CameraCheckScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_CAMERA_CHECK,
            "camera",
            "${idToString(R.string.scenes_camera_check)}",
            "${idToString(R.string.scenes_camera_found)}",
            "",
            "${idToString(R.string.scenes_check)}",
            "${idToString(R.string.scenes_check_finish)}",
            "${idToString(R.string.scenes_check_no)}",
            "home_camera_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_camera_check

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_UNINSTALL_CLEAN)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf("${idToString(R.string.scenes_check_net)}", "${idToString(R.string.scenes_check_net_err)}")
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/camera_check/images",
            "scenes/lottieFiles/camera_check/data.json",
            null,
            LottieFrame(0, 35),
            null
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_check_now_net)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_check_net_err_now)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_check_camera_now)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_camer_checking), "", taskList)
    }

}