package com.dn.baselib.anim

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.Lifecycle

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
class JumpAnimator(override val lifecycle: Lifecycle, private val view: View, private vararg val values: Float) : BaseAnimator(lifecycle) {

    override fun createAnimator(): Animator {
        return ObjectAnimator.ofFloat(view, "translationY", *values)
                .apply {
                    duration = 1500L
                    repeatCount = ValueAnimator.INFINITE
                    repeatMode = ValueAnimator.RESTART
                    interpolator = AccelerateDecelerateInterpolator()
                }
    }

}