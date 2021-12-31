package com.mckj.module.wifi.widget

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.mckj.baselib.util.SizeUtil
import kotlin.math.abs

/**
 * Describe:上拉覆盖控件
 *
 * Created By yangb on 2021/1/4
 */
class PullCoverLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

    companion object {
        const val TAG = "PullCoverLayout"

        /**
         * 最小滑动距离
         */
        private val SCROLL_MIN_DISTANCE = SizeUtil.dp2px(60f)
    }

    enum class Mode {
        /**
         * 默认模式，显示第一view和第二个view
         */
        NORMAL,

        /**
         * 滑动
         */
        SCROLL,

        /**
         * 覆盖
         */
        COVER,
    }

    interface OnPullCoverListener {

        fun modeCallback(mode: Mode)

        fun onScroll(x: Int, y: Int)

    }

    /**
     * 第一个view
     */
    private var mFirstView: View? = null

    /**
     * 第二个view
     */
    private var mSecondView: View? = null

    //宽度
    private var mWidth: Int = 0

    //高度
    private var mHeight: Int = 0

    /**
     * 第一个view的高度
     */
    private var mFirstViewHeight: Int = 0

    private var mLastY: Int = 0

    private var mScrollY: Int = 0
        set(value) {
            field = value
            mPullCoverListener?.onScroll(0, value)
        }

    /**
     * 滑动模式
     */
    private var mMode: Mode = Mode.NORMAL
        set(value) {
            field = value
            mPullCoverListener?.modeCallback(value)
        }

    //结束动画对象
    private var mFinishAnimator: ValueAnimator? = null

    //滑动动画
    private var mScrollAnimator: ValueAnimator? = null

    //滑动手势
    private var mGestureDetector: GestureDetector

    private var mPullCoverListener: OnPullCoverListener? = null

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    init {
        setWillNotDraw(false)
        mGestureDetector = GestureDetector(context, this)
        mGestureDetector.setIsLongpressEnabled(false)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        Log.i(TAG, "onFinishInflate: count:${childCount}")

        val count = childCount
        if (count > 0) {
            mFirstView = getChildAt(0)
        }
        if (count > 1) {
            mSecondView = getChildAt(1)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mFirstViewHeight = mFirstView?.measuredHeight ?: 0
//        Log.i(TAG, "onMeasure: mFirstViewHeight:$mFirstViewHeight")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mWidth = right - left
        mHeight = bottom - top
        val childLeft = paddingLeft
        val childTop = paddingTop
        preLayout()
        showFirstView(childLeft, childTop)
        showSecondView(childLeft, childTop)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Log.i(TAG, "onDown mMode:$mMode")
        if (mMode == Mode.COVER) {
            return false
        }
        mMode = Mode.NORMAL
        mFinishAnimator?.cancel()
        mFinishAnimator = null

        mScrollAnimator?.cancel()
        mScrollAnimator = null
        return true
    }

    override fun onShowPress(e: MotionEvent?) {
        //用户按下按键后100ms后回调，移动太早不回调
        Log.i(TAG, "onShowPress: ")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        //用户手指松开（UP事件）的时候如果没有执行onScroll()和onLongPress()这两个回调的话
        Log.i(TAG, "onSingleTapUp: ")
        return false
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        Log.i(TAG, "onScroll: distanceX:$distanceX distanceY:$distanceY mMode:$mMode")
        if (abs(distanceX) > abs(distanceY)) {
            return false
        }
        if (mMode == Mode.SCROLL) {
            mScrollY -= distanceY.toInt()
            requestLayout()
            return true
        }
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        Log.i(
            TAG,
            "onFling: velocityX:$velocityX velocityY:$velocityY mFinishAnimator:$mFinishAnimator"
        )
        if (mFinishAnimator == null && abs(velocityY) > 500 && abs(velocityY) > abs(velocityX)) {
            startScrollAnimation((velocityY / 20f).toInt())
            return true
        }
        return false
    }

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        if (ev == null) {
//            return super.dispatchTouchEvent(ev)
//        }
//        when (ev.action) {
//            MotionEvent.ACTION_DOWN -> {
//            }
//            MotionEvent.ACTION_MOVE -> {
//            }
//            MotionEvent.ACTION_CANCEL,
//            MotionEvent.ACTION_UP,
//            -> {
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (ev == null) {
            return super.onInterceptTouchEvent(ev)
        }
        mGestureDetector.onTouchEvent(ev)
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "onInterceptTouchEvent: down")
                mLastY = ev.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, "onInterceptTouchEvent: move mMode:$mMode")
                val y = ev.y.toInt()
                if (mMode == Mode.NORMAL && abs(y - mLastY) > 15) {
                    mMode = Mode.SCROLL
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP,
            -> {
                Log.i(TAG, "onInterceptTouchEvent: up")
            }
        }
        return mMode == Mode.SCROLL || super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) {
            return super.onTouchEvent(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "onTouchEvent: down")
            }
            MotionEvent.ACTION_MOVE -> {
                Log.i(TAG, "onTouchEvent: move")
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP,
            -> {
                startFinishAnimation()
                Log.i(TAG, "onTouchEvent: up")
            }
        }
        return mGestureDetector.onTouchEvent(event)
    }

    override fun onGenericMotionEvent(event: MotionEvent?): Boolean {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            return mGestureDetector.onGenericMotionEvent(event)
        }
        return super.onGenericMotionEvent(event)
    }

    /**
     * 布局预备
     */
    private fun preLayout() {
        /**
         * 第一个控件不允许下拉
         */
        if (mScrollY > 0) {
            mScrollY = 0
        }
        /**
         * 限制第二个控件上拉范围
         */
        if (mScrollY < -mFirstViewHeight) {
            mScrollY = -mFirstViewHeight
        }
    }

    private fun showFirstView(left: Int, top: Int) {
        Log.i(TAG, "showFirstView: mScrollY:$mScrollY")
        mFirstView?.let {
            val childTop = top + mScrollY
            it.layout(
                left,
                childTop,
                left + it.measuredWidth,
                childTop + it.measuredHeight
            )
        }
    }

    private fun showSecondView(left: Int, top: Int) {
        mSecondView?.let {
            val childTop = top + mFirstViewHeight + mScrollY
            it.layout(
                left,
                childTop,
                left + it.measuredWidth,
                childTop + it.measuredHeight
            )
        }
    }

    /**
     * 滑动完成动画
     *
     */
    private fun startFinishAnimation() {
        if (!isExeAnimator()) {
            return
        }
        startFinishAnimation(isCoverScroll())
    }

    /**
     * 滑动完成动画
     *
     */
    private fun startFinishAnimation(isCover: Boolean) {
        Log.i(TAG, "startFinishAnimation: isCover:$isCover")
        val mode: Mode
        val endScrollY = if (isCover) {
            mode = Mode.COVER
            -mFirstViewHeight
        } else {
            mode = Mode.NORMAL
            if (mFirstViewHeight > mHeight) {
                mHeight - mFirstViewHeight
            } else {
                0
            }
        }
        Log.i(TAG, "startFinishAnimation: mScrollY:$mScrollY endScrollY:$endScrollY")
        mFinishAnimator?.cancel()
        mFinishAnimator = ValueAnimator.ofInt(mScrollY, endScrollY).also {
            it.duration = 500 //动画时间
            it.repeatCount = 0 //重复次数
            it.interpolator = AccelerateInterpolator()
            it.addUpdateListener(AnimatorUpdateListener { animation ->
                mScrollY = animation.animatedValue as Int
                Log.i(TAG, "startFinishAnimation: mScrollY:$mScrollY")
                //动画结束
                if (mScrollY == endScrollY) {
                    mMode = mode
                }
                requestLayout()
            })
            it.start()
        }
    }

    /**
     * 滑动动画, 只限第一个控件高度大于父布局高度时执行
     *
     */
    private fun startScrollAnimation(scrollY: Int) {
        Log.i(TAG, "startScrollAnimation: mScrollY:$mScrollY scrollY:$scrollY")
        if (mHeight > mFirstViewHeight) {
            return
        }
        var endScrollY = mScrollY + scrollY
        if (endScrollY > 0) {
            endScrollY = 0
        } else if (endScrollY < -(mFirstViewHeight - mHeight)) {
            endScrollY = -(mFirstViewHeight - mHeight)
        }
        if (mScrollY == endScrollY) {
            return
        }
        Log.i(TAG, "startScrollAnimation: mScrollY:$mScrollY scrollY:$scrollY")
        mScrollAnimator?.cancel()
        mScrollAnimator = ValueAnimator.ofInt(mScrollY, endScrollY).also {
            it.duration = 200 //动画时间
            it.repeatCount = 0 //重复次数
            it.interpolator = DecelerateInterpolator()
            it.addUpdateListener(AnimatorUpdateListener { animation ->
                mScrollY = animation.animatedValue as Int
                //动画结束
                requestLayout()
            })
            it.start()
        }
    }

    /**
     * 是否执行动画
     */
    private fun isExeAnimator(): Boolean {
        return mHeight + abs(mScrollY) > mFirstViewHeight
    }

    /**
     * 是否覆盖滑动
     */
    private fun isCoverScroll(): Boolean {
        return if (mFirstViewHeight > mHeight) {
            abs(mScrollY) + mHeight - mFirstViewHeight > SCROLL_MIN_DISTANCE
        } else {
            abs(mScrollY) > SCROLL_MIN_DISTANCE
        }
    }

    /**
     * 设置显示模式
     */
    fun setMode(mode: Mode) {
        if (mMode == mode) {
            return
        }
        mMode = mode
        startFinishAnimation(mode == Mode.COVER)
    }

    fun setOnPullCoverListener(pullCoverListener: OnPullCoverListener) {
        mPullCoverListener = pullCoverListener
    }

}