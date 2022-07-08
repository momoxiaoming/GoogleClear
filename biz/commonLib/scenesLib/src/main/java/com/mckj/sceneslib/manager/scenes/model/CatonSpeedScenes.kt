package com.mckj.sceneslib.manager.scenes.model

import android.graphics.Color
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.api.client.base.JunkClient
import com.mckj.api.client.task.CleanCooperation
import com.org.openlib.help.Consumer
import com.mckj.baselib.util.ResourceUtil
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.overseas.scan.uninstall.OvsResidualFragment
import com.org.openlib.utils.FragmentUtil
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class CatonSpeedScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_CATON_SPEED,
            "caton_speed",
            ResourceUtil.getString(R.string.scenes_caton_boost),
            if (ScenesManager.getInstance().isRegisterCleanerBody()) {
                ResourceUtil.getString(R.string.scenes_clean_up_junk_and_keep_your_phone_smooth)
            } else {
                ResourceUtil.getString(R.string.scenes_keep_your_phone_smooth)
            },
            "",
            ResourceUtil.getString(R.string.scenes_one_click_optimization),
            ResourceUtil.getString(R.string.scenes_clean_up),
            ResourceUtil.getString(R.string.scenes_the_best_current_mobile_phone_optimization),
            "home_caton_click",
            recommendDesc = TextUtils.string2SpannableStringForColor(
                ResourceUtil.getString(R.string.scenes_caton_cache_slowing_down_your_phone),
                ResourceUtil.getString(R.string.scenes_slow_down),
                color = Color.parseColor("#FA3C32"),
            ),
            followAudit = false
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_func_caton_opt

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(
            ResourceUtil.getString(R.string.scenes_too_many_programs_running_in_the_background),
            ResourceUtil.getString(
                R.string.scenes_severely_reduced_mobility
            )
        )
    }

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = OvsResidualFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "",
//            "scenes/lottieFiles/caton_speed.zip",
            R.raw.caton_speed,
            LottieFrame(0, 80),
            LottieFrame(0, 80),
            LottieFrame(40, 94),
            R.raw.caton_speed
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_optimizing_phone_residue)) {
            JunkClient.instance.autoClean(CleanCooperation.getResidualCleanExecutor()) {}
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_optimizing_phone_memory)) {
            JunkClient.instance.autoClean(CleanCooperation.getCacheExecutor()) {}
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_freeing_up_phone_space)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(
            ResourceUtil.getString(R.string.scenes_caton_optimization_in_progress),
            "",
            taskList
        )
    }


    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_CATON_SPEED,
        )
        list?.addAll(arrayListOf)
        return list
    }
}