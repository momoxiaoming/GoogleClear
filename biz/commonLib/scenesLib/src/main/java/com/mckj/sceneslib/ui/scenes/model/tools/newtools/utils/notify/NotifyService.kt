package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

class NotifyService : NotificationListenerService() {

    companion object{
        const val SINGLE_NOTIFY="single_notify"
        const val ALL_NOTIFY="all_notify"
        const val KEY_NOTIFY="key_notify"


        fun sendCleanNotifyBroadcast(context: Context,action:String,key: String=""){
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(action).apply { putExtra(
                KEY_NOTIFY,key) })
        }
    }

    private val  job by lazy {  Job() }
    private val scope by lazy { CoroutineScope(job) }
    private val mCleanNotifyReceiver by lazy { CleanNotifyReceiver() }

    override fun onCreate() {
        super.onCreate()
        val intentFilter = IntentFilter().apply {
            addAction(SINGLE_NOTIFY)
            addAction(ALL_NOTIFY)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(mCleanNotifyReceiver,intentFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        NotifyHelper.interceptAllNotify(activeNotifications, this,scope)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn?.let {
            NotifyHelper.interceptAddNotify(it, this,scope)
        }
    }

    override fun onNotificationRemoved(
        sbn: StatusBarNotification?,
        rankingMap: RankingMap?,
        reason: Int
    ) {
        super.onNotificationRemoved(sbn, rankingMap, reason)
        Log.d("8888888", "onNotificationRemoved: -------$reason")
        when(reason){
            REASON_CANCEL,REASON_CLICK->{
                sbn?.let {
                    NotifyHelper.interceptRemoveNotify(it,this,scope)
                }
            }
            REASON_CANCEL_ALL->{
                NotifyHelper.interceptRemoveAllNotify()
            }
        }
    }

    inner class CleanNotifyReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                when(it.action){
                    SINGLE_NOTIFY->{
                        val key = it.getStringExtra(KEY_NOTIFY)
                        if (!key.isNullOrBlank()) {
                            cancelNotification(key)
                        }
                    }
                    ALL_NOTIFY->{
                        NotifyHelper.clearSelectNotifyList(this@NotifyService)
                    }
                    else->{}
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        NotifyHelper.clearNotifyList()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mCleanNotifyReceiver)

    }

}