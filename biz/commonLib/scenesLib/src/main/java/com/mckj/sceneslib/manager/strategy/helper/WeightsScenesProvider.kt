package com.mckj.sceneslib.manager.strategy.helper

import com.mckj.sceneslib.manager.scenes.ScenesType

object WeightsScenesProvider {


    val guideStrategySrc by lazy {
        arrayListOf(
            ScenesType.TYPE_ANTIVIRUS,
            ScenesType.TYPE_UNINSTALL_CLEAN,
            ScenesType.TYPE_SHORT_VIDEO_CLEAN,
            ScenesType.TYPE_NETWORK_SPEED
        )
    }

     val weightsStrategySrc by lazy {
        arrayListOf(
            WeightsBean(7, ScenesType.TYPE_JUNK_CLEAN),
            WeightsBean(10, ScenesType.TYPE_PHONE_SPEED),
            WeightsBean(9, ScenesType.TYPE_COOL_DOWN),
            WeightsBean(8, ScenesType.TYPE_POWER_SAVE)
        )
    }

   val allWeightsScenesSrc  = arrayListOf<Int>()

    init {
        collectWeightsScenes()
    }

    /**
     * 收集权重场景
     */
    private fun collectWeightsScenes(){
        allWeightsScenesSrc.clear()
        weightsStrategySrc.forEach {
            allWeightsScenesSrc.add(it.scenesType)
        }
        guideStrategySrc.forEach {
            allWeightsScenesSrc.add(it)
        }
    }

    /**
     * 判断是否是第一梯队场景
     * @return Boolean
     */
     fun checkIsFirstEchelon(scenesType: Int): Boolean {
        var state = false
        for (it in weightsStrategySrc) {
            if (it.scenesType == scenesType) {
                state = true
                break
            }
        }
        return state
    }

}
data class WeightsBean(val weight:Int,val scenesType:Int)