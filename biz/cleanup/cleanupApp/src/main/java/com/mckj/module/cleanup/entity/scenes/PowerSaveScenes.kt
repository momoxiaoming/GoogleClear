package com.mckj.module.cleanup.entity.scenes

import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.RecommendData
import com.mckj.sceneslib.manager.scenes.ScenesType

class PowerSaveScenes : AbsHomeRecommendScenes() {
    private val appNumber = (3..5).random().toString()
    override fun initScenesData(): RecommendData {
        return RecommendData(
            ScenesType.TYPE_POWER_SAVE,
            ResourceUtil.getString(R.string.scenes_battery_saver),
            desc = String.format(
                ResourceUtil.getString(R.string.cleanup_home_recomend_power_save),
                appNumber
            ),
            ResourceUtil.getString(R.string.cleanup_home_recomend_power_save_btn_two),
            title = TextUtils.string2SpannableStringForColor(
                String.format(
                    ResourceUtil.getString(R.string.cleanup_home_recomend_power_save_title_one),
                    appNumber
                ),
                appNumber + ResourceUtil.getString(R.string.cleanup_home_recomend_power_save_unit),
                color = Color.parseColor("#FF2626"), isBold = false
            )
        )
    }

    override fun getIconResId(): Int {
        return R.drawable.img_home_guide_prompt_power_saving
    }
}