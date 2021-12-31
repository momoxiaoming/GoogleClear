package com.mckj.module.wifi.ui.viewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiItemDetailBinding
import com.mckj.module.wifi.entity.WifiDetailEntity

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class WifiDetailViewBinder : DataBindingViewBinder<WifiDetailEntity>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): WifiDetailViewHolder {
        return WifiDetailViewHolder(inflater.inflate(R.layout.wifi_item_detail, parent, false))
    }

    inner class WifiDetailViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<WifiDetailEntity, WifiItemDetailBinding>(
            itemView
        ) {

        override fun bindData(t: WifiDetailEntity) {
            t.position = adapterPosition
            mBinding.entity = t
        }
    }

}