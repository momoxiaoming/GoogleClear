package com.mckj.gallery.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/26 18:32
 * @desc
 */
class SpanStringProducer {

    fun getColorSpan(str: String, color: Int): SpannableStringBuilder {
        val spanString = SpannableStringBuilder()
        spanString.append(str)
        val colorSpan = ForegroundColorSpan(color)
        spanString.setSpan(colorSpan, 0, spanString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return spanString
    }

}