package com.mckj.module.cleanup.util

import android.graphics.drawable.GradientDrawable
import com.mckj.module.cleanup.manager.ArgbEvaluator


/**
 * Describe:
 *
 * Created By yangb on 2021/3/2
 */
class ColorDrawableAnim(private val start: IntArray, private val end: IntArray) {

    companion object {
        const val TAG = "ColorDrawableAnim"
    }

    private val mDrawable: GradientDrawable by lazy {
        GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            start
        )
    }

    private val mArgbEvaluator: ArgbEvaluator by lazy {
        ArgbEvaluator()
    }

    /**
     * 获取Drawable
     */
    fun getDrawable(progress: Float): GradientDrawable? {
        if (start.size != end.size) {
            Log.i(TAG, "getDrawable error: start.size[${start.size}] != end.size[${end.size}]")
            return null
        }
        Log.i(TAG, "getDrawable: progress:$progress size:${start.size}")
        return when (start.size) {
            1 -> {
                val color = mArgbEvaluator.evaluate(progress, start[0], end[0])
                Log.i(TAG, "getDrawable: color:$color start:${start[0]} end:${end[0]}")
                mDrawable.setColor(color)
                mDrawable
            }
            2,
            3 -> {
                val startColor = mArgbEvaluator.evaluate(progress, start[0], end[0])
                val endColor = mArgbEvaluator.evaluate(progress, start[1], end[1])
                mDrawable.colors = intArrayOf(startColor, endColor)
                mDrawable
            }
            else -> {
                null
            }
        }
    }

    /**
     * 获取Color
     */
    fun getColor(progress: Float): Int? {
        if (start.size != end.size) {
            Log.i(TAG, "getColor error: start.size[${start.size}] != end.size[${end.size}]")
            return null
        }
        Log.i(TAG, "getColor: progress:$progress size:${start.size}")
        return if (start.isNotEmpty()) {
            mArgbEvaluator.evaluate(progress, start[0], end[0])
        } else {
            null
        }
    }

}