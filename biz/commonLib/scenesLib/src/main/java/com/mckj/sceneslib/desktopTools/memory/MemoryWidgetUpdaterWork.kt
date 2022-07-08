package com.mckj.sceneslib.desktopTools.memory

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.org.openlib.utils.ProcessUtil

/**
 * Describe:桌面控件更新
 *
 * Created By yangb on 2020/11/4
 */
class MemoryWidgetUpdaterWork(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    companion object {
        const val TAG = "SpeedWidgetUpdaterWork"
    }

    override fun doWork(): Result {
        MemoryWidgetUtil.updateMemoryWidget(applicationContext, ProcessUtil.getUsedMemoryPercent())
        MemoryWorks.startUpdateWidgetWork(applicationContext, 5)
        return Result.success()
    }

}