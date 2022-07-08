package com.mckj.sceneslib.util

import android.content.res.Resources
import kotlin.math.roundToInt

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.util
 * @data  2022/4/19 12:17
 */

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
val Float.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()
val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()