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
class NetworkSpeedScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_NETWORK_SPEED,
            "network_speed",
            ResourceUtil.getString(R.string.scenes_network_acceleration),
            ResourceUtil.getString(R.string.scenes_optimize_network_increase_internet_speed),
            "",
            ResourceUtil.getString(R.string.scenes_one_key_acceleration),
            ResourceUtil.getString(R.string.scenes_optimization_completed),
            String.format(ResourceUtil.getString(R.string.scenes_network_acceleration_more),"${(10..20).random()}"),
            "home_network_click",
            recommendDesc = ResourceUtil.getString(R.string.scenes_the_current_network_speed_is_slow_and_can_be_optimized)
            ,followAudit = true
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_networkacceleration_function

    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_NETWORK_SPEED,
        )
        list?.addAll(arrayListOf)
        return list
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(ResourceUtil.getString(R.string.scenes_network_needs_to_be_optimized), ResourceUtil.getString(
                    R.string.scenes_find_a_better_internet_connection))
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

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "",
//            "scenes/lottieFiles/network_speed.zip",
            R.raw.network_speed,
            LottieFrame(0, 40),
            LottieFrame(40, 120),
            LottieFrame(120, 151),
            R.raw.network_speed
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_optimizing_router)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_looking_for_a_premium_internet_connection)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_optimizing_network_security_configuration)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_the_network_is_accelerating), "", taskList)
    }

}