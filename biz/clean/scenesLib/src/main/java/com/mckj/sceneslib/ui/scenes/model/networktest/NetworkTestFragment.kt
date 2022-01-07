package com.mckj.sceneslib.ui.scenes.model.networktest

import androidx.lifecycle.ViewModelProvider
import androidx.core.util.Consumer
import com.dn.baselib.entity.ResponseLiveData
import com.dn.baselib.ext.showToast
import com.mckj.sceneslib.R
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.util.Log

import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.sceneslib.databinding.ScenesFragmentNetworkTestBinding
import org.jetbrains.anko.backgroundResource

/**
 * Describe:网络测速
 *
 * Created By yangb on 2021/3/4
 */
class NetworkTestFragment :
    LottieFragment<ScenesFragmentNetworkTestBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "NetworkTestFragment"

        fun newInstance(consumer: Consumer<Boolean>): NetworkTestFragment {
            return NetworkTestFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    private val mNetworkModel by lazy {
        ViewModelProvider(this, NetworkTestViewModelFactory()).get(
            NetworkTestViewModel::class.java
        )
    }

    override fun getLayoutId() = R.layout.scenes_fragment_network_test

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
        super.initData()
        NetworkData.getInstance().loadConnectInfo()
    }

    override fun initView() {
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        NetworkData.getInstance().connectInfoLiveData.observe(viewLifecycleOwner) { connect ->
            mBinding.networkNameTv.text = connect?.name ?: ""
        }

        mNetworkModel.mNetworkSpeedLiveData.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            if (it.code == ResponseLiveData.SUCCESS) {
                val speedNum = it.t ?: 0L
                val speed = FileUtil.getFileSizeNumberText(speedNum)
                val unit = FileUtil.getFileSizeUnitText(speedNum)
                mBinding.networkSpeedTv.text = "$speed$unit/s"
                setSpeedAnim(speedNum)
            } else {
                showToast("网络加速失败:网络异常")
            }
        }
    }

    /**
     * 清理开始动画
     */
    override fun startAnim(consumer: Consumer<Boolean>) {
        mModel.startLottie(mBinding.networkSpeedLottie, consumer)
    }

    /**
     * 清理中动画
     */
    override fun runningAnim(consumer: Consumer<Boolean>) {
        //初始化一遍lottie
        mBinding.networkSpeedLottie.apply {
            cancelAnimation()
            setMinAndMaxFrame(0, 69)
        }
        mNetworkModel.testNetworkSpeed(viewLifecycleOwner) {
            consumer.accept(it)
        }
    }

    /**
     * min 33
     * max 52
     */
    private fun setSpeedAnim(speedSize: Long) {
        val minFrame = 33
        val maxFrame = 52
        val totalFrame = 69
        //最高速度 25 M/s
        val max = 25 * 1024 * 1024
        val percent: Float = if (speedSize > max) {
            1f
        } else {
            speedSize * 1f / max
        }
        val frame: Float = (maxFrame - minFrame) * percent + minFrame
        Log.i(TAG, "setSpeedAnim: frame:$frame progress:${frame / totalFrame}")
        mBinding.networkSpeedLottie.apply {
            progress = frame / totalFrame
        }
    }

    /**
     * 清理结束动画
     */
    override fun endAnim(consumer: Consumer<Boolean>) {
        consumer.accept(true)
    }

    override fun preFinish(result : Boolean) {
        val speedNum = mNetworkModel.mNetworkSpeedLiveData.value?.t ?: 0L
        val speed = FileUtil.getFileSizeNumberText(speedNum)
        val unit = FileUtil.getFileSizeUnitText(speedNum)
        mModel.getScenesData().landingName = "$speed$unit/s"
        mModel.getScenesData().landingDesc = "${mNetworkModel.getSpeedName(speedNum)}"
        mConsumer?.accept(result)
    }

}