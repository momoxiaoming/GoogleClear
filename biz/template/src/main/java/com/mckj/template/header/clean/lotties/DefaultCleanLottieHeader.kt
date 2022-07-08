package com.mckj.template.header.clean.lotties

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieAnimationView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.api.entity.CacheJunk
import com.mckj.template.R
import com.mckj.template.databinding.CleanupHeaderLottiesBinding
import com.mckj.template.entity.TepConstants
import com.mckj.template.header.clean.AbsCleanHeader
import com.mckj.template.impl.IAction
import kotlin.properties.Delegates

/**
 * 带lottie的动画
 */
open class DefaultCleanLottieHeader(context: Context, iAction: IAction) :
    AbsCleanLottieHeader<CleanupHeaderLottiesBinding>(context, iAction) {

    companion object {
//        private const val SCAN_NORMAL = "header/scan_normal.zip"
//        private const val SCAN_WARN = "header/scan_warn.zip"
        private val SCAN_NORMAL = R.raw.scan_normal
        private val SCAN_WARN = R.raw.scan_warn
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

    override fun getScanDesc(): TextView? {
        return mBinding.junkDescTv
    }

    override fun getScanStatus(): TextView? {
        return mBinding.scanStatus
    }

    override fun getJunkDesc(): TextView? {
        return mBinding.junkDescTv
    }

    override fun getScanAction(): TextView? {
        return mBinding.scanAction
    }

    override fun getScanEmptyView(): View? {
        return mBinding.emptyJunkIcon
    }

    override fun getResId(): Int {
        return R.layout.cleanup_header_lotties
    }

    override fun getHeaderBg(): Int? {
        return R.drawable.cleanup_header_bg
    }

    override fun getDefaultAnimPath(): Int {
        return SCAN_NORMAL
    }

    override fun getWarnAnimPath(): Int {
        return SCAN_WARN
    }


}