package com.dn.baselib.util

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayInputStream

/**
 * DrawableUtils
 *
 * @author mmxm
 * @date 2021/6/18 15:19
 */
object DrawableUtils {
    /**
     * byte 转drawable
     * @param context Context
     * @param var2 ByteArray
     * @return Drawable?
     */
    fun byteToDrawable(context: Context, byteArray: ByteArray): Drawable? {
        var inputStream: ByteArrayInputStream? = null
        var bitmapDrawable: BitmapDrawable? = null
        try {
            inputStream = ByteArrayInputStream(byteArray)
            val options = BitmapFactory.Options()
            options.inDensity = 480
            options.inTargetDensity = context.resources.displayMetrics.densityDpi
            val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
            bitmapDrawable = BitmapDrawable(context.resources, bitmap)
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close()
                } catch (var15: Exception) {
                    var15.printStackTrace()
                }
            }
        }
        return bitmapDrawable
    }
}