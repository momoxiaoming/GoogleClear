package com.dn.openlib.time

import android.os.SystemClock

/**
 * 小工具，用于统计两点时间 间隔
 *
 * 每调用一次[end]都会刷新 end节点。
 * 可以调多次[end]。
 *
 * 调用[cost]来获取[begin]与最后一次[end]的之间的间隔
 *
 * ```
 * begin()
 * end()
 * cost()
 *
 * end()
 * cost
 * ```
 *
 * Created by holmes on 2020/7/1.
 **/
class TimeElapse {

    companion object {
        const val NONE = -1L
    }

    private var beginTime: Long = 0L
    private var endTime: Long = 0L

    /**
     * end与begin节点之间的时间间隔
     *
     * 如果没有调用过[end]，则是以当前时间与begin的间隔
     *
     * 如果没有调用过[begin]，则是[NONE](-1)
     */
    fun cost(): Long {
        if (beginTime == 0L) {
            return NONE
        }
        val node = endTime.let {
            if (it > 0L) {
                it
            } else {
                SystemClock.elapsedRealtime()
            }
        }
        return Math.abs(beginTime - node)
    }

    fun begin(): Long {
        beginTime = SystemClock.elapsedRealtime()
        endTime = 0L
        return beginTime
    }

    fun end(): Long {
        endTime = SystemClock.elapsedRealtime()
        return 0
    }

    /**
     * 如果[cost]比[minimum]大，则返回cost, 并重置[begin]
     *
     *  如果[cost] 为 [NONE], 也会重置[begin]， 返回 0L
     *
     * 否则返回 [NONE](-1)
     *
     * 常规用法:
     * ```
     * if (timeElapse.resetBeginIfCostThan(INTERVAL) != TimeElapse.NONE) {
     *  // do something
     * } else {
     *  // too fast
     * }
     * ```
     *
     */
    fun resetBeginIfCostThan(minimum: Long): Long {
        val cost = cost()
        if (cost == NONE ||
            cost > minimum
        ) {
            begin()
            return Math.max(cost, 0L)
        }
        return NONE
    }

}