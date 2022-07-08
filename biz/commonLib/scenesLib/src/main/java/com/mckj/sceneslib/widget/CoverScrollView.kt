package com.mckj.sceneslib.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView


/**
 * 覆盖模式的ScrollView,支持默认和覆盖模式
 *
 * @author mmxm
 * @date 2021/4/22 16:45
 */


class CoverScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    ids: Int = 0
) : NestedScrollView(context, attrs, ids) {

    companion object {
        var TAG: String = this::class.java.name

        private fun log(msg: String) {
            Log.d(TAG, msg)
        }
    }

    /**
     * 当前状态
     */
    private var state = ScrollMode.DEFAULT

    /**
     * 相关事件监听
     */
    private var event: ScrollEventListener? = null

    /**
     * 首布局高度
     */
    private var topHeight = 0

    /**
     * 是否开启覆盖模式,默认为
     */
    private var enableCover = false

    /**
     * 自动覆盖的上拉高度比例， 0-1
     */
    private val coverLimitHeight = 200f

    /**
     * 当前滚动的距离
     */
    private var currentScrollY = 0


    override fun onFinishInflate() {
        super.onFinishInflate()
    }

    fun setOnScrollEventListener(event: ScrollEventListener) {
        this.event = event
    }

    /**
     * true开启覆盖模式,false关闭覆盖模式
     */
    fun enableCover(bl: Boolean) {
        this.enableCover = bl
    }

    /**
     * 更改当前布局状态状态
     */
    fun changState() {
        if (!enableCover) {
            log("覆盖模式已关闭,无法切换状态")
            return
        }
        if (state == ScrollMode.COVER) {
            scrollTop()
        } else {
            scrollBottom()
        }
    }

    fun isTop(): Boolean {
        return state == ScrollMode.DEFAULT
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val v = getChildAt(0) as ViewGroup
        topHeight = (v.getChildAt(0).measuredHeight)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        log("onScrollChanged--x->$l---y--$t")
        event?.scrollXy(l, t)
        currentScrollY = t
        if (state == ScrollMode.DEFAULT && enableCover) {
            if (currentScrollY < topHeight - coverLimitHeight) {
                return
            }
            scrollBottom()
        }
    }

    /**
     * onNestedScroll方法会接管子view已消耗或者为消耗的滚动距离,通过此方法当前view可做响应滚动处理
     */
    override fun onNestedScroll(
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int,
        consumed: IntArray
    ) {
        log("state->$state----vew--->$target---onNestedScroll-->dy-$dyConsumed--dyUnconsumed-->$dyUnconsumed---type--$type")
        if (state == ScrollMode.DEFAULT) {
            consumed[1] = dyUnconsumed
            scrollBy(dxUnconsumed, dyUnconsumed)
        }
    }

    private fun scrollTop() {
        fling(0)
        fullScroll(FOCUS_UP)
        smoothScrollTo(0, 0);
        state = ScrollMode.DEFAULT
        event?.event(state)
    }

    private fun scrollBottom() {
        smoothScrollTo(0, topHeight);
        this.state = ScrollMode.COVER
        event?.event(state)
    }


    /**
     * 判断是否是默认状态
     */
    fun isDefaultState(): Boolean {
        //优先判断坐标
        return if (currentScrollY < (topHeight - coverLimitHeight)) {
            this.state=ScrollMode.DEFAULT
            true
        }else{
            this.state==ScrollMode.DEFAULT
        }

    }

    /**
     * 子view 调用此方法时,会先调用父view的该方法,如果父view返回true表示父view愿意接受并接管后续的滚动事件
     * 否者子view将全权接管滑动操作
     * @param child View
     * @param target View
     * @param nestedScrollAxes Int
     * @return Boolean
     */
    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        if (nestedScrollAxes == ViewCompat.SCROLL_AXIS_HORIZONTAL) {
            return false
        }
        if (!isDefaultState()) {
            return false
        }
        log("onStartNestedScroll--${super.onStartNestedScroll(child, target, nestedScrollAxes)}")
        return true
    }

    /**
     * onInterceptTouchEvent事件传递方向为 父布局-->子布局,true时表示事件被当前布局拦截
     * @param ev MotionEvent
     * @return Boolean
     */
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        log("onInterceptTouchEvent-->${ev?.y}---$topHeight---${scrollY}")
        val temp = topHeight - scrollY
        val y = ev!!.y

        return if (temp > 0 && y < temp) {
            //说明点击的是上方layout
            if (isDefaultState()) { //rang
                return super.onInterceptTouchEvent(ev)
            } else {
                true
            }
        } else {
            //点击的是下方layout
            state == ScrollMode.DEFAULT
        }

//        return state == ScrollMode.DEFAULT
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {

        return !(!isDefaultState() || !super.onTouchEvent(ev))
    }
}

interface ScrollEventListener {
    /**
     * true 表示底部模式
     * @param boolean Boolean
     */
    fun event(state: ScrollMode)

    fun scrollXy(t: Int, oldt: Int)
}

enum class ScrollMode {
    DEFAULT,
    COVER,
}