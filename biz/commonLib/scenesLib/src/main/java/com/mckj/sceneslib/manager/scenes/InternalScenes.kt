package com.mckj.sceneslib.manager.scenes

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.org.openlib.help.Consumer
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.LottieFrame
import com.mckj.sceneslib.entity.ScenesJumpData
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.ui.overseas.scan.OvsScenesScanFragment
import com.mckj.sceneslib.ui.scenes.ScenesContainerActivity
import com.org.openlib.utils.FragmentUtil

/**
 * 抽象场景国内的实现
 */
class InternalScenes:IOvsScenes {
    override fun jumpPage(context: Context, data: ScenesJumpData) {
        ScenesContainerActivity.startActivity(context, data)
    }

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = OvsScenesScanFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun addExecuteScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        //国内直接跳过
        consumer.accept(true)
    }

    override fun addEndScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        //国内直接跳过
        consumer.accept(true)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "",
            R.raw.rocket,
            LottieFrame(0, 40),
            LottieFrame(40, 80),
            LottieFrame(80, 94),
            R.raw.rocket
        )
    }

    override fun getExecuteLottie(): ScenesLottieData? {
        return null
    }

    override fun getEndLottie(): ScenesLottieData? {
        return null
    }

}