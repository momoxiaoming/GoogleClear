package com.mckj.datalib.manager

import com.mckj.datalib.entity.GuideData

/**
 * Describe:
 *
 * Created By yangb on 2021/3/24
 */
class GuideManager private constructor() {

    companion object {
        const val TAG = "GuideManager"

        private val INSTANCE by lazy { GuideManager() }

        fun getInstance(): GuideManager = INSTANCE
    }

    /**
     * 清理类集合
     *
     * key-类型 GuideData
     */
    private val mGuideMap: MutableMap<Int, GuideData> by lazy { mutableMapOf() }

    fun register(type: Int, data: GuideData) {
        mGuideMap[type] = data
    }

    fun register(data: GuideData) {
        register(data.type, data)
    }

    fun unregister(type: Int) {
        mGuideMap.remove(type)
    }

    fun unregisterAll() {
        mGuideMap.clear()
    }

    /**
     * 获取清理对象
     */
    fun getGuideData(type: Int): GuideData? {
        return mGuideMap[type]
    }

}