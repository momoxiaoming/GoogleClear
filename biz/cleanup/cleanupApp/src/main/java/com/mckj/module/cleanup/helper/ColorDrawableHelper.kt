package com.mckj.module.cleanup.helper

import android.graphics.drawable.GradientDrawable
import com.mckj.module.cleanup.util.ColorDrawableAnim

/**
 * Describe:
 *
 * Created By yangb on 2021/5/17
 */
class ColorDrawableHelper(private val startColors: IntArray, private val endColors: IntArray) {

    /**
     * 过渡视图
     */
    private val mColorDrawableAnim: ColorDrawableAnim by lazy {
        ColorDrawableAnim(startColors, endColors)
    }

    /**
     * 通过大小获取颜色过渡值
     */
    fun getDrawableBySize(size: Long): GradientDrawable? {
        val progress: Float = getProgress(size)
        return mColorDrawableAnim.getDrawable(progress)
    }

    fun getColorBySize(size: Long): Int? {
        val progress: Float = getProgress(size)
        return mColorDrawableAnim.getColor(progress)
    }

    private fun getProgress(size: Long): Float {
        val maxSize = 100 * 1024 * 1024
        return if (size > 0) {
            if (size > maxSize) {
                1f
            } else {
                size.toFloat() / maxSize
            }
        } else {
            0f
        }
    }

}