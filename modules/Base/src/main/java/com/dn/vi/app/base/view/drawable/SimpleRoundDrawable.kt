package com.dn.vi.app.base.view.drawable

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.StateSet
import androidx.annotation.RequiresApi
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable.Companion.create
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable.Companion.createFullRadius
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable.Companion.createOutline
import org.jetbrains.anko.dip

/**
 * 常用圆角矩形
 *
 * 一般图，可以直接用 createXX方法，快熟创建常用类。
 * [create], [createFullRadius], [createOutline]
 *
 * Created by holmes on 2018/3/18.
 */
class SimpleRoundDrawable(context: Context?, color: Int) : Drawable() {

    companion object {

        /**
         * 生成一个颜色为[color], 圆角为[roundRadius]的Drawable
         */
        @JvmStatic
        fun create(color: Int, roundRadius: Float): SimpleRoundDrawable {
            return SimpleRoundDrawable(null, color).also { d ->
                d.roundRadius = roundRadius
            }
        }

        /**
         * 生成一个颜色为[color], 完全圆角的Drawable
         * (左右，两边都是 子弹头)
         */
        @JvmStatic
        fun createFullRadius(color: Int): SimpleRoundDrawable {
            return SimpleRoundDrawable(null, color).also { d ->
                d.fullRound = true
            }
        }

        /**
         * 生成一个outline用的drawable, 不用draw
         */
        @JvmStatic
        fun createOutline(roundRadius: Float): SimpleRoundDrawable {
            return SimpleRoundDrawable(null, 0).also { d ->
                d.roundRadius = roundRadius
            }
        }

    }

    private val pressedState = intArrayOf(android.R.attr.state_pressed)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 自动全圆角， 左右都是完整的半圆
     * 会自动根据边框大小设置 [roundRadius]
     */
    var fullRound: Boolean = false

    var color: Int = color
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    var colorPressed: Int = 0
        set(value) {
            if (field != value) {
                field = value
                if (pressed) {
                    invalidateSelf()
                }
            }
        }

    var strokeColor: Int = 0
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    var strokeWidth: Float = 1f

    var strokeColorPressed: Int = 0
        set(value) {
            if (field != value) {
                field = value
                if (pressed) {
                    invalidateSelf()
                }
            }
        }

    var roundRadius: Float = context?.dip(2)?.toFloat() ?: 5f
        set(value) {
            if (field != value) {
                field = value
                invalidateSelf()
            }
        }

    private val roundRectf = RectF()
    private val padding = Rect()
    private val size = Point()
    private var pressed = false

    init {
        paint.style = Paint.Style.FILL
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        roundRectf.set(bounds)
        if (fullRound) {
            roundRadius = roundRectf.height() / 2f
        }
    }

    override fun isStateful(): Boolean {
        if (colorPressed != 0) {
            return true
        }
        return super.isStateful()
    }

    override fun onStateChange(state: IntArray?): Boolean {
        if (state == null || colorPressed == 0) {
            return false
        }
        val old = pressed
        pressed = StateSet.stateSetMatches(pressedState, state)
        if (old != pressed) {
            return true
        }
        return super.onStateChange(state)
    }

    override fun draw(canvas: Canvas) {
        paint.color = if (!pressed || colorPressed == 0) color else colorPressed
        val stroke = if (!pressed || strokeColorPressed == 0) strokeColor else strokeColorPressed
        if (paint.color == 0 && stroke == 0) {
            return
        }

        if (roundRadius > 1f) {
            canvas.drawRoundRect(roundRectf, roundRadius, roundRadius, paint)
        } else {
            canvas.drawRect(bounds, paint)
        }

        if (stroke != 0) {
            // draw stroke

            val oldStyle = paint.style
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = strokeWidth
            paint.color = stroke

            if (roundRadius > 1f) {
                canvas.drawRoundRect(roundRectf, roundRadius, roundRadius, paint)
            } else {
                canvas.drawRect(bounds, paint)
            }

            paint.style = oldStyle
        }

    }

    override fun getIntrinsicHeight(): Int {
        return if (size.y > 0) size.y else 35
    }

    override fun getIntrinsicWidth(): Int {
        return if (size.x > 0) size.x else 35
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    fun setSize(w: Int, h: Int) {
        size.set(w, h)
        invalidateSelf()
    }

    fun setPadding(l: Int, t: Int, r: Int, b: Int) {
        padding.set(l, t, r, b)
        invalidateSelf()
    }

    override fun getPadding(p: Rect): Boolean {
        if (padding.left != 0 && padding.top != 0 &&
            padding.right != 0 && padding.bottom != 0
        ) {
            p.set(padding)
            return true
        }
        return super.getPadding(padding)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutline(outline: Outline) {
        if (roundRadius > 1f) {
            outline.setRoundRect(
                roundRectf.left.toInt(), roundRectf.top.toInt(),
                roundRectf.right.toInt(), roundRectf.bottom.toInt(),
                roundRadius
            )
        } else {
            outline.setRect(bounds)
        }
        outline.alpha = 1F
    }

}