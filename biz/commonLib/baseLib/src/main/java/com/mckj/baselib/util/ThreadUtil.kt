package com.mckj.baselib.util

import android.os.Looper

/**
 * Describe:
 *
 * Created By yangb on 2020/11/19
 */
object ThreadUtil {

    /**
     * 判断是否为主线程
     */
    fun isMainThread(): Boolean {
        return Thread.currentThread() == Looper.getMainLooper().thread
    }

}