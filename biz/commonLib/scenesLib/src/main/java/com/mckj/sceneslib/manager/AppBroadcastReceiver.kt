package com.mckj.sceneslib.manager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.kt.arouter
import com.dn.vi.app.cm.log.VLog
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.utils.ProcessUtil
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.PopupScenesType
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.manager.scenes.model.OneKeySpeedScenes
import com.mckj.sceneslib.util.Log
import com.mckj.sceneslib.util.NotifyUtil
import com.org.proxy.EvAgent

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
        val ACTION_MEMORY_WIDGET = "${PACKAGE_NAME}.action.memory.widget"

        //清理海外弹窗广播
        val ACTION_OVERSEAS_CLICK = "${PACKAGE_NAME}.action.overseas.click"
        //用户是否在桌面状态改变
        val ACTION_HOME_STATUS = "${PACKAGE_NAME}.action.homestatus.change"

        //壁纸火箭出现
        val ACTION_OVS_ROCKET_STATUS = "${PACKAGE_NAME}.action.homestatus.wall.change"

    }

    private val log by lazy {
        VLog.scoped(TAG)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        log.i("onReceive: action:$action")
        when (action) {
            ACTION_SPEED_NOTIFY -> {
                NotifyUtil.collapseStatusBar(context!!)
                EvAgent.sendEvent("notify_speedup_click")
                jumpSpeedActivity()
            }
            ACTION_SPEED_WIDGET -> {
                EvAgent.sendEvent("widget_long_icon_click")
                jumpSpeedActivity()
            }
            ACTION_HOME_NOTIFY -> {
                NotifyUtil.collapseStatusBar(context!!)
                EvAgent.sendEvent("notify_home_click")
                jumpHomeActivity()
            }
            ACTION_HOME_WIDGET -> {
                jumpHomeActivity()
            }
            ACTION_NOTIFY_WIDGET_ADDED -> {
                EvAgent.sendEvent("app_small_tools_add")
            }
            ACTION_MEMORY_WIDGET -> {
                St.stDesktopWidgetPrePop()
                jumpSpeedActivity()
            }

            ACTION_OVERSEAS_CLICK -> {
                //海外清理点击事件
                val scenes_type = intent.getIntExtra("scenes_type", -1)
                val bizScenesType = getBizScenesTypeByPopScenesType(scenes_type)
                log.i("bizScenesType = $bizScenesType , scenes_type = $scenes_type")
                if (bizScenesType != -1) {
                    jumpOverSeasClickScenes(context,bizScenesType)
                }
            }
        }
    }

    /**
     * 通过ng的站外弹窗类型，匹配到对应的站内场景
     * @param scenesType ng的站外弹窗类型 [PopupScenesType]
     * @return 站内场景类型。[ScenesType]
     *
     * 如果没有匹配到，返回-1
     */
    private fun getBizScenesTypeByPopScenesType(scenesType: Int): Int {
        return when (scenesType) {
            PopupScenesType.TYPE_RECHARGE_OUT,
            PopupScenesType.TYPE_RECHARGE_IN,
            PopupScenesType.TYPE_BATTERY_LEVEL -> {
                //超级省电
                ScenesType.TYPE_POWER_SAVE
            }

            PopupScenesType.TYPE_APP_INSTALL,
            PopupScenesType.TYPE_APP_REMOVE,
            PopupScenesType.TYPE_UNINSTALL_CLEAN -> {
                //卸载残留
                ScenesType.TYPE_UNINSTALL_CLEAN

            }

            PopupScenesType.TYPE_PHONE_SPEED,
            PopupScenesType.TYPE_RAM -> {
                //手机加速
                ScenesType.TYPE_PHONE_SPEED
            }

            PopupScenesType.TYPE_CATON -> {
                //卡顿优化
                ScenesType.TYPE_CATON_SPEED
            }

            PopupScenesType.TYPE_JUNK -> {
                //垃圾清理
                ScenesType.TYPE_JUNK_CLEAN
            }

            else -> {
                //nothing
                -1
            }
        }
    }

    private fun jumpOverSeasClickScenes(context: Context?, scenesType: Int) {

        Log.i(TAG, "sceneType:$scenesType")
        if (context == null || scenesType == -1) {
            Log.i(TAG, "context == null")
            return
        }
        val scene = ScenesManager.getInstance().getScenes(scenesType)
        if (scene == null) {
            Log.i(TAG, "注册场景")
            ScenesManager.getInstance().registerByType(scenesType)
        }
        ScenesManager.getInstance().jumpPage(AppMod.app, scenesType,"out")
    }


    private fun jumpSpeedActivity() {
        ScenesManager.getInstance().register(OneKeySpeedScenes())
        ScenesManager.getInstance().jumpPage(AppMod.app, ScenesType.TYPE_ONE_KEY_SPEED)
        St.stDesktopWidgetPop()
    }


    private fun jumpHomeActivity() {
        //是否运行在最前台
        val isRunningForeground = ProcessUtil.isRunningForeground()
        Log.i(TAG, "jumpHomeActivity: isRunningForeground:$isRunningForeground")
        if (!isRunningForeground) {
            val result = ProcessUtil.setTopApp()
            Log.i(TAG, "jumpHomeActivity: result:$result")
            if (!result) {
                ARouter.getInstance().build(ARouterPath.APP_ACTIVITY_SPLASH).navigation()
            }
        }
    }

}