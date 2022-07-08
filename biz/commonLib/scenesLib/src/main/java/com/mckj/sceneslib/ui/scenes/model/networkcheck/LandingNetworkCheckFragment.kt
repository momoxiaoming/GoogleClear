package com.mckj.sceneslib.ui.scenes.model.networkcheck

import android.graphics.Paint
import androidx.lifecycle.ViewModelProvider
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderNetworkCheckBinding
import com.org.openlib.helper.startTitleFragment

/**
 * Describe:落地页-防止蹭网
 *
 * Created By yangb on 2021/4/23
 */
class LandingNetworkCheckFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderNetworkCheckBinding, NetworkCheckViewModel>() {

    override fun getLayoutId() = R.layout.scenes_fragment_landing_header_network_check

    override fun getViewModel(): NetworkCheckViewModel {
        return ViewModelProvider(requireActivity(), NetworkCheckViewModelFactory()).get(
            NetworkCheckViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        mBinding.contentDetailDescTv.text = "点击查看连接设备 >>"
        mBinding.contentDetailDescTv.paint.flags = Paint.UNDERLINE_TEXT_FLAG

        mBinding.contentDetailDescTv.onceClick {
            requireContext().startTitleFragment(ARouterPath.Scenes.FRAGMENT_WIFI_DEVICE_DETAIL) {
                val list = mModel.mWifiDeviceListLiveData.value
                if (list != null && list is ArrayList) {
                    it.withParcelableArrayList("list", list)
                }
            }
        }
    }

    override fun initObserver() {
        super.initObserver()
        mModel.mWifiDeviceListLiveData.observe(viewLifecycleOwner) { list ->
            mBinding.contentDetailNameTv.text = "发现${list?.size ?: 0}台设备"
        }
    }

}