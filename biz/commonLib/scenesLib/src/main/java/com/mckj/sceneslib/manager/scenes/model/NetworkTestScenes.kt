package com.mckj.sceneslib.manager.scenes.model

import android.graphics.Color
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.cm.utils.TextUtils
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.ResourceUtil
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.LottieFrame
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.entity.ScenesLandingStyle
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.landing.content.LandingContentOneKeyCleanFragment
import com.mckj.sceneslib.ui.scenes.landing.content.LandingHeaderReportFragment
import com.mckj.sceneslib.ui.scenes.landing.header.LandingContentReportFragment
import com.mckj.sceneslib.ui.scenes.landing.header.LandingHeaderEnvelopeFragment
import com.mckj.sceneslib.ui.scenes.model.networktest.NetworkTestFragment
import com.org.openlib.utils.FragmentUtil

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
            ResourceUtil.getString(R.string.scenes_wifi_speed_test),
            ResourceUtil.getString(R.string.scenes_inquiry_net_speed),
            "查看网速",
            ResourceUtil.getString(R.string.scenes_test_now),
            ResourceUtil.getString(R.string.scenes_test_speed_finish),
            "网络加速${(10..20).random()}%以上",
            "home_speedtest_click",
            recommendDesc = TextUtils.string2SpannableStringForColor(
                ResourceUtil.getString(R.string.scenes_how_net_defeat),
                ResourceUtil.getString(R.string.scenes_defeat),
                color = Color.parseColor("#FA3C32"), isBold = true
            ),
            followAudit = true
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_land_network_test

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = NetworkTestFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun addEndScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        //直接跳过
        consumer.accept(true)
    }

    override fun addExecuteScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        //直接跳过
        consumer.accept(true)
    }

    override fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        FragmentUtil.show(
            activity.supportFragmentManager,
            LandingContentReportFragment(),
            parent.id
        )

//        if (ScenesManager.getInstance().isRegisterWifiBody()) {
//            FragmentUtil.show(
//                activity.supportFragmentManager,
//                LandingHeaderReportFragment(),
//                parent.id
//            )
//        }else{
//
//        }
    }

    override fun addLandingContentView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        super.addLandingContentView(activity, parent, style)

//        if (ScenesManager.getInstance().isRegisterWifiBody()) {
//            FragmentUtil.show(
//                activity.supportFragmentManager,
//                LandingContentReportFragment(),
//                parent.id
//            )
//        } else {
//        }
    }


    override fun getGuideTypes(): List<Int>? {
        return if (ScenesManager.getInstance().isRegisterWifiBody()) {
            super.getRecommendTypes()?.shuffled()?.take(1)
        } else {
            super.getGuideTypes()
        }
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "",
//            "scenes/lottieFiles/network_test.zip",
            R.raw.network_test,
            LottieFrame(0, 33),
            LottieFrame(33, 52),
            LottieFrame(52, 69),
            R.raw.network_test
        )
    }


    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_NETWORK_TEST
        )
        list?.addAll(arrayListOf)
        return list
    }

}