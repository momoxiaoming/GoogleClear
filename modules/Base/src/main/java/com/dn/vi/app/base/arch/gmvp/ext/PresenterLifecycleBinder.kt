package com.dn.vi.app.base.arch.gmvp.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.dn.vi.app.base.arch.gmvp.BasePresenter

/**
 * 自动将[p]的[subscribe]和[unsubscribe]与
 * [lifecycleOwner]的create 和 destroy绑定。
 * Created by holmes on 3/10.
 */
class PresenterLifecycleBinder(
    val lifecycleOwner: LifecycleOwner,
    val p: BasePresenter
) : BasePresenter, LifecycleObserver {

    private val lifecycle: Lifecycle
        get() = lifecycleOwner.lifecycle

    init {
        lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    override fun subscribe() {
        p.subscribe()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    override fun unsubscribe() {
        p.unsubscribe()
        lifecycle.removeObserver(this)
    }

}

