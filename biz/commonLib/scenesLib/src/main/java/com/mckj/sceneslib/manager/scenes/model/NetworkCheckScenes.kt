package com.mckj.sceneslib.manager.scenes.model

import android.graphics.Color
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.cm.utils.TextUtils
import com.org.openlib.help.Consumer
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.networkcheck.LandingNetworkCheckFragment
import com.mckj.sceneslib.ui.scenes.model.networkcheck.NetworkCheckFragment
import com.org.openlib.utils.FragmentUtil

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
            "防止蹭网",
            "防止网络被偷连",
            "新功能",
            "立即检测",
            "检测完成",
            "检测共有多少个设备",
            "home_rubnet_click",
            mSceneCategory = SceneCategory.WIFI,
            recommendDesc = TextUtils.string2SpannableString(
                "看看有多少人在偷连你的网络",
                "偷连",
                30,
                color = Color.parseColor("#FA3C32"),
                true
            )
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_prevent_function

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
            "",
//            "scenes/lottieFiles/network_check.zip",
            R.raw.network_check,
            LottieFrame(0, 23),
            LottieFrame(23, 116),
            null,
            R.raw.network_check
        )
    }

    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_NETWORK_CHECK
        )
        list?.addAll(arrayListOf)
        return list
    }
}