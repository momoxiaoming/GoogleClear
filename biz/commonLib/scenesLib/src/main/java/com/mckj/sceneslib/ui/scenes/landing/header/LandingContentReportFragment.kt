package com.mckj.sceneslib.ui.scenes.landing.header

import android.view.View
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.helper.FingerHelper
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderReportBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import kotlin.math.roundToInt
import kotlin.math.roundToLong


class LandingContentReportFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderReportBinding, ScenesViewModel>() {

    /**
     * 手指帮助类
     */
    private val mFingerHelper by lazy { FingerHelper() }

    /**
     * 手指view
     */
    private var mFingerView: View? = null

    private val viewModel: ScenesFragmentLandingHeaderReportViewModel by lazy {
        ViewModelProvider(this).get(ScenesFragmentLandingHeaderReportViewModel::class.java)
    }

    override fun getLayoutId(): Int {
        return R.layout.scenes_fragment_landing_header_report
    }

    override fun initData() {
        //下载速度
        val speedNum = mModel.getScenesData().landingName
        val downloadNum = java.lang.Long.valueOf(speedNum.toString())

        viewModel.downloadNum = downloadNum

        viewModel.historyTopData.observe(this) {
            val uploadSpeed = FileUtil.getFileSizeNumberText(it)
            val uploadUnit = FileUtil.getFileSizeUnitText(it)
            mBinding.tvHistory.text = String.format(getString(R.string.scenes_historical_high),uploadSpeed,uploadUnit)

        }

    }


    private fun setHistorySpeedText() {
        val percentBySpeed = getPercentBySpeed(viewModel.downloadNum)
        mBinding.tvDefeat.text = String.format(getString(R.string.scenes_faster_than_x_user),percentBySpeed)
    }

    private fun getPercentBySpeed(downloadNum: Long): String {
        //将网速换算成兆字节
        val m = 1024 * 1024 * 8
        return when (downloadNum) {
            in 0..1 * m -> {
                //0-10%
                (0 + (downloadNum / m.toFloat()) * 10).roundToInt().toString()
            }
            in 1 * m + 1..4 * m -> {
                //10-20
                //转换成M
                val fm = (downloadNum - 1 * m) / m.toFloat()
                //1-4M均分+10
                (fm / 3 * 10 + 10).roundToInt().toString()
            }
            in 4 * m + 1..10 * m -> {
                //20-30
                //转换成M
                val fm = (downloadNum - 4 * m) / m.toFloat()
                (fm / 6 * 10 + 20).roundToInt().toString()
            }

            in 10 * m + 1..20 * m -> {
                //30-40
                val fm = (downloadNum - 10 * m) / m.toFloat()
                (fm / 10 * 10 + 30).roundToInt().toString()
            }
            in 20 * m + 1..50 * m -> {
                //40-50
                val fm = (downloadNum - 20 * m) / m.toFloat()
                (fm / 30 * 10 + 40).roundToInt().toString()
            }
            in 50 * m + 1..100 * m -> {
                //50-70
                val fm = (downloadNum - 50 * m) / m.toFloat()
                (fm / 25 * 10 + 50).roundToInt().toString()
            }
            in 100 * m + 1..200 * m -> {
                //70-85
                val fm = (downloadNum - 100 * m) / m.toFloat()
                (fm / 100 * 15 + 70).roundToInt().toString()
            }
            in 200 * m + 1..500 * m -> {
                //85-98
                val fm = (downloadNum - 200 * m) / m.toFloat()
                (fm / 300 * 13 + 85).roundToInt().toString()
            }
            else -> "99"
        }
    }

    private fun setCurrentSpeedText() {
        val seriesText = viewModel.getSeriesTimeStr()
        val movieText = viewModel.getMovieTimeStr()
        val musicText = viewModel.getMusicTimeStr()

        mBinding.tvSeries.text = String.format(getString(R.string.scenes_download_episode_time),seriesText)
        mBinding.tvMovie.text = String.format(getString(R.string.scenes_download_movie_time),movieText)
        mBinding.tvMusic.text = String.format(getString(R.string.scenes_download_music_time),musicText)
    }

    private fun setUploadText(downloadNum: Long) {
        //上传速度大概在20%-25%之间
        val random = Math.random() + 4
        val uploadMun = (downloadNum / random).roundToLong()
        val uploadSpeed = FileUtil.getFileSizeNumberText(uploadMun)
        val uploadUnit = FileUtil.getFileSizeUnitText(uploadMun)
        mBinding.tvUpload.text = "$uploadSpeed$uploadUnit/s"
    }

    private fun setDownLoadText(downloadNum: Long) {
        val downSpeed = FileUtil.getFileSizeNumberText(downloadNum)
        val downUnit = FileUtil.getFileSizeUnitText(downloadNum)
        mBinding.tvDownload.text = "$downSpeed$downUnit/s"
    }


    override fun initView() {
        //测速报告展示
        St.stSpeedReportShow()

        //延时
        val delayNum = mModel.getScenesData().landingDesc
        var delayText = ResourceUtil.getString(R.string.scenes_test_request_timeout)
        if ("-1" != delayNum) {
            delayText = delayNum.toString() + "ms"
        }
        mBinding.tvDelayTime.text = delayText

        setDownLoadText(viewModel.downloadNum)

        setUploadText(viewModel.downloadNum)

        setCurrentSpeedText()

        setHistorySpeedText()

        if (ScenesManager.getInstance().isRegisterWifiBody()) {
            showFooter()
        }

    }

    private fun showFooter() {
        mBinding.footerReport.visibility = View.VISIBLE
        val type = mModel.getScenes().getGuideTypes()?.get(0)
        val name = type?.let { mModel.getScenes().getNameByType(it) }
        mBinding.clItem.text = name
        mBinding.clItem.setOnClickListener {
            //网络测速_抢红包测速_点击
            if (type == ScenesType.TYPE_ENVELOPE_TEST) {
                St.stSpeedRedPacketClick()
            }

            if (type != null) {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), type)
            }
        }

        //手指动画展示
        mBinding.clItem.doOnLayout {
            mFingerHelper.remove(mFingerView)
            mFingerView = mFingerHelper.showFinger(
                mBinding.footerReport,
                mBinding.clBg,
                20, SizeUtil.dp2px(-30f), 100f
            )
        }
    }

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mFingerHelper.remove(mFingerView)
    }
}