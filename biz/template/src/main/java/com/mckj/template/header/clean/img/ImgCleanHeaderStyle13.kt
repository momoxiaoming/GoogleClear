package com.mckj.template.header.clean.img

import android.content.Context
import android.graphics.Paint
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.doOnLayout
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.helper.FingerHelper
import com.mckj.template.R
import com.mckj.template.databinding.CleanImgHeaderStyle13Binding
import com.mckj.template.impl.IAction
import org.jetbrains.anko.textColor

open class ImgCleanHeaderStyle13(context: Context, iAction: IAction) :
    AbsCleanImgHeader<CleanImgHeaderStyle13Binding>(context, iAction) {

    /**
     * 手指帮助类
     */
    private val mFingerHelper: FingerHelper by lazy { FingerHelper() }
    private var mFingerView: View? = null

    /**
     * 扫描出的垃圾文案描述,无则不写
     */
    override fun getScanJunkDescStr(): String {
        return ""
    }

    /**
     * 是否显示手指动画
     */
    protected open fun isShowFinger(): Boolean {
        return false
    }

    /**
     * 扫描为空的提示图标
     */
    protected open fun getEmptyIconRes(): Int {
        return R.drawable.cleanup_empty_icon
    }


    override fun getHeaderNormalBg(): Int? {
        return R.drawable.cleanup_header_bg
    }

    override fun getHeaderScanningBg(): Int? {
        return R.drawable.cleanup_header_bg
    }

    override fun getHeaderEndBg(): Int? {
        return R.drawable.cleanup_header_bg
    }

    /**
     * 扫描为空的提示文案
     */
    protected open fun getEmptyDesc(): String {
        return ResourceUtil.getString(R.string.scenes_text_clean)
    }

    /**
     * 扫描为空的提示文案颜色
     */
    protected open fun getEmptyDescColor(): Int {
        return R.color.color_666666
    }

    override fun getScanSizeColor(): Int {
        return R.color.white
    }

    override fun getRoot(): View {
        val view = super.getRoot()
        mBinding.emptyIcon.setBackgroundResource(getEmptyIconRes())
        mBinding.emptyDesc.text = getEmptyDesc()
        mBinding.emptyDesc.textColor = ResourceUtil.getColor(getEmptyDescColor())

        //bg大小
        val constraintSet = ConstraintSet()
        constraintSet.clone(mBinding.rootContainer)
        constraintSet.constrainWidth(R.id.header_bg, SizeUtil.dp2px(getBgWidth()!!))
        constraintSet.constrainHeight(R.id.header_bg, SizeUtil.dp2px(getBgHeight()!!))
        constraintSet.applyTo(mBinding.rootContainer)

        //按钮大小
        var lp = mBinding.scanAction.layoutParams
        lp.width = SizeUtil.dp2px(getBtnWidth()!!)
        lp.height = SizeUtil.dp2px(getBtnHeight()!!)
        mBinding.scanAction.layoutParams = lp

        //top图大小
        var lp1 = mBinding.topImg.layoutParams
        lp1.width = SizeUtil.dp2px(getTopImgWidth()!!)
        lp1.height = SizeUtil.dp2px(getTopImgHeight()!!)
        mBinding.scanAction.layoutParams = lp

        return view
    }

    override fun getAnimView(): View? {
        return mBinding.imgView
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

    override fun getHeaderView(): View? {
        return mBinding.headerBg
    }

    override fun getResId(): Int {
        return R.layout.clean_img_header_style_13
    }


    override fun normalView(requestPermission: Boolean) {
        super.normalView(requestPermission)
        if (isShowFinger()) {
            hideFinger()
        }
        mBinding.scanStatus.gone()
        mBinding.scanAction.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        mBinding.scanAction.paint.isAntiAlias = true
        mBinding.scanAction.background = null
        mBinding.topImg.background = ResourceUtil.getDrawable(getTopNorBg())
        getScanAction()?.let {
            val str = if (requestPermission) {
                ResourceUtil.getString(R.string.cleanup_open_permissions)
            } else {
                ResourceUtil.getString(R.string.cleanup_click_rescan)
            }
            it.text = str
        }
    }

    override fun scanView(junkSize: Long) {
        super.scanView(junkSize)
        mBinding.scanStatus.show()
        mBinding.scanAction.paint.flags = 0
        mBinding.topImg.background = ResourceUtil.getDrawable(getTopScanBg())
        getScanActionNormalColor()?.let { color ->
            setScanActionColor(color)
        }
    }

    override fun endView(warn: Boolean, junkSize: Long) {
        super.endView(warn, junkSize)
        if (isShowFinger()) {
            showFinger()
        }
        getScanActionWarnColor()?.let { color ->
            setScanActionColor(color)
        }
        mBinding.scanAction.paint.flags = 0
        mBinding.scanStatus.show()
        mBinding.topImg.background = ResourceUtil.getDrawable(getTopEndBg())
    }


    private fun showFinger() {
//        if (context is Activity) {
//            val activity = context as Activity
//            val root =
//                activity.window.decorView.findViewById<FrameLayout>(android.R.id.content) ?: return
        mBinding.scanAction.doOnLayout {
            mFingerHelper.remove(mFingerView)
            mFingerView = mFingerHelper.showFinger(
                mBinding.rootContainer,
                mBinding.scanAction,
                SizeUtil.dp2px(-30f), 0, SizeUtil.dp2px(40f).toFloat()
            )
//            }
        }
    }

    private fun hideFinger() {
        mFingerHelper.remove(mFingerView)
        mFingerView = null
    }

    protected open fun setBgWidth(): Int? {
        return null
    }

    protected open fun setBgHeight(): Int? {
        return null
    }

    protected open fun getTopNorBg(): Int {
        return R.color.color_trans
    }

    protected open fun getTopScanBg(): Int {
        return R.color.color_trans
    }

    protected open fun getTopEndBg(): Int {
        return R.color.color_trans
    }
}