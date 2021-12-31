package com.dn.vi.app.base.helper

import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 处理timeout
 * created on 2020/12/26
 * @author holmes
 */
class TimeoutCont constructor(private val scope: CoroutineScope) {

    /**
     * 使用Main scope
     */
    constructor() : this(CoroutineScope(Job() + Dispatchers.Main))

    /**
     * 创建并启动一个timeout的job.
     * 如果超时了，则[onTimeout]会被执行
     *
     * timeout计时和[onTimeout]在[scope]协程下
     *
     * @return 需要取消timeout, 则调用Drone.done方法，标识已完成
     */
    fun createJob(timeMillis: Long, onTimeout: Runnable): Drone {
        val drone = Drone()
        scope.launch(Dispatchers.Default) {
            // debug { "timeout start" }
            val ok = withTimeoutOrNull(timeMillis) {
                drone.prepare()
                return@withTimeoutOrNull "done"
            }
            if (ok == null) {
                //debug { "timeout" }
                withContext(scope.coroutineContext) {
                    onTimeout.run()
                }
            }
            //debug { "done" }
        }
        return drone
    }

    /**
     * 配置timeout.
     * 如果一个任务执行完了，就调一下[done]
     *
     * 此时，如果 没有超时，就会取消点timeoutJob
     *
     */
    class Drone internal constructor() {

        private val isDone = AtomicBoolean()

        fun done() {
            isDone.compareAndSet(false, true)
        }

        internal suspend fun prepare(): Boolean {
            for (index in 0 until 1000) {
                if (isDone.get()) {
                    break
                }
                delay(50L)
            }
            return true
        }


    }

}