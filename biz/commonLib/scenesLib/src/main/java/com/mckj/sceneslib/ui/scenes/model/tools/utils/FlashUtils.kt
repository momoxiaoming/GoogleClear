package com.mckj.sceneslib.ui.scenes.model.tools.utils

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.mckj.baselib.helper.getApplicationContext
import com.org.openlib.utils.Log

object FlashUtils {
    private lateinit var manager: CameraManager
    private lateinit var mCamera: Camera

    //记录手电筒状态
    var open_status: Boolean = false
    fun init() {
        open_status = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager =
                getApplicationContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
        }
    }

    //打开手电筒
    fun open() {
        init()
        if (open_status) { //如果已经是打开状态，不需要打开
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                manager.setTorchMode("0", true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            val packageManager = getApplicationContext().packageManager
            val features = packageManager.systemAvailableFeatures
            for (featureInfo in features) {
                // 判断设备是否支持闪光灯
                if (PackageManager.FEATURE_CAMERA_FLASH == featureInfo.name) {
                    val parameters = mCamera.parameters
                    parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
                    mCamera.parameters = parameters
                    mCamera.startPreview()
                }
            }
        }
        //记录手电筒状态为打开
        open_status = true
    }

    //关闭手电筒
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun close() {
        //如果已经是关闭状态，不需要打开
        if (!open_status) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                manager.setTorchMode("0", false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            mCamera.stopPreview()
            mCamera.release()
        }
        //记录手电筒状态为关闭
        open_status = false
    }
}