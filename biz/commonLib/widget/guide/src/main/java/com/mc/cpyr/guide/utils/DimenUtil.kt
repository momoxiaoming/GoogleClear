package com.mc.cpyr.guide.utils

import android.content.Context

/**
 * DimenUtil
 *
 * @author mmxm
 * @date 2021/3/8 17:46
 */
object DimenUtil {
    /** sp转换成px  */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.applicationContext.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /** px转换成sp  */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.applicationContext.resources.displayMetrics.density
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /** dip转换成px  */
    fun dp2px(context: Context, dipValue: Float): Int {
        val scale = context.applicationContext.resources.displayMetrics.scaledDensity
        return (dipValue * scale + 0.5f).toInt()
    }

    /** px转换成dip  */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.applicationContext.resources.displayMetrics.scaledDensity
        return (pxValue / scale + 0.5f).toInt()
    }
}