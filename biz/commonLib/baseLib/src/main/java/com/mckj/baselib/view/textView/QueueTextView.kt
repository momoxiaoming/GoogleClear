package com.mckj.baselib.view.textView

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.mckj.baselib.R
import org.jetbrains.anko.textColor

/**
 * 一个存放多个textview的组件,最多可存放3个textView
 *
 * @author mmxm
 * @date 2021/6/23 10:49
 */
@SuppressLint("Recycle")
class QueueTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleRes) {

    private var firstTextView: TextView
    private var secondTextView: TextView
    private var endTextView: TextView

    init {
        val typedArray: TypedArray =context.obtainStyledAttributes(attrs, R.styleable.QueueTextView) //
        val firstTextColor = typedArray.getColor(R.styleable.QueueTextView_first_textColor,resources.getColor(R.color.black) )
        val firstText = typedArray.getString(R.styleable.QueueTextView_first_text)
        val firstTextSize = typedArray.getDimension(R.styleable.QueueTextView_first_textSize, 16f)
        val firstTextStyle = typedArray.getInt(R.styleable.QueueTextView_first_textStyle, 0)

        val secondTextColor = typedArray.getColor(R.styleable.QueueTextView_second_textColor,resources.getColor(R.color.black))
        val secondText = typedArray.getString(R.styleable.QueueTextView_second_text)
        val secondTextSize =typedArray.getDimension(R.styleable.QueueTextView_second_textSize, 16f)
        val secondTextStyle = typedArray.getInt(R.styleable.QueueTextView_second_textStyle, 0)

        val endTextColor = typedArray.getColor( R.styleable.QueueTextView_end_textColor,resources.getColor(R.color.black))
        val endText = typedArray.getString(R.styleable.QueueTextView_end_text)
        val endTextSize =typedArray.getDimension(R.styleable.QueueTextView_end_textSize, 16f)
        val endTextStyle = typedArray.getInt(R.styleable.QueueTextView_end_textStyle, 0)

        firstTextView = TextView(context)
        firstTextView.text = firstText
        firstTextView.textColor = firstTextColor
        firstTextView.textSize = firstTextSize
        firstTextView.setTypeface(Typeface.DEFAULT,firstTextStyle)


        secondTextView = TextView(context)
        secondTextView.text = secondText
        secondTextView.textColor = secondTextColor
        secondTextView.textSize = secondTextSize
        secondTextView.setTypeface(Typeface.DEFAULT,secondTextStyle)

        endTextView = TextView(context)
        endTextView.text = endText
        endTextView.textColor = endTextColor
        endTextView.textSize = endTextSize
        endTextView.setTypeface(Typeface.DEFAULT,endTextStyle)

        initView()

    }



    private fun initView() {
        addView(firstTextView)
        addView(secondTextView)
        addView(endTextView)
    }


}