package com.mckj.sceneslib.manager.strategy

import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.strategy.helper.WeightsScenesProvider

abstract class AbsStrategy {

   companion object{
      private const val SAFE_TIME = 3 * 60 * 1000
   }

   abstract val strategyType: Int
   protected val mScenesManager by lazy { ScenesManager.getInstance() }

   /**
    * 刷新场景保护时间
    * @param scenesType Int
    */
   fun refreshScenesPopTime(scenesType: Int) {
      if (!checkScenesSafeTimeState(scenesType)) {
         val scenes = mScenesManager.getScenes(scenesType)
         scenes?.let {
            it.popTime = System.currentTimeMillis()
         }
      }
   }

   /**
    * 检查场景是否处于保护时间
    * @param scenesType Int
    * @return Boolean
    */
   fun checkScenesSafeTimeState(scenesType: Int): Boolean {
      val scenes = mScenesManager.getScenes(scenesType)
      return if (scenes != null) {
         System.currentTimeMillis() - scenes.popTime < SAFE_TIME
      } else {
         false
      }
   }

   /**
    * 是否是既定场景
    * @param scenesType Int
    * @return Boolean
    */
   fun isWeightsScenes(scenesType: Int)= WeightsScenesProvider.allWeightsScenesSrc.contains(scenesType)


  abstract fun  getScenesTypeList(currentScenes: AbstractScenes?=null):List<Int>
}