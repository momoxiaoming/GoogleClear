package com.dn.baselib.anim

import android.animation.Animator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
abstract class BaseAnimator(open val lifecycle: Lifecycle) : LifecycleObserver {

    /**
     * 是否播放
     */
    private var isPlay: AtomicBoolean = AtomicBoolean(false)

    private val mAnimator by lazy {
        createAnimator()
    }

    /**
     * 创建动画
     */
    abstract fun createAnimator(): Animator

    /**
     * 开始执行
     */
    open fun play(delay: Long = 0) {
        isPlay.set(true)
        mAnimator.startDelay = delay
        lifecycle.removeObserver(this)
        lifecycle.addObserver(this)
    }

    /**
     * 关闭动画
     */
    open fun close() {
        isPlay.set(false)
        end()
    }

    fun isRunning(): Boolean = mAnimator.isRunning

    protected open fun start() {
        if (isPlay.get() && !mAnimator.isRunning) {
            mAnimator.start()
        }
    }

    protected open fun end() {
        if (mAnimator.isRunning) {
            mAnimator.end()
        }
    }

    protected open fun cancel() {
        if (mAnimator.isRunning) {
            mAnimator.cancel()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        cancel()
    }

}