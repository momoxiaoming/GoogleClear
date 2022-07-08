package com.mckj.sceneslib.ui.scenes.model.networktest

import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.R
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.util.Log
import com.mckj.baselib.entity.ResponseLiveData
import com.mckj.baselib.helper.showToast
import com.dn.vi.app.cm.utils.FileUtil
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.databinding.ScenesFragmentNetworkTestBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.task.ScenesTaskFragment
import com.org.openlib.utils.FragmentUtil
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
            navigationIcon = null
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
//                mBinding.adTips.tvTime.visibility = View.GONE
                mBinding.networkSpeedTv.text = "$speed$unit/s"
                mBinding.networkSpeedUpDescTv.text = getString(R.string.scenes_current_speed)
                setSpeedAnim(speedNum)
            } else {
                showToast(getString(R.string.scenes_net_test_error_net_error))
            }
        }
    }


    /**
     * 清理开始动画
     */
    override fun startAnim(consumer: Consumer<Boolean>) {
        St.stLevelFlashShow(mModel.enterFlag,mModel.getScenesData().name)
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
        val taskData = mNetworkModel.getTaskData(viewLifecycleOwner)
        if (taskData != null) {
            //网络测速开始
            St.stSpeedScanStart()
            mNetworkModel.isSpeedScanStart = true
            mBinding.taskLayout.show()
            FragmentUtil.show(childFragmentManager,ScenesTaskFragment() , R.id.task_layout)

            mModel.runTask(taskData){size, index, result ->
                Log.i(TAG, "runningAnim: size:$size index:$index result:$result")
                if (index >= size - 1) {
                    mModel.endTaskLottie(mBinding.networkSpeedLottie, consumer)
                    mNetworkModel.isSpeedScanStart = false
                    consumer.accept(true)
                }
            }
        } else {
            mBinding.taskLayout.gone()
            consumer.accept(true)
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
        St.stLevelFlashEnd(mModel.enterFlag,mModel.getScenesData().name)
        consumer.accept(true)
    }

    override fun preFinish(result: Boolean) {
        val speedNum = mNetworkModel.mNetworkSpeedLiveData.value?.t ?: 0L
        val speed = FileUtil.getFileSizeNumberText(speedNum)
        val unit = FileUtil.getFileSizeUnitText(speedNum)
        mModel.getScenesData().landingName = "$speed$unit/s"
        mModel.getScenesData().landingDesc = String.format(getString(R.string.scenes_speed_like),mNetworkModel.getSpeedName(speedNum))

        //覆盖原属性
        //下载速度
        mModel.getScenesData().landingName = speedNum.toString()
//        //延时
        mModel.getScenesData().landingDesc = mNetworkModel.hostDelay.value.toString()
        mConsumer?.accept(result)
    }

    override fun onDestroyView() {
        //界面被销毁，并且处于测速状态，那么很有可能是点击了返回键
        if (mNetworkModel.isSpeedScanStart){
            St.stSpeedScanReturnClick()
        }
        super.onDestroyView()
    }

}