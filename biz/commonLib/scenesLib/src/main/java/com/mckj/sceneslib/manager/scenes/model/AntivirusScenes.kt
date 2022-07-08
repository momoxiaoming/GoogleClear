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
import com.mckj.sceneslib.ui.overseas.scan.uninstall.OvsResidualFragment
import com.org.openlib.utils.FragmentUtil
import kotlinx.coroutines.delay

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class AntivirusScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_ANTIVIRUS,
            "antivirus",
            ResourceUtil.getString(R.string.scenes_mobile_phone_antivirus),
            ResourceUtil.getString(R.string.scenes__never_scanned_for_viruses),
            "",
            ResourceUtil.getString(R.string.scenes_detect_immediately),
            ResourceUtil.getString(R.string.scenes_antivirus_completed),
            ResourceUtil.getString(R.string.scenes_the_phone_is_already_safe),
            "home_antivirus_click",
            mSceneCategory = SceneCategory.CLEANER,
            recommendDesc = TextUtils.string2SpannableString(
                ResourceUtil.getString(R.string.scenes_mobile_phone_virus_will_threaten_internet_security),
                ResourceUtil.getString(R.string.scenes_internet_safety),
                30,
                color = Color.parseColor("#FA3C32"),
                true
            ),
            followAudit = true
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_antivirus_function

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(ResourceUtil.getString(R.string.scenes_mobile_phone_lost_protection), ResourceUtil.getString(
                    R.string.scenes_mobile_phone_security_is_threatened))
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "",
            R.raw.antivirus,
            LottieFrame(0, 63),
            LottieFrame(0, 63),
            LottieFrame(63, 93),
            R.raw.antivirus
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
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_detecting_privacy_risks)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_scanning_for_virus_apps)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_checking_network_security)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_mobile_phone_virus_killing), "", taskList)
    }

    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_ANTIVIRUS
        )
        list?.addAll(arrayListOf)
        return list
    }
}