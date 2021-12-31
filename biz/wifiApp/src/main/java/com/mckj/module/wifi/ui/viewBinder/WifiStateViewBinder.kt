package com.mckj.module.wifi.ui.viewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.sceneslib.manager.network.WifiState
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiItemWifiStateBinding
import com.mckj.openlib.helper.onceClick

/**
 * Describe:
 *
 * Created By yangb on 2020/10/20
 */
class WifiStateViewBinder : DataBindingViewBinder<WifiState>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return WifiStateViewHolder(inflater.inflate(R.layout.wifi_item_wifi_state, parent, false))
    }

    inner class WifiStateViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<WifiState, WifiItemWifiStateBinding>(itemView) {

        init {
            mBinding.wifiStateBtn.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val state = adapter.items[position] as WifiState
                    onItemClick(it, position, state)
                }
            }
        }

        override fun bindData(t: WifiState) {
            when (t) {
                WifiState.TYPE_SCAN_EMPTY -> {
                    mBinding.wifiStateTv.text = "附近没有WiFi可连接"
                    mBinding.wifiStateBtn.visibility = View.GONE
                }
                WifiState.TYPE_WIFI_ENABLED -> {
                    mBinding.wifiStateTv.text = "WiFi已打开"
                    mBinding.wifiStateBtn.visibility = View.GONE
                }
                WifiState.TYPE_WIFI_ENABLING -> {
                    mBinding.wifiStateTv.text = "WiFi打开中"
                    mBinding.wifiStateBtn.visibility = View.GONE
                }
                WifiState.TYPE_WIFI_DISABLED -> {
                    mBinding.wifiStateTv.text = "WiFi开关未开启"
                    mBinding.wifiStateBtn.text = "立即开启"
                    mBinding.wifiStateBtn.visibility = View.VISIBLE
                }
                WifiState.TYPE_WIFI_DISABLING -> {
                    mBinding.wifiStateTv.text = "WiFi关闭中"
                    mBinding.wifiStateBtn.visibility = View.GONE
                }
                WifiState.TYPE_LOCATION_DISABLE -> {
                    mBinding.wifiStateTv.text = "定位未打开"
                    mBinding.wifiStateBtn.text = "立即开启"
                    mBinding.wifiStateBtn.visibility = View.VISIBLE
                }
                WifiState.TYPE_LOCATION_PERMISSION -> {
                    mBinding.wifiStateTv.text = "定位权限已拒绝"
                    mBinding.wifiStateBtn.text = "立即开启"
                    mBinding.wifiStateBtn.visibility = View.VISIBLE
                }
                else -> {
                    mBinding.wifiStateTv.text = "WiFi未知状态"
                    mBinding.wifiStateBtn.visibility = View.GONE
                }
            }
        }
    }

}