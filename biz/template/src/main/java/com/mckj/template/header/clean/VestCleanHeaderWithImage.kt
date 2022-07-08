package com.mckj.template.header.clean

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.databinding.DataBindingUtil
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.helper.FingerHelper
import com.mckj.template.R
import com.mckj.template.databinding.VestCleanHeaderBinding
import com.mckj.template.entity.TepConstants
import com.mckj.template.impl.IAction
import org.jetbrains.anko.textColor

open class VestCleanHeaderWithImage(context: Context, iAction: IAction) :
    CleanHeaderWithImage(context, iAction) {

    private lateinit var mBinding: VestCleanHeaderBinding

    /**
     * 手指帮助类
     */
    private val mFingerHelper: FingerHelper by lazy { FingerHelper() }
    private var mFingerView: View? = null

    override fun getRoot(): View {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.vest_clean_header,
            null,
            false
        )
        rootView = mBinding.root
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        rootView.layoutParams = layoutParams
        mBinding.scanIcon.setOnClickListener {
            startScan()
        }
        mBinding.scanStatus.setOnClickListener {
            lookDetail()
        }
        mBinding.scanAction.setOnClickListener {
            if (uiStatus == TepConstants.ScanUIStatus.SCAN) {
                stop()
            } else if (uiStatus == TepConstants.ScanUIStatus.DENY
                || uiStatus == TepConstants.ScanUIStatus.COMPLETE_CLEAN
            ) {
                startScan()
            } else {
                lookDetail()
            }
        }
        return rootView
    }

    override fun getAnimView(): View? {
        return mBinding.scanIcon
    }

    override fun getScanSize(): TextView? {
        return mBinding.scanResult
    }

    override fun getScanUnit(): TextView? {
        return null
    }

    override fun getScanDesc(): TextView? {
        return mBinding.scanStatus
    }

    override fun getScanStatus(): TextView? {
        return mBinding.scanStatus
    }

    override fun getScanAction(): TextView? {
        return mBinding.scanAction
    }

    override fun getScanEmptyView(): View? {
        return mBinding.smileIcon
    }

    override fun getJunkDesc(): TextView? {
        return null
    }

    override fun initView(context: Context): View {
        return getRoot()
    }

    override fun normalView(requestPermission: Boolean) {
        mBinding.scanIcon.setImageDrawable(ICON_DEFAULT)
        mBinding.scanResultLayout.gone()
        mBinding.scanActionStatus.gone()
        mBinding.scanAction.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        mBinding.scanAction.paint.isAntiAlias = true
        mBinding.scanActionLl.background = null
        mBinding.scanAction.textColor = ResourceUtil.getColor(R.color.primaryTextColor)
        if (requestPermission) {
            mBinding.scanStatus.text = ResourceUtil.getString(R.string.cleanup_open_permissions)
            mBinding.scanAction.text = ResourceUtil.getString(R.string.cleanup_open_permissions)
        } else {
            mBinding.scanStatus.text = ResourceUtil.getString(R.string.cleanup_very_clean)
            mBinding.scanAction.text = ResourceUtil.getString(R.string.cleanup_click_to_cleanup)
        }
        hideFinger()
    }

    override fun scanView() {
        mBinding.scanIcon.setImageDrawable(ICON_DEFAULT)
        mBinding.scanStatus.text = ResourceUtil.getString(R.string.cleanup_scanning)
        mBinding.scanAction.text = ResourceUtil.getString(R.string.cleanup_stop_scanning)
        mBinding.scanResultLayout.show()
        mBinding.scanResult.show()
        mBinding.smileIcon.gone()
        mBinding.scanActionLl.background =
            ResourceUtil.getDrawable(R.drawable.shape_scan_button)
        mBinding.scanActionStatus.show()
        mBinding.scanAction.textColor = ResourceUtil.getColor(R.color.primaryTextColor)
        mBinding.scanActionStatus.background =
            ResourceUtil.getDrawable(R.drawable.shape_scan_dot_blue)
    }

    override fun endView(warn: Boolean) {
        mBinding.scanIcon.setImageDrawable(ICON_DEFAULT)
        mBinding.scanStatus.text = ResourceUtil.getString(R.string.cleanup_view_trash_details)
        mBinding.scanAction.text =ResourceUtil.getString(R.string.scenes_click_to_clean)
        mBinding.scanResultLayout.show()
        mBinding.scanResult.show()
        mBinding.smileIcon.gone()
        mBinding.scanActionLl.background =
            ResourceUtil.getDrawable(R.drawable.shape_scan_button)
        mBinding.scanActionStatus.show()
        if (warn) {
            mBinding.scanAction.textColor = ResourceUtil.getColor(R.color.scan_warn)
            mBinding.scanActionStatus.background =
                ResourceUtil.getDrawable(R.drawable.shape_scan_dot_red)
            mBinding.scanResult.textColor = ResourceUtil.getColor(R.color.scan_warn)
        } else {
            mBinding.scanAction.textColor = ResourceUtil.getColor(R.color.primaryTextColor)
            mBinding.scanResult.textColor = ResourceUtil.getColor(R.color.primaryTextColor)
            mBinding.scanActionStatus.background =
                ResourceUtil.getDrawable(R.drawable.shape_scan_dot_blue)
        }
        showFinger()
    }


    private fun showFinger() {
        mBinding.scanAction.doOnLayout {
            mFingerHelper.remove(mFingerView)
            mFingerView = mFingerHelper.showFinger(
                mBinding.container,
                mBinding.scanAction,
                SizeUtil.dp2px(-40f), 0, SizeUtil.dp2px(60f).toFloat()
            )
        }
    }

    private fun hideFinger() {
        mFingerHelper.remove(mFingerView)
        mFingerView = null
    }
}