package com.dn.vi.app.base.arch.gmvp

import android.content.Context

/**
 * provide a context
 * Created by holmes on 2020/5/22.
 **/
interface ContextView {

    /**
     * 返回最近的 App 组件的Context
     * Activity --> Application
     */
    fun getContext(): Context

}