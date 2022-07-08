package com.mckj.module.cleanup.entity.scenes

import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.RecommendData
import com.mckj.sceneslib.manager.scenes.ScenesType

class AntivirusScenes : AbsHomeRecommendScenes() {
    private val appNumber = (5..8).random().toString()
    override fun initScenesData(): RecommendData {
        return RecommendData(
            ScenesType.TYPE_ANTIVIRUS,
            ResourceUtil.getString(R.string.scenes_mobile_phone_antivirus),
            desc = String.format(
                ResourceUtil.getString(R.string.cleanup_home_recomend_antivirus),
                appNumber
            ),
            ResourceUtil.getString(R.string.cleanup_home_recomend_antivirus_btn),
            title = TextUtils.string2SpannableStringForColor(
                String.format(
                    ResourceUtil.getString(R.string.cleanup_home_recomend_antivirus_title_one),
                    appNumber
                ),
                appNumber + ResourceUtil.getString(R.string.cleanup_home_recomend_antivirus_unit),
                color = Color.parseColor("#FF2626"), isBold = false
            ),
        )
    }

    override fun getIconResId(): Int {
        return R.drawable.img_home_guide_prompt_antivirus
    }
}