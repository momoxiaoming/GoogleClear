package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AccountYearAdapterItemBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountYearInfo

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter
 * @data  2022/4/12 17:48
 */
class AccountYearAdapter:BaseQuickAdapter<AccountYearInfo,BaseDataBindingHolder<AccountYearAdapterItemBinding>>(R.layout.account_year_adapter_item) {
    override fun convert(
        holder: BaseDataBindingHolder<AccountYearAdapterItemBinding>,
        item: AccountYearInfo
    ) {
        holder.dataBinding?.apply {
             acMonthDate.text = item.accountDate
            acMonthMoney.text= "支出：${item.accountMoney}"
        }
    }

}