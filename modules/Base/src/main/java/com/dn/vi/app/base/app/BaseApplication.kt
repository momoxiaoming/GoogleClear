package com.dn.vi.app.base.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 *  Application  or MultiDexApplication
 * Created by holmes on 2020/5/19.
 **/
abstract class BaseApplication : Application() {

    protected val delegate: BaseAppDelegate by lazy {
        createAppDelegate()
    }

    protected abstract fun createAppDelegate(): BaseAppDelegate

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)  // compat all platform version

        delegate.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        delegate.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        delegate.onTerminate()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        delegate.onLowMemory()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        delegate.onTrimMemory(level)
    }

}