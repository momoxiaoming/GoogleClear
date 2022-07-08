package com.mckj.sceneslib.manager.scenes

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.org.openlib.help.Consumer
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.LottieFrame
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.entity.ScenesJumpData
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.ui.overseas.ScenesContainerOsActivity
import com.mckj.sceneslib.ui.overseas.end.OvsScenesEndFragment
import com.mckj.sceneslib.ui.overseas.execute.OvsScenesExecuteFragment
import com.mckj.sceneslib.ui.overseas.scan.OvsScenesScanFragment
import com.org.openlib.utils.FragmentUtil

class OvsScenes:IOvsScenes {
    override fun jumpPage(context: Context, data: ScenesJumpData){
        ScenesContainerOsActivity.startActivity(context, data)
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
        val fragment = OvsScenesExecuteFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun addEndScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = OvsScenesEndFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "",
            R.raw.phone_speed_ovs,
            LottieFrame(0, 125),
            LottieFrame(63, 125),
            LottieFrame(0, 62),
            R.raw.phone_speed_ovs
        )
    }

    override fun getExecuteLottie(): ScenesLottieData? {
        return ScenesLottieData(
            "",
            R.raw.common_optimize,
            LottieFrame(0, 51),
            LottieFrame(0, 51),
            LottieFrame(0, 51),
            R.raw.common_optimize
        )
    }

    override fun getEndLottie(): ScenesLottieData? {
        return ScenesLottieData(
            "",
            R.raw.common_end,
            LottieFrame(0, 66),
            LottieFrame(0, 66),
            LottieFrame(0, 66),
            R.raw.common_end
        )
    }

}