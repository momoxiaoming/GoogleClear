package com.mckj.module.cleanup.entity.scenes

import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.RecommendData
import com.mckj.sceneslib.manager.scenes.ScenesType

class NetWorkSpeedScene: AbsHomeRecommendScenes() {
    private val appNumber = (30..50).random().toString()
    override fun initScenesData(): RecommendData {
        return RecommendData(
            ScenesType.TYPE_NETWORK_SPEED,
            ResourceUtil.getString(R.string.scenes_network_acceleration),
            desc = ResourceUtil.getString(R.string.cleanup_home_recomend_net_speed),
            ResourceUtil.getString(R.string.cleanup_home_recomend_net_speed_btn),
            title = TextUtils.string2SpannableStringForColor(
                String.format(
                    ResourceUtil.getString(R.string.cleanup_home_recomend_net_speed_title_one),
                    appNumber
                ),
                "${appNumber}kb/s",
                color = Color.parseColor("#FF2626"), isBold = false
            ),
        )
    }

    override fun getIconResId(): Int {
        return R.drawable.img_home_guide_network_expedite
    }
}