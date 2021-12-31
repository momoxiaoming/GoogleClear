package com.dn.vi.app.cm.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.util.Log
import com.dn.vi.app.cm.log.VLog

/**
 * create on 2021/7/13
 */
object BtManager {

    private val logger = VLog.scoped("bt::")

    @Volatile
    private var isUsbCharging = false

    fun isUsbCharging(): Boolean {
        return isUsbCharging
    }

    fun init(context: Context) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        try {
            context.registerReceiver(mReceiver, filter)
        } catch (e: Throwable) {
            //
        }
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            val isCharging =
                status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
            if (isCharging) {
                val chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
                val usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
                val acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
                logger.d("Bt::", "recv: uc=$usbCharge,ac=$acCharge")
                isUsbCharging = usbCharge
            } else {
                logger.d("Bt::", "recv: c=false")
                isUsbCharging = false
            }
        }
    }
}