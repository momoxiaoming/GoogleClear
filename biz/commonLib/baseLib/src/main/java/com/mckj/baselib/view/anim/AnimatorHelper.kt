package com.mckj.baselib.view.anim

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.Lifecycle

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */

object AnimatorHelper {

    /**
     * 执行动画
     */
    fun playUpdate(from: Int, to: Int, time: Long = 500L, update: (Int) -> Unit): ValueAnimator {
        return ValueAnimator.ofInt(from, to).apply {
            duration = time
            repeatCount = 0
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                update(it.animatedValue as Int)
            }
            start()
        }
    }

}

/**
 * 启动放大缩小动画
 */
fun View.playZoom(lifecycle: Lifecycle): BaseAnimator {
    return ZoomAnimator(lifecycle, this, 1f, 1.1f, 1f).apply { play() }
}

/**
 * 上下跳动画
 */
fun View.playJump(lifecycle: Lifecycle): BaseAnimator {
    return JumpAnimator(lifecycle, this, -10.0f, 5.0f, -10.0f).apply {
        play((0L..1000L).random())
    }
}

/**
 * 旋转动画
 */
fun View.playSpin(lifecycle: Lifecycle): BaseAnimator {
    return SpinAnimator(lifecycle, this, -90f, 360f, -90f).apply { play() }
}

/**
 * 回转动画
 */
fun View.playRotation(lifecycle: Lifecycle): BaseAnimator {
    return RotationAnimator(lifecycle, this, 500,0f, 360f).apply { play() }
}

/**
 * 回转动画，指定延迟
 */
fun View.playRotation(lifecycle: Lifecycle, delay:Long): BaseAnimator {
    return RotationAnimator(lifecycle, this, delay,0f, 360f).apply { play() }
}

/**
 * 逆向回转动画
 */
fun View.playRotationReverse(lifecycle: Lifecycle): BaseAnimator {
    return RotationAnimator(lifecycle, this, 500,360f, 0f).apply { play() }
}

/**
 * 逆向回转动画, 指定延迟
 */
fun View.playRotationReverse(lifecycle: Lifecycle, delay: Long): BaseAnimator {
    return RotationAnimator(lifecycle, this, delay,360f, 0f).apply { play() }
}

/**
 * 抖动动画
 */
fun View.jitter(lifecycle: Lifecycle): BaseAnimator {
    return JitterAnimator(lifecycle, this).apply { play() }
}
