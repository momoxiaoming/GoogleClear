package com.org.openlib.kits


import android.app.Application
import android.content.Context

import com.alibaba.android.arouter.launcher.ARouter
import com.alibaba.android.arouter.utils.Consts
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.BaseAppDelegate
import com.dn.vi.app.cm.http.OkApi
import com.dn.vi.app.cm.kt.printTimeMillisSysLog
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.cm.utils.ProcessUtils
import com.org.openlib.BuildConfig
import com.org.openlib.R
import com.org.proxy.AppProxy
import com.org.proxy.AppProxyEnv
import com.tencent.mmkv.MMKV
import z.hol.gq.GsonQuick
import z.hol.gq.GsonQuickLogger
import java.util.concurrent.Callable

/**
 * 通用的app 初始化delegate实现
 * Created by holmes on 2020/11/26.
 **/
abstract class DefaultAppDelegate(app: Application) : BaseAppDelegate(app)
     {

    companion object {
        const val TAG = "DefaultAppDe"
    }

    /**
     * 日志的前缀
     */
    open val logPrefix: String = "VI"

    /**
     * 代码版本。
     * 如果是同版本更新，就修改一下内存版本
     */
    open var codeVersion: String = ""

    protected open var forceDebug: Boolean = false



    override fun attachBaseContext(base: Context?) {
        printTimeMillisSysLog("$TAG, attachCtx") {
            super.attachBaseContext(base)
        }
    }

    override fun onCreate() {
        printTimeMillisSysLog("$TAG, onCreate") {
            super.onCreate()
        }
    }

    override fun onCreateEnv(processName: String, remoteProc: Boolean) {
        if (remoteProc) {
            return
        }

        // === ARouter ===
        if (codeVersion.isNullOrEmpty()) {
            codeVersion = app.getString(R.string.pipe__code_ver)
        }
        if (codeVersion.isNullOrEmpty()) {
            codeVersion = BuildConfig.CODE_VER
        }
        printTimeMillisSysLog("$TAG, onCreateEnv aRouter.env") {
            if (Debuger.debugOn) {
                ARouter.openLog()
                ARouter.openDebug()
            } else {
                val oldCodeVersion = AppProxy.getCodeVersion()
                if (oldCodeVersion != codeVersion) {
                    VLog.i("clear arouter caches. ${oldCodeVersion} -> ${codeVersion}")
                    // 清掉arouter记录
                    app.getSharedPreferences(Consts.AROUTER_SP_CACHE_KEY, Context.MODE_PRIVATE)
                        .also { asp ->
                            asp.edit().putString(Consts.LAST_VERSION_NAME, "")
                                .putInt(Consts.LAST_VERSION_CODE, 0).clear().apply()
                        }
                }
            }
        }
        printTimeMillisSysLog("$TAG, onCreateEnv aRouter.init") {
            ARouter.init(app)
        }
        // === $ ===
        // === 平台SDK ===
        printTimeMillisSysLog("$TAG, onCreateEnv initSdk") {
            val initState = AppProxyEnv.getInitState()
            if (initState.isFirst()) {
                AppProxy.preInitSdk(app, codeVersion)
            } else {
                AppProxy.initSdk(app, codeVersion)
            }
        }


        // 放到SDK下面，因为SDK也用到了MMKV，避免多次初始化
        if (MMKV.getRootDir().isNullOrEmpty()) {
            MMKV.initialize(app)
            VLog.i("mmkv inited")
        } else {
            VLog.i("mmkv already inited")
        }


        // === $ ===

        OkApi.init(app)
        OkApi.DEBUG = Debuger.debugOn


//        val lifeMonitor = AppLife()
//        app.registerActivityLifecycleCallbacks(lifeMonitor)
//        LogHelper.registerLogService()

    }

    override fun initLog() {
        super.initLog()
        forceDebug = BuildConfig.DEBUG

        // 因为初始化log，太早了，下面的debugOn开关，可能拿不到
        Debuger.setContextProvider(Callable {
            app
        })
        Debuger.forceDebug = forceDebug

        val processName = ProcessUtils.currentProcessName
        val procNamespace = ProcessUtils.getProcessScope(processName)
        val logPrefix = this.logPrefix
        val namespace = if (procNamespace.isNullOrEmpty()) {
            logPrefix
        } else {
            "${logPrefix}::${procNamespace}"
        }
        VLog.initializeXLog(app, namespace, Debuger.debugOn)
        VLog.TAG_PREFIX = logPrefix
        VLog.setDefaultTag("main")

        GsonQuick.setLogger(object : GsonQuickLogger {
            override fun e(p0: String?, p1: java.lang.Exception?) {
                VLog.w(p0 ?: "null json")
                VLog.printErrStackTrace(p1, "gson parse error")
            }
        })
    }

    override fun onPostInitLog() {

    }
}
