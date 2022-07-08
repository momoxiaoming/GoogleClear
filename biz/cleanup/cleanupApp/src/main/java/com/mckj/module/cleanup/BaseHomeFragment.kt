package com.mckj.module.cleanup

import androidx.databinding.ViewDataBinding
import com.mckj.baselib.base.databinding.AbstractViewModel
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.module.cleanup.gen.CleanupSp
import com.mckj.module.cleanup.strategy.HomeGuideStrategy
import com.mckj.module.cleanup.strategy.HomeRecommendStrategy
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.manager.scenes.model.JunkCleanScenes

/**
 * 带首次引导和无操作弹出推荐弹窗的抽象Fragment
 * 如果要使用此功能，继承此类。
 *
 * 用法同原[DataBindingFragment]
 */
abstract class BaseHomeFragment<T : ViewDataBinding, VM : AbstractViewModel> :
    DataBindingFragment<T, VM>() {

    override fun onFirstVisible() {
        super.onFirstVisible()
    }

    override fun onResume() {
        super.onResume()
//        lifecycleInit()  //关闭首页强弹
    }

    private var needInit = true
    private fun lifecycleInit() {
        if (!needInit){
            return
        }
        val activity = activity
        activity?.let {
            needInit = false
            val lifecycle = it.lifecycle
            val recommendStrategy = HomeRecommendStrategy(childFragmentManager)
            lifecycle.addObserver(recommendStrategy)
            ScenesManager.getInstance().register(JunkCleanScenes())
            checkAndStartGuide()
            val maskStrategy = HomeGuideStrategy(childFragmentManager)
            lifecycle.addObserver(maskStrategy)
        }

    }


    private var isFirstGuide: Boolean = true

    /**
     * 检查并开始引导
     */
    private fun checkAndStartGuide() {
        val firstCleanGuide = CleanupSp.instance.firstCleanGuide
        isFirstGuide = firstCleanGuide
        if (!firstCleanGuide) {
            //不是第一次引导
            return
        }
        //设置引导已过期
        CleanupSp.instance.firstCleanGuide = false
        val scenes = ScenesManager.getInstance().getScenes(ScenesType.TYPE_PHONE_SPEED)
        scenes?.enterFlag = "初次引导"
        context?.let {
            ScenesManager.getInstance().jumpNow(it, ScenesType.TYPE_PHONE_SPEED)
            CleanupSp.instance.isCleanupHomeGuideFirstPop = true
        }
    }
}