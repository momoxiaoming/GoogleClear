package com.mckj.module.cleanup.manager.jump

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import android.provider.Settings
//import com.mckj.cleanupx.ui.dialog.openFailDialog



/**
 * Describe:
 *
 * Created By yangb on 2021/7/22
 */
open class DefaultJumpPage : IJumpPage {
    override fun jumpWeather(context: Context): Boolean {
//        openFailDialog.show(context)
        return false
    }

    override fun jumpCalender(context: Context): Boolean {
        return try {
            val intent = Intent()
            val componentName = ComponentName(
                "com.android.calendar",
                "com.android.calendar.homepage.AllInOneActivity"
            )
            intent.component = componentName
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e : Exception){
            e.printStackTrace()
//            openFailDialog.show(context)
            false
        }
    }

    override fun jumpNetData(context: Context): Boolean {
        return try {
            val intent = Intent()
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                intent.action = Settings.ACTION_DATA_USAGE_SETTINGS
            } else {
                intent.action = Settings.ACTION_DATA_ROAMING_SETTINGS
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
//            openFailDialog.show(context)
            false
        }
    }

    override fun jumpParam(context: Context): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_DEVICE_INFO_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
//            openFailDialog.show(context)
            false
        }
    }

    override fun jumpFontSize(context: Context): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
//            openFailDialog.show(context)
            false
        }
    }

    override fun jumpAlbum(context: Context): Boolean {
        return try {
            val intent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
//            openFailDialog.show(context)
            false
        }
    }

    override fun jumpClock(context: Context): Boolean {
        return try {
            val intent = Intent(AlarmClock.ACTION_SET_ALARM)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
//            openFailDialog.show(context)
            false
        }
    }


}