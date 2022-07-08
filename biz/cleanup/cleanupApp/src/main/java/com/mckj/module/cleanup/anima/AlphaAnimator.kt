package com.mckj.module.cleanup.anima

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import androidx.lifecycle.Lifecycle
import com.mckj.baselib.view.anim.BaseAnimator

class AlphaAnimator(override val lifecycle: Lifecycle, private val view: View, private vararg val values: Float, private val time:Long = 1000) : BaseAnimator(lifecycle)  {
    override fun createAnimator(): Animator {
        val alpha = ObjectAnimator.ofFloat(view, "alpha", *values)
            .apply {
                duration = time
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.RESTART
            }

        return alpha
    }
}