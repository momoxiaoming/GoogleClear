package com.mckj.module.cleanup.view

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.module.cleanup.R


/**
 * Created by tjy on 2017/7/25.
 */
class CirclePercentBar : View {

    private var mArcEnd: Float
    private var isShowData: Boolean = false
    private var mArcStart: Float
    private var mArcStartAngle: Float
    private var mArcColor: Int
    private var mArcWidth: Int
    private var mCenterTextColor: Int
    private var mCenterTextSize: Int
    private var mCircleRadius: Int
    private var arcPaint: Paint? = null
    private var arcCirclePaint: Paint? = null
    private var centerTextPaint: Paint? = null
    private var arcRectF: RectF? = null
    private var textBoundRect: Rect? = null
    private var mCurData = 0f
    private var mSuffix = "%"
    private var arcStartColor: Int
    private var arcEndColor: Int
    private var startCirclePaint: Paint? = null

    constructor(context: Context) : this(context,null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    )

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        val mContext = context
        val typedArray =
            mContext.obtainStyledAttributes(attrs, R.styleable.CirclePercentBar, defStyleAttr, 0)
        mArcColor = typedArray.getColor(R.styleable.CirclePercentBar_arcColor, 0xff0000)
        mArcWidth = typedArray.getDimensionPixelSize(
            R.styleable.CirclePercentBar_arcWidth, SizeUtil.dp2px(
                20f
            )
        )
        mCenterTextColor =
            typedArray.getColor(R.styleable.CirclePercentBar_centerTextColor, 0x0000ff)
        mCenterTextSize = typedArray.getDimensionPixelSize(
            R.styleable.CirclePercentBar_centerTextSize, SizeUtil.dp2px(
                20f
            )
        )
        mCircleRadius = typedArray.getDimensionPixelSize(
            R.styleable.CirclePercentBar_circleRadius, SizeUtil.dp2px(
                100f
            )
        )
        arcStartColor = typedArray.getColor(
            R.styleable.CirclePercentBar_arcStartColor,
            Color.parseColor("#3198F4")
        )
        arcEndColor = typedArray.getColor(
            R.styleable.CirclePercentBar_arcEndColor,
            Color.parseColor("#3198F4")
        )
        mArcStartAngle = typedArray.getFloat(
            R.styleable.CirclePercentBar_arcStartAngle, -90f
        )
        mArcStart = typedArray.getFloat(
            R.styleable.CirclePercentBar_arcStart, 0f
        )
        mArcEnd = typedArray.getFloat(
            R.styleable.CirclePercentBar_arcEnd, 360f
        )
        isShowData = typedArray.getBoolean(
            R.styleable.CirclePercentBar_isShowData, false
        )

        typedArray.recycle()

        initPaint()
    }

    private fun initPaint() {
        startCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        startCirclePaint!!.style = Paint.Style.FILL
        //startCirclePaint.setStrokeWidth(mArcWidth);
        startCirclePaint!!.color = arcStartColor
        arcCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        arcCirclePaint!!.style = Paint.Style.STROKE
        arcCirclePaint!!.strokeWidth = mArcWidth.toFloat()
        arcCirclePaint!!.color = mArcColor
        arcCirclePaint!!.strokeCap = Paint.Cap.ROUND
        arcPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        arcPaint!!.style = Paint.Style.STROKE
        arcPaint!!.strokeWidth = mArcWidth.toFloat()
        arcPaint!!.color = mArcColor
        arcPaint!!.strokeCap = Paint.Cap.ROUND
        centerTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        centerTextPaint!!.style = Paint.Style.FILL
        centerTextPaint!!.color = mCenterTextColor
        centerTextPaint!!.textSize = mCenterTextSize.toFloat()

        //圓弧的外接矩形
        arcRectF = RectF()

        //文字的边界矩形
        textBoundRect = Rect()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            measureDimension(widthMeasureSpec),
            measureDimension(heightMeasureSpec)
        )
    }

    private fun measureDimension(measureSpec: Int): Int {
        var result: Int
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = mCircleRadius * 2
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        canvas.rotate(mArcStartAngle, (width / 2).toFloat(), (height / 2).toFloat())
        arcRectF!![(width / 2 - mCircleRadius + mArcWidth / 2).toFloat(), (height / 2 - mCircleRadius + mArcWidth / 2
                ).toFloat(), (width / 2 + mCircleRadius - mArcWidth / 2).toFloat()] =
            (height / 2 + mCircleRadius - mArcWidth / 2).toFloat()
        canvas.drawArc(arcRectF!!, mArcStart, mArcEnd, false, arcCirclePaint!!)

        arcCirclePaint!!.color = mArcColor
        startCirclePaint!!.color = arcStartColor
        arcPaint?.color = arcStartColor

        if(mCurData == 0f){
            startCirclePaint!!.color = ResourceUtil.getColor(R.color.trans)
        }else{
            startCirclePaint!!.color = arcStartColor
        }

        canvas.drawArc(arcRectF!!, 0f, 360 * mCurData / 100, false, arcPaint!!)
        canvas.rotate(90f, (width / 2).toFloat(), (height / 2).toFloat())
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2 - mCircleRadius + mArcWidth / 2).toFloat(),
            (mArcWidth / 2).toFloat(),
            startCirclePaint!!
        )
        var data = mCurData.toString() + mSuffix
        centerTextPaint!!.getTextBounds(data, 0, data.length, textBoundRect)
        if(!isShowData){
            data=""
        }
        canvas.drawText(
            data,
            (width / 2 - textBoundRect!!.width() / 2).toFloat(),
            (height *0.4 + textBoundRect!!.height() / 2).toFloat(),
            centerTextPaint!!
        )
    }

    fun setPercentData(data: Float, suffix: String, interpolator: TimeInterpolator?) {
        val valueAnimator = ValueAnimator.ofFloat(mCurData, data)
        valueAnimator.duration = (Math.abs(mCurData - data) * 30).toLong()
        valueAnimator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Float
            mCurData = Math.round(value * 10).toFloat() / 10
            mSuffix = suffix
            //数值改变，重设颜色
            initColor(mCurData)
            if(data == 0f){
                startCirclePaint!!.color = ResourceUtil.getColor(R.color.trans)
                arcPaint!!.color = ResourceUtil.getColor(R.color.trans)
            }else{
                startCirclePaint!!.color = arcStartColor
                arcPaint!!.color = arcStartColor
            }

            invalidate()
        }
        valueAnimator.interpolator = interpolator
        valueAnimator.start()
    }

    private fun initColor(data: Float) {
//        if(data >= 50*1.0){
//            mArcColor = Color.parseColor("#FFD7D7")
//            arcStartColor = Color.parseColor("#E73938")
//            arcEndColor = Color.parseColor("#E73938")
//        }else{
//            mArcColor = Color.parseColor("#DAEBFF")
//            arcStartColor = Color.parseColor("#3198F4")
//            arcEndColor = Color.parseColor("#3198F4")
//        }
    }

}