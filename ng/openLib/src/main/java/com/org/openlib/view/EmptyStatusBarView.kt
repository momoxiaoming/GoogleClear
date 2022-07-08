package com.org.openlib.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.org.openlib.utils.SystemUiUtil

/**
 * Describe:
 *
 * Created By yangb on 2021/1/29
 */
class EmptyStatusBarView(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet? = null) : this(
        context,
        attributeSet,
        0
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
            SystemUiUtil.getStatusBarHeight(context)
        )
    }

}