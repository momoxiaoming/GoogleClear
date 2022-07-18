package com.org.proxy

import android.app.Activity
import android.app.Application
import android.os.Bundle

import androidx.lifecycle.MutableLiveData
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.cm.utils.AppUtil
import com.dn.vi.app.repo.kv.KvLite
import com.dn.vi.app.repo.kv.KvSp

import com.org.proxy.log.log

import java.lang.ref.WeakReference
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

/**
 * AppProxy
 *
 * @author mmxm
 * @date 2022/7/4 21:47
 */
object AppProxy {
    val appVersion: String = AppUtil.getAppVersion(AppMod.app)
    val lsn: String = ""
    val oaid: String = ""
    val deviceId: String = ""
    val channel: String = ""
    val projectId: String = ""
    val appId: String = ""
    val packageName: String = AppMod.app.packageName
    val bootTime: Long = 0L


    private val isInit = AtomicBoolean(false)

    /**
     * 应用代码版本
     * 内部版本，用于表示代码是否有改过
     */
    private val appCodeMarkKey: String
        get() = KvLite.joinKeys("appSdk", "code", "ver")

    private var preInitAction: Runnable? = null

    /**
     * 模块里面的scoped log
     */
    internal val log: VLog.Logger
        get() {
            return VLog.scoped("appProxy")
        }

    private val activityLife: ActivityLifecycle by lazy { ActivityLifecycle() }


    fun requireAudit(): Boolean {
        return false
    }

    /**
     * 获取当前记录的代码版本
     */
    fun getCodeVersion(): String {
        return KvSp.getKv(appCodeMarkKey)
    }

    fun preInitSdk(app: Application, codeVersion: String) {
        synchronized(AppProxy) {
            registerActivityLifeCallback(app)
            preInitAction = Runnable {
                initSdk(app, codeVersion)
            }

            registerActivityLifeCallback(app)
        }
    }

    fun postInitSdk() {
        val action = preInitAction
        if (action == null) {
            log.w("no need postInit, because preInit not call")
            return
        }
        log.i("post init sdk")
        action.run()
    }

    fun initSdk(app: Application, codeVersion: String) {
        if (isInit.compareAndSet(false, true)) {
            AppProxyEnv.increaseInitState()
        }
    }

    /**
     * 注册应用内的 生命周期统计
     */
    fun registerActivityLifeCallback(app: Application) {
        app.unregisterActivityLifecycleCallbacks(activityLife)
        app.registerActivityLifecycleCallbacks(activityLife)
    }

    private class ActivityLifecycle : Application.ActivityLifecycleCallbacks {

        private var topRef: WeakReference<Activity>? = null

        private val activityCount = AtomicInteger()
        private val mainCount = AtomicInteger()

        private val resumedCount = AtomicInteger()
        private val startedCount = AtomicInteger()
        private val liveResumedCount = MutableLiveData<Int>()

        private var pausedAt: Long = 0

        override fun onActivityPaused(activity: Activity) {
            VLog.scoped("ActivityLifecycle").i("onActivityPaused->${activity.componentName}")

        }

        override fun onActivityResumed(activity: Activity) {
            VLog.scoped("ActivityLifecycle").i("onActivityResumed->${activity.componentName}")

        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            VLog.scoped("ActivityLifecycle").i("onActivityDestroyed->${activity.componentName}")
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityStopped(activity: Activity) {
            VLog.scoped("ActivityLifecycle").i("onActivityStopped->${activity.componentName}")

        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            log("ActivityLifecycle", "onActivityCreated->${activity.componentName}")

        }
    }
}