package com.mckj.vest.defaultwifi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.dex.utils.StringHexConfig
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.LocationUtil
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.baselib.util.WifiUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.datalib.helper.FingerHelper
import com.mckj.module.wifi.gen.St
import com.mckj.module.wifi.ui.wifi.WifiViewModel
import com.mckj.module.wifi.ui.wifi.WifiViewModelFactory
import com.mckj.module.wifi.utils.Log
import com.mckj.openlib.helper.onceClick
import com.mckj.openlib.helper.startFragment
import com.mckj.sceneslib.manager.network.ConnectInfo
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.vest.defaultwifi.R
import com.mckj.vest.defaultwifi.databinding.WifiFragmentWifiHeaderBinding
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.backgroundResource
/**
 * Describe:
 *
 * Created By yangb on 2021/3/19
 */
open class HomeHeaderFragment :
    DataBindingFragment<WifiFragmentWifiHeaderBinding, WifiViewModel>() {

    /**
     * 手指帮助类
     */
    private val mFingerHelper by lazy { FingerHelper() }

    /**
     * 手指view
     */
    private var mFingerView: View? = null

    override fun onCreateRootView(inflater: LayoutInflater, container: ViewGroup?): View? {
        val view = super.onCreateRootView(inflater, container)
        mBinding.titleLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.titleLayout.headerToolbar.apply {
            navigationIcon = null
            title = ResourceUtil.getText(R.string.app_name)
        }
        return view
    }

    override fun getLayoutId() = R.layout.wifi_fragment_wifi_header

    override fun getViewModel(): WifiViewModel {
        return ViewModelProvider(requireActivity(), WifiViewModelFactory()).get(
            WifiViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        mBinding.smartBtn.onceClick {
            mModel.smartClickListener(requireActivity())
        }

        mBinding.smartBtn.doOnLayout {
            mFingerHelper.remove(mFingerView)
            mFingerView = mFingerHelper.showFinger(
                mBinding.rootLayout,
                mBinding.smartBtn,
                SizeUtil.dp2px(48f),
                SizeUtil.dp2px(12f)
            )
        }
        mBinding.titleSwitchLayout.onceClick {
            St.stHomeWifiSwitchClick()
            requireActivity().startFragment(ARouterPath.Wifi.FRAGMENT_WIFI_LIST)
        }
    }

    override fun initObserver() {
        super.initObserver()
        //监听在线配置更新
        NetworkData.getInstance().connectInfoLiveData.observe(viewLifecycleOwner, Observer {
            Log.i(TAG, "initObserver: mConnectInfoLiveData:it:$it")
            updateConnectView(it)
        })
    }

    override fun onResume() {
        super.onResume()
    }

    /**
     * 更新网络连接信息
     *
     * 判断定位权限->判断定位开关->判断wifi开关->判断有无网络
     */
    private fun updateConnectView(connectInfo: ConnectInfo) {
        when (connectInfo.networkType) {
            ConnectInfo.NetworkType.NOT_CONNECTED -> {
                //无网络
                setStatusByNotConnected(connectInfo)
            }
            ConnectInfo.NetworkType.WIFI -> {
                //Wifi
                setStatusByWifi(connectInfo)
            }
            ConnectInfo.NetworkType.MOBILE_2G,
            ConnectInfo.NetworkType.MOBILE_3G,
            ConnectInfo.NetworkType.MOBILE_4G,
            ConnectInfo.NetworkType.MOBILE_5G -> {
                //移动网络
                setStatusByMobile(connectInfo)
            }
            else -> {
                setStatusByOther(connectInfo)
            }
        }
        /**
         * 判断定位权限
         */
        if (!LocationUtil.isLocationPermission()) {
            mBinding.contentNetworkDescTv.text = "请开启定位权限"
            mBinding.smartBtn.text = "立即开启"
            return
        }
        /**
         * 判断定位开关
         */
        if (!LocationUtil.isLocationEnable()) {
            mBinding.contentNetworkDescTv.text = "请开启定位开关"
            mBinding.smartBtn.text = "立即开启"
            return
        }
        /**
         * 判断wifi开关
         */
        if (!WifiUtil.isWifiEnable()) {
            mBinding.contentNetworkDescTv.text = "请开启WiFi开关"
            mBinding.smartBtn.text = "立即开启"
            return
        }
        if (connectInfo.networkType == ConnectInfo.NetworkType.NOT_CONNECTED) {
            //网络未连接
            mBinding.contentNetworkDescTv.text = "请连接WiFi"
            mBinding.smartBtn.text = "立即连接"
        } else {
            mBinding.contentNetworkDescTv.text = "网速较慢,建议加速"
            mBinding.smartBtn.text = StringHexConfig.cstr_33
        }
    }

    private fun setStatusByNotConnected(connectInfo: ConnectInfo) {
        mBinding.contentNetworkMaskLottie.cancelAnimation()
        mBinding.contentNetworkMaskLottie.gone()
        mBinding.contentNetworkMaskIv.show()

        mBinding.contentNetworkMaskIv.imageResource = R.drawable.wifi_icon_wifi_not_connected
        mBinding.contentNetworkNameTv.text = "当前无网络"
        mBinding.contentNetworkDescTv.text = "请打开网络"
    }

    private fun setStatusByWifi(connectInfo: ConnectInfo) {
        mBinding.contentNetworkMaskLottie.show()
        mBinding.contentNetworkMaskIv.gone()
        mBinding.contentNetworkMaskLottie.playAnimation()
        mBinding.contentNetworkNameTv.text = connectInfo.name

    }

    private fun setStatusByMobile(connectInfo: ConnectInfo) {
        mBinding.contentNetworkMaskLottie.cancelAnimation()
        mBinding.contentNetworkMaskLottie.gone()
        mBinding.contentNetworkMaskIv.show()

        mBinding.contentNetworkMaskIv.imageResource = R.drawable.wifi_icon_connect_mobile
        mBinding.contentNetworkNameTv.text = when (connectInfo.networkType) {
            ConnectInfo.NetworkType.MOBILE_2G -> {
                "当前2G网络"
            }
            ConnectInfo.NetworkType.MOBILE_3G -> {
                "当前3G网络"
            }
            ConnectInfo.NetworkType.MOBILE_4G -> {
                "当前4G网络"
            }
            ConnectInfo.NetworkType.MOBILE_5G -> {
                "当前5G网络"
            }
            else -> {
                "当前移动网络"
            }
        }
    }

    private fun setStatusByOther(connectInfo: ConnectInfo) {
        mBinding.contentNetworkMaskLottie.cancelAnimation()
        mBinding.contentNetworkMaskLottie.gone()
        mBinding.contentNetworkMaskIv.show()

        mBinding.contentNetworkMaskIv.imageResource = R.drawable.wifi_icon_connect_wifi
        mBinding.contentNetworkNameTv.text = "未知网络"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mFingerHelper.remove(mFingerView)
    }

}