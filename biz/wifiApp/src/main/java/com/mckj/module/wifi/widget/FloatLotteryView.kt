package com.mckj.module.wifi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.mckj.baselib.helper.toActivity
import com.mckj.baselib.view.anim.playSpin
import com.mckj.module.wifi.databinding.WifiViewFloatLotteryBinding

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
class FloatLotteryView(
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

    private lateinit var mBinding: WifiViewFloatLotteryBinding

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding = WifiViewFloatLotteryBinding.inflate(LayoutInflater.from(context), this, true)
        //启动动画
        val activity = context.toActivity()
        if (activity != null) {
            mBinding.lotteryIv.playSpin(activity.lifecycle)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mBinding.unbind()
    }

}