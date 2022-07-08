package com.mckj.sceneslib.manager.strategy.helper

import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.strategy.GuideStrategy
import com.mckj.sceneslib.manager.strategy.WeightsStrategy

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.manager.scenes
 * @data  2022/4/25 10:53
 */
class StrategyHelper private constructor(){

    companion object {
        const val NONE_SCENES=-1
        private val INSTANCE by lazy { StrategyHelper() }
        fun getInstance(): StrategyHelper = INSTANCE
    }

    private val mStrategyManager by lazy { StrategyManager.getInstance() }
    private val weightsStrategy by lazy { mStrategyManager.getStrategy(StrategyType.WEIGHTS_STRATEGY) }
    private val guideStrategy by lazy { mStrategyManager.getStrategy(StrategyType.GUIDE_STRATEGY) }

    /**
     * 落地页权重引导策略
     * @param currentScenes AbstractScenes?
     * @return List<Int>
     */
    fun doWeightsStrategy(currentScenes: AbstractScenes?)=weightsStrategy.getScenesTypeList(currentScenes)


    /**
     * 首页引导策略
     * @param doGuideEvent Function1<Int, Unit>
     */
    fun doGuideStrategy(doGuideEvent: (Int) -> Unit) {
        val weightsScenesTypeList = weightsStrategy.getScenesTypeList()
        if (weightsScenesTypeList.isNotEmpty()) {
            doGuideEvent(weightsScenesTypeList[0])
        } else {
            St.stRecommendPoolProtect("1")
            val guideScenesTypeList = guideStrategy.getScenesTypeList()
            if (guideScenesTypeList.isNotEmpty()) {
                doGuideEvent(guideScenesTypeList[0])
            } else {
                St.stRecommendFunctionProtect("1")
                doGuideEvent(NONE_SCENES)
            }
        }
    }




}