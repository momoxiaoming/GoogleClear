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
import com.mckj.sceneslib.ui.scenes.model.signalboost.SignalBoostFragment

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
open class SignalBoostScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_SIGNAL_BOOST,
            "signal_boost",
            idToString(R.string.scenes_xhzq),
            idToString(R.string.scenes_xhgr),
            idToString(R.string.scenes_kgl),
            idToString(R.string.scenes_now_yh),
            idToString(R.string.scenes_now_yh_finish),
            "${idToString(R.string.scenes_xhzq)}12%",
            "home_signalboost_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_signal_boost

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = SignalBoostFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/signal_boost/images",
            "scenes/lottieFiles/signal_boost/data.json",
            null,
            LottieFrame(0, 27),
            null
        )
    }

}