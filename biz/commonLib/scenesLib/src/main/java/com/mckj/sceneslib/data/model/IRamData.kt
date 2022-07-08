package com.mckj.sceneslib.data.model

/**
 * Describe:内存优化
 *
 * Created By yangb on 2020/10/22
 */
interface IRamData {

    /**
     * 清理内存
     */
    suspend fun killAllProcessesToPercent(): Float

}