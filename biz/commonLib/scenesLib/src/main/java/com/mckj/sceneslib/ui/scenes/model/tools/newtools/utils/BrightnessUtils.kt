package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.content.Context
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.content.ContentResolver
import android.view.WindowManager
import android.net.Uri
import android.app.Activity
import com.dn.vi.app.cm.log.VLog

/**
 * @Description
 * @CreateTime 2022/3/29 9:27
 * @Author
 */
object BrightnessUtils {
    //需要添加setting权限
//    private fun isAutoBrightness(context: Context): Boolean {
//        var isAutoBrightness = false
//        try {
//            isAutoBrightness = Settings.System.getInt(
//                context.contentResolver,
//                Settings.System.SCREEN_BRIGHTNESS_MODE
//            ) === Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
//        } catch (e: SettingNotFoundException) {
//            e.printStackTrace()
//            VLog.i("错误:${e} = ${e.printStackTrace()}")
//        }
//        return isAutoBrightness
//    }

    //获取当前屏幕的亮度
    fun getScreenBrightness(context: Context):Int{
        var nowBrightnessValue = 0
        val resolver = context.contentResolver
        try {
            nowBrightnessValue =Settings.System.getInt(
                resolver, Settings.System.SCREEN_BRIGHTNESS)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return nowBrightnessValue
    }

    //设置亮度
    // 程序退出之后亮度失效
    fun setCurWindowBrightness(context: Context, brightness:Int){
        // 如果开启自动亮度，则关闭。否则，设置了亮度值也是无效的
//        if (isAutoBrightness(context)){
//            stopAutoBrightness(context)
//        }
        //context转换Activity
        val activity = context as Activity
        val lp = activity.window.attributes
        var b = brightness
        //异常处理
        if (brightness <1) {
            b = 1
        }
        if (brightness > 255){
            b = 255
        }
        lp.screenBrightness =  ((b)).toFloat() * (1f / 255f)
        activity.window.attributes = lp
    }
    //设置系统亮度
    //程序退出之后亮度依旧有效
    fun setSystemBrightness(context: Context,brightness: Int){
        var b = brightness
        //异常处理
        if (brightness <1){
             b =  1
        }
        if (brightness > 255){
            b = 255
        }
        saveBrightness(context,b)
    }
    //停止自动亮度调节
    private fun stopAutoBrightness(context: Context) {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
    }
    //开启亮度自动调节
    open fun startAutoBrightness(context: Context) {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        )
    }
    //保存亮度设置状态
    private fun saveBrightness(context: Context,brightness: Int){
        val uri = Settings.System.getUriFor("screen_brightness")
        Settings.System.putInt(context.contentResolver,
        "screen_brightness",brightness)
        context.contentResolver.notifyChange(uri,null)
    }
}