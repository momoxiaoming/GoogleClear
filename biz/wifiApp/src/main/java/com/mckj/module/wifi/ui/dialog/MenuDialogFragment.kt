package com.mckj.module.wifi.ui.dialog

import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.dn.vi.app.base.app.UI
import com.dn.vi.app.base.app.kt.getAs
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.helper.showToast
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiDialogMenuBinding
import com.mckj.module.wifi.entity.WifiMenuItem
import com.mckj.module.wifi.gen.St
import com.mckj.module.wifi.ui.viewBinder.DialogMenuViewBinder
import com.mckj.module.wifi.ui.wifi.WifiViewModel
import com.mckj.module.wifi.ui.wifi.WifiViewModelFactory
import com.mckj.openlib.helper.onceClick

/**
 * Describe:WifiMenuDialogFragment
 *
 * Created By yangb on 2020/10/15
 */
class MenuDialogFragment : LightDialogBindingFragment() {

    companion object {
        const val TAG = "WifiMenuDialogFragment"

        @JvmStatic
        fun newInstance(wifiInfo: WifiInfo): MenuDialogFragment {
            transportData {
                put("wifi_info", wifiInfo)
            }
            return MenuDialogFragment()
        }
    }

    override val gravity: Int
        get() = Gravity.BOTTOM

    override val dialogWindowWidth: Int
        get() = UI.screenWidth

    private lateinit var mBinding: WifiDialogMenuBinding
    private val mModel by lazy {
        ViewModelProvider(
            requireActivity(),
            WifiViewModelFactory()
        ).get(WifiViewModel::class.java)
    }
    var mWifiInfo: WifiInfo? = null

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        mBinding = WifiDialogMenuBinding.inflate(inflater, container, false)
        return mBinding
    }

    override fun onDialogWindowCreated(window: Window) {
        super.onDialogWindowCreated(window)
        window.setWindowAnimations(R.style.WifiDialogBottom2Top)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        St.stWifilistGetpopShow()
        transportData {
            mWifiInfo = getAs<WifiInfo>("wifi_info")
        }
        if (mWifiInfo == null) {
            dismissAllowingStateLoss()
            return
        }
        mBinding.info = mWifiInfo
        val manager = GridLayoutManager(context, 4)
        mBinding.menuRecycler.layoutManager = manager
        mBinding.menuCancelTv.onceClick {
            St.stWifilistGetpopCloseClick()
            dismiss()
        }
        loadMenu()
    }

    private val mWifiMenuAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        val wifiMenuViewBinder = DialogMenuViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<WifiMenuItem> {
                override fun onItemClick(view: View, position: Int, t: WifiMenuItem) {
                    val wifiInfo = mWifiInfo
                    if (wifiInfo == null) {
                        showToast("WiFi信息异常")
                        return
                    }
                    val result = mModel.menuClickListener(requireActivity(), t, wifiInfo)
                    if (result) {
                        dismissAllowingStateLoss()
                    }
                }
            }
        }
        adapter.register(WifiMenuItem::class, wifiMenuViewBinder)
        adapter
    }


    private fun loadMenu() {
        val list = mutableListOf<WifiMenuItem>()
        if (mWifiInfo?.isConnect == true) {
            list.add(WifiMenuItem(WifiMenuItem.TYPE_NETWORK_TEST))
            list.add(WifiMenuItem(WifiMenuItem.TYPE_SECURITY_CHECK))
            list.add(WifiMenuItem(WifiMenuItem.TYPE_NETWORK_SIGNAL_BOOST))
            list.add(WifiMenuItem(WifiMenuItem.TYPE_NETWORK_SPEED_UP))
            list.add(WifiMenuItem(WifiMenuItem.TYPE_HOTSPOT_INFO))
//            list.add(WifiMenuItem(WifiMenuItem.TYPE_REPORT_FISHING))
            list.add(WifiMenuItem(WifiMenuItem.TYPE_NETWORK_DISCONNECT))
        } else {
            list.add(WifiMenuItem(WifiMenuItem.TYPE_PWD_CONNECT))
            list.add(WifiMenuItem(WifiMenuItem.TYPE_SECURITY_CHECK))
            list.add(WifiMenuItem(WifiMenuItem.TYPE_HOTSPOT_INFO))
            //去掉举报钓鱼，因为在部分机型上举报失败
//            list.add(WifiMenuItem(WifiMenuItem.TYPE_REPORT_FISHING))
        }
//        if (wifiInfo?.isExists == true) {
//            list.add(MenuItem(MenuItem.TYPE_NETWORK_FORGET))
//        }
        setWifiMenuAdapter(list)
    }

    private fun setWifiMenuAdapter(list: List<WifiMenuItem>) {
        if (mBinding.menuRecycler.adapter == null) {
            mBinding.menuRecycler.adapter = mWifiMenuAdapter
        }
        mWifiMenuAdapter.items = list
        mWifiMenuAdapter.notifyDataSetChanged()
    }

}