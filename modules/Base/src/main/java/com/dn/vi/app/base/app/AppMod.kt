package com.dn.vi.app.base.app

import android.app.Application
import com.dn.vi.app.base.di.BaseAppComponent

/**
 * App Module
 * 如果使用 dagger可以方便全局注入
 * Created by holmes on 2020/5/21.
 **/
object AppMod {

    private lateinit var baseAppComponent: BaseAppComponent

    internal fun initWithComponent(appc: BaseAppComponent) {
        if (!this::baseAppComponent.isInitialized) {
            synchronized(this) {
                if (!this::baseAppComponent.isInitialized) {
                    baseAppComponent = appc
                }
            }
        }
    }

    /**
     * 供外面注入用
     */
    val appComponent: BaseAppComponent
        get() = baseAppComponent

    val app: Application
        get() = appComponent.getApp()


    // === quick methods for app (context) ===

    fun getString(res: Int): String {
        return app.getString(res)
    }

    fun getText(res: Int): CharSequence {
        return app.getText(res)
    }

    // === ===
}