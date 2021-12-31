package com.mckj.module.wifi.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

/**
 * Describe:
 *
 * Created By yangb on 2021/1/4
 */
class MaxRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(
    context,
    attrs,
    defStyleAttr
) {

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context) : this(context, null)

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(
            Int.MAX_VALUE shr 2,
            MeasureSpec.AT_MOST
        )
        super.onMeasure(widthSpec, expandSpec)
    }
}