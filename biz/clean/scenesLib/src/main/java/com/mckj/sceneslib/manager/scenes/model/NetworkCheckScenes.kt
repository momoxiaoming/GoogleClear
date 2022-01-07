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
import com.mckj.sceneslib.ui.scenes.model.networkcheck.LandingNetworkCheckFragment
import com.mckj.sceneslib.ui.scenes.model.networkcheck.NetworkCheckFragment

/**
 * Describe:
 *
 * Created By yangb on 2021/6/2
 */
class NetworkCheckScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_NETWORK_CHECK,
            "network_check",
            idToString(R.string.scenes_fcw),
            idToString(R.string.scenes_fcw_desc),
            idToString(R.string.scenes_new_tool),
            idToString(R.string.scenes_check),
            idToString(R.string.scenes_check_finish),
            idToString(R.string.scenes_check_sb),
            "home_rubnet_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_network_check

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = NetworkCheckFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        FragmentUtil.show(activity.supportFragmentManager, LandingNetworkCheckFragment(), parent.id)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/network_check/images",
            "scenes/lottieFiles/network_check/data.json",
            LottieFrame(0, 23),
            LottieFrame(23, 116),
            null
        )
    }

}