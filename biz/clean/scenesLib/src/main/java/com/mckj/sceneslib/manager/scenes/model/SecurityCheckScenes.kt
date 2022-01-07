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
import com.mckj.sceneslib.ui.scenes.model.securitycheck.SecurityCheckFragment

/**
 * Describe:
 *
 * Created By yangb on 2021/6/2
 */
class SecurityCheckScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_SECURITY_CHECK,
            "security_check",
            idToString(R.string.scenes_aqjc),
            idToString(R.string.scenes_jcwlaq),
            "",
            idToString(R.string.scenes_check),
            idToString(R.string.scenes_check_finish),
            idToString(R.string.scenes_securty_desc),
            "home_safetycheck_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_security_check

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = SecurityCheckFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/security_check/images",
            "scenes/lottieFiles/security_check/data.json",
            null,
            LottieFrame(0, 31),
            null
        )
    }

}