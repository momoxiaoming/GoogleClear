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
class HuaweiJumpPage : DefaultJumpPage() {

    override fun jumpWeather(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.huawei.android.totemweatherapp",
                "com.huawei.android.totemweatherapp.WeatherHome"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            jumpWeatherTwo(context)
        }
    }

    // 华为荣耀X10天气
    private fun jumpWeatherTwo(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.huawei.android.totemweather",
                "com.huawei.android.totemweather.WeatherHome"
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
            jumpCalenderTwo(context)
        }
    }

    //华为荣耀X10日历
    private fun jumpCalenderTwo(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.huawei.calendar",
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
                "com.huawei.systemmanager",
                "com.huawei.netassistant.ui.NetAssistantMainActivity"
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

    override fun jumpParam(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.settings",
                "com.android.settings.Settings${'$'}DeviceInfoSettingsActivity"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            super.jumpParam(context)
        }
    }



    override fun jumpAlbum(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.gallery3d",
                "com.huawei.gallery.app.GalleryMain"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            jumpAlbumTwo(context)
        }
    }


    //华为荣耀x10相册
    private fun jumpAlbumTwo(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.huawei.photos",
                "com.huawei.gallery.app.GalleryMain"
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
                "com.huawei.deskclock",
                "com.android.deskclock.AlarmsMainActivity"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            jumpClockTwo(context)
        }
    }

    // 畅享9plus闹钟列表
    private fun jumpClockTwo(context: Context) : Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.deskclock",
                "com.android.deskclock.AlarmsMainActivity"
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