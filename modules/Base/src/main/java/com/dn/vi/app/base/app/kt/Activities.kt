package com.dn.vi.app.base.app.kt

import com.dn.vi.app.base.app.BaseActivity
import com.dn.vi.app.base.app.LifeQueue
import com.dn.vi.app.base.helper.StepRunner
import kotlin.coroutines.Continuation

// activity 相关的扩展
// Created by holmes on 2021/4/13.

/**
 * baseActivity 里面获取一个lifeQueue.
 *
 */
val BaseActivity.lifeQueue: LifeQueue
    get() {
        while (true) {
            val exists = this.internalLifeQueue.get() as? LifeQueue
            if (exists != null) {
                return exists
            }
            val newQueue = LifeQueue(this)
            if (this.internalLifeQueue.compareAndSet(null, newQueue)) {
                return newQueue;
            }
        }
    }


/**
 * 只在activity resume的时候执行
 */
fun BaseActivity.resumedContStep(runner: StepRunner, block: (cont: Continuation<Boolean>) -> Unit): StepRunner.Step {
    return with(runner) {
        contStep { cont ->
            lifeQueue.submit(Runnable {
                block(cont)
            })
        }
    }
}