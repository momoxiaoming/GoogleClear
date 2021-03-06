package com.mckj.baselib.view.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.lifecycle.Lifecycle

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
class RotationAnimator(
    override val lifecycle: Lifecycle,
    private val view: View,
    private val mDuration: Long,
    private vararg val values: Float
) : BaseAnimator(lifecycle) {

    override fun createAnimator(): Animator {
        return ObjectAnimator.ofFloat(view, "rotation", *values)
            .apply {
                duration = mDuration
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
                interpolator = LinearInterpolator()
            }
    }

}