package com.mckj.module.cleanup.manager

import android.animation.TypeEvaluator
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Describe:
 *
 * Created By yangb on 2021/3/2
 */
class ArgbEvaluator : TypeEvaluator<Int> {

    override fun evaluate(fraction: Float, startValue: Int, endValue: Int): Int {
        //起始颜色ARGB颜色通道拆分
        var startA = (startValue shr 24 and 0xff) / 255.0f
        var startR = (startValue shr 16 and 0xff) / 255.0f
        var startG = (startValue shr 8 and 0xff) / 255.0f
        var startB = (startValue and 0xff) / 255.0f//透明度

        //结束颜色ARGB颜色通道拆分
        var endA = (endValue shr 24 and 0xff) / 255.0f
        var endR = (endValue shr 16 and 0xff) / 255.0f
        var endG = (endValue shr 8 and 0xff) / 255.0f
        var endB = (endValue and 0xff) / 255.0f//透明度

        //颜色数值线性增加
        startR = startR.toDouble().pow(2.2).toFloat()
        startG = startG.toDouble().pow(2.2).toFloat()
        startB = startB.toDouble().pow(2.2).toFloat()


        endR = endR.toDouble().pow(2.2).toFloat()
        endG = endG.toDouble().pow(2.2).toFloat()
        endB = endB.toDouble().pow(2.2).toFloat()

        // 根据时间因子，计算出过渡的颜色插值
        var a = startA + fraction * (endA - startA)
        var r = startR + fraction * (endR - startR)
        var g = startG + fraction * (endG - startG)
        var b = startB + fraction * (endB - startB)

        // 再将颜色转换回ARGB[0，255]
        a *= 255.0f
        r = r.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
        g = g.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f
        b = b.toDouble().pow(1.0 / 2.2).toFloat() * 255.0f

        //将分离ARGB颜色通道打包装车
        return (a.roundToInt() shl 24) or (r.roundToInt() shl 16) or (g.roundToInt() shl 8) or b.roundToInt()
    }
}