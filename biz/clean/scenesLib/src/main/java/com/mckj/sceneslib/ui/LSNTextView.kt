package com.mckj.sceneslib.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import org.jetbrains.anko.textColor

/**
 * LSNTextView
 *
 * @author mmxm
 * @date 2021/7/29 21:34
 */
class LSNTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr)  {


    init {
        textColor=Color.parseColor("#60ffffff")
    }

}