package com.mckj.module.wifi.manager

import com.mckj.module.wifi.entity.FloatData
import com.mckj.module.wifi.entity.FloatEntity

/**
 * Describe:悬浮控件数据管理
 *
 * Created By yangb on 2021/1/25
 */
class FloatDataManager {

    companion object {
        const val TAG = "FloatDataManager"

        /**
         * 倒计时间隔 10分钟
         */
        const val MAX_COUNT_DOWN = 10 * 60 * 1000

        private val INSTANCE by lazy { FloatDataManager() }

        fun getInstance(): FloatDataManager = INSTANCE

    }

    /**
     * 浮动控件数据集
     */
    private val mFloatData: FloatData by lazy {
        getData()
    }


    /**
     * 获取悬浮数据
     */
    private fun getData(): FloatData {
        var floatData = FloatData.getObject()
        if (floatData == null) {
            val goldList = mutableListOf<FloatEntity>()
            val lotteryList = mutableListOf<FloatEntity>()
            val wheelList = mutableListOf<FloatEntity>()
            floatData = FloatData(goldList, lotteryList, wheelList)
        }
        return floatData
    }

    /**
     * 保存数据
     */
    fun saveData() {
        FloatData.saveObject(mFloatData)
    }

    fun getFloatEntity(index: Int, type: Int): FloatEntity {
        return when (type) {
            FloatEntity.TYPE_LOTTERY -> {
                getFloatLotteryEntity(index)
            }
            FloatEntity.TYPE_WHEEL -> {
                getFloatLotteryEntity(index)
            }
            else -> {
                getFloatWheelEntity(index)
            }
        }
    }


    /**
     * 获取悬浮金币
     */
    fun getFloatGoldEntity(index: Int): FloatEntity {
        var entity = mFloatData.goldList.find { it.index == index }
        if (entity == null) {
            entity = FloatEntity(index, FloatEntity.TYPE_GOLD)
            mFloatData.goldList.add(entity)
        }
        return entity
    }

    /**
     * 获取悬浮抽奖
     */
    fun getFloatLotteryEntity(index: Int): FloatEntity {
        var entity = mFloatData.lotteryList.find { it.index == index }
        if (entity == null) {
            entity = FloatEntity(index, FloatEntity.TYPE_LOTTERY)
            mFloatData.lotteryList.add(entity)
        }
        return entity
    }

    /**
     * 获取悬浮大转盘
     */
    fun getFloatWheelEntity(index: Int): FloatEntity {
        var entity = mFloatData.wheelList.find { it.index == index }
        if (entity == null) {
            entity = FloatEntity(index, FloatEntity.TYPE_WHEEL)
            mFloatData.wheelList.add(entity)
        }
        return entity
    }

}