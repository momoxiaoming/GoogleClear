package com.dn.vi.app.base.arch.gmvp

import androidx.lifecycle.Lifecycle
import com.dn.vi.app.base.lifecycle.RxLifecycleDelegate

/**
 * Lifecycle
 * Created by holmes on 2020/5/21.
 **/
interface LifecycleView {

    /**
     * Android Lifecycle
     */
    fun getLifecycle(): Lifecycle

    /**
     * 提供 rx Lifecycle
     */
    fun <T> bindUntilDestroy(): RxLifecycleDelegate.LifecycleTransformer<T>

}