package com.mckj.module.wifi.ui.viewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dn.vi.dex.utils.StringHexConfig
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiItemDialogMenuBinding
import com.mckj.module.wifi.entity.WifiMenuItem
import com.mckj.openlib.helper.onceClick
import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class DialogMenuViewBinder : DataBindingViewBinder<WifiMenuItem>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): DialogWifiMenuViewHolder {
        return DialogWifiMenuViewHolder(
            inflater.inflate(
                R.layout.wifi_item_dialog_menu,
                parent,
                false
            )
        )
    }

    inner class DialogWifiMenuViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<WifiMenuItem, WifiItemDialogMenuBinding>(
            itemView
        ) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val scanResult = adapter.items[position] as WifiMenuItem
                    onItemClick(it, position, scanResult)
                }
            }
        }

        override fun bindData(t: WifiMenuItem) {
            when (t.type) {
                WifiMenuItem.TYPE_NETWORK_TEST -> {
                    mBinding.itemDialogMenuIv.imageResource = R.drawable.wifi_icon_pop_speed_test
                    mBinding.itemDialogMenuNameTv.text = "网络测速"
                }
                WifiMenuItem.TYPE_SECURITY_CHECK -> {
                    mBinding.itemDialogMenuIv.imageResource = R.drawable.wifi_icon_pop_security
                    mBinding.itemDialogMenuNameTv.text = "安全检测"
                }
                WifiMenuItem.TYPE_NETWORK_SIGNAL_BOOST -> {
                    mBinding.itemDialogMenuIv.imageResource = R.drawable.wifi_icon_pop_signal_boost
                    mBinding.itemDialogMenuNameTv.text = "信号增强"
                }
                WifiMenuItem.TYPE_NETWORK_SPEED_UP -> {
                    mBinding.itemDialogMenuIv.imageResource = R.drawable.wifi_icon_pop_speed_up
                    mBinding.itemDialogMenuNameTv.text = StringHexConfig.cstr_33
                }
                WifiMenuItem.TYPE_HOTSPOT_INFO -> {
                    mBinding.itemDialogMenuIv.imageResource = R.drawable.wifi_icon_pop_hotspot_info
                    mBinding.itemDialogMenuNameTv.text = "热点信息"
                }
                WifiMenuItem.TYPE_REPORT_FISHING -> {
                    mBinding.itemDialogMenuIv.imageResource =
                        R.drawable.wifi_icon_pop_report_fishing
                    mBinding.itemDialogMenuNameTv.text = "举报钓鱼"
                }
                WifiMenuItem.TYPE_NETWORK_DISCONNECT -> {
                    mBinding.itemDialogMenuIv.imageResource = R.drawable.wifi_icon_pop_disconnect
                    mBinding.itemDialogMenuNameTv.text = "断开网络"
                }
                WifiMenuItem.TYPE_PWD_CONNECT -> {
                    mBinding.itemDialogMenuIv.imageResource =
                        R.drawable.wifi_icon_pop_password_connect
                    mBinding.itemDialogMenuNameTv.text = "密码连接"
                }
            }
        }

    }

}