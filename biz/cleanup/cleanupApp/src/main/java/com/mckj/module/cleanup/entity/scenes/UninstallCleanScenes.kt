package com.mckj.module.cleanup.entity.scenes

import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.RecommendData
import com.mckj.sceneslib.manager.scenes.ScenesType

class UninstallCleanScenes: AbsHomeRecommendScenes() {
    val number = (488..588).random().toString()
    override fun initScenesData(): RecommendData {
        return RecommendData(
            ScenesType.TYPE_UNINSTALL_CLEAN,
            ResourceUtil.getString(R.string.scenes_unstall_clean),
            String.format(
                ResourceUtil.getString(R.string.cleanup_home_recomend_uninstall_clean),
                number
            ),
            ResourceUtil.getString(R.string.cleanup_home_recomend_uninstall_clean_btn),
            title = TextUtils.string2SpannableStringForColor(
                String.format(
                    ResourceUtil.getString(R.string.cleanup_home_recomend_uninstall_clean_title_one),
                    number
                ),
                number + "MB",
                color = Color.parseColor("#FF2626"), isBold = false
            ),
        )
    }

    override fun getIconResId(): Int {
        return R.drawable.img_home_guide_prompt_vestigital
    }
}