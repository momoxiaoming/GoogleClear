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
open class ShortVideoCleanScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_SHORT_VIDEO_CLEAN,
            "short_video_clean",
            idToString(R.string.scenes_video_clean),
            idToString(R.string.scenes_video_junk),
            "",
            idToString(R.string.scenes_clean_now),
            idToString(R.string.scenes_clean_finish),
            idToString(R.string.scenes_junk_clean_ok),
            "home_smallvideo_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_short_video

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(idToString(R.string.scenes_video_file_many), idToString(R.string.scenes_video_file_many2))
    }

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_CAMERA_CHECK, ScenesType.TYPE_QQ_CLEAN)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/short_video/images",
            "scenes/lottieFiles/short_video/data.json",
            null,
            LottieFrame(0, 100),
            LottieFrame(100, 151)
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(idToString(R.string.scenes_short_video_desc1)) {
            JunkManager.getInstance().scanAndClean(JunkType.AUTO_SCAN)
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_short_video_desc2)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(idToString(R.string.scenes_short_video_desc3)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_short_video_desc4), "", taskList)
    }

    override fun isRequestPermission(): Boolean {
        return true
    }
}