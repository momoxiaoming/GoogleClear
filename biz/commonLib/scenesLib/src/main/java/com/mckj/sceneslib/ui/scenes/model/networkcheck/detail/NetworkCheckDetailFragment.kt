package com.mckj.sceneslib.ui.scenes.model.networkcheck.detail

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.WifiDevice
import com.mckj.sceneslib.ui.viewbinder.WifiDeviceDetailViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.datalib.entity.ARouterPath
import com.mckj.datalib.ui.EmptyViewModel
import com.mckj.datalib.ui.EmptyViewModelFactory
import com.mckj.sceneslib.databinding.ScenesFragmentNetworkCheckDetailBinding

/**
 * Describe:
 *
 *
 * Created By yangb on 2021/4/25
 */
@Route(path = ARouterPath.Scenes.FRAGMENT_WIFI_DEVICE_DETAIL)
class NetworkCheckDetailFragment :
    DataBindingFragment<ScenesFragmentNetworkCheckDetailBinding, EmptyViewModel>() {

    companion object {
        const val TAG = "NetworkCheckDetailFragment"
    }

    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(WifiDevice::class, WifiDeviceDetailViewBinder())
        adapter
    }

    override fun getLayoutId() = R.layout.scenes_fragment_network_check_detail

    override fun getViewModel(): EmptyViewModel {
        return ViewModelProvider(requireActivity(), EmptyViewModelFactory()).get(
            EmptyViewModel::class.java
        )
    }

    override fun initData() {
        val list: List<WifiDevice>? = arguments?.getParcelableArrayList("list")
        if (list != null) {
            setAdapter(list)
        }
    }

    override fun initView() {
        //初始化标题
        activity?.title = "连接设备"
        mBinding.detailRecycler.layoutManager = LinearLayoutManager(context)
    }

    private fun setAdapter(list: List<WifiDevice>) {
        if (mBinding.detailRecycler.adapter == null) {
            mBinding.detailRecycler.adapter = mAdapter
        }
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }

}