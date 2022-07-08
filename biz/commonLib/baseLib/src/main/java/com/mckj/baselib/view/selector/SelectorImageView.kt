package com.mckj.baselib.view.selector

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * Describe:SelectorImageView
 *
 * Created By yangb on 2020/9/25
 */
class SelectorImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun setPressed(pressed: Boolean) {
        super.setPressed(pressed)
        changeAlpha(pressed)
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        changeAlpha(selected)
    }

    private fun changeAlpha(changed: Boolean) {
        alpha = if (changed) {
            0.8f
        } else {
            1f
        }
    }

}