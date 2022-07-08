package com.mckj.module.cleanup.manager.jump.model

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import com.mckj.module.cleanup.manager.jump.DefaultJumpPage
import com.org.openlib.utils.Log

/**
 * Describe:
 *
 * Created By yangb on 2021/7/22
 */
class OppoJumpPage : DefaultJumpPage() {

    override fun jumpWeather(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.coloros.weather",
                "com.coloros.weather.OppoMainActivity"
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
                "com.android.calendar.AllInOneActivity"
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
                "com.coloros.simsettings",
                "com.coloros.simsettings.OppoSettings\$OppoDataUsageSummaryActivity"
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
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.settings",
                "com.oppo.settings.flipfont.FontSettingsActivity"
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
        return try {
            if(getAndroidSDKVersion() >= 29){
                val intent = Intent()
                val componentName = ComponentName(
                    "com.coloros.gallery3d",
                    "com.coloros.gallery3d.app.MainActivity"
                )
                intent.component = componentName
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
                context.startActivity(intent)
            } else {
                val intent = Intent()
                val componentName = ComponentName(
                    "com.coloros.gallery3d",
                    "com.oppo.gallery3d.app.Gallery"
                )
                intent.component = componentName
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.jumpAlbum(context)
        }
    }

    //获得当前手机android sdk版本
    fun getAndroidSDKVersion(): Int {
        var version: Int = 0
        try {
            version = Integer.valueOf(Build.VERSION.SDK_INT)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        Log.e("androidSdkVersion","is $version")
        return version
    }

}