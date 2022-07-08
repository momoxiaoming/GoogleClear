package com.mckj.sceneslib.manager.scenes.model

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.ResourceUtil
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.overseas.scan.uninstall.OvsResidualFragment
import com.org.openlib.utils.FragmentUtil
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/3/24
 */
class SingleSpeedScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_SIGNAL_SPEED,
            "single_speed",
            ResourceUtil.getString(R.string.scenes_signal_optimization),
            ResourceUtil.getString(R.string.scenes_analyze_signal_interference_factors),
            "",
            ResourceUtil.getString(R.string.scenes_optimize_now),
            ResourceUtil.getString(R.string.scenes_optimization_completed),
            ResourceUtil.getString(R.string.scenes_signal_optimized_to_the_best),
            "home_signal_click",
            mSceneCategory = SceneCategory.CLEANER,
            recommendDesc = ResourceUtil.getString(R.string.scenes_check_and_fix_problems_affecting_internet_access)
                    ,followAudit = true
        )
    }

    override fun getGuideIconResId() = R.drawable.ic__signaloptimization_function

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(ResourceUtil.getString(R.string.scenes_the_network_needs_to_be_optimized), ResourceUtil.getString(
                    R.string.scenes_find_a_better_internet_connection))
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "",
            R.raw.signal_speed,
            LottieFrame(0, 35),
            LottieFrame(0, 35),
            null,
            R.raw.signal_speed
        )
    }

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        //适配通用场景的传统大Lottie
        val fragment = OvsResidualFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_checking_wifi_network_status)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_looking_for_a_premium_internet_connection)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_optimizing_network_signal)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_signal_optimizing), "", taskList)
    }

    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_SIGNAL_SPEED,
            ScenesType.TYPE_NETWORK_SPEED
        )
        list?.addAll(arrayListOf)
        return list
    }
}