package com.mckj.module.cleanup.entity.scenes

import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.RecommendData
import com.mckj.sceneslib.manager.scenes.ScenesType

class ShortVideoCleanScenes : AbsHomeRecommendScenes() {
    val number = (80..98).random().toString() + "%"
    override fun initScenesData(): RecommendData {
        return RecommendData(
            ScenesType.TYPE_SHORT_VIDEO_CLEAN,
            ResourceUtil.getString(R.string.scenes_video_clean),
            desc = ResourceUtil.getString(R.string.cleanup_home_recomend_video_clean),
            ResourceUtil.getString(R.string.cleanup_home_recomend_video_clean_btn),
            title = TextUtils.string2SpannableStringForColor(
                String.format(
                    ResourceUtil.getString(R.string.cleanup_home_recomend_video_clean_title_one),
                    number
                ),
                number,
                color = Color.parseColor("#FF2626"), isBold = false
            )
        )
    }

    override fun getIconResId(): Int {
        return R.drawable.img_home_guide_video_clear
    }
}