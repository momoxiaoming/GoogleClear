package com.mckj.template.header.clean.lotties

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.airbnb.lottie.LottieAnimationView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.api.entity.CacheJunk
import com.mckj.baselib.util.ResourceUtil
import com.mckj.datalib.helper.FingerHelper
import com.mckj.template.R
import com.mckj.template.databinding.CleanupHeaderLottiesBinding
import com.mckj.template.entity.TepConstants
import com.mckj.template.header.clean.AbsCleanHeader
import com.mckj.template.impl.IAction
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor
import kotlin.properties.Delegates

/**
 * 带lottie的动画
 */
abstract class AbsCleanLottieHeader<T : ViewDataBinding>(context: Context, iAction: IAction) :
    AbsCleanHeader(context) {

    companion object {
        const val ANIM_STEP_01 = 1//普通扫描动画
        const val ANIM_STEP_02 = 2//中间过度动画，马甲带过度动画的重写即可
        const val ANIM_STEP_03 = 3//结束，警告动画
    }

    protected lateinit var mBinding: T

    protected var mCacheJunk: CacheJunk? = null

    private var mAnimStep: Int = 0//动画步骤、记录防止重复设置

    /**
     * 状态监听，需要单独处理页面状态的在scanStatusChange 里面监听即可
     */
    var uiStatus: Int by Delegates.observable(-1) { _, _, uistatus ->
        scanStatusChange(uistatus)
    }


    override fun getRoot(): View {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getResId(),
            null,
            false
        )
        rootView = mBinding.root
        getHeaderBg()?.let {
            rootView.setBackgroundResource(it)
        }
        return rootView
    }


    abstract fun getResId(): Int

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

    /**
     * 扫描状态
     */
    override fun scanIdle(cacheJunk: CacheJunk?, animEnable: Boolean) {
        mCacheJunk = cacheJunk
        uiStatus = TepConstants.ScanUIStatus.SCAN
    }

    /**
     * 结束扫描
     */
    override fun scanEnd(cacheJunk: CacheJunk?) {
        dealStatus(cacheJunk)
    }

    /**
     * 清洁状态
     */
    override fun completeCleanStatus(delayTime: Long) {

    }

    /**
     * 根据扫描出来的垃圾大小回调不同的页面状态
     * 默认:清洁状态，健康状态，普通状态，警告状态。不同的马甲有不同的显示状态要求的，自行合并状态
     */
    private fun dealStatus(cacheJunk: CacheJunk?): Int {
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

    /**
     * 国内马甲默认的页面显示状态
     */
    protected open fun scanStatusChange(status: Int) {
        getScanSize()?.text = FileUtil.getFileSizeNumberText(mCacheJunk?.junkSize ?: 0)
        getScanUnit()?.text = FileUtil.getFileSizeUnitText(mCacheJunk?.junkSize ?: 0)
//        getScanDesc()?.text = "垃圾"
        when (status) {
            TepConstants.ScanUIStatus.DENY -> {
                normalView(true)
            }
            TepConstants.ScanUIStatus.COMPLETE_CLEAN -> {
                normalView(false)
            }
            TepConstants.ScanUIStatus.SCAN -> {
                scanView(mCacheJunk?.junkSize ?: 0)
            }
            TepConstants.ScanUIStatus.HEALTHY -> {
                endView(false, mCacheJunk?.junkSize ?: 0)
            }
            TepConstants.ScanUIStatus.NORMAL, TepConstants.ScanUIStatus.WARN -> {
                endView(true, mCacheJunk?.junkSize ?: 0)
            }
        }
    }

    /**
     * 普通状态显示
     * @param requestPermission :是否显示权限拒绝状态
     */
    protected open fun normalView(requestPermission: Boolean) {
        //处理扫描结果
        getScanSize()?.let {
            it.text = ""
        }
        getScanUnit()?.let {
            it.text = ""
        }
        getScanDesc()?.let {
            it.text = ""
        }
        getScanEmptyView()?.let {
            if (!requestPermission) {
                it.show()
            }
        }
        //处理动画
        getAnimView()?.let {
            if (it !is LottieAnimationView) {
                return
            }
            playAnimation(getDefaultAnimPath(), ANIM_STEP_01)
        }
        //处理扫描状态
        getScanStatus()?.let {
            it.text = ""
        }
        //处理扫描动作显示
        getScanAction()?.let {
            val str = if (requestPermission) "打开权限" else "垃圾清理"
            it.text = str
            getScanBtnBackground()?.apply {
                it.backgroundResource = this
            }
            it.setOnClickListener(mScanActionListener)
        }
    }

    /**
     * 扫描状态
     * @param junkSize 扫描大小
     */
    protected open fun scanView(junkSize: Long) {
        val junkSize = mCacheJunk?.junkSize ?: 0
        //处理扫描结果
        getScanSize()?.let {
            it.text = FileUtil.getFileSizeNumberText(junkSize)
            it.textColor = ResourceUtil.getColor(getScanSizeColor())
        }
        getScanUnit()?.let {
            it.text = FileUtil.getFileSizeUnitText(junkSize)
            it.textColor = ResourceUtil.getColor(getScanSizeColor())
        }
        getScanDesc()?.let {
            it.text = getScanJunkDescStr()
        }
        getScanEmptyView()?.let {
            if (it.visibility == View.VISIBLE) {
                it.gone()
            }
        }
        //处理动画
        getAnimView()?.let {
            if (it !is LottieAnimationView) {
                return
            }
            playAnimation(getDefaultAnimPath(), ANIM_STEP_01)
        }
        //处理扫描状态
        getScanStatus()?.let {
            it.text = "正在扫描中..."
            it.setOnClickListener(null)
        }
        //处理扫描动作显示
        getScanAction()?.let {
            it.text = "停止扫描"
            getScanEndBtnBackground()?.apply {
                it.backgroundResource = this
            }
            it.setOnClickListener(mScanActionListener)
        }

    }

    /**
     * 结束状态
     * @param warn 使用开启警告状态
     * @param junkSize 扫描大小
     */
    protected open fun endView(warn: Boolean, junkSize: Long) {
        getAnimView()?.let {
            if (it !is LottieAnimationView) {
                return
            }
            if (warn) {
                playAnimation(getWarnAnimPath(), ANIM_STEP_03)
            } else {
                playAnimation(getDefaultAnimPath(), ANIM_STEP_01)
            }

        }
        //处理扫描结果
        getScanSize()?.let {
            it.text = FileUtil.getFileSizeNumberText(junkSize)
            it.textColor = ResourceUtil.getColor(getScanSizeColor())
        }
        getScanUnit()?.let {
            it.text = FileUtil.getFileSizeUnitText(junkSize)
        }
        getScanDesc()?.let {
            it.text = getScanJunkDescStr()
        }
        //处理动画
        getAnimView()?.let {
            if (it !is LottieAnimationView) {
                return
            }
            playAnimation(getDefaultAnimPath(), ANIM_STEP_01)
        }
        //处理扫描状态
        getScanStatus()?.let {
            it.text = "查看垃圾详情>"
            it.setOnClickListener(mLookDetailListener)
        }
        //处理扫描动作显示
        getScanAction()?.let {
            it.text = "一键清理"
            getScanEndBtnBackground()?.apply {
                it.backgroundResource = this
            }
            it.setOnClickListener(mCleanListener)
        }

    }

    /**
     * 播放动画
     */
    protected fun playAnimation(path: Int, step: Int) {
        getAnimView()?.apply {
            if (this !is LottieAnimationView) {
                return
            }
            if (mAnimStep == step) {
                return
            }
            mAnimStep = step
            setAnimation(path)
            if (isAnimating) {
                cancelAnimation()
            }
            playAnimation()
        }
    }

    protected open fun getDefaultAnimPath(): Int {
        return 0
    }

    protected open fun getWarnAnimPath(): Int {
        return 0
    }

    protected open fun getHeaderBg(): Int? {
        return null
    }

    protected open fun getScanSizeColor(): Int {
        return R.color.color_FE523E
    }

    protected open fun getScanBtnBackground(): Int? {
        return null
    }

    protected open fun getScanEndBtnBackground(): Int? {
        return null
    }

    protected open fun getScanJunkDescStr(): String {
        return "垃圾"
    }


    /**
     * 扫描、停止扫描监听
     */
    protected open val mScanActionListener = View.OnClickListener {
        if (uiStatus == TepConstants.ScanUIStatus.SCAN) {
            iAction.stop()
        } else {
            iAction.startScan()
        }
    }

    protected open val mCleanListener = View.OnClickListener {
        if (uiStatus != TepConstants.ScanUIStatus.SCAN) {
            iAction.startClean()
        }
    }

    /**
     * 查看详情
     */
    protected open val mLookDetailListener = View.OnClickListener {
        if (uiStatus != TepConstants.ScanUIStatus.SCAN) {
            iAction.lookDetail()
        }
    }

}