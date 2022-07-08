package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.graphics.drawable.Drawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.*

class SpanStringHelper {

    private var content = SpannableString("")

    fun build() = content

    /**
     * 设置富文本内容
     * @param content String
     * @return SpanStringHelper
     */
    fun setContent(text: String): SpanStringHelper {
        if (content.isEmpty()) {
            content = SpannableString(text)
        }
        return this
    }

    /**
     * 设置前景颜色
     * @param color Int
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setForeColorSpan(color:Int, vararg key:String):SpanStringHelper {
        setType({ start, end ->
            content.setSpan(
                ForegroundColorSpan(color),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }, *key)
        return this
    }

    /**
     * 设置背景色
     * @param color Int
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setBackColorSpan(color:Int,vararg key:String):SpanStringHelper {
        setType({ start, end ->
            content.setSpan(
                BackgroundColorSpan(color),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }, *key)
        return this
    }

    /**
     * 设置中划线
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setStrikethroughSpan(vararg key:String):SpanStringHelper {
        setType({ start, end ->
            content.setSpan(
                StrikethroughSpan(),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }, *key)
        return this
    }

    /**
     * 设置下划线
     * @param key Array<out String>
     */
    fun setUnderlineSpan(vararg key:String):SpanStringHelper{
        setType({ start, end ->
            content.setSpan(
                UnderlineSpan(),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }, *key)
        return this
    }

    /**
     * 设置字体上标
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setSuperscriptSpan(vararg key:String):SpanStringHelper{
        setType({ start, end ->
            content.setSpan(
                SuperscriptSpan(),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }, *key)
        return this
    }

    /**
     * 设置字体下标
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setSubscriptSpan(vararg key:String):SpanStringHelper{
        setType({ start, end ->
            content.setSpan(
                SubscriptSpan(),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }, *key)
        return this
    }

    /**
     * 设置图片
     * @param drawable Drawable
     * @param align Int
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setImageSpan(drawable:Drawable,vararg key:String,align:Int=ImageSpan.ALIGN_BASELINE):SpanStringHelper{
        setType({ start, end ->
            drawable.setBounds(0,0,drawable.intrinsicWidth,drawable.intrinsicHeight)
            content.setSpan(
                ImageSpan(drawable,align),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }, *key)
        return this
    }


    /**
     *  * 注意
     * TextView.setMovementMethod 设置后点击才能生效
     * TextView.setHighlightColor 去掉点击的背景色
     * 设置文本点击事件
     * @param clickSpan ClickableSpan
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setClickableSpan(clickSpan: ClickableSpan,vararg key:String):SpanStringHelper{
        setType({ start, end ->
            content.setSpan(
                clickSpan,
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }, *key)
        return this
    }


    /**
     * 设置字体样式
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setTextStyleSpan(typeStyle:Int,vararg key:String):SpanStringHelper{
        setType({ start, end ->
            content.setSpan(StyleSpan(typeStyle),start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        },*key)
        return this
    }

    /**
     * 设置字体大小
     * @param size Int
     * @param key Array<out String>
     * @return SpanStringHelper
     */
    fun setTextSizeSpan(size:Int,vararg key:String):SpanStringHelper{
        setType({ start, end ->
            content.setSpan(AbsoluteSizeSpan(size,true),start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        },*key)
        return this
    }

    private fun setType(block:(start:Int, end:Int)->Unit, vararg key:String){
        for (str in key) {
            if (str.isEmpty()) {
                continue
            }
            val startIndex = content.indexOf(str)

            if (startIndex < 0) {
                continue
            }
            block(startIndex,startIndex+str.length)
        }
    }
}