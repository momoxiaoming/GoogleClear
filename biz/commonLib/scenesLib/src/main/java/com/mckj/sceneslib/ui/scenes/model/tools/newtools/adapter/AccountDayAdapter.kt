package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import android.view.View
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AccountDayAdapterItemBinding
import com.mckj.sceneslib.databinding.AccountInfoAdapterItemBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.AccountHelper

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter
 * @data  2022/4/12 17:48
 */
class AccountDayAdapter:BaseQuickAdapter<AccountInfo,BaseDataBindingHolder<AccountDayAdapterItemBinding>>(R.layout.account_day_adapter_item) {

    private var mPosition = -1

    fun setGonePosition(position:Int){
        mPosition = position
    }

    override fun convert(
        holder: BaseDataBindingHolder<AccountDayAdapterItemBinding>,
        item: AccountInfo
    ) {
        holder.dataBinding?.apply {
            val payTypeIcon = AccountHelper.getPayTypeIcon()
            payTypeIcon[item.payType]?.let {
                acDayIcon.setImageResource(it)
            }
            acDayType.text = item.payType
            acDayMoney.text ="-${item.payMoney.toDouble()}"
            if ( holder.adapterPosition == mPosition)  indexView.isVisible =false
        }
    }

}