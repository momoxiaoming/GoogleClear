package com.mckj.template.header.clean

import android.content.Context
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.api.entity.CacheJunk
import com.mckj.baselib.util.ResourceUtil
import com.mckj.template.R
import com.mckj.template.entity.TepConstants
import com.mckj.template.impl.IAction
import kotlin.properties.Delegates

/**
 * 该曾处理逻辑部分
 */
abstract class CleanHeaderWithImage(context: Context, val iAction: IAction) :
    AbsCleanHeader(context) {

    companion object {
        val ICON_DEFAULT = ResourceUtil.getDrawable(R.drawable.cleanup_scan_default)
        val ICON_SCAN = ResourceUtil.getDrawable(R.drawable.cleanup_scaning)
        val ICON_END = ResourceUtil.getDrawable(R.drawable.cleanup_scan_default)
        val ICON_WARN = ResourceUtil.getDrawable(R.drawable.cleanup_scan_end)
        val ICON_COMPLETE_CLEAN = ResourceUtil.getDrawable(R.drawable.cleanup_complete_clean)
    }

    var uiStatus: Int by Delegates.observable(-1) { _, _, uistatus ->
        scanStatusChange(uistatus)
    }

    var mCacheJunk: CacheJunk? = null

    override fun updateUI(context: Context, data: Boolean) {

    }


    /**
     * 权限拒绝状态
     */
    override fun denyStatus() {
        mCacheJunk = null
        uiStatus = TepConstants.ScanUIStatus.DENY
    }

    /**
     * 默认状态
     */
    override fun defaultScanStatus() {

    }

    override fun scanIdle(cacheJunk: CacheJunk?, animEnable: Boolean) {
        mCacheJunk = cacheJunk
        uiStatus = TepConstants.ScanUIStatus.SCAN
    }

    override fun scanEnd(cacheJunk: CacheJunk?) {
        junkStatus(cacheJunk)
    }

    override fun completeCleanStatus(delayTime: Long) {
        mCacheJunk = null
        uiStatus = TepConstants.ScanUIStatus.COMPLETE_CLEAN
    }

    private fun junkStatus(cacheJunk: CacheJunk?): Int {
        mCacheJunk = cacheJunk
        val size = cacheJunk?.junkSize ?: 0
        uiStatus = if (size <= TepConstants.CLEAN) {
            TepConstants.ScanUIStatus.COMPLETE_CLEAN
        } else if (size > TepConstants.CLEAN && size <= TepConstants.HEALTHY) {
            TepConstants.ScanUIStatus.HEALTHY
        } else if (size > TepConstants.HEALTHY && size < TepConstants.WARN) {
            TepConstants.ScanUIStatus.NORMAL
        } else {
            TepConstants.ScanUIStatus.WARN
        }
        return uiStatus
    }

    protected open fun scanStatusChange(status: Int) {
//        if (mCacheJunk?.junkSize ?: 0 > TepConstants.CLEAN) {
//            getScanResult()?.text = StorageUtil.getUnit(mCacheJunk?.junkSize)
//        }

        getScanSize()?.text = FileUtil.getFileSizeNumberText(mCacheJunk?.junkSize ?: 0)
        getScanUnit()?.text = FileUtil.getFileSizeUnitText(mCacheJunk?.junkSize ?: 0)
        when (status) {
            TepConstants.ScanUIStatus.DENY -> {
                normalView(true)
            }
            TepConstants.ScanUIStatus.COMPLETE_CLEAN -> {
                normalView(false)
            }
            TepConstants.ScanUIStatus.SCAN -> {
                scanView()
            }
            TepConstants.ScanUIStatus.HEALTHY -> {
                endView(false)
            }
            TepConstants.ScanUIStatus.NORMAL, TepConstants.ScanUIStatus.WARN -> {
                endView(true)
            }
        }

    }

    protected fun startScan() {
        iAction.startScan()
    }

    protected fun startClean() {
        iAction.startClean()
    }

    protected fun lookDetail() {
        iAction.lookDetail()
    }

    protected fun stop() {
        iAction.stop()
    }


    abstract fun normalView(requestPermission: Boolean)

    abstract fun scanView()

    abstract fun endView(warn: Boolean)

}