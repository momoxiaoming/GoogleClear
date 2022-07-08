package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.NetworkAdapterItemBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NetAppInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.CheckNetWorkHelper

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter
 * @data  2022/4/11 16:18
 */
class NetworkAdapter:BaseQuickAdapter<NetAppInfo,BaseDataBindingHolder<NetworkAdapterItemBinding>>(R.layout.network_adapter_item) {
    override fun convert(
        holder: BaseDataBindingHolder<NetworkAdapterItemBinding>,
        item: NetAppInfo
    ) {
        holder.dataBinding?.apply {
            Glide.with(context).load(item.appLogo).error(R.drawable.ic_network_app).into(networkAppIcon)
            networkAppName.text = item.appName
            networkItemUp.text = CheckNetWorkHelper.renderFileSize(item.txBytesValue)
            networkItemDown.text = CheckNetWorkHelper.renderFileSize(item.rxBytesValue)
        }
    }

}