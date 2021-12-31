package com.dn.vi.app.cm.utils

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan

/**
 * Describe:
 *
 * Created By yangb on 2020/9/27
 */
object TextUtils {

    fun string2SpannableStringForSize(content: String, vararg keys: String?, sizeDip: Int) =
        string2SpannableStringForSize(content, keys = *keys, sizeDip = sizeDip, isBold = false)

    fun string2SpannableStringForSize(
        content: String, vararg keys: String?, sizeDip: Int, isBold: Boolean
    ): SpannableString {
        val result = SpannableString(content)
        if (sizeDip <= 0) {
            return result
        }
        for (key in keys) {
            if (key == null) {
                continue
            }
            val startIndex = content.indexOf(key)
            if (startIndex < 0) {
                return result
            }
            warpSpannableStringSize(result, sizeDip, startIndex, startIndex + key.length)
            warpSpannableStringBoldStyle(result, isBold, startIndex, startIndex + key.length)
        }
        return result
    }

    fun string2SpannableStringForColor(
        content: String, vararg keys: String?, color: Int
    ): SpannableString {
        return string2SpannableStringForColor(
            content = content, keys = *keys, color = color, isBold = false
        )
    }

    fun string2SpannableStringForColor(
        content: String, vararg keys: String?, color: Int, isBold: Boolean
    ): SpannableString {
        val result = SpannableString(content)
        if (color == 0) {
            return result
        }
        for (key in keys) {
            if (key == null) {
                continue
            }
            val startIndex = content.indexOf(key)
            if (startIndex < 0) {
                return result
            }
            warpSpannableStringColor(result, color, startIndex, startIndex + key.length)
            warpSpannableStringBoldStyle(result, isBold, startIndex, startIndex + key.length)
        }
        return result
    }

    fun string2SpannableString(
        content: String, key: String?, sizeDip: Int, color: Int, isBold: Boolean
    ): SpannableString {
        val result = SpannableString(content)
        if (key == null || color == 0) {
            return result
        }
        val startIndex = content.indexOf(key)
        if (startIndex < 0) {
            return result
        }
        warpSpannableStringSize(result, sizeDip, startIndex, startIndex + key.length)
        warpSpannableStringColor(result, color, startIndex, startIndex + key.length)
        warpSpannableStringBoldStyle(result, isBold, startIndex, startIndex + key.length)
        return result
    }

    fun string2SpannableString(
        content: String, key: String?, sizeDip: Int, background: Int
    ): SpannableString {
        val result = SpannableString(content)
        if (key == null || background == 0) {
            return result
        }
        val startIndex = content.indexOf(key)
        if (startIndex < 0) {
            return result
        }
        warpSpannableStringSize(result, sizeDip, startIndex, startIndex + key.length)
        warpSpannableStringBackground(result, background, startIndex, startIndex + key.length)
        return result
    }

    /**
     * 组装字体大小
     */
    private fun warpSpannableStringSize(
        content: SpannableString, sizeDip: Int, start: Int, end: Int
    ) {
        content.setSpan(
            AbsoluteSizeSpan(sizeDip, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    /**
     * 组装字体颜色
     */
    private fun warpSpannableStringColor(
        content: SpannableString, color: Int, start: Int, end: Int
    ) {
        content.setSpan(
            ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    /**
     * 组装字体样式
     */
    private fun warpSpannableStringBoldStyle(
        content: SpannableString, isBold: Boolean, start: Int, end: Int
    ) {
        if (isBold) {
            content.setSpan(
                StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    /**
     * 组装字体背景
     */
    private fun warpSpannableStringBackground(
        content: SpannableString, background: Int, start: Int, end: Int
    ) {
        content.setSpan(
            BackgroundColorSpan(background), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

}
