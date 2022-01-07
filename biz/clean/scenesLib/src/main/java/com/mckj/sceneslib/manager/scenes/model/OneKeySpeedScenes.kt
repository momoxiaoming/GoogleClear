package com.mckj.sceneslib.manager.scenes.model

import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By yangb on 2021/3/24
 */
class OneKeySpeedScenes : PhoneSpeedScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_ONE_KEY_SPEED,
            "one_key_speed",
            idToString(R.string.scenes_yjjs),
            idToString(R.string.scenes_phone_now_kj),
            idToString(R.string.scenes_ncbz),
            idToString(R.string.scenes_now_Speed),
            idToString(R.string.scenes_speed_finish),
            idToString(R.string.scenes_yh_zj),
            "home_wifispeedup_click"
        )
    }

}