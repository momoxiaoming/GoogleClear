package com.mckj.sceneslib.manager.scenes.model

import android.graphics.Color
import com.mckj.api.client.base.JunkClient
import com.mckj.api.client.task.CleanCooperation
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.api.util.ScopeHelper
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.util.MemoryUtil
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 */
open class PhoneSpeedScenes : AbstractScenes() {
    val memeoryUsePercent = MemoryUtil.queryStorageUsedPercent()

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_PHONE_SPEED,
            "phone_speed",
            ResourceUtil.getString(R.string.scenes_phone_boost),
            ResourceUtil.getString(R.string.scenes_release_room),
            ResourceUtil.getString(R.string.scenes_ram_high),
            ResourceUtil.getString(R.string.scenes_release_now),
            ResourceUtil.getString(R.string.scenes_release_success),
            "",
            "home_speedup_click",
            recommendDesc = TextUtils.string2SpannableStringForColor(
                String.format(
                    ResourceUtil.getString(R.string.scenes_memory_used_percentage),
                    memeoryUsePercent
                ),
                "$memeoryUsePercent",
                color = Color.parseColor("#FA3C32"), isBold = true
            ),
            followAudit = false,
            landingSafetyHeaderName = ResourceUtil.getString(R.string.scenes_phone_speed_landing_header_name),
            landingSafetyHeaderDes = "",
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_land_phone_speed

    override fun getTaskData(): ScenesTaskData? {
        val connectName: String = NetworkData.getInstance().connectInfoLiveData.value?.name ?: ""
        var junkSize = 0L
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_analyzing_your_device)) {
            ScopeHelper.launch {
                JunkClient.instance.autoClean(CleanCooperation.getCacheExecutor()) {
                    junkSize += it.junkSize
                }
                setLandDesc(junkSize)
            }
            delay(1000L)
            return@ScenesTask true
        })
//        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_checking_ram_usage)) {
//            setLandDesc(junkSize)
//            delay(1000L)
//            return@ScenesTask true
//        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_finding_running_apps)) {
            ScopeHelper.launch {
                JunkClient.instance.autoClean(CleanCooperation.getApkCleanExecutor()) {
                    junkSize += it.junkSize

                }
                setLandDesc(junkSize)
            }
            delay(1000L)
            return@ScenesTask true
        })
        return ScenesTaskData(
            ResourceUtil.getString(R.string.scenes_scanning),
            "",
            taskList
        )//扫描....
    }

    private fun setLandDesc(
        totalSize: Long
    ) {
        getData().landingDesc = if (ScenesManager.getInstance().isRegisterWifiBody()) {
            "success"
        } else {
            super.getCleanLandingDesc(totalSize, (400L..700L).random())
        }
    }

    override fun repulsionTypes(): MutableList<Int>? {
        return arrayListOf(
            ScenesType.TYPE_PHONE_SPEED,

            )
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(
            ResourceUtil.getString(R.string.scenes_apps_running_background), ResourceUtil.getString(
                R.string.scenes_phone_run_slow
            )
        )
    }

    private val lottieSrc by lazy { ScenesLottieData(
        "",
        R.raw.lottie_phone_speed,
        LottieFrame(12, 37),
        LottieFrame(50, 74),
        LottieFrame(76, 125),
        R.raw.lottie_phone_speed,
        LottieFrame(0, 11),
        LottieFrame(38, 49),
    ) }


    override fun getLottieData(): ScenesLottieData? {
        return lottieSrc
    }

    override fun getExecuteLottie(): ScenesLottieData? {
        return lottieSrc
    }

    override fun getEndLottie(): ScenesLottieData? {
        return lottieSrc
    }


}