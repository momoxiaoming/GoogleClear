package com.dn.openlib.ui.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.WindowInsets
import com.dn.vi.app.cm.log.debug

/**
 * 自己填充
 * 并自己记住 第一次生效的window inset
 *
 * 一般要放在最顶层 layout的第一个 fitsSystemWindow , 避免被其它的fits view把inset吃了
 *
 * Created by holmes on 2020/7/20.
 **/
class AutoInsetView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object{
        const val TAG = "AutoInsetView"
    }

    private val cachedInset = Rect()

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        if (cachedInset.top > 0) {
            return insets
        }
        cachedInset.left = insets.systemWindowInsetLeft
        cachedInset.top = insets.systemWindowInsetTop
        cachedInset.right = insets.systemWindowInsetRight
        cachedInset.bottom = insets.systemWindowInsetBottom
        debug { "auto inset ${cachedInset} [#${hashCode()}], consumed by other: ${insets.isConsumed}" }
        if (cachedInset.top != measuredHeight) {
            requestLayout()
        }
        return insets //super.onApplyWindowInsets(insets)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measuredWidth = widthMeasureSpec.onMeasureSpace { mode, size ->
            size
        }
        val measuredHeight = widthMeasureSpec.onMeasureSpace { mode, size ->
            cachedInset.top
        }
        // Log.i(TAG, "onMeasure measuredWidth:$measuredWidth measuredHeight:$measuredHeight")
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    private inline fun Int.onMeasureSpace(block: (mode: Int, size: Int) -> Int): Int {
        return block(
            MeasureSpec.getMode(this),
            MeasureSpec.getSize(this)
        )
    }

}