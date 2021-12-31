package com.dn.vi.app.base.app.kt

import com.dn.vi.app.base.app.BaseFragment
import com.dn.vi.app.base.app.LifeQueue
import com.dn.vi.app.base.app.LifecycleWrapData
import com.dn.vi.app.base.helper.StepRunner
import kotlin.coroutines.Continuation

// Fragment 相关的扩展
// Created by holmes on 2021/4/13.
//

/**
 * baseFragment 里面获取一个lifeQueue.
 */
val BaseFragment.lifeQueue: LifeQueue
    get() {
        while (true) {
            val exists = this.internalLifeQueue.get() as? LifecycleWrapData<LifeQueue>
            if (exists != null) {
                return exists.data
            }
            val newQueue = LifeQueue(this)
            val wrap = LifeQueueWrap(newQueue)
            if (this.internalLifeQueue.compareAndSet(null, wrap)) {
                return newQueue
            }
        }
    }

private class LifeQueueWrap(override val data: LifeQueue) : LifecycleWrapData<LifeQueue>() {

    override fun onResumed(tag: Any?) {
        data.syncLifecycle()
    }

    override fun onPaused(tag: Any?) {
    }

}

/**
 * 只在Fragment resume的时候执行
 */
fun BaseFragment.resumedContStep(runner: StepRunner, block: (cont: Continuation<Boolean>) -> Unit): StepRunner.Step {
    return with(runner) {
        contStep { cont ->
            lifeQueue.submit(Runnable {
                block(cont)
            })
        }
    }
}