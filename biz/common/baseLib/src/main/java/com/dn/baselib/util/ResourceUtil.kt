package com.dn.baselib.util

import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import com.dn.baselib.ext.getGlobalResource

/**
 * Describe:资源获取类
 *
 * Created By yangb on 2020/10/10
 */
object ResourceUtil {

    fun getText(@StringRes resId: Int) = getGlobalResource().getText(resId)

    fun getColor(@ColorRes resId: Int) = ResourcesCompat.getColor(getGlobalResource(), resId, null)

    fun getDimen(@DimenRes resId: Int) = getGlobalResource().getDimension(resId)

    fun getDrawable(@DrawableRes resId: Int) = ResourcesCompat.getDrawable(getGlobalResource(), resId, null)

}


