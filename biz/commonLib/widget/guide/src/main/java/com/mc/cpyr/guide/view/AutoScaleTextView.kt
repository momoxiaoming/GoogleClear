package com.mc.cpyr.guide.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.mc.cpyr.guide.R


class AutoScaleTextView : AppCompatTextView {
    private var preferredTextSize = 0f
    private var minTextSize = 0f
    private var textPaint: Paint? = null

    constructor(context: Context?) : super(context!!, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.autoScaleTextViewStyle
    ) {
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        textPaint = Paint()
        val a: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.AutoScaleTextView, defStyleAttr, 0)
        minTextSize = a.getDimension(R.styleable.AutoScaleTextView_minTextSize, 10f)
        a.recycle()
        preferredTextSize = this.textSize
        Log.d(
            TAG,
            "this.preferredTextSize = $preferredTextSize, this.minTextSize = $minTextSize"
        )
    }

    /**
     * 设置最小的size
     *
     * @param minTextSize
     */
    fun setMinTextSize(minTextSize: Float) {
        this.minTextSize = minTextSize
    }

    /**
     * 根据填充内容调整textview
     *
     * @param text
     * @param textWidth
     */
    private fun refitText(text: String?, textWidth: Int) {
        if (textWidth <= 0 || text == null || text.length == 0) {
            return
        }
        val targetWidth: Int = textWidth - this.getPaddingLeft() - this.getPaddingRight()
        Log.d(TAG, "targetWidth = $targetWidth")
        val threshold = 0.5f
        textPaint?.set(this.getPaint())
        textPaint?.textSize = preferredTextSize
        if (textPaint?.measureText(text)!! <= targetWidth) {
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, preferredTextSize)
            return
        }
        var tempMinTextSize = minTextSize
        var tempPreferredTextSize = preferredTextSize
        Log.d(
            TAG,
            "this.preferredTextSize = $tempPreferredTextSize, this.minTextSize = $tempMinTextSize"
        )
        while (tempPreferredTextSize - tempMinTextSize > threshold) {
            val size = (tempPreferredTextSize + tempMinTextSize) / 2
            textPaint?.textSize = size
            if (textPaint?.measureText(text)!! >= targetWidth) {
                tempPreferredTextSize = size
            } else {
                tempMinTextSize = size
            }
        }
        Log.d(TAG, "this.minTextSize = $tempMinTextSize")
        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, tempMinTextSize)
    }

    protected override fun onTextChanged(
        text: CharSequence,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        refitText(text.toString(), this.getWidth())
    }

    protected override fun onSizeChanged(width: Int, h: Int, oldw: Int, oldh: Int) {
        if (width != oldw) {
            refitText(this.text.toString(), width)
        }
    }

    companion object {
        private const val TAG = "AutoScaleTextview"
    }
}