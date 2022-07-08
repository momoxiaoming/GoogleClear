package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AccountAdapterItemBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountTypeInfo

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter
 * @data  2022/4/12 17:48
 */
class AccountPayTypeAdapter:BaseQuickAdapter<AccountTypeInfo,BaseDataBindingHolder<AccountAdapterItemBinding>>(R.layout.account_adapter_item) {
    override fun convert(
        holder: BaseDataBindingHolder<AccountAdapterItemBinding>,
        item: AccountTypeInfo
    ) {
        holder.dataBinding?.apply {
            accountRecordIcon.setImageResource(item.payIcon)
            accountRecordName.text = item.payType
        }
    }
}