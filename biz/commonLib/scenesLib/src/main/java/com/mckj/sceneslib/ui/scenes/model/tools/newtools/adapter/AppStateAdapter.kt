package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.NotifyAdapterItemAppBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AppInfo

class AppStateAdapter :
    BaseQuickAdapter<AppInfo, BaseDataBindingHolder<NotifyAdapterItemAppBinding>>(R.layout.notify_adapter_item_app) {

    init {
        addChildClickViewIds(R.id.notify_app_switch)
    }

    override fun convert(holder: BaseDataBindingHolder<NotifyAdapterItemAppBinding>, item: AppInfo) {
        holder.dataBinding?.apply {
            appData = item
        }
    }
}