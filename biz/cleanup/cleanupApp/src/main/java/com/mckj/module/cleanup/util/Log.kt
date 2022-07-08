package com.mckj.module.cleanup.util

import com.dn.vi.app.cm.log.VLog

/**
 * Describe:
 *
 * Created By yangb on 2020/9/18
 */
object Log {

    val log  = VLog.scoped("biz:cleanup")

    @JvmStatic
    fun i(tag : String, msg : String){
        log.i("$tag $msg")
    }

    @JvmStatic
    fun d(tag : String, msg : String){
        log.d("$tag $msg")
    }

    @JvmStatic
    fun e(tag : String, msg : String){
        log.e("$tag $msg")
    }

    @JvmStatic
    fun w(tag : String, msg : String){
        log.w("$tag $msg")
    }

    @JvmStatic
    fun v(tag : String, msg : String){
        log.v("$tag $msg")
    }

}