package com.mckj.sceneslib.manager.scenes.model

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.core.util.Consumer
import com.dn.baselib.ext.idToString
import com.dn.openlib.utils.FragmentUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.LottieFrame
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.entity.ScenesLandingStyle
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.ap.WifiApFragment
import com.mckj.sceneslib.ui.scenes.model.ap.WifiApLandingHeaderFragment

/**
 * Describe:
 *
 * Created By yangb on 2021/6/2
 */
class HotSharingScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_HOT_SHARING,
            "hot_sharing",
            idToString(R.string.scenes_hot_share),
            idToString(R.string.scenes_share_ll),
            "",
            idToString(R.string.scenes_share_now),
            idToString(R.string.scenes_share_finish),
            "",
            "home_sharing_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_hot_sharing

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = WifiApFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        FragmentUtil.show(activity.supportFragmentManager, WifiApLandingHeaderFragment(), parent.id)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/ap/images",
            "scenes/lottieFiles/ap/data.json",
            null,
            LottieFrame(0, 68),
            LottieFrame(68, 114),
        )
    }

}