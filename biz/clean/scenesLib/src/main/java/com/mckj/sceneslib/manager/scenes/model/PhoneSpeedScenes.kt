package com.mckj.sceneslib.manager.scenes.model

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.dn.baselib.ext.idToString
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.cleancore.manager.junk.JunkManager
import com.mckj.cleancore.tools.JunkType
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 */
open class PhoneSpeedScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_PHONE_SPEED,
            "phone_speed",
            "${idToString(R.string.scenes_phone_speed)}",
            "${idToString(R.string.scenes_clean_phone)}",
            "${idToString(R.string.scenes_memory_Low)}",
            "${idToString(R.string.scenes_now_Speed)}",
            "${idToString(R.string.scenes_phone_speed_finish)}",
            "${idToString(R.string.scenes_phone_opt_finish)}",
            "home_speedup_click"
        )
    }

    override fun getGuideIconResId() = R.drawable.scenes_icon_guide_phone_speed

    override fun getTaskData(): ScenesTaskData? {
        val connectName: String = NetworkData.getInstance().connectInfoLiveData.value?.name ?: ""

        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask("${idToString(R.string.scenes_phone_speed_ram)}") {
            JunkManager.getInstance().scanAndClean(JunkType.APP_CACHE)
            delay(1000L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask("${idToString(R.string.scenes_phone_clean_ad)}") {
            delay(1000L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask("${idToString(R.string.scenes_phone_clean_ing)}") {
            JunkManager.getInstance().scanAndClean(JunkType.APK_FILE)
            delay(1000L)
            return@ScenesTask true
        })
        return ScenesTaskData(idToString(R.string.scenes_phone_speed_ing), connectName, taskList)
    }

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_WECHAT_CLEAN)
    }

    override fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        super.addLandingHeaderView(activity, parent, ScenesLandingStyle.SPEED)
    }

    override fun addLandingContentView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        super.addLandingContentView(activity, parent, ScenesLandingStyle.SPEED)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf("${idToString(R.string.scenes_many_app)}", "${idToString(R.string.scenes_phone_lazy)}")
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/phone_speed/images",
            "scenes/lottieFiles/phone_speed/data.json",
            null,
            LottieFrame(0, 60),
            LottieFrame(60, 117)
        )
    }

}