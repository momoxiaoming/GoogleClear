package com.mckj.baselib.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.mckj.baselib.helper.getApplication
import com.mckj.baselib.helper.getApplicationContext

/**
 * Describe:定位工具类
 *
 * Created By yangb on 2020/10/23
 */
object LocationUtil {

    const val TAG = "LocationUtil"

    /**
     * 定位是否打开
     */
    fun isLocationEnable(): Boolean {
        val locationManager = getLocationManager()
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /**
     * 定位权限是否获取
     */
    fun isLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            getApplication(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 控制定位开关
     */
    fun locationEnable(enable: Boolean): Boolean {
        openWifiSetting()
        return true
    }

    /**
     * 打开系统定位设置页面
     */
    fun openWifiSetting() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        getApplicationContext().startActivity(intent)
    }

    /**
     * 打开app设置页面
     */
    fun openSelfSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.fromParts("package", getApplication().packageName, null)
        getApplicationContext().startActivity(intent)
    }

    /**
     * 获取LocationManager
     */
    fun getLocationManager(): LocationManager {
        return getApplication().getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

}