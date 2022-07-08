package com.mckj.sceneslib.manager.jump.model

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.mckj.sceneslib.manager.jump.DefaultJumpPage


/**
 * Describe:
 *
 * Created By yangb on 2021/7/22
 */
class XiaomiJumpPage : DefaultJumpPage() {

    override fun jumpWeather(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.miui.weather2",
                "com.miui.weather2.ActivityWeatherMain"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.jumpWeather(context)
        }
    }

    override fun jumpCalender(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.calendar",
                "com.android.calendar.homepage.AllInOneActivity"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.jumpCalender(context)
        }
    }

    override fun jumpNetData(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.miui.securitycenter",
                "com.miui.networkassistant.ui.activity.TrafficSortedActivity"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.jumpNetData(context)
        }
    }

    override fun jumpFontSize(context: Context): Boolean {
        return try{
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings${'$'}PageLayoutActivity"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.jumpFontSize(context)
        }
    }

    override fun jumpAlbum(context: Context): Boolean {
        return try{
            val intent = Intent()
            val componentName = ComponentName(
                "com.miui.gallery",
                "com.miui.gallery.activity.HomePageActivity"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)

            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.jumpAlbum(context)
        }
    }

    override fun jumpClock(context: Context): Boolean {
        return try{
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.deskclock",
                "com.android.deskclock.DeskClockTabActivity"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.jumpClock(context)
        }
    }

}