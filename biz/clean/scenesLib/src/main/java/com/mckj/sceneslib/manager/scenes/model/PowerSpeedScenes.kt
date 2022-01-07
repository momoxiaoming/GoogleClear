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
class PowerSpeedScenes : PowerSaveScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_POWER_SPEED,
            "power_speed",
            idToString(R.string.scenes_sdjs),
            idToString(R.string.scenes_yhhdyy),
            "",
            idToString(R.string.scenes_now_Speed),
            idToString(R.string.scenes_speed_finish),
            idToString(R.string.scenes_yh_zj),
            "home_savepower_click"
        )
    }

}