package com.dn.vi.app.cm.utils

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.dn.vi.app.cm.R

/**
 * Describe:动态获取资源文件
 *
 * Created By yangb on 2021/8/21
 */
object DAttrUtil {

    @JvmStatic
    fun getPrimaryColorId(context: Context): Int {
        val color = getColorId(context, R.attr.colorPrimary)
        return if(color != 0){
            color
        }else{
            R.color.color_16B334
        }
    }

    @JvmStatic
    fun getPrimaryColor(context: Context): Int {
        val color = getColorId(context, R.attr.colorPrimary)
        return if(color != 0){
            ContextCompat.getColor(context, getColorId(context, R.attr.colorPrimary))
        }else{
            Color.parseColor("#16B334")
        }
    }

    @JvmStatic
    fun getPrimaryDarkColor(context: Context): Int {
        val color = getColorId(context, R.attr.colorPrimaryDark)
        return if(color != 0){
            ContextCompat.getColor(context, getColorId(context, R.attr.colorPrimary))
        }else{
            Color.parseColor("#00b248")

        }
    }

    @JvmStatic
    fun getPrimaryLightColor(context: Context): Int {
        val color = getColorId(context, R.attr.colorAccent)
        return if(color != 0){
            ContextCompat.getColor(context, getColorId(context, R.attr.colorPrimary))
        }else{
            Color.parseColor("#9cff57")
        }
    }

    @JvmStatic
    fun getColorId(context: Context, attrResId: Int): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(attrResId, typedValue, true)
        return typedValue.resourceId
    }


}