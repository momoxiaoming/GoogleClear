package com.mckj.template.impl

import com.mckj.api.entity.CacheJunk

/**
 * @author xx
 * @version 1
 * @createTime 2022/2/28 11:00
 * @desc
 */
interface ICleanStatus {
    fun denyStatus()
    fun defaultScanStatus()
    fun scanIdle(cacheJunk: CacheJunk?, animEnable: Boolean = true)
    fun scanEnd(cacheJunk: CacheJunk?)
    fun completeCleanStatus(delayTime: Long = 2000L)
}