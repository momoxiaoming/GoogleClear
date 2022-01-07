package com.mckj.sceneslib.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dn.baselib.util.ProcessUtil
import com.dn.vi.app.base.app.AppMod
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.util.Log
import com.mckj.sceneslib.util.NotifyUtil

/**
 * Describe:
 *
 * Created By yangb on 2020/11/4
 */
class AppBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val TAG = "AppBroadcastReceiver"

        private val PACKAGE_NAME = AppMod.app.packageName

        val ACTION_HOME_NOTIFY = "${PACKAGE_NAME}.action.home.notify"
        val ACTION_HOME_WIDGET = "${PACKAGE_NAME}.action.home.widget"
        val ACTION_SPEED_NOTIFY = "${PACKAGE_NAME}.action.speed.notify"
        val ACTION_SPEED_WIDGET = "${PACKAGE_NAME}.action.speed.widget"
        val ACTION_NOTIFY_WIDGET_ADDED = "${PACKAGE_NAME}.action.WIDGET_ADDED"

    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        Log.i(TAG, "onReceive: action:$action")
        when (action) {
            ACTION_SPEED_NOTIFY -> {
                NotifyUtil.collapseStatusBar(context!!)

                jumpSpeedActivity()
            }
            ACTION_SPEED_WIDGET -> {

                jumpSpeedActivity()
            }
            ACTION_HOME_NOTIFY -> {
                NotifyUtil.collapseStatusBar(context!!)

                jumpHomeActivity()
            }
            ACTION_HOME_WIDGET -> {
                jumpHomeActivity()
            }
            ACTION_NOTIFY_WIDGET_ADDED -> {

            }
        }
    }

    private fun jumpSpeedActivity() {
        ScenesManager.getInstance().jumpPage(AppMod.app, ScenesType.TYPE_ONE_KEY_SPEED)
    }

    private fun jumpHomeActivity() {
        //是否运行在最前台
        val isRunningForeground = ProcessUtil.isRunningForeground()
        Log.i(TAG, "jumpHomeActivity: isRunningForeground:$isRunningForeground")
        if (!isRunningForeground) {
            val result = ProcessUtil.setTopApp()
            Log.i(TAG, "jumpHomeActivity: result:$result")
            if (!result) {
//                ARouter.getInstance().build(ARouterPath.APP_ACTIVITY_SPLASH).navigation()
            }
        }
    }

}