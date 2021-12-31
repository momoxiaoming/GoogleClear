package com.dn.vi.app.base.view.drawable

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import androidx.appcompat.graphics.drawable.DrawableWrapper

/**
 * 在以有drawable的基础上。
 * 上下左右，四边画一条divider线.
 *
 * 注：边框线，是没有实际空间的, 直接盖在原背景上
 *
 * Created by holmes on 7/11.
 */
@SuppressLint("RestrictedApi")
class BorderLineDividerDrawable(drawable: Drawable?) : DrawableWrapper(drawable) {

    companion object {

        /**
         * attach to a view
         */
        @JvmStatic
        fun attach(view: View): BorderLineDividerDrawable {
            val bg = view.background
            if (bg !is BorderLineDividerDrawable) {
                val bd = BorderLineDividerDrawable(bg ?: ColorDrawable(Color.TRANSPARENT))
                view.background = bd
                return bd
            }
            return bg
        }

    }

    var lineColor: Int = 0xffEDEDED.toInt()
        set(value) {
            if (field != value) {
                field = value
                paint.color = value
                invalidateSelf()
            }
        }
    var lineWidth: Int = 1
        set(value) {
            if (field != value && value > 0) {
                field = value
                invalidateSelf()
            }
        }
    /**
     * like [Gravity.TOP], [LEFT|TOP|RIGHT|BOTTOM]
     */
    var gravity: Int = Gravity.TOP
        set(value) {
            if (field != value && value > 0) {
                field = value
                invalidateSelf()
            }
        }

    /**
     * 相应方向上的差值
     */
    var offset: Int = 0

    private val paint by lazy {
        Paint().also { p ->
            p.color = lineColor
            p.style = Paint.Style.FILL
        }
    }
    private val lineBound = Rect()

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val bound = lineBound

        // left
        val orgBound = bounds
        if ((gravity and Gravity.LEFT) == Gravity.LEFT) {
            bound.set(orgBound)
            bound.right = bound.left + lineWidth
            if (offset > 0) {
                bound.top = bound.top + offset
                bound.bottom = bound.bottom - offset
            }
            canvas.drawRect(bound, paint)
        }
        // top
        if ((gravity and Gravity.TOP) == Gravity.TOP) {
            bound.set(orgBound)
            bound.bottom = bound.top + lineWidth
            if (offset > 0) {
                bound.left = bound.left + offset
                bound.right = bound.right - offset
            }
            canvas.drawRect(bound, paint)
        }
        // right
        if ((gravity and Gravity.RIGHT) == Gravity.RIGHT) {
            bound.set(orgBound)
            bound.left = bound.right - lineWidth
            if (offset > 0) {
                bound.top = bound.top + offset
                bound.bottom = bound.bottom - offset
            }
            canvas.drawRect(bound, paint)
        }
        // bottom
        if ((gravity and Gravity.BOTTOM) == Gravity.BOTTOM) {
            bound.set(orgBound)
            bound.top = bound.bottom - lineWidth
            if (offset > 0) {
                bound.left = bound.left + offset
                bound.right = bound.right - offset
            }
            canvas.drawRect(bound, paint)
        }

    }

}