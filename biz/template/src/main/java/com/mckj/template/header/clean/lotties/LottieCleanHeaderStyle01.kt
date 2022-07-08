package com.mckj.template.header.clean.lotties

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.core.view.doOnLayout
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.helper.FingerHelper
import com.mckj.template.R
import com.mckj.template.databinding.CleanLottieHeaderStyle01Binding
import com.mckj.template.impl.IAction
import org.jetbrains.anko.textColor

open class LottieCleanHeaderStyle01(context: Context, iAction: IAction) :
    AbsCleanLottieHeader<CleanLottieHeaderStyle01Binding>(context, iAction) {

    /**
     * 手指帮助类
     */
    private val mFingerHelper: FingerHelper by lazy { FingerHelper() }
    private var mFingerView: View? = null

    override fun getDefaultAnimPath(): Int {
        return 0
    }

    override fun getWarnAnimPath(): Int {
        return 0
    }

    /**
     * 扫描出的垃圾文案描述
     */
    override fun getScanJunkDescStr(): String {
        return "待清理"
    }

    /**
     * 是否显示手指动画
     */
    protected open fun isShowFinger(): Boolean {
        return false
    }

    protected open fun getEmptyIconRes(): Int {
        return R.drawable.cleanup_empty_icon
    }

    protected open fun getEmptyDesc(): String {
        return ResourceUtil.getString(R.string.scenes_text_clean)
    }

    protected open fun getEmptyDescColor(): Int {
        return R.color.color_666666
    }


    override fun getRoot(): View {
        val view = super.getRoot()
        mBinding.emptyIcon.setBackgroundResource(getEmptyIconRes())
        mBinding.emptyDesc.text = getEmptyDesc()
        mBinding.emptyDesc.textColor = ResourceUtil.getColor(getEmptyDescColor())
        return view
    }

    override fun getAnimView(): View? {
        return mBinding.lottieView
    }

    override fun getScanSize(): TextView? {
        return mBinding.junkSizeTv
    }

    override fun getScanUnit(): TextView? {
        return mBinding.junkUnitTv
    }

    override fun getJunkDesc(): TextView? {
        return mBinding.junkDescTv
    }

    override fun getScanDesc(): TextView? {
        return mBinding.junkDescTv
    }

    override fun getScanStatus(): TextView? {
        return mBinding.scanStatus
    }

    override fun getScanAction(): TextView? {
        return mBinding.scanAction
    }

    override fun getScanEmptyView(): View? {
        return mBinding.emptyJunkView
    }

    override fun getResId(): Int {
        return R.layout.clean_lottie_header_style_01
    }


    override fun normalView(requestPermission: Boolean) {
        super.normalView(requestPermission)
        if (isShowFinger()) {
            hideFinger()
        }
    }

    override fun endView(warn: Boolean, junkSize: Long) {
        super.endView(warn, junkSize)
        if (isShowFinger()) {
            showFinger()
        }
    }


    private fun showFinger() {
        mBinding.scanAction.doOnLayout {
            mFingerHelper.remove(mFingerView)
            mFingerView = mFingerHelper.showFinger(
                mBinding.rootContainer,
                mBinding.scanAction,
                SizeUtil.dp2px(-30f), 0, SizeUtil.dp2px(40f).toFloat()
            )
        }
    }

    private fun hideFinger() {
        mFingerHelper.remove(mFingerView)
        mFingerView = null
    }

}