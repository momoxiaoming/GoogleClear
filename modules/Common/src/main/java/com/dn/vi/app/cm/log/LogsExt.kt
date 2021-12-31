/**
 * 日志的kt扩展方法。主要是方便使用
 *
 * 并优化，减少 String拼接。
 *
 * ```
 *
 * // 使用
 * val a = "wtf"
 * val b = 1024
 * debug { "打个debug日志. a=${a}, b=${b}" }
 *
 * ```
 *
 * Created by holmes on 2020/5/19.
 **/
package com.dn.vi.app.cm.log


/**
 * 是否输出[level]级别的日志
 * 一般配合if, 减少非必须 runtime 字符串拼接
 */
fun considerLog(level: Int): Boolean {
    if (VLog.getLogLevel() <= level) {
        return true
    }
    return false
}

/**
 * Log debug
 */
inline fun debug(message: () -> Any?) {
    if (considerLog(VLog.LEVEL_DEBUG)) {
        VLog.d(message()?.toString())
    }
}

/**
 * Log info
 */
inline fun info(message: () -> Any?) {
    if (considerLog(VLog.LEVEL_INFO)) {
        VLog.i(message()?.toString())
    }
}

/**
 * Log warn
 */
inline fun warn(message: () -> Any?) {
    if (considerLog(VLog.LEVEL_WARNING)) {
        VLog.w(message()?.toString())
    }
}

/**
 * Log error
 */
inline fun error(message: () -> Any?) {
    if (considerLog(VLog.LEVEL_ERROR)) {
        VLog.e(message()?.toString())
    }
}

/**
 * Log error, with exception
 */
inline fun error(t: Throwable?, message: () -> Any?) {
    if (considerLog(VLog.LEVEL_ERROR)) {
        val msg = message()?.toString()
        VLog.e(msg)
        if (t != null) {
            VLog.printErrStackTrace(t, msg)
        }
    }
}


