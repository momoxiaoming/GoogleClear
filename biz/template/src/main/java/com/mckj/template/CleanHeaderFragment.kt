package com.mckj.template

import androidx.lifecycle.ViewModelProvider
import com.mckj.api.client.JunkConstants
import com.mckj.api.client.JunkExecutor
import com.mckj.api.client.base.JunkClient
import com.mckj.api.client.task.CleanCooperation
import com.mckj.api.entity.AppJunk
import com.mckj.api.entity.JunkInfo
import com.mckj.api.entity.ScanBean
import android.util.Log
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.template.header.clean.AbsCleanHeader
import com.mckj.template.entity.TepConstants
import com.mckj.template.gen.St
import com.mckj.template.impl.IAction
import com.org.openlib.utils.onceClick
import com.mckj.template.databinding.CleanupFragmentHeaderContainerBinding
import com.mckj.template.header.clean.VestCleanHeaderWithImage
import com.org.openlib.helper.NavigationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor

/**
 * @author leix
 * @version 1
 * @createTime 2022/2/25 11:45
 * @desc 清理头部
 */
open class CleanHeaderFragment :
    DataBindingFragment<CleanupFragmentHeaderContainerBinding, BaseHomeViewModel>() {

    private companion object {
        const val LOG = "HomeHeader"
    }

    private var executor: JunkExecutor? = null
    private var mStatus = -1
    private var mScanBean: ScanBean? = null
    private var isDeny = false
    protected lateinit var mHeader: AbsCleanHeader

    override fun initData() {
        JunkClient.instance.getExecutor(JunkConstants.Session.APP_CACHE)?.let {
            executor = it
        } ?: let {
            executor = CleanCooperation.getCacheExecutor()
        }
    }


    override fun initView() {
        mBinding.headerContainer.removeAllViews()
        mHeader = getHeader()
        mBinding.headerContainer.addView(mHeader.rootView)
        subscribeUi()
        autoScan()
        mBinding.setting.onceClick {
            St.stHomeSetupClick("home")
            NavigationHelper.openAbout(requireContext())
        }
        mBinding.appName.textColor = ResourceUtil.getColor(R.color.app_name_color)
//        mBinding.emptyContainer.layoutParams?.let {
//            it.height = SizeUtil.dp2px(getEmptyViewHeight())
//            mBinding.emptyContainer.layoutParams = it
//        }

        mBinding.headerBg.layoutParams?.let {
            it.height = SizeUtil.dp2px(getHeaderBgHeight())
            mBinding.headerBg.layoutParams = it
        }

//        mBinding.headerBg.layoutParams?.let {
//            getHeaderBgHeight()?.let {
//                val constraintSet = ConstraintSet()
//                constraintSet.clone(mBinding.rootLayout)
//                constraintSet.constrainWidth(R.id.header_bg, SizeUtil.dp2px(it))
//                constraintSet.constrainHeight(R.id.header_bg, SizeUtil.dp2px(it))
//                constraintSet.applyTo(mBinding.rootLayout)
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        if (isDeny) {
            autoScan()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_header_container
    }

    override fun getViewModel(): BaseHomeViewModel {
        return ViewModelProvider(requireActivity()).get(
            BaseHomeViewModel::class.java
        )
    }

    protected var iAction = object : IAction {
        override fun startScan() {
            scan()
        }

        override fun startClean() {
            if (enable()) {
                clean()
            }
        }

        override fun stop() {
            if (canStop()) {
                stopScan()
            }
        }

        override fun lookDetail() {
            if (enable()) {
                lookDetailList()
            }
        }
    }

    protected open fun getHeader(): AbsCleanHeader {
        return VestCleanHeaderWithImage(requireActivity(), iAction)
    }


    private fun subscribeUi() {
        executor?.getScanData()?.observe(this) {
            mScanBean = it
            when (it.status) {
                JunkConstants.ScanStatus.SILEN -> {
                    return@observe
                }
                JunkConstants.ScanStatus.START, JunkConstants.ScanStatus.SCAN_IDLE -> {
                    mHeader.scanIdle(it.junk, mStatus != JunkConstants.ScanStatus.SCAN_IDLE)
                    mModel.updateStatus(BaseHomeViewModel.NORMAL)
                }
                JunkConstants.ScanStatus.COMPLETE -> {
                    viewScope.launch(Dispatchers.Main) {
                        withContext(Dispatchers.IO) {
                            delay(2000)
                        }
                        mHeader.scanEnd(it.junk)
//
//                        if (isWarn(it.junk?.junkSize ?: 0)) {
//                            mModel.updateStatus(BaseHomeViewModel.WARN)
//                        } else {
//                        }
                        if (it.junk?.junkSize!! <= 0L) {
                            mModel.updateStatus(BaseHomeViewModel.NORMAL)
                        }else{
                            mModel.updateStatus(BaseHomeViewModel.END)
                        }
                    }
                }
                JunkConstants.ScanStatus.CACHE -> {
                    mHeader.scanEnd(it.junk)
                }
                JunkConstants.ScanStatus.CLEAN -> {
                    mHeader.scanEnd(null)
                    mModel.updateStatus(BaseHomeViewModel.NORMAL)
                }
            }
            mStatus = it.status
        }
    }

    /**
     * 自动扫描
     */
    protected fun autoScan() {
        mModel.startFlow(requireActivity(), consumer = { status, junk ->
            when (status) {
                TepConstants.CleanHeaderStatus.DENY -> {
                    isDeny = true
                    mHeader.denyStatus()
                    mModel.updateStatus(BaseHomeViewModel.NORMAL)
                }
                TepConstants.CleanHeaderStatus.CACHE -> {
                    isDeny = false
                    Log.d(LOG, "loadFromCache")
                    executor?.let {
                        JunkClient.instance.loadCache(it, junk)
                    }
                    mHeader.defaultScanStatus()
                }
                else -> {
                    isDeny = false
                    Log.d(LOG, "autoScan")
                    executor?.let {
                        JunkClient.instance.scan(it)
                    }
                    mHeader.defaultScanStatus()
                }
            }
        })
    }

    protected fun scan() {
        mModel.requestPermission(requireActivity()) { accept ->
            if (accept) {
                executor?.scan()
                mHeader.defaultScanStatus()
            }
        }
    }

    protected fun stopScan() {
        executor?.stop()
    }

    protected fun clean() {
        if (!mModel.hasPermission(requireContext())) {
            scan()
        } else {
            val list = mutableListOf<JunkInfo>()
            var junkSize = 0L
            mScanBean?.junk?.appJunks?.forEach { appJunk ->
                junkSize += appJunk.junkSize
                appJunk.junks?.apply {
                    list.addAll(this)
                }
            }
            if (isClean(junkSize)) {
                return
            }
            mModel.forwardCleanDetail(requireActivity(), list)
        }
    }

    protected fun lookDetailList() {
        val list = mutableListOf<AppJunk>()
        mScanBean?.junk?.appJunks?.forEach {
            list.add(it)
        }
        mModel.lookDetail(requireActivity(), list)
    }


    private fun enable(): Boolean {
        return mStatus == JunkConstants.ScanStatus.COMPLETE || mStatus == JunkConstants.ScanStatus.CLEAN
    }

    private fun canStop(): Boolean {
        return mStatus == JunkConstants.ScanStatus.SCAN_IDLE || mStatus == JunkConstants.ScanStatus.START
    }

    private fun isClean(junkSize: Long): Boolean {
        return junkSize < TepConstants.CLEAN
    }

    private fun isWarn(junkSize: Long): Boolean {
        return junkSize > TepConstants.WARN
    }


//    private fun setNormalBg() {
//        getNormalBg()?.let {
//            mBinding.rootLayout.backgroundResource = it
//        }
//    }
//
//    private fun setWarnBg() {
//        getWarnBg()?.let {
//            mBinding.rootLayout.backgroundResource = it
//        }
//    }

    protected open fun getNormalBg(): Int? {
        return null
    }

    protected open fun getWarnBg(): Int? {
        return null
    }

    protected open fun getEmptyViewHeight(): Float {
        return 0f
    }

    protected open fun getHeaderBgHeight(): Float {
        return 0f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        JunkClient.instance.clearExecutor(JunkConstants.Session.APP_CACHE)
        stopScan()
    }
}