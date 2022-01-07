package com.mckj.sceneslib.ui.scenes.model.networkcheck

import android.graphics.Color
import androidx.core.util.Consumer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dn.baselib.ext.showToast
import com.dn.vi.app.cm.utils.TextUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.WifiDevice
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.viewbinder.WifiDeviceViewBinder

import com.mckj.sceneslib.databinding.ScenesFragmentNetworkCheckBinding

import org.jetbrains.anko.backgroundResource

/**
 * Describe:防止蹭网
 *
 * Created By yangb on 2021/4/23
 */
class NetworkCheckFragment :
    LottieFragment<ScenesFragmentNetworkCheckBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "NetworkCheckFragment"

        fun newInstance(consumer: Consumer<Boolean>): NetworkCheckFragment {
            return NetworkCheckFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    private val mNetworkModel by lazy {
        ViewModelProvider(requireActivity(), NetworkCheckViewModelFactory()).get(
            NetworkCheckViewModel::class.java
        )
    }

    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(WifiDevice::class, WifiDeviceViewBinder())
        adapter
    }

    override fun getLayoutId() = R.layout.scenes_fragment_network_check

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), NetworkCheckViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
        super.initData()
    }

    override fun initView() {
        //初始化标题
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            setNavigationOnClickListener {
                mModel.isFinish.value = true
            }
        }
        mBinding.networkCheckRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun initObserver() {
        super.initObserver()
        mNetworkModel.mWifiDeviceListLiveData.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    override fun startAnim(consumer: Consumer<Boolean>) {
        mModel.startLottie(mBinding.networkCheckLottie, consumer)
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        mNetworkModel.mConnectInfoLiveData.observe(viewLifecycleOwner) {
            val liveData = it.t
            if (liveData != null) {
                setConnectInfo(liveData)
                _runningAnim(consumer)
            } else {
                showToast(it.msg)
                consumer.accept(false)
            }
        }
    }

    private fun _runningAnim(consumer: Consumer<Boolean>) {
        mModel.runningLottie(mBinding.networkCheckLottie, consumer)
        mNetworkModel.scanWifiDevice { result ->
            mModel.endTaskLottie(mBinding.networkCheckLottie, consumer)
        }
    }

    override fun endAnim(consumer: Consumer<Boolean>) {
        mModel.endLottie(mBinding.networkCheckLottie, consumer)
    }

    override fun preFinish(result : Boolean) {
        mConsumer?.accept(result)
    }

    /**
     * 设置连接信息
     */
    private fun setConnectInfo(connectInfo: ConnectInfo) {
        mBinding.wifiNameTv.text = "已连接:${connectInfo.name}"
    }

    private fun setAdapter(list: List<WifiDevice>) {
        if (mBinding.networkCheckRecycler.adapter == null) {
            mBinding.networkCheckRecycler.adapter = mAdapter
        }
        val count = list.size
        val content = "发现${count}台设备"
        mBinding.networkCheckCountTv.text = TextUtils.string2SpannableStringForColor(
            content, count.toString(), color = Color.parseColor("#FFFC00")
        )
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
        mBinding.networkCheckRecycler.scrollToPosition(mAdapter.itemCount - 1)
    }

}