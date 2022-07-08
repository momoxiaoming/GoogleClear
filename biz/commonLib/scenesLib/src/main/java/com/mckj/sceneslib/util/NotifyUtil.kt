package com.mckj.sceneslib.util

import android.content.Context
import android.os.Build


/**
 * Describe:通知栏工具类
 *
 * Created By yangb on 2020/10/31
 */

object NotifyUtil {

    fun collapseStatusBar(context: Context) {
        try {
            val statusBarManager = context.getSystemService("statusbar")
            val collapse = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
                statusBarManager.javaClass.getMethod("collapse")
            } else {
                statusBarManager.javaClass.getMethod("collapsePanels")
            }
            collapse.invoke(statusBarManager)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
