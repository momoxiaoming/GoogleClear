package com.mckj.sceneslib.manager.scenes.model

import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:红包赚钱
 *
 * Created By yangb on 2021/3/24
 */
class GameSpeedScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_GAME_SPEED,
            "game_speed",
            idToString(R.string.scenes_game_speed),
            "",
            "",
            idToString(R.string.scenes_now_Speed),
            idToString(R.string.scenes_speed_ok),
            idToString(R.string.scenes_yh_zj),
        )
    }

}