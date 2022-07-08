package com.mc.cpyr.guide.config

import android.R
import android.view.View

/**
 * 蒙版配置项
 *
 * @author mmxm
 * @date 2021/3/8 16:57
 */
 class Configuration {

   var mTargetView:View?=null

    /**
     * 高亮区域的padding
     */
    var mPadding = 0

    /**
     * 高亮区域的左侧padding
     */
    var mPaddingLeft = 0

    /**
     * 高亮区域的顶部padding
     */
    var mPaddingTop = 0

    /**
     * 高亮区域的右侧padding
     */
    var mPaddingRight = 0

    /**
     * 高亮区域的底部padding
     */
    var mPaddingBottom = 0

    /**
     * 是否可以透过蒙层点击，默认不可以
     */
    var mOutsideTouchable = false

    /**
     * 遮罩透明度
     */
    var mAlpha = 255

    /**
     * 遮罩覆盖区域控件Id
     *
     *
     * 该控件的大小既该导航页面的大小
     */
    var mFullingViewId = -1

    /**
     * 目标控件Id
     */
    var mTargetViewId = -1

    /**
     * 高亮区域的圆角大小
     */
    var mCorner = 0

    /**
     * 高亮区域的图形样式，默认为矩形
     */
    var mGraphStyle = Component.ROUNDRECT

    /**
     * 遮罩背景颜色id
     */
    var mFullingColorId = R.color.black

    /**
     * 是否在点击的时候自动退出导航
     */
    var mAutoDismiss = true

    /**
     * 是否覆盖目标控件
     */
    var mOverlayTarget = false

    var mShowCloseButton = false


    var mEnterAnimationId = -1

    var mExitAnimationId = -1
}