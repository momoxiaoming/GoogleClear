package com.mckj.sceneslib.manager.scenes.model

import android.graphics.Color
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.baselib.util.ResourceUtil
import com.org.openlib.utils.ProcessUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.entity.ScenesTask
import com.mckj.sceneslib.entity.ScenesTaskData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.util.MemoryUtil
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/3/24
 */
class OneKeySpeedScenes : AbstractScenes() {
    val memeoryUsePercent = MemoryUtil.queryStorageUsedPercent()

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_ONE_KEY_SPEED,
            "one_key_speed",
            ResourceUtil.getString(R.string.scenes_phone_boost),
            ResourceUtil.getString(R.string.scenes_release_room),
            ResourceUtil.getString(R.string.scenes_ram_high),
            ResourceUtil.getString(R.string.scenes_release_now),
            ResourceUtil.getString(R.string.scenes_release_success),
            "",
            "home_wifispeedup_click",
            recommendDesc = TextUtils.string2SpannableStringForColor(
                String.format(ResourceUtil.getString(R.string.scenes_memory_used_percentage),memeoryUsePercent),
                "$memeoryUsePercent",
                color = Color.parseColor("#FA3C32"),
            ), followAudit = true
        )
    }
    override fun getGuideIconResId() = R.drawable.ic_land_one_key_speed

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_free_up_ram)) {
            ProcessUtil.killAllProcessesToMemory()
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_clean_ads_boosting)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_optimizing_storage_space)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_phone_boost_ing), "", taskList)
    }

    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_ONE_KEY_SPEED,
            ScenesType.TYPE_NETWORK_TEST,
            ScenesType.TYPE_SIGNAL_SPEED
        )
        list?.addAll(arrayListOf)
        return list
    }

}