package com.mckj.module.wifi.ui.viewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiItemConnectingBinding
import com.mckj.module.wifi.entity.ConnectingEntity

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class ConnectingViewBinder : DataBindingViewBinder<ConnectingEntity>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): ConnectingViewHolder {
        return ConnectingViewHolder(
            inflater.inflate(
                R.layout.wifi_item_connecting,
                parent,
                false
            )
        )
    }

    inner class ConnectingViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<ConnectingEntity, WifiItemConnectingBinding>(
            itemView
        ) {

        override fun bindData(t: ConnectingEntity) {
            mBinding.entity = t
            t.position = adapterPosition
        }
    }

}