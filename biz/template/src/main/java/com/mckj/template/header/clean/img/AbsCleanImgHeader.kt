package com.mckj.template.header.clean.img

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.api.entity.CacheJunk
import com.mckj.baselib.util.ResourceUtil
import com.mckj.template.R
import com.mckj.template.entity.TepConstants
import com.mckj.template.header.clean.AbsCleanHeader
import com.mckj.template.impl.IAction
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.textColor
import kotlin.properties.Delegates

/**
 * 静态图片的头部
 */
abstract class AbsCleanImgHeader<T : ViewDataBinding>(context: Context, iAction: IAction) :
    AbsCleanHeader(context) {


    protected lateinit var mBinding: T

    protected var mCacheJunk: CacheJunk? = null


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
        getHeaderNormalBg()?.let {
            setHeaderBg(it)
        }
        val lp = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        rootView.layoutParams = lp
        setScanActionSize()

        //垃圾size文字大小
        getScanSize().let { tv->
            setJunkSize()?.let {
                tv?.textSize = it
            }
        }

        getScanUnit().let { tv->
            setJunkUnitSize()?.let {
                tv?.textSize = it
            }
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
            }else{
                it.gone()
            }
        }
        //处理背景图片
        getHeaderNormalIcon()?.let { it ->
            setImageIcon(it)
        }
        getHeaderNormalBg()?.let {
            setHeaderBg(it)
        }

        //处理扫描状态
        getScanStatus()?.let {
            it.text = ""
        }
        //处理扫描动作显示
        getScanAction()?.let {
            val str = if (requestPermission) ResourceUtil.getString(R.string.cleanup_open_permissions) else ResourceUtil.getString(R.string.scenes_junk_clean)
            getScanActionNormalColor()?.let { color ->
                setScanActionColor(color)
            }
            it.text = str
            getBtnNormalBg()?.let { bg ->
                it.backgroundResource = bg
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
        //处理扫描图片
        getHeaderScanningIcon()?.let {
            setImageIcon(it)
        }
        //处理背景图片
        getHeaderScanningBg()?.let {
            setHeaderBg(it)
        }

        //处理扫描状态
        getScanStatus()?.let {
            it.text = ResourceUtil.getString(R.string.cleanup_scanning)
            it.textColor = ResourceUtil.getColor(getScanStatusColor())
            it.setOnClickListener(null)
        }
        //处理扫描动作显示
        getScanAction()?.let {
            getScanActionNormalColor()?.let { color ->
                setScanActionColor(color)
            }
            it.text = ResourceUtil.getString(R.string.cleanup_stop_scanning)
            getBtnScanningBg()?.let { bg ->
                it.backgroundResource = bg
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
        //处理背景图片
        getHeaderEndIcon()?.let { bg ->
            setImageIcon(bg)
        }
        getHeaderEndBg()?.let {
            setHeaderBg(it)
        }
        //处理扫描状态
        getScanStatus()?.let {
            it.text = ResourceUtil.getString(R.string.cleanup_view_trash_details)
            it.setOnClickListener(mLookDetailListener)
        }
        //处理扫描动作显示
        getScanAction()?.let {
            if (warn) {
                getScanActionWarnColor()?.let { color ->
                    setScanActionColor(color)
                }
            } else {
                getScanActionNormalColor()?.let { color ->
                    setScanActionColor(color)
                }
            }
            it.text = ResourceUtil.getString(R.string.scenes_click_to_clean)
            getBtnEndBg()?.let { bg ->
                it.backgroundResource = bg
            }
            it.setOnClickListener(mCleanListener)
        }

    }

    /**
     * 设置背景图片
     */
    protected fun setImageIcon(res: Int) {
        //设置图片
        getAnimView()?.let {
            if (it !is ImageView) return@let
            it.setBackgroundResource(res)
        }
    }

    protected fun setHeaderBg(res: Int) {
        //设置主背景
        getHeaderView()?.let { it ->
            it.setBackgroundResource(res)
        }
    }

    //设置按钮大小
    private fun setScanActionSize() {
        getScanAction()?.let { scanAction ->
            getScanActionSize()?.let {
                scanAction.textSize = it
            }
        }
    }

    protected fun setScanActionColor(res: Int) {
        getScanAction()?.let { scanAction ->
            scanAction.textColor = ResourceUtil.getColor(res)
        }
    }

    /**
     * 动态获取头部的icon
     */
    protected open fun getHeaderNormalIcon(): Int? {
        return null
    }

    protected open fun getHeaderScanningIcon(): Int? {
        return null
    }

    protected open fun getHeaderEndIcon(): Int? {
        return null
    }

    /**
     * 动态获取头部的背景图
     */
    //主背景资源id
    protected open fun getHeaderNormalBg(): Int? {
        return null
    }

    protected open fun getHeaderScanningBg(): Int? {
        return null
    }

    protected open fun getHeaderEndBg(): Int? {
        return null
    }

    protected open fun getScanSizeColor(): Int {
        return R.color.color_FE523E
    }

    protected open fun getScanStatusColor(): Int {
        return R.color.color_FE523E
    }

    protected open fun getBtnNormalBg(): Int? {
        return null
    }

    protected open fun getBtnScanningBg(): Int? {
        return null
    }

    protected open fun getBtnEndBg(): Int? {
        return null
    }

    protected open fun getScanJunkDescStr(): String {
        return ResourceUtil.getString(R.string.scenes_dump)
    }

    /**
     * scan按钮的文字颜色和大小
     */
    protected open fun getScanActionSize(): Float? {
        return null
    }

    protected open fun getBgWidth(): Float? {
        return 177f
    }

    protected open fun getBgHeight(): Float? {
        return 165f
    }

    protected open fun getBtnWidth(): Float? {
        return null
    }

    protected open fun getBtnHeight(): Float? {
        return null
    }

    protected open fun getTopImgWidth(): Float? {
        return 37f
    }

    protected open fun getTopImgHeight(): Float? {
        return 45f
    }

    protected open fun getScanActionNormalColor(): Int? {
        return null
    }

    protected open fun getScanActionWarnColor(): Int? {
        return null
    }

    //主背景view
    protected open fun getHeaderView(): View? {
        return null
    }

    /**
     * 垃圾的文字大小
     */
    protected open fun setJunkSize(): Float? {
        return null
    }

    protected open fun setJunkUnitSize(): Float? {
        return null
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