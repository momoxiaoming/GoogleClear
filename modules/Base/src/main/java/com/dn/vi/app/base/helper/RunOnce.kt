package com.dn.vi.app.base.helper

/**
 * 只运行一次
 * Created by holmes on 19-11-7.
 */
abstract class RunOnce : Runnable {

    @Volatile
    private var times: Int = 0

    final override fun run() {
        if (times > 0) {
            return
        }
        synchronized(this) {
            if (times > 0) {
                return
            }
            times++
        }
        running()
    }

    /**
     * 重置计数。
     * 重置后，[running]会再次运行
     */
    fun reset() {
        synchronized(this) {
            times = 0
        }
    }

    /**
     * 具体的事
     */
    abstract fun running()

}

/**
 * Create a run once Runnable
 */
inline fun runOnce(crossinline block: () -> Unit): Runnable {
    return object : RunOnce() {
        override fun running() {
            block()
        }
    }
}