package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemWifiDeviceBinding
import com.mckj.sceneslib.entity.WifiDevice
import com.dn.baselib.base.databinding.DataBindingViewBinder

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class WifiDeviceViewBinder : DataBindingViewBinder<WifiDevice>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_wifi_device, parent, false))
    }

    inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<WifiDevice, ScenesItemWifiDeviceBinding>(
            itemView
        ) {

        override fun bindData(t: WifiDevice) {
            mBinding.nameTv.text = t.ip
        }
    }

}