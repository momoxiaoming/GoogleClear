package com.org.proxy.log

import android.util.Log
import com.org.proxy.BuildConfig

/**
 * LogUtils
 *
 * @author mmxm
 * @date 2022/7/14 22:14
 */
object LogUtils {

     var openLog:Boolean=BuildConfig.DEBUG

    fun i(tag:String,msg:String){
        if(openLog){
            Log.i(tag,msg)
        }
    }
}

inline  fun Any.log(tag: String,msg:String){
    LogUtils.i(tag,msg)
}