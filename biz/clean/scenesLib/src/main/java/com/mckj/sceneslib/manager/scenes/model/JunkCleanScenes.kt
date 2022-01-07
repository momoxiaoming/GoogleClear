package com.mckj.sceneslib.manager.scenes.model

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.core.util.Consumer
import com.dn.baselib.ext.idToString
import com.dn.openlib.utils.FragmentUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.LottieFrame
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.junk.JunkFragment
import com.mckj.sceneslib.entity.ScenesData

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
            idToString(R.string.scenes_junk_clean),
            idToString(R.string.scenes_junk_many),
            "",
            idToString(R.string.scenes_clean_now),
            idToString(R.string.scenes_clean_finish),
            idToString(R.string.scenes_yh_zj),
        )
    }

    override fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = JunkFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(idToString(R.string.scenes_fx_junk), idToString(R.string.scenes_jjql))
    }

    override fun getGuideTypes(): List<Int>? {
        return arrayListOf(ScenesType.TYPE_PHONE_SPEED, ScenesType.TYPE_WECHAT_CLEAN)
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/junk/images",
            "scenes/lottieFiles/junk/data.json",
            null,
            LottieFrame(0, 65),
            LottieFrame(65, 92)
        )
    }

    override fun isRequestPermission(): Boolean {
        return true
    }
}