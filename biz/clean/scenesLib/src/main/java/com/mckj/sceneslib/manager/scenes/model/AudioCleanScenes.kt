package com.mckj.sceneslib.manager.scenes.model

import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By yangb on 2021/3/24
 */
class AudioCleanScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_AUDIO_CLEAN,
            "audio_clean",
            "音乐清理",
            "清理不喜欢的音乐",
            "",
            "立即清理",
            "清理完成",
            "已优化到最佳",
            "home_music_click"
        )
    }

    override fun isRequestPermission(): Boolean {
        return true
    }
}