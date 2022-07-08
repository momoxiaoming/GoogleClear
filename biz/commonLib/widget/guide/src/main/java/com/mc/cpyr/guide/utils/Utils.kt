package com.mc.cpyr.guide.utils

import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.mc.cpyr.guide.config.Component
import com.mc.cpyr.guide.view.MaskView


/**
 * Utils
 *
 * @author mmxm
 * @date 2021/3/8 17:47
 */
object Utils {

    /**
     * 设置Component
     */
    fun componentToView(inflater: LayoutInflater, c: Component?): View? {
        if(c==null)return null
        val view = c.getView(inflater)
        val lp = MaskView.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.offsetX = c.getXOffset()
        lp.offsetY = c.getYOffset()
        lp.targetAnchor = c.getAnchor()
        lp.targetParentPosition = c.getFitPosition()
        view.layoutParams = lp
        return view
    }

    /**
     * Rect在屏幕上去掉状态栏高度的绝对位置
     */
    fun getViewAbsRect(view: View, parentX: Int, parentY: Int): Rect {
        val loc = IntArray(2)
        view.getLocationInWindow(loc)

        //在ViewPager上，获取控件相对位置时，有时会超过屏幕位置，
        //在ViewPager上，获取控件相对位置时，有时会超过屏幕位置，
        val screenWidth: Int = getScreenWidth(view.context)
        val screenHeight: Int = getScreenHeight(view.context)
        var x = loc[0] % screenWidth
        var y = loc[1] % screenHeight
        if (x < 0) {
            x += screenWidth
        }
        if (y < 0) {
            y += screenHeight
        }

        val rect = Rect()
        rect[x, y, x + view.measuredWidth] = y + view.measuredHeight
        rect.offset(-parentX, -parentY)
        return rect
    }

    /**
     * 对于部分悬浮窗视图,定位rect用此方法
     * @param view  悬浮窗
     * @param x   悬浮窗在窗口的x坐标
     * @param y 悬浮窗在窗口的y坐标
     * @return
     */
    fun getViewAbsWindowRect(view: View, x: Int, y: Int): Rect {
        val pl = IntArray(2)
        view.getLocationOnScreen(pl)
        val mRect = Rect()
        view.getDrawingRect(mRect)
        val rect = Rect()
        rect[pl[0], y, x + mRect.right] = y + mRect.bottom
        return rect
    }


    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    private fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(outMetrics)
        return outMetrics.widthPixels
    }

    /**
     * 获得屏幕高度
     */
    private fun getScreenHeight(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getRealMetrics(outMetrics)
        return outMetrics.heightPixels
    }
}