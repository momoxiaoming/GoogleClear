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
 * Created By yangb on 2021/3/24
 */
class CameraCheckScenes : AbstractScenes() {

    override fun initData(): ScenesData {
        return ScenesData(
            ScenesType.TYPE_CAMERA_CHECK,
            "camera",
            ResourceUtil.getString(R.string.scenes_camera_detection),
            ResourceUtil.getString(R.string.scenes_find_cameras),
            "",
            ResourceUtil.getString(R.string.scenes_check_now),
            ResourceUtil.getString(R.string.scenes_detection_completed),
            ResourceUtil.getString(R.string.scenes_camera_no_exception_found),
            "home_camera_click",
            recommendDesc = TextUtils.string2SpannableString(
                ResourceUtil.getString(R.string.scenes_camera_check_privacy_leaks),
                ResourceUtil.getString(R.string.scenes_camera_privacy_leaks),
                30,
                color = Color.parseColor("#FA3C32"),
                true
            ),
            followAudit = false
        )
    }

    override fun getGuideIconResId() = R.drawable.ic_camera_function

    override fun repulsionTypes(): MutableList<Int>? {
        val list = super.repulsionTypes()
        val arrayListOf = arrayListOf(
            ScenesType.TYPE_CAMERA_CHECK
        )
        list?.addAll(arrayListOf)
        return list
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

    override fun getWarningDescList(): List<String>? {
        return arrayListOf(ResourceUtil.getString(R.string.scenes_camera_network_wait_check), ResourceUtil.getString(
                    R.string.scenes_network_intrusion_need_check))
    }

    override fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "",
            R.raw.camera_check,
            LottieFrame(0, 35),
            LottieFrame(0, 35),
            LottieFrame(0, 35),
            R.raw.camera_check
        )
    }

    override fun getTaskData(): ScenesTaskData? {
        val taskList = mutableListOf<ScenesTask>()
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_check_network_security_ing)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_network_intrusion_discover)) {
            delay(700L)
            return@ScenesTask true
        })
        taskList.add(ScenesTask(ResourceUtil.getString(R.string.scenes_looking_camera)) {
            delay(700L)
            return@ScenesTask true
        })
        return ScenesTaskData(ResourceUtil.getString(R.string.scenes_camera_detection_ing), "", taskList)
    }

}