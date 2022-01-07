package com.mckj.sceneslib.manager.scenes.model

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.core.util.Consumer
import com.dn.baselib.ext.idToString
import com.dn.openlib.utils.FragmentUtil

import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.LottieFrame
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.networktest.NetworkTestFragment

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class NetworkTestScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_NETWORK_TEST,
            "network_test",
            idToString(R.string.scenes_net_cs),
            idToString(R.string.scenes_wldx),
            idToString(R.string.scenes_ckws),
            idToString(R.string.scenes_ljcs),
            idToString(R.string.scenes_cswc),
            "${idToString(R.string.scenes_wljs)}${(10..20).random()}%",
            "home_speedtest_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_network_test

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = NetworkTestFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_ANTIVIRUS)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/network_test/images",
            "scenes/lottieFiles/network_test/data.json",
            LottieFrame(0, 33),
            LottieFrame(33, 52),
            LottieFrame(52, 69)
        )
    }

}