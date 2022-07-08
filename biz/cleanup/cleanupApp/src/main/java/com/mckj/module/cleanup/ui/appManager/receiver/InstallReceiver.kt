package com.mckj.module.cleanup.ui.appManager.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.org.openlib.utils.Log

open class InstallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action?.equals("android.intent.action.PACKAGE_ADDED") == true){
            val packName = intent.dataString
            Log.i("uninstall","install packge:${packName}")
        }

        if(intent?.action?.equals("android.intent.action.PACKAGE_REMOVED") == true){
            val packName = intent.dataString
            Log.i("uninstall","uninstall packge:${packName}")
        }
    }
}