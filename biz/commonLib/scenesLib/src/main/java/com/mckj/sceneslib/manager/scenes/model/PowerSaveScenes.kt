package com.mckj.sceneslib.manager.scenes.model

import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
open class PowerSaveScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_POWER_SAVE,
            "power_save",
            ResourceUtil.getString(R.string.scenes_battery_saver),
            ResourceUtil.getString(R.string.scenes_power_consuming_batter),
            "",
            ResourceUtil.getString(R.string.scenes_optimizing_now),
            ResourceUtil.getString(R.string.scenes_optimizing_finish),
            ResourceUtil.getString(R.string.scenes_close_all_consuming_power_apps),
            "home_savepower_click",
            mSceneCategory = SceneCategory.CLEANER,
            recommendDesc = TextUtils.string2SpannableStringForColor(
                ResourceUtil.getString(R.string.scenes_consuming_power_1),
                ResourceUtil.getString(R.string.scenes_consuming_power_2),
                color = Color.parseColor("#FA3C32"), isBold = true
            ),
            followAudit = false,
            landingSafetyHeaderName = ResourceUtil.getString(R.string.scenes_power_save_landing_name),
            landingSafetyHeaderDes = ResourceUtil.getString(R.string.scenes_power_save_landing_desc),
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_land_power_save

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(
            ResourceUtil.getString(R.string.scenes_background_apps_more), ResourceUtil.getString(
                R.string.scenes_power_consumption_fast
            )
        )
    }

    private val lottieSrc by lazy {
        ScenesLottieData(
            "",
            R.raw.lottie_power_save,
            LottieFrame(12, 37),
            LottieFrame(50, 74),
            LottieFrame(76, 125),
            R.raw.lottie_power_save,
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

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_checking_battery_health)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_analyzing_charge_capacity)) {
            delay(700L)
            return@ScenesTask true
        })
//        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_recognizing_power_consumption)) {
//            delay(700L)
//            return@ScenesTask true
//        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_scanning), "", taskList)
    }


    override fun repulsionTypes(): MutableList<Int>? {
        return  arrayListOf(
            ScenesType.TYPE_POWER_SAVE
        )
    }
}