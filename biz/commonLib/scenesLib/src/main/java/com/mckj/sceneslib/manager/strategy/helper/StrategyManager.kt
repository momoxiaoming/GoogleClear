package com.mckj.sceneslib.manager.strategy.helper

import com.mckj.sceneslib.manager.strategy.AbsStrategy
import com.mckj.sceneslib.manager.strategy.GuideStrategy
import com.mckj.sceneslib.manager.strategy.WeightsStrategy

class StrategyManager private constructor(){

    companion object{
        private val INSTANCE by lazy { StrategyManager() }
        fun getInstance() = INSTANCE

    }
    private val mStrategyMap  = hashMapOf<Int, AbsStrategy>()

    init {
        registerStrategy(WeightsStrategy())
        registerStrategy(GuideStrategy())
    }

   private fun registerStrategy(strategy: AbsStrategy){
        mStrategyMap[strategy.strategyType] = strategy
    }


    fun getStrategy(strategyType: Int):AbsStrategy{
        return mStrategyMap[strategyType]!!
    }

}