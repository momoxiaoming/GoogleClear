package com.mckj.sceneslib.manager.strategy

import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.strategy.helper.StrategyType
import com.mckj.sceneslib.manager.strategy.helper.WeightsScenesProvider

class GuideStrategy : AbsStrategy() {

    override val strategyType: Int
        get() = StrategyType.GUIDE_STRATEGY


    /**
     * 获取第二梯队推荐场景
     * @return List<Int>
     */
     override fun getScenesTypeList(currentScenes: AbstractScenes?): List<Int> {
        val list = arrayListOf<Int>()
        for (scenesType in WeightsScenesProvider.guideStrategySrc) {
                if (checkScenesSafeTimeState(scenesType)) {
                    continue
                }
                list.add(scenesType)
            }
        return list.shuffled()
    }


}