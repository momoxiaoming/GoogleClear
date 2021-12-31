package com.mckj.module.wifi.ui.dialog.connecting

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dn.vi.app.base.app.kt.getAs
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.sceneslib.manager.network.NetworkData
import com.mckj.sceneslib.manager.network.NetworkState
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.baselib.helper.showToast
import com.mckj.module.wifi.databinding.WifiDialogConnectingBinding
import com.mckj.module.wifi.entity.*
import com.mckj.module.wifi.ui.viewBinder.ConnectingViewBinder
import com.mckj.module.wifi.utils.Log
import com.mckj.module.wifi.ui.wifi.WifiViewModel
import com.mckj.module.wifi.ui.wifi.WifiViewModelFactory

/**
 * Describe:连接wifi
 *
 * Created By yangb on 2020/10/15
 */
class ConnectingDialogFragment : LightDialogBindingFragment() {

    companion object {
        const val TAG = "ConnectingDialog"

        @JvmStatic
        fun newInstance(wifiInfo: WifiInfo, password: String): ConnectingDialogFragment {
            transportData {
                put("wifi_info", wifiInfo)
                put("password", password)
            }
            return ConnectingDialogFragment()
        }
    }

    //wifi信息
    private var mWifiInfoObservableField: WifiInfoObservableField = WifiInfoObservableField()

    //密码
    private var mPassword: String = ""

    private val mWifiModel by lazy {
        ViewModelProvider(requireActivity(), WifiViewModelFactory()).get(
            WifiViewModel::class.java
        )
    }

    private val mModel by lazy {
        ViewModelProvider(this, ConnectingViewModelFactory()).get(ConnectingViewModel::class.java)
    }

    private lateinit var mBinding: WifiDialogConnectingBinding

    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        val viewBinder = ConnectingViewBinder()
        adapter.register(ConnectingEntity::class, viewBinder)
        adapter
    }

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        transportData {
            val wifiInfo = getAs<WifiInfo>("wifi_info")
            mWifiInfoObservableField.wifiInfoField.set(wifiInfo)
            mPassword = getAs<String>("password") ?: ""
        }
        mBinding = WifiDialogConnectingBinding.inflate(inflater, container, false)
        mBinding.field = mWifiInfoObservableField
        return mBinding
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.let {
            it.setCanceledOnTouchOutside(false)
        }
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        initObserver()
        initView()
    }

    private fun initData() {
        val wifiInfo = mWifiInfoObservableField.wifiInfoField.get()
        if (wifiInfo == null) {
            showToast("WiFi信息异常")
            dismissAllowingStateLoss()
            return
        }
        mModel.connecting(this, wifiInfo, mPassword)
        mModel.connectTimeout(this, 30 * 1000)
    }

    private fun initObserver() {
        mModel.mListLiveData.observe(this, Observer {
            setAdapter(it)
        })
        mModel.mPositionLiveData.observe(this, Observer {
            mAdapter.notifyDataSetChanged()
        })
        mModel.mPercentLiveData.observe(this, Observer {
            mBinding.connectingProgress.progress = it.toInt()
        })
        NetworkData.getInstance().networkStateLiveData.observe(true, this) { state ->
            if (!state.isWifi) {
                return@observe
            }
            Log.i(TAG, "initObserver: state:$state")
            when (state.networkState) {
                NetworkState.CONNECTED -> {
                    //WiFi连接成功
                    mModel.verifyConnectWifi(this, mWifiInfoObservableField.wifiInfoField.get()!!)
                }
                NetworkState.ERROR_AUTHENTICATING -> {
                    showToast("验证失败")
                    dispatchButtonClickObserver(state.networkState.ordinal)
                    mWifiModel.removeConnect(mWifiInfoObservableField.wifiInfoField.get()!!)
                    mModel.connectFinish(this, false)
                }
                else -> {
                }
            }
        }
    }

    private fun initView() {
        val manager = LinearLayoutManager(requireContext())
        mBinding.connectingRecycler.layoutManager = manager
    }

    private fun setAdapter(list: List<ConnectingEntity>) {
        if (mBinding.connectingRecycler.adapter == null) {
            mBinding.connectingRecycler.adapter = mAdapter
        }
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }

}