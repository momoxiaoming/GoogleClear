package com.mckj.sceneslib.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.mckj.sceneslib.R
import com.mckj.sceneslib.util.Log

/**
 * Describe:
 *
 * Created By yangb on 2021/6/8
 */
class BezierBackgroundView(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    companion object {
        const val TAG = "BezierBackgroundView"
    }

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context) : this(context, null)

    /**
     * 贝塞尔曲线高度
     */
    private var mBezierHeight: Float = 0f

    /**
     * 百分比（0-1）
     */
    private var mBezierProgress: Float = 0f

    private val mBezierPath: Path by lazy {
        Path()
    }
    private var mBezierDrawable: Drawable? = null

    init {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.BezierBackgroundView)
        mBezierHeight = typedArray.getDimension(R.styleable.BezierBackgroundView_bezierHeight, 0f)
        mBezierProgress = typedArray.getFloat(R.styleable.BezierBackgroundView_bezierProgress, 0f)
        typedArray.recycle()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val width = right - left
        val height = bottom - top
        Log.i(TAG, "onLayout: width:$width height:$height")
        resetBezierPath(width, height)
        resetDrawable(width, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val drawable = mBezierDrawable
        if (drawable != null) {
            canvas.save()
            canvas.clipPath(mBezierPath)
            drawable.draw(canvas)
            canvas.restore()
        }
    }

    /**
     * 重置曲线path
     */
    private fun resetBezierPath(width: Int, height: Int) {
        val bezierHeight = mBezierHeight * mBezierProgress * 2
        Log.i(TAG, "resetBezierPath: bezierHeight:$bezierHeight")
        mBezierPath.reset()
        //左上角
        mBezierPath.moveTo(0f, 0f)
        //右上角
        mBezierPath.lineTo(width.toFloat(), 0f)
        //右下角
        mBezierPath.lineTo(width.toFloat(), height - mBezierHeight)

        //贝塞尔曲线控制点
        val controlX: Float = width / 2f
        val controlY: Float = (height - mBezierHeight) + bezierHeight
        //控制点---左下角
        mBezierPath.quadTo(controlX, controlY, 0f, height - mBezierHeight)
        mBezierPath.close()
    }

    private fun resetDrawable(width: Int, height: Int) {
        mBezierDrawable?.setBounds(0, 0, width, height)
    }

    fun setBezierDrawable(drawable: Drawable?) {
        mBezierDrawable = drawable
        requestLayout()
    }

    fun getBezierDrawable(): Drawable? {
        return mBezierDrawable
    }

    fun setBezierHeight(height: Float) {
        val h = when {
            height < 0 -> {
                0f
            }
            else -> {
                height
            }
        }
        mBezierHeight = h
        requestLayout()
    }

    fun getBezierHeight(): Float {
        return mBezierHeight
    }

    fun setBezierProgress(progress: Float) {
        val p = when {
            progress > 1 -> {
                1f
            }
            progress < -1 -> {
                0f
            }
            else -> {
                progress
            }
        }
        mBezierProgress = p
        requestLayout()
    }

    fun getBezierProgress(): Float {
        return mBezierProgress
    }

}