package com.mckj.vest

import android.content.Context
import com.mckj.baselib.util.ResourceUtil
import com.mckj.template.header.clean.img.ImgCleanHeaderStyle13
import com.mckj.template.impl.IAction
import org.jetbrains.anko.textColor

class VestHeader(context: Context, iAction: IAction) : ImgCleanHeaderStyle13(context, iAction) {


    override fun isShowFinger(): Boolean {
        return true
    }

    override fun getEmptyIconRes(): Int {
        return R.drawable.ic_smiling
    }

    override fun getEmptyDesc(): String {
        return "CLEAN!"
    }

    override fun getEmptyDescColor(): Int {
        return R.color.white
    }


    override fun getHeaderNormalBg(): Int? {
        return R.drawable.img_home_clear
    }

    override fun getHeaderScanningBg(): Int? {
        return R.drawable.img_home_clear
    }

    override fun getHeaderEndBg(): Int? {
        return R.drawable.img_home_clear
    }

    override fun getBgWidth(): Float? {
        return 133f
    }

    override fun getBgHeight(): Float? {
        return 133f
    }

    override fun getTopNorBg(): Int {
        return R.drawable.transparent
    }

    override fun getTopScanBg(): Int {
        return R.drawable.ic_home_clear
    }

    override fun getTopEndBg(): Int {
        return R.drawable.ic_home_clear_red
    }

    override fun setJunkSize(): Float? {
        return 20f
    }

    override fun getScanStatusColor(): Int {
        return R.color.color_ffffff
    }

    override fun getHeaderScanningIcon(): Int? {
        return null
    }

    override fun getBtnNormalBg(): Int? {
        return null
    }

    override fun getBtnScanningBg(): Int? {
        return R.drawable.ic_func_one_key_speed
    }

    override fun getBtnEndBg(): Int? {
        return R.drawable.ic_func_one_key_speed
    }

    override fun getBtnWidth(): Float? {
        return 156f
    }

    override fun getBtnHeight(): Float? {
        return 38f
    }

    override fun getScanActionSize(): Float? {
        return 16f
    }

    override fun getTopImgWidth(): Float? {
        return 31f
    }

    override fun getTopImgHeight(): Float? {
        return 32f
    }

    override fun normalView(requestPermission: Boolean) {
        super.normalView(requestPermission)
        mBinding.scanAction.textColor = ResourceUtil.getColor(R.color.white)
    }

    override fun getScanActionNormalColor(): Int? {
        return R.color.color_6578ff
    }

    override fun getScanActionWarnColor(): Int? {
        return R.color.color_FF7295
    }
}