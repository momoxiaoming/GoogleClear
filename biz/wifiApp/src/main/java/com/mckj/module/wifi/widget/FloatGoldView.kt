package com.mckj.module.wifi.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.mckj.baselib.helper.toActivity
import com.mckj.baselib.view.anim.playJump
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiViewFloatGoldBinding
import com.mckj.module.wifi.entity.FloatEntity
import com.mckj.module.wifi.manager.FloatDataManager
import java.util.*

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
class FloatGoldView(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attributeSet, defStyleAttr) {

    companion object {
        const val TAG = "FloatGoldView"
    }

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet? = null) : this(
        context,
        attributeSet,
        0
    )

    private var mIndex = 0
    private lateinit var mBinding: WifiViewFloatGoldBinding
    private val mFloatEntity: FloatEntity by lazy {
        FloatDataManager.getInstance().getFloatEntity(mIndex, FloatEntity.TYPE_GOLD)
    }

    init {
        val typedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.FloatGoldView)
        mIndex = typedArray.getInteger(R.styleable.FloatGoldView_index, 0)
        typedArray.recycle()
    }

    fun refreshTime() {
        mFloatEntity.time = Date().time
        FloatDataManager.getInstance().saveData()
    }

    fun isCountdownFinish(): Boolean = mBinding.floatGoldTv.isCountdownFinish()

    fun start() {
        val time = (FloatDataManager.MAX_COUNT_DOWN - (Date().time - mFloatEntity.time)) / 1000
        mBinding.floatGoldTv.start(time)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mBinding = WifiViewFloatGoldBinding.inflate(LayoutInflater.from(context), this, true)
        //启动动画
        val activity = context.toActivity()
        if (activity != null) {
            playJump(activity.lifecycle)
        }
        mBinding.floatGoldTv.setData(mIndex, FloatEntity.TYPE_GOLD)
        start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mBinding.floatGoldTv.close()
        mBinding.unbind()
    }

}