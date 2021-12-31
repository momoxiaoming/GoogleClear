package com.mckj.module.wifi.ui.viewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiItemWifiInfoBinding
import com.mckj.openlib.helper.onceClick
import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class WifiInfoViewBinder : DataBindingViewBinder<WifiInfo>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): WifiInfoViewHolder {
        return WifiInfoViewHolder(inflater.inflate(R.layout.wifi_item_wifi_info, parent, false))
    }

    inner class WifiInfoViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<WifiInfo, WifiItemWifiInfoBinding>(itemView) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val wifiInfo = adapter.items[position] as WifiInfo
                    onItemClick(it, position, wifiInfo)
                }
            }

            mBinding.itemWifiInfoMoreIv.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val wifiInfo = adapter.items[position] as WifiInfo
                    onItemClick(it, position, wifiInfo)
                }
            }
        }

        override fun bindData(t: WifiInfo) {
            mBinding.wifiInfo = t
            val signalLevel = t.getSignalLevel()
            mBinding.itemWifiInfoIv.imageResource = if (t.isInputNeedPassword()) {
                when (signalLevel) {
                    WifiInfo.SIGNAL_LEVEL_1 -> {
                        R.drawable.wifi_icon_signal_lock_4
                    }
                    WifiInfo.SIGNAL_LEVEL_2 -> {
                        R.drawable.wifi_icon_signal_lock_3
                    }
                    WifiInfo.SIGNAL_LEVEL_3 -> {
                        R.drawable.wifi_icon_signal_lock_2
                    }
                    WifiInfo.SIGNAL_LEVEL_4 -> {
                        R.drawable.wifi_icon_signal_lock_1
                    }
                    else -> {
                        R.drawable.wifi_icon_signal_lock_0
                    }
                }
            } else {
                when (signalLevel) {
                    WifiInfo.SIGNAL_LEVEL_1 -> {
                        R.drawable.wifi_icon_signal_unlock_4
                    }
                    WifiInfo.SIGNAL_LEVEL_2 -> {
                        R.drawable.wifi_icon_signal_unlock_3
                    }
                    WifiInfo.SIGNAL_LEVEL_3 -> {
                        R.drawable.wifi_icon_signal_unlock_2
                    }
                    WifiInfo.SIGNAL_LEVEL_4 -> {
                        R.drawable.wifi_icon_signal_unlock_1
                    }
                    else -> {
                        R.drawable.wifi_icon_signal_unlock_0
                    }
                }
            }
        }
    }

}