package com.mckj.api.client.base

import com.mckj.api.client.JunkConstants
import com.mckj.api.client.JunkExecutor
import com.mckj.api.entity.*
import com.org.openlib.help.Consumer2
import com.org.openlib.help.Consumer3


/**
 * @author leix
 * @version 1
 * @createTime 2021/9/28 11:59
 * @desc
 */
class JunkClient {

    companion object {
        const val TAG = "JunkClient"
        val instance: JunkClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { JunkClient() }
    }

    private val mExecutorMap: MutableMap<Int, JunkExecutor> by lazy { mutableMapOf() }


    fun scan(executor: JunkExecutor) {
        mExecutorMap[executor.mType]?.let {
            it.scan()
        } ?: let {
            executor.scan()
            mExecutorMap[executor.mType] = executor
        }
    }

    fun loadCache(executor: JunkExecutor, cacheJunk: CacheJunk?) {
        mExecutorMap[executor.mType]?.let {
            it.loadCache(cacheJunk)
        } ?: let {
            executor.loadCache(cacheJunk)
            mExecutorMap[executor.mType] = executor
        }
    }

    suspend fun silentScan(
        executor: JunkExecutor,
        consumer: Consumer3<Int, AppJunk?, CacheJunk?>? = null,
        block: (cacheJunk: CacheJunk) -> Unit
    ) {
        mExecutorMap[executor.mType]?.let {
            it.silentScan(consumer, block)
        } ?: let {
            executor.silentScan(consumer, block)
            mExecutorMap[executor.mType] = executor
        }
    }

    /**
     * 自清理
     */
    fun autoClean(executor: JunkExecutor, block: (junkInfo: JunkInfo) -> Unit) {
//        mExecutorMap[executor.mType] = executor
        executor.autoClean(block)
    }

    /**
     * @param type    JunkConstants.Session
     * @param junks  清理的颗粒列表
     */
    fun clean(
        type: Int,
        junks: MutableList<JunkInfo>,
        consumer2: Consumer2<Int, JunkInfo?>? = null,
        block: ((junkInfo: JunkInfo) -> Unit)? = null
    ) {
        mExecutorMap[type]?.clean(junks, consumer2, block)
    }

    /**
     * @param type    JunkConstants.Session
     * @param junks  清理的颗粒列表
     */
    suspend fun cleanBySuspend(
        type: Int,
        junks: MutableList<JunkInfo>,
        consumer2: Consumer2<Int, JunkInfo?>? = null,
        delay: Long? = 0,
        block: ((junkInfo: JunkInfo) -> Unit)? = null
    ) {
        val junkExecutor = mExecutorMap[type]
        if (junkExecutor == null) {
            consumer2?.accept(JunkConstants.ScanStatus.CLEAN, null)
        } else {
            junkExecutor.cleanBySuspend(junks, consumer2, block, delay)
        }
    }


    fun stop(type: Int) {
        mExecutorMap[type]?.stop()
    }

    fun stop(executor: JunkExecutor) {
        mExecutorMap[executor.mType]?.stop()
    }

    fun getExecutor(type: Int): JunkExecutor? {
        return mExecutorMap[type]
    }

    fun clear() {
        mExecutorMap.clear()
    }
    fun clearExecutor(type: Int) {
        mExecutorMap.remove(type)
    }
}