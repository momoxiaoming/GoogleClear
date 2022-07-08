package com.mckj.template.util

import com.dn.vi.app.cm.log.VLog

/**
 *  author :leo
 *  date : 2022/3/18 11:26
 *  description :
 */
object Log {

    val log  = VLog.scoped("biz:template")

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