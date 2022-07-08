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
class VivoJumpPage : DefaultJumpPage() {
    override fun jumpWeather(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.vivo.weather",
                "com.vivo.weather.WeatherMain"
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
                "com.bbk.calendar",
                "com.bbk.calendar.MainActivity"
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
                "com.iqoo.secure",
                "com.iqoo.secure.datausage.DataUsageDetail"
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
                "com.bbk.theme",
                "com.bbk.theme.font.FontSizeBig"
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
            val intent = Intent()
            val componentName = ComponentName(
                "com.vivo.gallery",
                "com.android.gallery3d.vivo.GalleryTabActivity"
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
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.BBKClock",
                "com.android.BBKClock.Timer"
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