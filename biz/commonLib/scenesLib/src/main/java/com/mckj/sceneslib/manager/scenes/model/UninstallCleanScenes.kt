package com.mckj.sceneslib.manager.scenes.model

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.mckj.api.client.JunkExecutor
import com.mckj.api.client.base.JunkClient
import com.mckj.api.client.task.CleanCooperation
import com.mckj.api.entity.CacheJunk
import com.org.openlib.help.Consumer
import com.mckj.api.util.ScopeHelper
import com.mckj.baselib.util.ResourceUtil
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.overseas.scan.uninstall.OvsResidualFragment
import com.org.openlib.utils.FragmentUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Describe:
 *
 * Created By yangb on 2021/3/24
 */
class UninstallCleanScenes : AbstractScenes() {
    private var mExecutor: JunkExecutor? = null
    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_UNINSTALL_CLEAN,
            "uninstall_clean",
            ResourceUtil.getString(R.string.scenes_residual_clean),
            ResourceUtil.getString(R.string.scenes_clean_to_freeup_space),
            "",
            ResourceUtil.getString(R.string.scenes_click_to_clean),
            ResourceUtil.getString(R.string.scenes_clean_up),
            "",
            "home_uninstall_click",
            recommendDesc = ResourceUtil.getString(R.string.scenes_leftover_junk_files_after_uninstallation)
                    ,followAudit = true
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_land_esidual_clean

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = OvsResidualFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }


    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_PHONE_SPEED,
            ScenesType.TYPE_JUNK_CLEAN,
            ScenesType.TYPE_CATON_SPEED
        )
        list?.addAll(arrayListOf)
        return list
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(ResourceUtil.getString(R.string.scenes_junk_files_produced_during_uninstallation), ResourceUtil.getString(
                    R.string.scenes_junk_images_produced_during_uninstallation))
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

//        return ScenesLottieData(
//            "",
//            "scenes/lottieFiles/uclean.zip",
//            LottieFrame(0, 37),
//            LottieFrame(37, 87),
//            LottieFrame(87, 137)
//        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_checking_storage_space)) {
            JunkClient.instance.autoClean(CleanCooperation.getResidualCleanExecutor()) {}
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_searching_uninstall_traces)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_analyzing_junk_creating_behavior)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_scanning), "", taskList)
    }

    override fun isRequestPermission(): Boolean {
        return true
    }

    override fun isScenesAutoRefresh(): Boolean {
        return true
    }

    override fun executorTask(block: (() -> Unit)?) {
        var junkSize = 0L
        var fileSize = 0L
        ScopeHelper.launch {
            withContext(Dispatchers.IO) {
                if (mExecutor == null) {
                    mExecutor = CleanCooperation.getResidualCleanExecutor()
                }
                var cacheJunk = CacheJunk()
                mExecutor?.silentScan {
                    cacheJunk = it
                }
                cacheJunk.appJunks?.forEach { appJunk ->
                    fileSize += appJunk.junks?.size ?: 0
                    junkSize += appJunk.junkSize
                }
                getData().landingDesc = if (ScenesManager.getInstance().isRegisterWifiBody()) {
                    ResourceUtil.getString(R.string.scenes_optimized)
                } else {
                    if (junkSize == 0L) {
                        junkSize = (10 * 1024 * 1024..15 * 1024 * 1024).random().toLong()
                    }
                    if (fileSize == 0L) {
                        fileSize = (400..700).random().toLong()
                    }
                    super.getCleanLandingDesc(junkSize, fileSize)
                }
                block?.invoke()
            }
        }
    }

    override fun stopTask() {
        mExecutor?.stop()
    }

}