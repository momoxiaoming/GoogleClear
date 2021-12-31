package com.dn.vi.app.base.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.dn.vi.app.base.app.callback.AppCallbackManager
import com.dn.vi.app.base.di.BaseAppModule
import com.dn.vi.app.base.di.DaggerBaseAppComponent
import com.dn.vi.app.cm.kt.printTimeMillisSysLog
import com.dn.vi.app.cm.utils.ProcessUtils
import io.reactivex.rxjava3.plugins.RxJavaPlugins

/**
 *  基础 Application  实现代理
 * Created by holmes on 2020/5/19.
 **/
open class BaseAppDelegate(val app: Application) {

    /**
     * onCreateEnv后，再使用
     */
    private lateinit var appStatus: AppStatus

    open fun attachBaseContext(base: Context?) {
        printTimeMillisSysLog("BaseAppDe attachCtx: initLog") {
            initLog()
        }
        printTimeMillisSysLog("BaseAppDe attachCtx: postInitLog") {
            onPostInitLog()
        }
        printTimeMillisSysLog("BaseAppDe attachCtx: initWithComponent") {
            val lifeDetector = AppLifeDetector()
            app.registerActivityLifecycleCallbacks(lifeDetector)
            appStatus = AppLifeStatus(lifeDetector)

            val appComponent =
                DaggerBaseAppComponent.builder().baseAppModule(BaseAppModule(this, appStatus))
                    .build()
            AppMod.initWithComponent(appComponent)
        }
        if (base != null) {
            printTimeMillisSysLog("BaseAppDe attachCtx: AppCb attached") {
                AppCallbackManager.getInstance().attachBaseContext(app, base)
            }
        }
    }

    /**
     * Log 初始化后执行。
     * initLog --> onPostInitLog --> dispatch attachBaseContext
     */
    open fun onPostInitLog() {

    }

    open fun onCreate() {
        printTimeMillisSysLog("BaseAppDe onCreate: created") {
            val processName = ProcessUtils.currentProcessName
            val remoteProc = ProcessUtils.isRemoteProcess(processName)
            RxJavaPlugins.setErrorHandler(DefaultRxErrorHandler())
            if (!remoteProc) {
                printTimeMillisSysLog("BaseAppDe onCreate: createdEnv") {
                    onCreateEnv(processName, remoteProc)
                }
            }
            printTimeMillisSysLog("BaseAppDe onCreate: AppCb created") {
                AppCallbackManager.getInstance().onCreate(app)
            }
        }
    }

    open fun onTerminate() {
        AppCallbackManager.getInstance().onTerminate(app)
    }

    open fun onLowMemory() {
        AppCallbackManager.getInstance().onLowMemory(app)
    }

    open fun onTrimMemory(level: Int) {
        AppCallbackManager.getInstance().onTrimMemory(app, level)
    }

    /**
     * 初始化环境
     *
     * @param processName 当前进程名
     * @param remoteProc 当前是否是 远程进程。大多数情况下，如果是true,
     *                  则不会做一些sdk的初始化。
     * @see ProcessUtils
     */
    open fun onCreateEnv(processName: String, remoteProc: Boolean) {

    }

    /**
     * 初始化日志
     */
    open fun initLog() {

    }

    /**
     * app activity 状态监测
     */
    private class AppLifeDetector : Application.ActivityLifecycleCallbacks {

        var aliveCount = 0
        var foregroundCount = 0

        var exitTime = 0L

        override fun onActivityPaused(activity: Activity?) {
            foregroundCount--
        }

        override fun onActivityResumed(activity: Activity?) {
            foregroundCount++
        }

        override fun onActivityStarted(activity: Activity?) {
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
            aliveCount--
            if (aliveCount == 0) {
                exitTime = System.currentTimeMillis()
            }
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            aliveCount++
        }

    }

    private class AppLifeStatus(val detector: AppLifeDetector) : AppStatus {
        override fun isAlive(): Boolean {
            return detector.aliveCount > 0
        }

        override fun isForeground(): Boolean {
            return detector.foregroundCount > 0
        }
    }

}