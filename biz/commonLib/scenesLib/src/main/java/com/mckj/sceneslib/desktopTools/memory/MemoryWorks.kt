package com.mckj.sceneslib.desktopTools.memory

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * work 的管理
 * Created by holmes on 2020/9/9.
 **/
object MemoryWorks {

    var mDoWorkEnable = false

    @JvmStatic
    fun startUpdateWidgetWork(context: Context, delaySec: Long) {
        if (!mDoWorkEnable) {
            return
        }
        val req = OneTimeWorkRequestBuilder<MemoryWidgetUpdaterWork>()
            .let {
                if (delaySec > 0L) {
                    it.setInitialDelay(delaySec, TimeUnit.SECONDS)
                } else {
                    it
                }
            }
            .build()
        WorkManager.getInstance(context)
            .enqueueUniqueWork("SpeedWidgetUpdaterWork", ExistingWorkPolicy.REPLACE, req)
    }


}