package com.mckj.sceneslib.util

import com.dn.vi.app.cm.log.VLog

/**
 * Describe:
 *
 * Created By yangb on 2020/9/18
 */
object Log {

    val log = VLog.scoped("biz:appScenesLib")

    fun i(tag: String, msg: String) {
        log.i("$tag $msg")
    }

    fun d(tag: String, msg: String) {
        log.d("$tag $msg")
    }

    fun e(tag: String, msg: String) {
        log.e("$tag $msg")
    }

    fun w(tag: String, msg: String) {
        log.w("$tag $msg")
    }

}