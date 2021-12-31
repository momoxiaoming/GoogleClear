package com.mckj.module.wifi.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.mckj.baselib.helper.getApplication
import com.mckj.sceneslib.R
import com.mckj.sceneslib.manager.AppBroadcastReceiver
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.tz.gg.appproxy.AppProxy
import com.tz.gg.appproxy.evs.AuditListener


/**
 * Describe:wifi通知栏工具类
 *
 * Created By yangb on 2020/10/31
 */

object WifiNotifyUtil {

    const val TAG = "WifiNotifyUtil"

    //Wifi类通知栏
    const val CHANNEL_ID_WIFI = "wifi"

    //wifi一键加速通知栏Id
    const val NOTIFY_ID_WIFI_SPEED = 1001

    init {
        createChannel()
    }

    /**
     * 创建通知栏通道
     */
    fun createChannel() {
        Log.i(TAG, "createChannel: ")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID_WIFI,
                "WiFi通知",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.enableLights(false)//是否显示灯光
            channel.setShowBadge(false) //是否显示角标

            getNotificationManager().createNotificationChannel(channel)
        }
    }

    /**
     * 显示一键加速通知栏
     */
    fun showWifiSpeed(connectInfo: ConnectInfo) {
//        Log.i(TAG, "showWifiSpeed: ")
//        val context = getApplication()
//        //创建Notification
//        val notification = NotificationCompat.Builder(getApplication(), CHANNEL_ID_WIFI)
//            .setSmallIcon(R.drawable.scenes_icon_small_launch)
//            .setWhen(System.currentTimeMillis())
//            .setCustomContentView(getViewByWifiSpeed(context, connectInfo))
//            .setAutoCancel(false)
//            .setOngoing(!AppProxy.requireAudit())//通知不会被清除通知清理
//            .build()
//        getNotificationManager().notify(NOTIFY_ID_WIFI_SPEED, notification)
    }

    private fun getPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(action)
        intent.setClass(context, AppBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getViewByWifiSpeed(context: Context, connectInfo: ConnectInfo): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.scenes_notify_speed)
        val titleName = if (AppProxy.requireAudit()) {
            AppProxy.appName
        } else {
            connectInfo.name
        }
        remoteViews.setTextViewText(R.id.speed_name_tv, titleName)
        remoteViews.setTextViewText(
            R.id.speed_state_tv, if (connectInfo.networkType == ConnectInfo.NetworkType.WIFI) {
                "WiFi已连接"
            } else {
                "WiFi未连接"
            }
        )
        remoteViews.setOnClickPendingIntent(
            R.id.notify_layout,
            getPendingIntent(context, AppBroadcastReceiver.ACTION_HOME_NOTIFY)
        )
        remoteViews.setOnClickPendingIntent(
            R.id.speed_btn,
            getPendingIntent(context, AppBroadcastReceiver.ACTION_SPEED_NOTIFY)
        )
        return remoteViews
    }

    fun cancelWifiSpeed() {
        cancel(NOTIFY_ID_WIFI_SPEED)
    }

    /**
     * 取消通知栏
     */
    fun cancel(id: Int) {
        getNotificationManager().cancel(id)
    }

    fun getNotificationManager(): NotificationManager {
        return getApplication().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}
