package com.mc.cpyr.guide.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.mc.cpyr.guide.config.Component
import com.mc.cpyr.guide.utils.DimenUtil


/**
 * 蒙版view
 *
 * @author mmxm
 * @date 2021/3/8 16:31
 */
class MaskView  :ViewGroup{
    /**
     * 高亮区域
     */
    private val mTargetRect = RectF()

    /**
     * 蒙层区域
     */
    private val mOverlayRect = RectF()

    /**
     * 中间变量
     */
    private val mChildTmpRect = RectF()

    /**
     * 蒙层背景画笔
     */
    private val mFullingPaint: Paint

    /**
     * 设置padding
     */
    private var mPadding = 0
    private var mPaddingLeft = 0
    private var mPaddingTop = 0
    private var mPaddingRight = 0
    private var mPaddingBottom = 0

    /**
     * 是否覆盖目标区域
     */
    private var mOverlayTarget = false

    /**
     * 圆角大小
     */
    private var mCorner = 0

    /**
     * 设置单独的圆角,0代表正常,1代表圆角, 对应上左,上右,下左,下右 四个角
     */
    private var mAloneCorner = intArrayOf(0, 0, 0, 0)

    /**
     * 目标区域样式，默认为矩形
     */
    private var mStyle = Component.ROUNDRECT

    /**
     * 挖空画笔
     */
    private var mEraser: Paint

    /**
     * 橡皮擦Bitmap
     */
    private var mEraserBitmap: Bitmap?

    /**
     * 橡皮擦Cavas
     */
    private var mEraserCanvas: Canvas? = null

    private var ignoreRepadding = false

    private var mInitHeight = 0
    private var mChangedHeight = 0
    private var mFirstFlag = true



