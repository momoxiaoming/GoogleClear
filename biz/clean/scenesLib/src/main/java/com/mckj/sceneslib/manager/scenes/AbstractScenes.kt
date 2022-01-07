package com.mckj.sceneslib.manager.scenes

import android.content.Context
import android.view.ViewGroup
import androidx.core.util.Consumer
import androidx.fragment.app.FragmentActivity
import com.dn.openlib.utils.FragmentUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.*
import com.mckj.sceneslib.ui.scenes.ScenesContainerActivity
import com.mckj.sceneslib.ui.scenes.anim.ScenesAnimFragment
import com.mckj.sceneslib.ui.scenes.landing.content.LandingContentCardFragment
import com.mckj.sceneslib.ui.scenes.landing.content.LandingContentCleanFragment
import com.mckj.sceneslib.ui.scenes.landing.content.LandingContentSpeedFragment
import com.mckj.sceneslib.ui.scenes.landing.header.LandingHeaderCleanFragment
import com.mckj.sceneslib.ui.scenes.landing.header.LandingHeaderDefaultFragment
import com.mckj.sceneslib.ui.scenes.landing.header.LandingHeaderSpeedFragment
import com.mckj.sceneslib.util.DPermissionUtils

/**
 * Describe:
 *
 * Created By yangb on 2021/5/31
 */
abstract class AbstractScenes {

    /**
     * 场景数据
     */
    private var mData: ScenesData? = null

    /**
     * 初始化数据
     */
    abstract fun initData(): ScenesData

    /**
     * 获取引导icon
     */
    open fun getGuideIconResId() = R.drawable.scenes_icon_guide_warnning

    /**
     * 跳转
     */
    open fun jumpPage(context: Context): Boolean {
        if (isRequestPermission()) {
            requestPermission(context) { accept ->
                if (accept) {
                    jump(context)
                }
            }
        }else{
            jump(context)
        }
        return true
    }

    private fun jump(context: Context) {
        val entity = ScenesJumpData(
            getData().type,
            getGuideTypes(),
            "level_win",
            "clean_landing",
            "home_mfzs"
        )
        ScenesContainerActivity.startActivity(context, entity)
        handleClickEvent()
    }

    /**
     * 处理点击事件
     */
    protected fun handleClickEvent(customEvent: String? = null) {
        val event = if (customEvent.isNullOrEmpty()) {
            getData().clickEvent
        } else {
            customEvent
        }

    }

    /**
     * 添加场景view
     */
    open fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    ) {
        val fragment = ScenesAnimFragment.newInstance(consumer)
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    /**
     * 添加落地页头部view
     */
    open fun addLandingHeaderView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        val fragment = when (style) {
            ScenesLandingStyle.SPEED -> {
                LandingHeaderSpeedFragment()
            }
            ScenesLandingStyle.CLEAN -> {
                LandingHeaderCleanFragment()
            }
            else -> {
                LandingHeaderDefaultFragment()
            }
        }
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    /**
     * 添加落地页内容view
     */
    open fun addLandingContentView(
        activity: FragmentActivity,
        parent: ViewGroup,
        style: ScenesLandingStyle
    ) {
        val fragment = when (style) {
            ScenesLandingStyle.SPEED -> {
                LandingContentSpeedFragment()
            }
            ScenesLandingStyle.CLEAN -> {
                LandingContentCleanFragment()
            }
            else -> {
                LandingContentCardFragment()
            }
        }
        FragmentUtil.show(activity.supportFragmentManager, fragment, parent.id)
    }

    /**
     * 获取动画数据
     */
    open fun getLottieData(): ScenesLottieData? {
        return ScenesLottieData(
            "scenes/lottieFiles/rocket/images",
            "scenes/lottieFiles/rocket/data.json",
            LottieFrame(0, 40),
            LottieFrame(40, 80),
            LottieFrame(80, 94)
        )
    }

    /**
     * 获取引导类型
     */
    open fun getGuideTypes(): List<Int>? {
        return null
    }

    /**
     * 获取警告描述,实时获取
     */
    open fun getWarningDescList(): List<String>? {
        return null
    }

    /**
     * 获取场景任务数据
     */
    open fun getTaskData(): ScenesTaskData? {
        return null
    }

    /**
     * 获取场景数据
     */
    fun getData(): ScenesData {
        var data = mData
        if (data == null) {
            data = initData()
            mData = data
        }
        return data
    }


    open fun isRequestPermission(): Boolean {
        return false
    }

    open fun requestPermission(context: Context, block: (accept: Boolean) -> Unit) {
        if (context is FragmentActivity) {
            DPermissionUtils.checkStoragePermission(context, block)
        }
    }

}