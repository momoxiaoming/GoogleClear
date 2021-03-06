package com.mckj.baselib.view.anim

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.lifecycle.Lifecycle

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
class ZoomAnimator(
    override val lifecycle: Lifecycle,
    private val view: View,
    private vararg val values: Float,
    private val time: Long = 1000
) : BaseAnimator(lifecycle) {

    override fun createAnimator(): Animator {
        val animatorX = ObjectAnimator.ofFloat(view, "scaleX", *values)
            .apply {
                duration = time
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
            }
        val animatorY = ObjectAnimator.ofFloat(view, "scaleY", *values)
            .apply {
                duration = time
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
            }
        //创建动画集
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animatorX, animatorY)
        return animatorSet
    }

}