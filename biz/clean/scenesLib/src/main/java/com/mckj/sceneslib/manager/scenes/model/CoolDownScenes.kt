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
import com.mckj.sceneslib.ui.scenes.model.cooldown.CoolDownFragment

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class CoolDownScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_COOL_DOWN,
            "cool_down",
            idToString(R.string.scenes_phone_cool),
            idToString(R.string.scenes_phone_perfor),
            idToString(R.string.scenes_temp_high),
            idToString(R.string.scenes_cool_now),
            idToString(R.string.scenes_cool_finish),
            idToString(R.string.scenes_close_app),
            "home_cooldown_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_cool_down

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = CoolDownFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_POWER_SAVE, ScenesType.TYPE_CATON_SPEED)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(idToString(R.string.scenes_back_app), idToString(R.string.scenes_phone_temp_higt_now))
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/cool_down/images",
            "scenes/lottieFiles/cool_down/data.json",
            null,
            LottieFrame(0, 66),
            LottieFrame(66, 92)
        )
    }

}