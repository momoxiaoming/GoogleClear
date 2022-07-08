package com.mckj.sceneslib.manager.scenes.model

import android.graphics.Color
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.cm.utils.TextUtils
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.ResourceUtil
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.cooldown.CoolDownFragment
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.delay

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
            ResourceUtil.getString(R.string.scenes_cpu_cooler),
            ResourceUtil.getString(R.string.scenes_hot_effect_performance),
            ResourceUtil.getString(R.string.scenes_temperature_high),
            ResourceUtil.getString(R.string.scenes_cooler_now),
            ResourceUtil.getString(R.string.scenes_cooler_down_success),
            ResourceUtil.getString(R.string.scenes_closed_all_cause_hot_apps),
            "home_cooldown_click",
            recommendDesc = TextUtils.string2SpannableStringForColor(
                ResourceUtil.getString(R.string.scenes_cooler_text_1),
                ResourceUtil.getString(R.string.scenes_cooler_text_1_key),
                color = Color.parseColor("#FA3C32"),
            ),
            followAudit = false,
            landingSafetyHeaderName = "手机降温已完成",
            landingSafetyHeaderDes = "快去使用别的功能吧",
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_func_recommend_cool_down


    override fun repulsionTypes(): MutableList<Int>? {
        return arrayListOf(
            ScenesType.TYPE_COOL_DOWN,
        )
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(
            ResourceUtil.getString(R.string.scenes_background_apps_more), ResourceUtil.getString(
                R.string.scenes_phone_temperature_high
            )
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_checking_cpu_status)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_locating_power_draining_operations)) {
            delay(700L)
            return@ScenesTask true
        })
//        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_finding_over_heating_applications)) {
//            delay(700L)
//            return@ScenesTask true
//        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_scanning), "", taskList)
    }


    private val lottieSrc by lazy {
        ScenesLottieData(
            "",
            R.raw.lottie_cool_down,
            LottieFrame(12, 37),
            LottieFrame(50, 74),
            LottieFrame(76, 125),
            R.raw.lottie_cool_down,
            LottieFrame(0, 11),
            LottieFrame(38, 49),
        )
    }

    override fun getLottieData(): ScenesLottieData? {
        return lottieSrc
    }

    override fun getExecuteLottie(): ScenesLottieData? {
        return lottieSrc
    }

    override fun getEndLottie(): ScenesLottieData? {
        return lottieSrc
    }

    override fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        //对应CleanupSp.instance.cleanupHomeGuidePopTime
        //标记为蒙层已执行
        MMKV.defaultMMKV().encode("cleanup:home:guidePopTime",  System.currentTimeMillis())
        val decodeLong = MMKV.defaultMMKV().decodeLong("cleanup:home:guidePopTime")
        super.addLandingHeaderView(activity, parent, style)
    }
}