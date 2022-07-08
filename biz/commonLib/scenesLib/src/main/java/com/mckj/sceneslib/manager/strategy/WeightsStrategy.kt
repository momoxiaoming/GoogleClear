package com.mckj.sceneslib.manager.strategy

import com.mckj.sceneslib.manager.scenes.*
import com.mckj.sceneslib.manager.strategy.helper.StrategyType
import com.mckj.sceneslib.manager.strategy.helper.WeightsBean
import com.mckj.sceneslib.manager.strategy.helper.WeightsScenesProvider

class WeightsStrategy :AbsStrategy(){


    override val strategyType: Int
        get() = StrategyType.WEIGHTS_STRATEGY

    private val poolsSrc = WeightsScenesProvider.weightsStrategySrc

    /**
     *筛选有权重场景
     * @param scenesType Int
     */
    private fun filterScenesWights(scenesType: Int,list: MutableList<WeightsBean>){
        poolsSrc.forEach {
            if (it.scenesType == scenesType) {
                if (!list.contains(it)) {
                    list.add(it)
                }
            }
        }
    }

    /**
     * 获取第一梯队推荐场景
     * @param currentScenes AbstractScenes
     * @return List<Int>
     */
   override fun getScenesTypeList(currentScenes: AbstractScenes?):List<Int>{
        val weightsList = arrayListOf<WeightsBean>()
        val iterator = mScenesManager.getScenesMap().iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            //筛选权重场景
            filterScenesWights(next.key, weightsList)
        }
        val list = arrayListOf<Int>()
        //根据权重降序排序权重场景
        val sortedByDescending = weightsList.sortedByDescending { it.weight }
        //当前执行场景、处于保护时间不加入推荐列表
        for (it in sortedByDescending) {
            val scenes = mScenesManager.getScenes(it.scenesType)
            if (scenes!=null) {
                if (currentScenes != null) {
                    if (currentScenes.repulsionTypes()?.contains(it.scenesType) == true) {
                        continue
                    }
                }
                if (scenes.checkSafeTimeState()){
                    continue
                }
                list.add(it.scenesType)
            }
        }
        return list
    }




}