    init{
        setWillNotDraw(false)
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        val height = displayMetrics.heightPixels
        mOverlayRect.set(0f, 0f, width.toFloat(), height.toFloat())
        mEraserBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mEraserCanvas = Canvas(mEraserBitmap!!)
        mFullingPaint = Paint()
        mEraser = Paint()
        mEraser.color = -0x1
        //图形重叠时的处理方式，擦除效果
        mEraser.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        //位图抗锯齿设置
        mEraser.flags = Paint.ANTI_ALIAS_FLAG
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {


    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        try {
            clearFocus()
            mEraserCanvas!!.setBitmap(null)
            mEraserBitmap = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = MeasureSpec.getSize(heightMeasureSpec)
        if (mFirstFlag) {
            mInitHeight = h
            mFirstFlag = false
        }
        mChangedHeight = if (mInitHeight > h) {
            h - mInitHeight
        } else if (mInitHeight < h) {
            h - mInitHeight
        } else {
            0
        }
        setMeasuredDimension(w, h)
        mOverlayRect[0f, 0f, w.toFloat()] = h.toFloat()
        resetOutPath()
        val count = childCount
        var child: View?
        for (i in 0 until count) {
            child = getChildAt(i)
            child?.let { measureChild(it, widthMeasureSpec, heightMeasureSpec) }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val count = childCount
        var child: View?
        for (i in 0 until count) {
            child = getChildAt(i)
            if (child == null) {
                continue
            }
            val lp = child.layoutParams as LayoutParams ?: continue
            when (lp.targetAnchor) {
                LayoutParams.ANCHOR_LEFT -> {
                    mChildTmpRect.right = mTargetRect.left
                    mChildTmpRect.left = mChildTmpRect.right - child.measuredWidth
                    verticalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition)
                }
                LayoutParams.ANCHOR_TOP -> {
                    mChildTmpRect.bottom = mTargetRect.top
                    mChildTmpRect.top = mChildTmpRect.bottom - child.measuredHeight
                    horizontalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition)
                }
                LayoutParams.ANCHOR_RIGHT -> {
                    mChildTmpRect.left = mTargetRect.right
                    mChildTmpRect.right = mChildTmpRect.left + child.measuredWidth
                    verticalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition)
                }
                LayoutParams.ANCHOR_BOTTOM -> {
                    mChildTmpRect.top = mTargetRect.bottom
                    mChildTmpRect.bottom = mChildTmpRect.top + child.measuredHeight
                    horizontalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition)
                }
                LayoutParams.ANCHOR_OVER -> {
                    mChildTmpRect.left =
                        (mTargetRect.width().toInt() - child.measuredWidth shr 1).toFloat()
                    mChildTmpRect.top =
                        (mTargetRect.height().toInt() - child.measuredHeight shr 1).toFloat()
                    mChildTmpRect.right =
                        (mTargetRect.width().toInt() + child.measuredWidth shr 1).toFloat()
                    mChildTmpRect.bottom =
                        (mTargetRect.height().toInt() + child.measuredHeight shr 1).toFloat()
                    mChildTmpRect.offset(mTargetRect.left, mTargetRect.top)
                }
            }
            //额外的xy偏移
            mChildTmpRect.offset(DimenUtil.dp2px(context!!, lp.offsetX.toFloat()).toFloat(), DimenUtil.dp2px(context!!, lp.offsetY.toFloat()).toFloat())
            child.layout(
                mChildTmpRect.left.toInt(), mChildTmpRect.top.toInt(),
                mChildTmpRect.right.toInt(),
                mChildTmpRect.bottom.toInt()
            )
        }
    }

    private fun horizontalChildPositionLayout(child: View, rect: RectF, targetParentPosition: Int) {
        when (targetParentPosition) {
            LayoutParams.PARENT_START -> {
                rect.left = mTargetRect.left
                rect.right = rect.left + child.measuredWidth
            }
            LayoutParams.PARENT_CENTER -> {
                rect.left = (mTargetRect.width() - child.measuredWidth) / 2
                rect.right = (mTargetRect.width() + child.measuredWidth) / 2
                rect.offset(mTargetRect.left, 0f)
            }
            LayoutParams.PARENT_END -> {
                rect.right = mTargetRect.right
                rect.left = rect.right - child.measuredWidth
            }
        }
    }

    private fun verticalChildPositionLayout(child: View, rect: RectF, targetParentPosition: Int) {
        when (targetParentPosition) {
            LayoutParams.PARENT_START -> {
                rect.top = mTargetRect.top
                rect.bottom = rect.top + child.measuredHeight
            }
            LayoutParams.PARENT_CENTER -> {
                rect.top = (mTargetRect.width() - child.measuredHeight) / 2
                rect.bottom = (mTargetRect.width() + child.measuredHeight) / 2
                rect.offset(0f, mTargetRect.top)
            }
            LayoutParams.PARENT_END -> {
                rect.bottom = mTargetRect.bottom
                rect.top = mTargetRect.bottom - child.measuredHeight
            }
        }
    }

    private fun resetOutPath() {
        resetPadding()
    }

    /**
     * 设置padding
     */
    private fun resetPadding() {
        if (!ignoreRepadding) {
            if (mPadding != 0 && mPaddingLeft == 0) {
                mTargetRect.left -= mPadding.toFloat()
            }
            if (mPadding != 0 && mPaddingTop == 0) {
                mTargetRect.top -= mPadding.toFloat()
            }
            if (mPadding != 0 && mPaddingRight == 0) {
                mTargetRect.right += mPadding.toFloat()
            }
            if (mPadding != 0 && mPaddingBottom == 0) {
                mTargetRect.bottom += mPadding.toFloat()
            }
            if (mPaddingLeft != 0) {
                mTargetRect.left -= mPaddingLeft.toFloat()
            }
            if (mPaddingTop != 0) {
                mTargetRect.top -= mPaddingTop.toFloat()
            }
            if (mPaddingRight != 0) {
                mTargetRect.right += mPaddingRight.toFloat()
            }
            if (mPaddingBottom != 0) {
                mTargetRect.bottom += mPaddingBottom.toFloat()
            }
            ignoreRepadding = true
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams? {
        return LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun dispatchDraw(canvas: Canvas?) {
        val drawingTime = drawingTime
        try {
            var child: View?
            for (i in 0 until childCount) {
                child = getChildAt(i)
                drawChild(canvas, child, drawingTime)
            }
        } catch (e: NullPointerException) {
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mChangedHeight != 0) {
            mTargetRect.offset(0f, mChangedHeight.toFloat())
            mInitHeight = mInitHeight + mChangedHeight
            mChangedHeight = 0
        }
        mEraserBitmap?.eraseColor(Color.TRANSPARENT)
        mEraserCanvas!!.drawColor(mFullingPaint.color)
        if (!mOverlayTarget) {
            when (mStyle) {
                Component.ROUNDRECT -> mEraserCanvas!!.drawRoundRect(
                    mTargetRect,
                    mCorner.toFloat(),
                    mCorner.toFloat(),
                    mEraser
                )
                Component.CIRCLE -> mEraserCanvas!!.drawCircle(
                    mTargetRect.centerX(), mTargetRect.centerY(),
                    mTargetRect.width() / 2, mEraser
                )
                Component.ROUND_ALONE_RECT -> {
                    val path = RoundedRect(
                        mTargetRect.left,
                        mTargetRect.top,
                        mTargetRect.right,
                        mTargetRect.bottom,
                        mCorner.toFloat(),
                        mCorner.toFloat(),
                        mAloneCorner[0] == 1,
                        mAloneCorner[1] == 1,
                        mAloneCorner[2] == 1,
                        mAloneCorner[3] == 1
                    )
                    mEraserCanvas!!.drawPath(path, mEraser)
                }
                else -> mEraserCanvas!!.drawRoundRect(
                    mTargetRect,
                    mCorner.toFloat(),
                    mCorner.toFloat(),
                    mEraser
                )
            }
        }
        mEraserBitmap?.let { canvas.drawBitmap(it, mOverlayRect.left, mOverlayRect.top, null) }
    }



    /**
     * 是否已点击目标区域
     * @param x Float
     * @param y Float
     * @return Boolean
     */
    fun isClickTarget(x: Float, y: Float): Boolean {
        if (x > mTargetRect.left && x < mTargetRect.right && y < mTargetRect.bottom && y > mTargetRect.top) {
            return true
        }
        return false
    }

    /**
     * 绘制不同角度的圆角矩形
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param rx
     * @param ry
     * @param tl
     * @param tr
     * @param br
     * @param bl
     * @return
     */
    fun RoundedRect(
        left: Float, top: Float, right: Float, bottom: Float, rx1: Float, ry1: Float,
        tl: Boolean, tr: Boolean, br: Boolean, bl: Boolean
    ): Path {
        val path = Path()
        var rx=rx1
        var ry=ry1
        if (rx < 0) {
            rx = 0f
        }
        if (ry < 0) {
            ry = 0f
        }
        val width = right - left
        val height = bottom - top
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry
        path.moveTo(right, top + ry)
        if (tr) path.rQuadTo(0f, -ry, -rx, -ry) //top-right corner
        else {
            path.rLineTo(0f, -ry)
            path.rLineTo(-rx, 0f)
        }
        path.rLineTo(-widthMinusCorners, 0f)
        if (tl) path.rQuadTo(-rx, 0f, -rx, ry) //top-left corner
        else {
            path.rLineTo(-rx, 0f)
            path.rLineTo(0f, ry)
        }
        path.rLineTo(0f, heightMinusCorners)
        if (bl) path.rQuadTo(0f, ry, rx, ry) //bottom-left corner
        else {
            path.rLineTo(0f, ry)
            path.rLineTo(rx, 0f)
        }
        path.rLineTo(widthMinusCorners, 0f)
        if (br) path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner
        else {
            path.rLineTo(rx, 0f)
            path.rLineTo(0f, -ry)
        }
        path.rLineTo(0f, -heightMinusCorners)
        path.close() //Given close, last lineto can be removed.
        return path
    }

    fun setTargetRect(rect: Rect?) {
        mTargetRect.set(rect!!)
    }

    fun getTargetRect(): RectF? {
        return mTargetRect
    }

    fun setFullingAlpha(alpha: Int) {
        mFullingPaint.alpha = alpha
    }

    fun setFullingColor(color: Int) {
        mFullingPaint.color = color
    }

    fun setHighTargetCorner(corner: Int) {
        mCorner = corner
    }

    fun setHighTargetGraphStyle(style: Int) {
        mStyle = style
    }

    fun setOverlayTarget(b: Boolean) {
        mOverlayTarget = b
    }

    fun setPadding(padding: Int) {
        mPadding = padding
    }

    fun setPaddingLeft(paddingLeft: Int) {
        mPaddingLeft = paddingLeft
    }

    fun setPaddingTop(paddingTop: Int) {
        mPaddingTop = paddingTop
    }

    fun setPaddingRight(paddingRight: Int) {
        mPaddingRight = paddingRight
    }

    fun setPaddingBottom(paddingBottom: Int) {
        mPaddingBottom = paddingBottom
    }

    fun setAloneCorner(mAloneCorner: IntArray) {
        this.mAloneCorner = mAloneCorner
    }



    class LayoutParams : ViewGroup.LayoutParams {
        var targetAnchor = ANCHOR_BOTTOM
        var targetParentPosition = PARENT_CENTER
        var offsetX = 0
        var offsetY = 0

        constructor(c: Context?, attrs: AttributeSet?) : super(c, attrs) {}
        constructor(width: Int, height: Int) : super(width, height) {}
        constructor(source: ViewGroup.LayoutParams?) : super(source) {}

        companion object {
            const val ANCHOR_LEFT = 0x01
            const val ANCHOR_TOP = 0x02
            const val ANCHOR_RIGHT = 0x03
            const val ANCHOR_BOTTOM = 0x04
            const val ANCHOR_OVER = 0x05
            const val PARENT_START = 0x10
            const val PARENT_CENTER = 0x20
            const val PARENT_END = 0x30
        }
    }
}