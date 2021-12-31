package com.dn.vi.app.base.helper

import com.dn.vi.app.cm.log.VLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine

/**
 * 一步一步的，next ,next
 * 如果一步不完成，则中断
 *
 * 内容通过Coroutine来控制
 *
 * Created by holmes on 2020/6/18.
 **/
class StepRunner {

    companion object {

        @JvmStatic
        fun newRunner(): StepRunner {
            return StepRunner()
        }

        /**
         * DSL
         *
         * [step], [contStep]
         *
         * @param runningScope 要运行的在coroutine作用域
         */
        inline fun runner(
            runningScope: CoroutineScope, autoStart: Boolean = true,
            block: StepRunner.() -> Unit
        ): StepRunner {
            val r = newRunner()
            r.block()
            if (autoStart) {
                runningScope.launch {
                    r.start()
                }
            }
            return r
        }

    }


    private constructor()

    private val queue: LinkedList<Step> = LinkedList()

    /**
     * 如果中断了，会运行
     */
    var breakAction: Runnable? = null

    /**
     * 如果全部成功完成了，执行
     */
    var finishedAction: Runnable? = null

    private val starting = AtomicBoolean(false)

    fun put(step: Step) {
        queue.offer(step)
    }

    suspend fun start() {
        if (!starting.compareAndSet(false, true)) {
            return
        }

        var finished = true

        while (true) {
            val step = try {
                queue.pop()
            } catch (e: Exception) {
                break
            }
            val ok = step?.runStep() ?: false

            if (!ok) {
                finished = false
                break
            }
        }

        if (finished) {
            finishedAction?.run()
        } else {
            breakAction?.run()
        }
    }

    abstract class Step {

        /**
         * 运行一步
         * @return true 走next,  false 中断
         */
        abstract suspend fun runStep(): Boolean

    }

    // === DSL ===

    // 写法:
    /*
    runner {
        step {
            // ...
            true
        }

        contStep { cont ->
            object : callback {
                override fun onSuccess() {
                    cont.resume(true)
                }
                override fun onFailure() {
                    cont.resume(false)
                }
            }
        }
    }

    */


    /**
     * 默认 顺序运行的代码块
     */
    inline fun StepRunner.step(crossinline block: () -> Boolean): Step {
        return object : Step() {
            override suspend fun runStep(): Boolean = block()
        }.also { s ->
            this.put(s)
        }
    }

    /**
     * 传统， 异步代码块. 控制[cont]，来触发Step流程
     */
    inline fun StepRunner.contStep(crossinline block: (cont: Continuation<Boolean>) -> Unit): Step {
        return object : Step() {
            override suspend fun runStep(): Boolean {
                return suspendCoroutine<Boolean> { cont ->
                    val reenterCont = ReenterContinuation(cont)
                    block(reenterCont)
                }
            }
        }.also { s ->
            this.put(s)
        }
    }

    // === $ ===

    /**
     * 可以重入resume的continuation。
     * 但resume只有第一次有效。
     * 后面重入的都会会被 忽略。
     */
    class ReenterContinuation(private val baseCont: Continuation<Boolean>) :
        Continuation<Boolean> {
        private val resumed = AtomicBoolean()

        override val context: CoroutineContext
            get() = baseCont.context

        override fun resumeWith(result: Result<Boolean>) {
            if (resumed.compareAndSet(false, true)) {
                baseCont.resumeWith(result)
            } else {
                VLog.w("continuation is already Resumed")
            }
        }
    }

}