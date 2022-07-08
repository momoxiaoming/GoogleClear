package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.view.seekbar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.mckj.sceneslib.R
import org.jetbrains.anko.dip


/**
 * 纵向seekbar
 * 目前只支持进度从下到上
 * @author mmxm
 * @date 2022/3/29 19:26
 */
class VSeekbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ISeekbar {


    private val mWidth=dip(13)

    /**
     * 背景颜色
     */
    private val bgColor = Color.parseColor("#80000000")

    /**
     * 进度颜色
     */
    private val progressColor = Color.parseColor("#ffffff")

    /**
     * 指示器颜色
     */
    private val arcColor = Color.parseColor("#ffffff")

    /**
     * 画笔
     */
    private val paint = Paint()

    /**
     * 当前进度
     */
    private var mProgress: Int = 0
    private var isBoolean:Boolean = false

    /**
     * 当前最大progress
     */
    private var maxProgress: Int = 0


    private var callback: ISeekbarCallback? = null


    init {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.VSeekBar)
        isBoolean = typedArray.getBoolean(R.styleable.VSeekBar_seek_bar_boolean,false)
        typedArray.recycle()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            //1. 画出背景圆角矩形
            drawBoundRectForBg(it)
            //2.画出进度圆角矩形
            drawBoundRectForPrg(it)
            //3.绘制一个移动圆球
            drawAcrBitmap(it)

        }
    }

    private fun drawBoundRectForBg(canvas: Canvas) {
        val rx = width.toFloat() / 2
        val bx=(width-mWidth)/2
        val rectf = RectF(bx.toFloat(), 0f, width.toFloat()-bx, height.toFloat())
        paint.color = bgColor
        canvas.drawRoundRect(rectf, rx, rx, paint)
    }

    private fun drawBoundRectForPrg(canvas: Canvas) {
        maxProgress = if (isBoolean){
            255
        }else{
            6
        }
        val bx=(width-mWidth)/2
        paint.color = progressColor
        val rx = width.toFloat() / 2
        val top1 = height * (mProgress.toFloat() / maxProgress)
        val top = height - top1
        val rectf = RectF(bx.toFloat(), top, width.toFloat()-bx, height.toFloat())
        if (top < (height - rx)) {
            canvas.drawRoundRect(rectf, rx, rx, paint)
        }
    }

    private fun drawAcrBitmap(canvas: Canvas) {
        maxProgress = if (isBoolean){
            255
        }else{
            6
        }
        //1.算出圆形坐标
        val r = width / 2  //半径
        val maxY = height - r  //最大可移动距离
        val x = width / 2

        val top1 = height * (mProgress.toFloat() / maxProgress)
        val top = height - top1
        val y = when {
            top <= r -> {
                r
            }
            top > r && top <= (height - 2 * r) -> {
                top + r
            }
            else -> {
                height - r
            }
        }
        paint.color = arcColor
        canvas.drawCircle(x.toFloat(), y.toFloat(), r.toFloat(), paint)

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        maxProgress = if (isBoolean){
            255
        }else{
            6
        }
        val action = event?.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                //记录y坐标
            }
            MotionEvent.ACTION_MOVE -> {
                comProgressForY(event.y)
            }
            MotionEvent.ACTION_UP -> {
                comProgressForY(event.y)
            }
        }

        if (event != null) {
            return event.y < height
        } else {
            return true
        }
    }

    /**
     * 通过移动的y坐标计算出进度值
     * @param y Float
     */
    private fun comProgressForY(y: Float) {
        maxProgress = if (isBoolean){
            255
        }else{
            6
        }
        var movY = when {
            y < 0 -> {
                0f
            }
            y > height -> {
                height.toFloat()
            }
            else -> {
                y
            }
        }
        movY = height - movY
        val moveP = (movY / height) * maxProgress
        setProgress2(moveP.toInt())
    }

    override fun setProgress(int: Int) {
        findProgress(int)
        invalidate()
    }

    private fun setProgress2(int: Int) {
        findProgress(int)
        callback?.callback(int)
    }

    private fun findProgress(int: Int) {
        maxProgress = if (isBoolean){
            255
        }else{
            6
        }
        this.mProgress = when {
            int >= maxProgress -> {
                maxProgress
            }
            int <= 0 -> {
                0
            }
            else -> {
                int
            }
        }
        invalidate()
    }

    override fun setOnProgressCallback(callback: ISeekbarCallback) {
        this.callback = callback
    }
}