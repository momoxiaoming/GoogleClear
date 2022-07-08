package com.mckj.sceneslib.desktopTools.memory

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import com.mckj.sceneslib.R
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.AppBroadcastReceiver
import com.mckj.sceneslib.util.Log

/**
 * Describe:
 *
 * Created By yangb on 2020/11/4
 */
object MemoryWidgetUtil {

    const val TAG = "AppWidgetUtil"
    const val REQ_CODE = 10086

    /**
     * 启动更新
     */
    fun startUpdate(context: Context): Boolean {
        //初始化桌面快捷任务
        if (isAddMemoryAppWidget(context)) {
            MemoryWorks.mDoWorkEnable = true
            MemoryWorks.startUpdateWidgetWork(context, 0)
            return true
        }
        return false
    }


    /**
     * 是否添加桌面快捷键
     */
    fun isAddMemoryAppWidget(context: Context): Boolean {
        var result: Boolean = false
        do {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val manager = getAppWidgetManager(context)
                val componentName = ComponentName(context, MemoryWidgetProvider::class.java)
                result = isExists(manager, componentName)
            }
        } while (false)
        return result
    }

    fun startWork(context: Context) {
        if (!isAddMemoryAppWidget(context)) {
            addMemoryAppWidget(context)
            startUpdate(context)
        }
    }

    fun addMemoryAppWidget(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getAppWidgetManager(context)
            val componentName = ComponentName(context, MemoryWidgetProvider::class.java)
            if (isExists(manager, componentName)) {
                Log.i(TAG, "addSpeedAppWidget error: 桌面控件已经存在")
                return
            }
            try {
                val callback = PendingIntent.getBroadcast(
                    context, REQ_CODE,
                    Intent().also { intent ->
                        intent.action = "com.mckj.wifispeed.action.WIDGET_ADDED"
                        intent.setClass(context, AppBroadcastReceiver::class.java)
                    },
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                val result = manager.requestPinAppWidget(
                    componentName,
                    null,
                    callback
                )
                St.stDesktopWidgetSetSuccess(result.toString())
                Log.i(TAG, "addSpeedAppWidget: result:$result")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMemoryWidget(context: Context, memoryUsed: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getAppWidgetManager(context)
            val componentName = ComponentName(context, MemoryWidgetProvider::class.java)
            try {
                val ids = manager.getAppWidgetIds(componentName) ?: return
                ids.forEach {
                    manager.updateAppWidget(it, getMemoryRemoveViews(context, memoryUsed))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getMemoryRemoveViews(context: Context, memoryUsed: Long): RemoteViews? {
        try {
            val remoteViews = RemoteViews(context.packageName, R.layout.scenes_widget_speed)
            remoteViews.setTextViewText(
                R.id.percent,
                "${memoryUsed}% "
            )

            remoteViews.setImageViewResource(R.id.percent_icon, getIconRes(memoryUsed))
            remoteViews.setOnClickPendingIntent(
                R.id.rootView,
                getMemoryPendingIntent(context, AppBroadcastReceiver.ACTION_MEMORY_WIDGET)
            )
            return remoteViews
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getIconRes(memoryUsed: Long): Int {
        return when {
            memoryUsed == 0L -> R.drawable.scenes_cir_1
            (memoryUsed in 1..9) -> R.drawable.scenes_cir_2
            (memoryUsed in 10..19) -> R.drawable.scenes_cir_3
            (memoryUsed in 20..29) -> R.drawable.scenes_cir_4
            (memoryUsed in 30..39) -> R.drawable.scenes_cir_5
            (memoryUsed in 40..49) -> R.drawable.scenes_cir_6
            (memoryUsed in 50..59) -> R.drawable.scenes_cir_7
            (memoryUsed in 60..69) -> R.drawable.scenes_cir_8
            (memoryUsed in 70..79) -> R.drawable.scenes_cir_9
            (memoryUsed in 80..89) -> R.drawable.scenes_cir_10
            (memoryUsed in 90..99) -> R.drawable.scenes_cir_11
            memoryUsed == 100L -> R.drawable.scenes_cir_12
            else -> R.drawable.scenes_cir_1
        }
    }

    private var intent: Intent? = null
    private fun getMemoryPendingIntent(context: Context, action: String): PendingIntent {
        if (intent == null) {
            intent = Intent(action)
            intent?.setClass(context, AppBroadcastReceiver::class.java)
        }
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun isExists(manager: AppWidgetManager, componentName: ComponentName): Boolean {
        var result = false
        try {
            val ids = manager.getAppWidgetIds(componentName)
            result = ids != null && ids.isNotEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getAppWidgetManager(context: Context): AppWidgetManager {
        return context.getSystemService(AppWidgetManager::class.java)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, block: () -> Unit) {
        if (requestCode == REQ_CODE) {
            block.invoke()
        }
    }
}