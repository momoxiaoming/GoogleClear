package com.org.gradle.vest1.view

import android.animation.IntEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import com.org.gradle.vest1.R
import com.org.proxy.log.log


/**
 * CsProgressView
 *
 * @author mmxm
 * @date 2022/7/19 22:41
 */
class CsProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : android.view.View(context, attrs, defStyleAttr) {

    private var mWidth = 200f

    private var mHeight = 200f

    /**
     * 圆环宽度
     */
    private var pgWidth = 120f

    private var mProgress: Float = 0f
    private var mMaxProgress: Float = 100f



    /**
     * 背景圆画笔
     */
    private val bgPaint: Paint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = pgWidth
        paint.color = Color.parseColor("#f1f1f1")
        paint
    }

    /**
     * 圆环画笔
     */
    private val pgPaint: Paint by lazy {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = pgWidth
        paint.strokeCap = Paint.Cap.ROUND
        paint.color = resources.getColor(R.color.color_8a7aff)
        paint
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()// 获取圆、圆环的宽度
        mHeight = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        val widMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val defaultWidth = resources.displayMetrics.density.toInt() * 4
        if (mWidth == 0f || widMode != MeasureSpec.EXACTLY) {
            mWidth = defaultWidth.toFloat()
        }
        if (mHeight == 0f || heightMode != MeasureSpec.EXACTLY) {
            mHeight = defaultWidth.toFloat()
        }
        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //先画环背景
            drawBgCire(it)
            //再化圆弧
            drawCire(it)
        }

    }

    private fun drawBgCire(canvas: Canvas) {
        val cx = mWidth / 2f
        val cy = mHeight / 2f
        val radius = mWidth / 2 - pgWidth / 2
        canvas.drawCircle(cx, cy, radius, bgPaint)
    }

    private fun drawCire(canvas: Canvas) {
        val rectf =
            RectF(0f + pgWidth / 2, 0f + pgWidth / 2, mWidth - pgWidth / 2, mHeight - pgWidth / 2)
        val angle = 360 * mProgress / mMaxProgress
        canvas.drawArc(rectf, -90f, angle, false, pgPaint)

    }

    fun setProgress(progress: Float) {
        this.mProgress = progress
        invalidate()
    }

    @SuppressLint("Recycle")
    fun startProgress(start: Float, end: Float,animTime:Long=2000) {
        val anim=ValueAnimator.ofFloat(start,end)
        anim.duration = animTime
        anim.setTarget(this)
        anim.addUpdateListener {
            setProgress(it.animatedValue as Float)
        }
        anim.start()

    }

}