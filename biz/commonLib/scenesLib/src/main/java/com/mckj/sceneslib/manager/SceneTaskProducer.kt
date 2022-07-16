package com.mckj.sceneslib.manager

import android.util.Log
import com.mckj.sceneslib.gen.ScenesSp
import com.mckj.sceneslib.manager.scenes.ScenesManager


/**
 * @author xx
 * @version 1
 * @createTime 2021/11/12 11:18
 * @desc
 */
class SceneTaskProducer {
    companion object {
        const val TAG = "SceneTaskProducer"
        const val TIME_LIMIT = 1000 * 60 * 60 * 4

        private val INSTANCE by lazy { SceneTaskProducer() }

        fun getInstance(): SceneTaskProducer = INSTANCE
    }


    fun executeAutoTask(limit: Boolean = false) {
//        when {
//            limit && inTimes() -> realTask()
//            !limit -> realTask()
//        }
        if (inTimes()) {
            Log.d(TAG,"冷却时间内，不执行任务")
            return
        }
        ScenesSp.instance.executorTaskTime = System.currentTimeMillis()
        realTask()
    }

    private fun realTask() {
        val scenesMap = ScenesManager.getInstance().getScenesMap()
        scenesMap.forEach { (_, scene) ->
            scene.executorTask()
        }
    }

    fun stopTask() {
        val scenesMap = ScenesManager.getInstance().getScenesMap()
        scenesMap.forEach { (_, scene) ->
            scene.stopTask()
        }
    }


    private fun inTimes(): Boolean {
        return (ScenesSp.instance.executorTaskTime - System.currentTimeMillis()) < TIME_LIMIT
    }
}