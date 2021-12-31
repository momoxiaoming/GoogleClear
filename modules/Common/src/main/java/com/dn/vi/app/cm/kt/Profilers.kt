package com.dn.vi.app.cm.kt

import android.util.Log
import com.dn.vi.app.cm.log.VLog
import kotlin.system.measureTimeMillis

/**
 * 一些分析扩展
 * Created by holmes on 2020/5/20.
 **/
object Profilers {
    /**
     * 如果要开启耗时日志，就手动打开这个
     */
    const val enablePrint = false
}

/**
 * 输出运行时长日志
 * XLog输出
 */
inline fun printTimeMillis(tag: String, block: () -> Unit): Long {
    val cost = measureTimeMillis(block)
    VLog.scoped("timeUsage").d("[$tag] cost: ${cost} ms")
    return cost
}

/**
 * 输出运行时长日志
 * 使用系统 Log输出
 */
inline fun printTimeMillisSysLog(tag: String, block: () -> Unit): Long {
    if (!Profilers.enablePrint) {
        block()
        return 0L
    }
    val cost = measureTimeMillis(block)
    Log.d("timeUsage", "[$tag] cost: ${cost} ms")
    return cost
}

/**
 * 获取[compareTime]与当前时间的时间差
 */
inline fun timeNowDelta(compareTime: Long): Long {
    return Math.abs(System.currentTimeMillis() - compareTime)
}
