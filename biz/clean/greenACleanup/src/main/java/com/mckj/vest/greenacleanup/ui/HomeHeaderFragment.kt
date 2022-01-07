package com.mckj.vest.greenacleanup.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dn.baselib.anim.BaseAnimator
import com.dn.baselib.anim.playRotation
import com.dn.baselib.base.databinding.DataBindingFragment
import com.dn.baselib.ext.idToString
import com.dn.baselib.ext.onceClick
import com.dn.baselib.util.ResourceUtil
import com.dn.datalib.ARouterPath
import com.dn.datalib.helper.FingerHelper
import com.dn.baselib.util.SizeUtil
import com.dn.openlib.ui.startTitleFragment
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.cm.utils.DAttrUtil
import com.dn.vi.app.cm.utils.FileUtil

import com.mckj.module.cleanup.gen.St
import com.mckj.module.cleanup.helper.ColorDrawableHelper

import com.mckj.sceneslib.entity.ScanData
import com.mckj.sceneslib.manager.AutoScanManager
import com.mckj.vest.greenacleanup.R
import com.mckj.vest.greenacleanup.databinding.CleanupFragmentHomeHeaderBinding
import com.mckj.vest.greenacleanup.help.NavigationHelper
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor

/**
 * Describe:
 *
 * Created By yangb on 2021/3/19
 */
class HomeHeaderFragment : DataBindingFragment<CleanupFragmentHomeHeaderBinding, HomeViewModel>() {

    private val mColorDrawableHelper by lazy {
        ColorDrawableHelper(
            intArrayOf(0xFF18D96E.toInt(), 0xFF54F29F.toInt()),
            intArrayOf(0xFFFF6201.toInt(), 0xFFFEA61D.toInt())
        )
    }

    /**
     * 手指帮助类
     */
    private val mFingerHelper: FingerHelper by lazy { FingerHelper() }
    private var mFingerView: View? = null

    //扫描中的圆圈动画
    private var mScanAnimator: BaseAnimator? = null

    override fun onCreateRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        val view = super.onCreateRootView(inflater, container)
        mBinding.titleLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.titleLayout.headerToolbar.apply {
            navigationIcon = null
            title = ResourceUtil.getText(R.string.app_name)
        }
        mBinding.titleSettingIv.onceClick {
            St.stHomeSetupClick("home")
            NavigationHelper.openAbout(requireContext())
        }
        return view
    }

    override fun getLayoutId() = R.layout.cleanup_fragment_home_header

    override fun getViewModel(): HomeViewModel {
        return ViewModelProvider(requireActivity(), HomeViewModelFactory()).get(
            HomeViewModel::class.java
        )
    }

    override fun initData() {
        St.stCleanShow()
    }

    override fun initView() {
        mBinding.titleRemindIv.onceClick {
            //提醒界面
            St.stHomeAuthorityClick()
            requireContext().startTitleFragment(ARouterPath.Cleanup.FRAGMENT_POINT)
        }
        mBinding.titleRemindIv.gone()

        mBinding.scanningDescTv.onceClick {
            //垃圾详情
            mModel.showCleanDetail(requireContext())
        }
        mBinding.contentSmartBtn.onceClick(mSmartClickListener)
        mBinding.scanningLayout.onceClick(mSmartClickListener)
    }

    override fun initObserver() {
        super.initObserver()
        AutoScanManager.getInstance().scanDataLiveData.observe(viewLifecycleOwner, Observer {
            when (it.state) {
                ScanData.STATE_SCANNING -> {
                    warpScanDataByScanning(it)
                }
                ScanData.STATE_SCANNED -> {
                    warpScanDataByScanEd(it)
                }
                else -> {
                    //默认状态
                    warpScanDataByNormal(it)
                }
            }
        })
    }

    private val mSmartClickListener = View.OnClickListener {

        //扫描按钮
        val scanData =
            AutoScanManager.getInstance().scanDataLiveData.value ?: return@OnClickListener
        when (scanData.state) {
            ScanData.STATE_SCANNING -> {
                mModel.stopScanJunk()
            }
            ScanData.STATE_SCANNED -> {
                when (it) {
                    mBinding.contentSmartBtn -> {
                        St.stHomeCleanCleanClick()
                    }
                    mBinding.scanningLayout -> {
                        St.stHomeCleanIconcleanClick()
                    }
                }
                mModel.showCleanJunk(requireContext())
            }
            else -> {
                when (it) {
                    mBinding.contentSmartBtn -> {
                        St.stHomeCleanClick()
                    }
                    mBinding.scanningLayout -> {
                        St.stHomeIconcleanClick()
                    }
                }
                if (mModel.isStoragePermission(requireContext())) {
                    mModel.scanJunk()
                } else {
                    mModel.requestStoragePermission(requireActivity())
                }
            }
        }
    }

    /**
     * 填充扫描数据-默认状态
     */
    private fun warpScanDataByNormal(scanData: ScanData) {
        mBinding.rootLayout.background = mColorDrawableHelper.getDrawableBySize(0)
        mBinding.scanLayout.show()
        mBinding.scanningLayout.gone()

        mBinding.apply {
            contentSmartBtn.text = idToString(R.string.cleanup_scaning)
            contentSmartBtn.textColor = DAttrUtil.getPrimaryColor(requireContext())

            scanDescTv.text = getString(R.string.cleanup_clear_junk_desc)
        }
        mScanAnimator?.close()
        mFingerHelper.remove(mFingerView)
        mFingerView = null
    }

    /**
     * 填充扫描数据-正在扫描
     */
    private fun warpScanDataByScanning(scanData: ScanData) {
        mBinding.rootLayout.background = mColorDrawableHelper.getDrawableBySize(scanData.size)
        mBinding.scanLayout.gone()
        mBinding.scanningLayout.show()

        mBinding.apply {
            contentSmartBtn.text = idToString(R.string.cleanup_stop)
            contentSmartBtn.textColor = DAttrUtil.getPrimaryColor(requireContext())

            scanningDescTv.text = idToString(R.string.cleanup_scaning)
            scanningSizeTv.text = FileUtil.getFileSizeNumberText(scanData.size)
            scanningUnitTv.text = FileUtil.getFileSizeUnitText(scanData.size)
        }
        val scanAnimator = mScanAnimator
        if (scanAnimator == null) {
            mScanAnimator =
                mBinding.scanningIv.playRotation(viewLifecycleOwner.lifecycle)
        } else if (!scanAnimator.isRunning()) {
            scanAnimator.play()
        }
    }

    /**
     * 填充扫描数据-扫描结束
     */
    private fun warpScanDataByScanEd(scanData: ScanData) {
        mBinding.rootLayout.background = mColorDrawableHelper.getDrawableBySize(scanData.size)
        mBinding.scanLayout.gone()
        mBinding.scanningLayout.show()

        mBinding.apply {
            contentSmartBtn.text = idToString(R.string.cleanup_clean_1)
            contentSmartBtn.textColor = DAttrUtil.getPrimaryColor(requireContext())

            scanningDescTv.text = getString(R.string.cleanup_junk_detail)
            scanningSizeTv.text = FileUtil.getFileSizeNumberText(scanData.size)
            scanningUnitTv.text = FileUtil.getFileSizeUnitText(scanData.size)
        }
        mScanAnimator?.close()
        mBinding.contentSmartBtn.doOnLayout {
            mFingerHelper.remove(mFingerView)
            mFingerView = mFingerHelper.showFinger(
                mBinding.rootLayout,
                mBinding.contentSmartBtn, SizeUtil.dp2px(36f), SizeUtil.dp2px(10f)
            )
        }
    }

}