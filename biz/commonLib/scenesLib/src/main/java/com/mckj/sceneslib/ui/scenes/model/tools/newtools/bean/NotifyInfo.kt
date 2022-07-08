package com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean

import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.Icon

data class NotifyInfo(val title:String?, val contentText:String?,  val postTime:Long, val packageName:String, val key:String)

data class AppInfo(val packageName: String,val appName: String,val appIcon:Drawable,var isFilter:Boolean=true)

data class  NotifySet(val list: List<NotifyInfo>?, val isShow: Boolean=false)

data class  NotifySingle(val notify:NotifyInfo?,val isAdd:Boolean,val isShow: Boolean)

data class NotifyEmptyEvent(val emptyState:Boolean,val isShow: Boolean=false)