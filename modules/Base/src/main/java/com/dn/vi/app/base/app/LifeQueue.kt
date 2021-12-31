package com.dn.vi.app.base.app

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.drakeet.purewriter.ObscureDefaultLifecycleObserver
import com.drakeet.purewriter.addObserver
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 一个执行队列。
 * 只会在与相绑定的lifecycle，为resume的时候，依次执行
 * Created by holmes on 2020/9/10.
 **/
class LifeQueue(private val lifecycleOwner: LifecycleOwner) {

    private val queue = LinkedList<Runnable>()
    private val running = AtomicBoolean(false)
    private val ob = LifeOb()

    val isResumed: Boolean
        get() = lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)

    init {
        bindLifecycle(lifecycleOwner)
    }

    private fun bindLifecycle(lifecycleOwner: LifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(ob)
        ob.lifecycle = lifecycle
    }

    /**
     * 如果已注册的lifecycle失效了就重新绑定一下[lifecycleOwner]中新的
     * lifecycle
     */
    fun syncLifecycle() {
        val newLife = lifecycleOwner.lifecycle
        if (newLife != ob.lifecycle) {
            bindLifecycle(lifecycleOwner)

            if (isResumed) {
                performResume()
            }
        }
    }

    fun submit(run: Runnable) {
        synchronized(queue) {
            queue.add(run)
        }
        if (isResumed) {
            performResume()
        }
    }

    private fun performResume() {
        if (running.compareAndSet(false, true)) {
            resume()
            running.compareAndSet(true, false)
        }
    }

    private fun resume() {
        synchronized(queue) {
            if (queue.isEmpty()) {
                return
            }
        }

        while (true) {
            val head = try {
                synchronized(queue) {
                    queue.poll()
                }
            } catch (e: Exception) {
                null
            }
                ?: return
            head.run()
        }
    }

    private inner class LifeOb : ObscureDefaultLifecycleObserver {

        private var attachedLifecycle: WeakReference<Lifecycle>? = null

        /**
         * 绑定的lifecycle
         */
        var lifecycle: Lifecycle?
            set(value) {
                if (value != null) {
                    attachedLifecycle = WeakReference(value)
                } else {
                    attachedLifecycle?.clear()
                    attachedLifecycle = null
                }
            }
            get() = attachedLifecycle?.get()

        override fun onResume(owner: LifecycleOwner) {
            super.onResume(owner)
            performResume()
        }
    }
}