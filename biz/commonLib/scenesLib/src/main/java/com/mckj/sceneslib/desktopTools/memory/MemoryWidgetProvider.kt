package com.mckj.sceneslib.desktopTools.memory

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import com.mckj.baselib.helper.getApplicationContext
import com.mckj.baselib.util.Log
import com.org.openlib.utils.ProcessUtil

import com.org.proxy.EvAgent

/**
 * Describe:桌面控件-加速
 *
 * Created By yangb on 2020/11/4
 */
class MemoryWidgetProvider : AppWidgetProvider() {

    companion object {
        const val TAG = "SpeedWidgetProvider"
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i(TAG, "onUpdate: ")
        appWidgetManager ?: return
        appWidgetIds ?: return
        appWidgetIds.forEach { ids ->
            appWidgetManager.updateAppWidget(
                ids,
                MemoryWidgetUtil.getMemoryRemoveViews(
                    context ?: getApplicationContext(),
                    ProcessUtil.getUsedMemoryPercent()
                )
            )
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.i(TAG, "onDeleted: ")
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.i(TAG, "onEnabled: ")
        EvAgent.sendEvent("widget_long_create")
        MemoryWorks.mDoWorkEnable = true
        MemoryWorks.startUpdateWidgetWork(context ?: getApplicationContext(), 0)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.i(TAG, "onDisabled: ")
        MemoryWorks.mDoWorkEnable = false
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i(TAG, "onReceive: ")
    }

}