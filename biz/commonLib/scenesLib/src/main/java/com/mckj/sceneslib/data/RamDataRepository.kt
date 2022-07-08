package com.mckj.sceneslib.data

import com.mckj.sceneslib.data.model.impl.RamDataImpl

/**
 * Describe:内存管理
 *
 * Created By yangb on 2020/10/21
 */
class RamDataRepository {

    companion object {
        const val TAG = "WifiCheckRepository"
    }

    /**
     * 内存数据
     */
    private val mRamData by lazy { RamDataImpl() }

    /**
     * 内存清理
     */
    suspend fun killAllProcessesToPercent(): Float {
        return mRamData.killAllProcessesToPercent()
    }

}