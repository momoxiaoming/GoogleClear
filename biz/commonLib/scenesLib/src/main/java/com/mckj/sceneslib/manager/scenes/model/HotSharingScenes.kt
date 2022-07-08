package com.mckj.sceneslib.manager.scenes.model

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.org.openlib.help.Consumer
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.ap.WifiApFragment
import com.mckj.sceneslib.ui.scenes.model.ap.WifiApLandingHeaderFragment
import com.org.openlib.utils.FragmentUtil

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
            "热点分享",
            "手机流量一起用",
            "",
            "立即分享",
            "分享完成",
            "",
            "home_sharing_click",
            mSceneCategory = SceneCategory.WIFI,
            recommendDesc = "和别人一起共用流量"
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_hotspot_function

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
            "",
//            "scenes/lottieFiles/ap.zip",
            R.raw.ap,
            null,
            LottieFrame(0, 68),
            LottieFrame(68, 114),
            R.raw.ap
        )
    }

    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_HOT_SHARING
        )
        list?.addAll(arrayListOf)
        return list
    }

    override fun loadAdAfterCondition(): Boolean {
        return true
    }
}