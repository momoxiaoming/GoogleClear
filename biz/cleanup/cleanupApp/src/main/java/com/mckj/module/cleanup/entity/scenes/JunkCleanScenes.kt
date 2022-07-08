package com.mckj.module.cleanup.entity.scenes

import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.RecommendData
import com.mckj.sceneslib.manager.scenes.ScenesType

class JunkCleanScenes : AbsHomeRecommendScenes() {
    val number = (2..7).random().toString()
    override fun initScenesData(): RecommendData {
        return RecommendData(
            ScenesType.TYPE_JUNK_CLEAN,
            ResourceUtil.getString(R.string.scenes_junk_clean),
            desc = String.format(
                ResourceUtil.getString(R.string.cleanup_home_recomend_junk_clean),
                number
            ),
            ResourceUtil.getString(R.string.cleanup_home_recomend_junk_clean_btn),
            title = TextUtils.string2SpannableStringForColor(
                String.format(
                    ResourceUtil.getString(R.string.cleanup_home_recomend_junk_clean_title_one),
                    number
                ),
                number + ResourceUtil.getString(R.string.cleanup_home_recomend_junk_clean_unit),
                color = Color.parseColor("#FF2626"), isBold = false
            ),
        )
    }

    override fun getIconResId(): Int {
        return R.drawable.img_home_guide_prompt_rubbish
    }
}