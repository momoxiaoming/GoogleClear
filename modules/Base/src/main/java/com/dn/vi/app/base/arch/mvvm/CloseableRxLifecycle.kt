package com.dn.vi.app.base.arch.mvvm

import com.dn.vi.app.base.lifecycle.RxLifecycleDelegate
import java.io.Closeable

/**
 * [RxLifecycleDelegate]的[Closeable]接口wrap.
 * [close]功能同[rxLifecycleDelegate.dispose()]
 *
 * Created by holmes on 2020/10/19.
 **/
class CloseableRxLifecycle(private val rxLifecycleDelegate: RxLifecycleDelegate) : Closeable {

    override fun close() {
        rxLifecycleDelegate.dispose()
    }

}