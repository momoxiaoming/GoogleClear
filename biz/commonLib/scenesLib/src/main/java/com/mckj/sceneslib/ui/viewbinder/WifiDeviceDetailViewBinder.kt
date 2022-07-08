package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemWifiDeviceDetailBinding
import com.mckj.sceneslib.entity.WifiDevice
import com.mckj.baselib.base.databinding.DataBindingViewBinder

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class WifiDeviceDetailViewBinder : DataBindingViewBinder<WifiDevice>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_wifi_device_detail, parent, false))
    }

    inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<WifiDevice, ScenesItemWifiDeviceDetailBinding>(
            itemView
        ) {

        override fun bindData(t: WifiDevice) {
            mBinding.nameTv.text = t.ip
        }
    }

}