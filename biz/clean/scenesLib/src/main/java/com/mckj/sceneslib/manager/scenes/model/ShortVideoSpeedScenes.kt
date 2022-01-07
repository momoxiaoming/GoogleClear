package com.mckj.sceneslib.manager.scenes.model

import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class ShortVideoSpeedScenes : ShortVideoCleanScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_SHORT_VIDEO_SPEED,
            "short_video_speed",
            idToString(R.string.scenes_video_speed),
            idToString(R.string.scenes_video_junk),
            "",
            idToString(R.string.scenes_now_Speed),
            idToString(R.string.scenes_speed_finish),
            idToString(R.string.scenes_junk_yh_ok),
            "home_smallvideo_click"
        )
    }
}