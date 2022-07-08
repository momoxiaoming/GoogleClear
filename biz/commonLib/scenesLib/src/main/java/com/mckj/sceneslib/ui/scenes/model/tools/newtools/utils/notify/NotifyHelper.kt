package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify

import android.app.Notification
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.dn.vi.app.base.app.AppMod
import com.google.gson.reflect.TypeToken
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NotifyEmptyEvent
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NotifyInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NotifySet
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NotifySingle
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.GsonHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.KvSpHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


object NotifyHelper {

    private const val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private val interceptNotifyList by lazy { arrayListOf<NotifyInfo>() }
    val currentNotify = MutableLiveData<NotifySingle>()
    val currentNotifyList = MutableLiveData<NotifySet>()
    val emptyEvent by lazy { MutableLiveData<NotifyEmptyEvent>() }
    private const val SP_NOTIFY_CONTENT_KEY = "sp_notify_content_key"


    fun startNotifyService(context: Context) {
        val intent = Intent(context, NotifyService::class.java)
        context.startService(intent)
    }

    fun interceptAllNotify(
        notifyList: Array<StatusBarNotification>,
        notifyController: NotificationListenerService,
        scope: CoroutineScope
    ) {
        val filterAppStateMap = AppStateProvider.getFilterAppMap()
        notifyList.forEach { sbn ->
            filterAppStateMap[sbn.packageName]?.let {
                createNotify(sbn)?.let {
                    // notifyController.cancelNotification(it.key)
                    if (!interceptNotifyList.contains(it)) {
                        addNotifyList(it)
                    }
                }
            }
        }
//            getNotifyFromSp()?.let {
//                it.forEach { data->
//                    if (!interceptNotifyList.contains(data)) {
//                        interceptNotifyList.add(data)
//                    }
//                }
//            }
        currentNotifyList.postValue(NotifySet(interceptNotifyList, true))
    }

    fun interceptAddNotify(
        notify: StatusBarNotification,
        notifyController: NotificationListenerService,
        scope: CoroutineScope
    ) {
        if (notify.packageName == AppMod.app.packageName || !notify.isClearable) return
        val filterAppMap = AppStateProvider.getFilterAppMap()
        filterAppMap[notify.packageName]?.let {
            createNotify(notify)?.let {
                //  notifyController.cancelNotification(it.key)
                addNotifyList(it)
                currentNotify.postValue(NotifySingle(it, isAdd = true, isShow = true))
            }
        }
    }

    fun interceptRemoveNotify(
        notify: StatusBarNotification,
        notifyController: NotificationListenerService,
        scope: CoroutineScope
    ) {
        notify.notification.extras.getCharSequence(Notification.EXTRA_TEXT)?.let { content ->
            interceptNotifyList.find { it.contentText == content && it.key == notify.key && it.postTime == notify.postTime }
                ?.let {
                    //  notifyController.cancelNotification(it.key)
                    currentNotify.postValue(NotifySingle(it, isAdd = false, isShow = true))
                    removeNotifyList(it)
                }
        }
    }

    fun interceptRemoveAllNotify(){
        clearNotifyList()
        emptyEvent.postValue(NotifyEmptyEvent(true, isShow = true))
    }

    private fun createNotify(it: StatusBarNotification): NotifyInfo? {
        val key = it.key
        val packageName = it.packageName
        val title = it.notification.extras.getString(Notification.EXTRA_TITLE)
        val contentText = it.notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString()
        val smallIcon =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) it.notification.smallIcon else null
        val postTime = it.postTime
        // val contentIntent = it.notification.contentIntent
        return if (!title.isNullOrEmpty() && smallIcon != null) {
            NotifyInfo(title, contentText, postTime, packageName, key)
        } else {
            null
        }
    }

    private fun addNotifyList(notify: NotifyInfo) {
        interceptNotifyList.add(0, notify)
        emptyEvent.postValue(NotifyEmptyEvent(emptyState = false, isShow = true))
        saveNotifyToSp()
    }



    fun removeNotifyList(notify: NotifyInfo) {
        interceptNotifyList.remove(notify)
        if (interceptNotifyList.isEmpty()) {
            emptyEvent.postValue(NotifyEmptyEvent(true, isShow = true))
        }
        saveNotifyToSp()
    }


    fun clearNotifyList() {
        interceptNotifyList.clear()
        saveNotifyToSp()
    }


    private val selectNotifyList = arrayListOf<NotifyInfo>()
    fun getSelectNotifyList(selectList:List<NotifyInfo>){
        selectNotifyList.clear()
        selectNotifyList.addAll(selectList)
    }


    fun clearSelectNotifyList(notifyService: NotifyService) {
        selectNotifyList.forEach {
                notifyService.cancelNotification(it.key)
                removeNotifyList(it)
        }
    }


    private fun saveNotifyToSp() {
        // KvSpHelper.putString(SP_NOTIFY_CONTENT_KEY, GsonHelper.toJson(interceptNotifyList))
    }


    private fun getNotifyFromSp(): List<NotifyInfo>? {
        val data = KvSpHelper.getString(SP_NOTIFY_CONTENT_KEY)
        return if (data != null) {
            val type = object : TypeToken<List<NotifyInfo>>() {}.type
            GsonHelper.jsonToObj<List<NotifyInfo>>(data, type)
        } else {
            null
        }
    }


    /**
     * 针对apk被清理重启后NotificaitonListenerService失效
     * @param context Context
     */
    fun toggleNotificationListenerService(context: Context) {
        val pm = context.packageManager
        pm.setComponentEnabledSetting(
            ComponentName(context, NotifyService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName(context, NotifyService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }


    /**
     * 跳转通知访问权限开启界面
     * @param context Context
     * @return Boolean
     */
    fun gotoNotificationAccessSetting(context: Context): Boolean {
        return try {
            val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) { //普通情况下找不到的时候需要再特殊处理找一次
            try {
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                val cn = ComponentName(
                    "com.android.settings",
                    "com.android.settings.Settings\$NotificationAccessSettingsActivity"
                )
                intent.component = cn
                intent.putExtra(":settings:show_fragment", "NotificationAccessSettings")
                context.startActivity(intent)
                return true
            } catch (e1: Exception) {
                e1.printStackTrace()
            }
            Toast.makeText(context, "对不起，您的手机暂不支持", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
            false
        }
    }

    /**
     * 判断权限是否已开启
     * @param context Context
     * @return Boolean
     */
    fun isNotificationListenerEnable(context: Context): Boolean {
        var enable = false
        val packageName: String = context.packageName
        val flat =
            Settings.Secure.getString(context.contentResolver, ENABLED_NOTIFICATION_LISTENERS)
        if (flat != null) {
            enable = flat.contains(packageName)
        }
        return enable
    }


}



