package com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes

import android.graphics.Color
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.cm.utils.TextUtils
import com.org.openlib.help.Consumer
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.SceneCategory
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.entity.ScenesLandingStyle
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.junk.JunkFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust.DustFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.landhead.DustHeadFragment
import com.org.openlib.utils.FragmentUtil

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.scenes
 * @data  2022/3/8 16:03
 */
class DustScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_DUST,
            "htlanding_msg",
            "声波除尘",
            "让手机声音更清晰",
            "",
            "立即除尘",
            "",
            "",
            "",
            mSceneCategory = SceneCategory.CLEANER,
            recommendDesc = TextUtils.string2SpannableString(
                "手机扬声器可能有灰尘",
                "有灰尘",
                30,
                color = Color.parseColor("#FA3C32"),
                true
            ),
            followAudit = false
        )
    }

    override fun getGuideIconResId() = R.drawable.cleanup_icon_sbcc

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = DustFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }


    override fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        FragmentUtil.show(activity.supportFragmentManager, DustHeadFragment.getInstance(), parent.id)
    }


    override fun loadAdAfterCondition(): Boolean {
        return true
    }
}