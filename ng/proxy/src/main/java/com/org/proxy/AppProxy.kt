package com.org.proxy

import android.app.Application
import com.dn.vi.app.repo.kv.KvLite
import com.dn.vi.app.repo.kv.KvSp

/**
 * AppProxy
 *
 * @author mmxm
 * @date 2022/7/4 21:47
 */
object AppProxy {
    val appVersion: String="1.0.0"
    val lsn: String=""
    val oaid: String=""
    val deviceId: String=""
    val channel: String=""
    val projectId: String=""
    val appId: String=""
    val packageName: String=""
    val bootTime:Long=0L
    /**
     * 应用代码版本
     * 内部版本，用于表示代码是否有改过
     */
    private val appCodeMarkKey: String
        get() = KvLite.joinKeys("appSdk", "code", "ver")

    fun requireAudit():Boolean{
        return false
    }

    /**
     * 获取当前记录的代码版本
     */
    fun getCodeVersion(): String {
        return KvSp.getKv(appCodeMarkKey)
    }

    fun preInitSdk(app: Application,  codeVersion: String) {

    }

    fun initSdk(app: Application,  codeVersion: String) {

    }
}