package com.mckj.sceneslib.manager.scenes.model

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.api.client.task.CleanCooperation
import com.org.openlib.help.Consumer
import com.mckj.api.util.ScopeHelper
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.ui.junk.JunkEndFragment
import com.mckj.sceneslib.ui.junk.JunkExecuteFragment
import com.mckj.sceneslib.ui.junk.JunkFragment

import com.org.openlib.utils.FragmentUtil

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class JunkCleanScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_JUNK_CLEAN,
            "junk_clean",
            ResourceUtil.getString(R.string.scenes_junk_clean),
            ResourceUtil.getString(R.string.scenes_clean_junk_free_ram),
            "",
            ResourceUtil.getString(R.string.scenes_album_clean),
            ResourceUtil.getString(R.string.scenes_clean_finish),
            "",
            recommendDesc = ResourceUtil.getString(R.string.scenes_release_space_no_caton),
            followAudit = true,
            landingSafetyHeaderName = "清理完成",
            landingSafetyHeaderDes = "手机很干净了，请使用其他功能",
        )
    }

//    private var  junkTotalSize = 0L
//
//    fun getJunkTotalSize(): Long {
//        return junkTotalSize
//    }
//
//
//    fun setJunkTotalSize(size: Long) {
//       junkTotalSize = size
//    }

    override fun getGuideIconResId() = R.drawable.ic_land_junk_clean

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = JunkFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

//    override fun addLandingHeaderView(
//        activity: FragmentActivity,
//        parent: ViewGroup,
//        style: ScenesLandingStyle
//    ) {
//        if (ScenesManager.getInstance().isRegisterCleanerBody()) {
//            FragmentUtil.show(
//                activity.supportFragmentManager,
//                LandingHeaderOneKeyCleanFragment(),
//                parent.id
//            )
//        } else {
//            super.addLandingHeaderView(activity, parent, style)
//        }
//    }

//    override fun addLandingContentView(
//        activity: FragmentActivity,
//        parent: ViewGroup,
//        style: ScenesLandingStyle
//    ) {
//        if (ScenesManager.getInstance().isRegisterCleanerBody()) {
//            FragmentUtil.show(
//                activity.supportFragmentManager,
//                LandingContentOneKeyCleanFragment(),
//                parent.id
//            )
//        } else {
//            super.addLandingContentView(activity, parent, style)
//        }
//    }

    override fun addExecuteScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = JunkExecuteFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun addEndScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = JunkEndFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(
            ResourceUtil.getString(R.string.scenes_junk_have_lot_junk), ResourceUtil.getString(
                R.string.scenes_junk_need_clean
            )
        )
    }

    override fun repulsionTypes(): MutableList<Int>? {
        return arrayListOf(
            ScenesType.TYPE_JUNK_CLEAN,
        )
    }

    private val lottieSrc by lazy {
        ScenesLottieData(
            "",
            R.raw.lottie_junk_clean,
            LottieFrame(12, 37),
            LottieFrame(50, 74),
            LottieFrame(76, 125),
            R.raw.lottie_junk_clean,
            LottieFrame(0, 11),
            LottieFrame(49, 49),
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


    override fun isRequestPermission(): Boolean {
        return true
    }

    override fun executorTask(block: (() -> Unit)?) {
        var junkSize = 0L
        var junkDesc = ""
        ScopeHelper.launch {
            CleanCooperation.getCacheExecutor().silentScan {
                junkSize += it.junkSize
            }

            junkDesc =
                "${FileUtil.getFileSizeNumberText(junkSize)}${
                    FileUtil.getFileSizeUnitText(
                        junkSize
                    )
                }"

            getData().scanSize = junkSize
            getData().scanSizeDesc = junkDesc

            block?.invoke()
        }
    }

}