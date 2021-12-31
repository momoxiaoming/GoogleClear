package com.mckj.module.wifi.ui.wifiList

import android.os.Build
import android.view.View
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.dex.utils.StringHexConfig
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.sceneslib.manager.network.WifiState
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.LocationUtil
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.WifiUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiFragmentWifiListBinding
import com.mckj.module.wifi.helper.WifiMenuHelper
import com.mckj.module.wifi.ui.dialog.MenuDialogFragment
import com.mckj.module.wifi.ui.viewBinder.WifiInfoViewBinder
import com.mckj.module.wifi.ui.viewBinder.WifiStateViewBinder
import com.mckj.module.wifi.utils.Log
import com.mckj.module.wifi.ui.wifi.WifiViewModel
import com.mckj.module.wifi.ui.wifi.WifiViewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Describe:wifi列表Dialog
 *
 * Created By yangb on 2021/1/12
 */
@Route(path = ARouterPath.Wifi.FRAGMENT_WIFI_LIST)
class WifiListFragment : DataBindingFragment<WifiFragmentWifiListBinding, WifiViewModel>() {

    companion object {
        const val TAG = "WifiListFragment"
    }

    private val mWifiListAdapter by lazy {
        val adapter = MultiTypeAdapter()
        val wifiInfoViewBinder = WifiInfoViewBinder()
        wifiInfoViewBinder.itemClickListener = mWifiItemClickListener
        adapter.register(WifiInfo::class, wifiInfoViewBinder)
        val wifiStateViewBinder = WifiStateViewBinder()
        wifiStateViewBinder.itemClickListener = mWifiStateItemClickListener
        adapter.register(WifiState::class, wifiStateViewBinder)
        adapter
    }
    private var mRefreshTime: Long = 0
    private var mRefreshJob: Job? = null

    override fun getLayoutId() = R.layout.wifi_fragment_wifi_list

    override fun getViewModel(): WifiViewModel {
        return ViewModelProvider(
            requireActivity(),
            WifiViewModelFactory()
        ).get(
            WifiViewModel::class.java
        )
    }

    override fun initData() {
        //显示广告
        mModel.showAd(requireActivity(), StringHexConfig.news_landing, mBinding.wifiListAdLayout)
    }

    override fun initView() {
        //状态栏字体改成黑色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window?.let {
                it.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        //初始化标题
        mBinding.headerLayout.headerLayout.setBackgroundResource(R.color.white)
        mBinding.headerLayout.headerToolbar.apply {
            title = "WiFi列表"
            setTitleTextColor(ResourceUtil.getColor(R.color.OpenColorTextBlack))
            navigationIcon = ResourceUtil.getDrawable(R.drawable.wifi_icon_back_black)
            setNavigationOnClickListener {
                mModel.isFinish.value = true
            }
        }
        mBinding.wifiListRecycler.layoutManager = LinearLayoutManager(context)
        //初始化刷新控件
        mBinding.wifiListRefresh.setColorSchemeResources(R.color.WifiColorGreen)
        mBinding.wifiListRefresh.setOnRefreshListener {
            //判断wifi是否打开
            mModel.startScan()
            //刷新广告
            //TODO 哪里放AD
            mModel.showAd(requireActivity(), StringHexConfig.news_landing, mBinding.wifiListAdLayout)
        }
    }

    override fun initObserver() {
        super.initObserver()
        val mediatorLiveData = MediatorLiveData<Boolean>()
        mediatorLiveData.addSource(NetworkData.getInstance().connectInfoLiveData) {
            mediatorLiveData.value = true
        }
        mediatorLiveData.addSource(NetworkData.getInstance().wifiInfoListLiveData) {
            mModel.mRefreshStateLiveData.value = false
            mediatorLiveData.value = true
        }
        mediatorLiveData.observe(viewLifecycleOwner) {
            if (it != true) {
                return@observe
            }
            val connectInfo: ConnectInfo? = NetworkData.getInstance().connectInfoLiveData.value
            val list: List<WifiInfo>? = NetworkData.getInstance().wifiInfoListLiveData.value
            mModel.refreshWifiInfoList(connectInfo, list)
        }
        mModel.mWifiInfoListLiveData.observe(viewLifecycleOwner, Observer {
            setAdapter(it)
        })
        mModel.mRefreshStateLiveData.observe(viewLifecycleOwner, Observer {
            mBinding.wifiListRefresh.isRefreshing = false
        })
    }

    override fun onResume() {
        super.onResume()
        //界面切换的时候刷新ui
        NetworkData.getInstance().loadConnectInfo()
        NetworkData.getInstance().loadWifiInfoList()
    }

    private fun setAdapter(list: List<Any>) {
        mRefreshJob?.cancel()
        mRefreshJob = scope.launch {
            val time = System.currentTimeMillis()
            if (time - mRefreshTime >= 500) {
                delay(500)
            } else {
                delay(0)
            }
            if (mBinding.wifiListRecycler.adapter == null) {
                mBinding.wifiListRecycler.adapter = mWifiListAdapter
            }
            mWifiListAdapter.items = list
            mWifiListAdapter.notifyDataSetChanged()
            mRefreshTime = time
        }
    }

    private val mWifiItemClickListener = object : AbstractViewBinder.OnItemClickListener<WifiInfo> {
        override fun onItemClick(view: View, position: Int, t: WifiInfo) {
            when (view.id) {
                R.id.item_wifi_info_more_iv -> {
                    Log.i(TAG, "onItemClick: DisplayMore position:$position, t:$t")
                    MenuDialogFragment.newInstance(t)
                        .rxShow(parentFragmentManager, MenuDialogFragment.TAG)
                }
                else -> {
                    Log.i(TAG, "onItemClick: other position:$position, t:$t")
                    WifiMenuHelper.showSmartConnectDialog(requireActivity(), t)
                }
            }
        }
    }

    private val mWifiStateItemClickListener =
        object : AbstractViewBinder.OnItemClickListener<WifiState> {
            override fun onItemClick(view: View, position: Int, t: WifiState) {
                Log.i(TAG, "onItemClick: t:$t")
                when (t) {
                    WifiState.TYPE_LOCATION_PERMISSION -> {
                        //开启定位权限
                        mModel.requestLocationPermission(requireActivity())
                    }
                    WifiState.TYPE_LOCATION_DISABLE -> {
                        //打开定位
                        LocationUtil.locationEnable(true)
                    }
                    WifiState.TYPE_WIFI_DISABLED,
                    WifiState.TYPE_WIFI_UNKNOWN -> {
                        //打开wifi
                        WifiUtil.setWifiEnable(true)
                    }
                    else -> {
                        WifiUtil.setWifiEnable(true)
                    }
                }
            }
        }

}