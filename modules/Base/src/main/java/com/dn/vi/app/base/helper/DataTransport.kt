package com.dn.vi.app.base.helper

import android.os.Handler
import com.dn.vi.app.cm.utils.ThreadUtils
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * 共享内存对象用的.
 * 默认8秒后，对象过期, 自动清理
 * @author holmes on 11/25/15.
 */
class DataTransport private constructor() {

    private val mTransPool: HashMap<Any, Any> = HashMap<Any, Any>(16)
    private val mLock: ReentrantReadWriteLock = ReentrantReadWriteLock()

    private val mGcHandler: Handler
    private val mGc: Gc

    init {
        val ht = ThreadUtils.getWorkHandlerThread()
        mGcHandler = Handler(ht.looper)
        mGc = Gc()
    }

    /**
     * 添加一个对象
     * @param key
     * *
     * @param obj
     */
    fun put(key: Any, obj: Any) {
        if (obj == null) {
            return
        }
        delayGc()
        mLock.writeLock().lock()
        try {
            mTransPool.put(key, obj)
        } finally {
            mLock.writeLock().unlock()
        }

    }

    /**
     * 获取一个对象,
     * 读取一次后，就会自动被清除
     * @param target
     * *
     * @return
     */
    fun get(target: Any): Any? {
        var v: Any? = null
        mLock.readLock().lock()
        try {
            v = mTransPool[target]
        } finally {
            mLock.readLock().unlock()
        }

        if (v != null) {
            remove(target)
        }

        return v
    }

    /**
     * 移除一个对象
     * @param target
     */
    fun remove(target: Any) {
        mLock.writeLock().lock()
        try {
            mTransPool.remove(target)
        } finally {
            mLock.writeLock().unlock()
        }
    }

    /**
     * 延迟GC
     */
    private fun delayGc() {
        mGcHandler.removeCallbacks(mGc)
        mGcHandler.postDelayed(mGc, GC_SCHEDULE)
    }

    private inner class Gc : Runnable {

        override fun run() {
            mLock.writeLock().lock()
            try {
                mTransPool.clear()
            } finally {
                mLock.writeLock().unlock()
            }

        }

    }

    companion object {

        /** 对象回收时间  */
        const val GC_SCHEDULE = 8000L

        @JvmStatic
        private var sInstance: DataTransport? = null

        @JvmStatic
        fun getInstance(): DataTransport {
            if (sInstance == null) {
                sInstance = DataTransport()
            }
            return sInstance!!
        }

    }
}
