package com.dn.vi.app.base.view.drawable

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * 就是个普通无色的drawable
 * Created by holmes on 17-7-17.
 */
class DividerDrawable(width: Int, height: Int) : Drawable() {

    constructor(size: Int) : this(size, size)

    private val iw = width
    private val ih = height
    private val paint by lazy { Paint() }

    private var marginLeft = 0
    private var marginTop = 0
    private var marginRight = 0
    private var marginBottom = 0
    private val drawRect: Rect by lazy { Rect() }

    var color: Int = 0

    fun setMargin(l: Int, t: Int, r: Int, b: Int) {
        marginLeft = l
        marginTop = t
        marginRight = r
        marginBottom = b
    }

    override fun getIntrinsicWidth(): Int {
        return iw
    }

    override fun getIntrinsicHeight(): Int {
        return ih
    }

    override fun draw(canvas: Canvas) {
        if (color != 0) {
            paint.color = color
            drawRect.set(bounds)
            drawRect.left += marginLeft
            drawRect.top += marginTop
            drawRect.right -= marginRight
            drawRect.bottom -= marginBottom
            canvas.drawRect(drawRect, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSPARENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

}