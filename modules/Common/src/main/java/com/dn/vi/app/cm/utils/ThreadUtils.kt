package com.dn.vi.app.cm.utils

import android.os.HandlerThread

/**
 * 针对Android的 Thread 环境
 * Created by holmes on 2020/5/21.
 **/
object ThreadUtils {

    private val workThread by lazy {
        val workThread = HandlerThread("work:handler")
        workThread.start()
        return@lazy workThread
    }

    /**
     * 一个后台工作线程。
     * 通过 Handler来通信
     */
    fun getWorkHandlerThread(): HandlerThread = workThread

}