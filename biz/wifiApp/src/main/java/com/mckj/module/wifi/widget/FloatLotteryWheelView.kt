package com.mckj.module.wifi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.mckj.module.wifi.databinding.WifiViewFloatLotteryWheelBinding

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
class FloatLotteryWheelView(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attributeSet, defStyleAttr) {

    companion object {
        const val TAG = "LotteryView"
    }

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet? = null) : this(
        context,
        attributeSet,
        0
    )

    private lateinit var mBinding: WifiViewFloatLotteryWheelBinding

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding =
            WifiViewFloatLotteryWheelBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mBinding.unbind()
    }

}