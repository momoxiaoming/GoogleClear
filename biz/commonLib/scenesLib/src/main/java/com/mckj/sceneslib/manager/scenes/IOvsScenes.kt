package com.mckj.sceneslib.manager.scenes

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.entity.ScenesJumpData
import com.mckj.sceneslib.entity.ScenesLottieData

/**
 * 抽象场景的国内外共同实现
 */
interface IOvsScenes {
    fun jumpPage(context: Context, data: ScenesJumpData)

    fun addScenesView(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    )

    fun addExecuteScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    )

    fun addEndScenesFragment(
        activity: FragmentActivity,
        parent: ViewGroup,
        consumer: Consumer<Boolean>
    )

    fun getLottieData(): ScenesLottieData?

    fun getExecuteLottie(): ScenesLottieData?

    fun getEndLottie(): ScenesLottieData?
}