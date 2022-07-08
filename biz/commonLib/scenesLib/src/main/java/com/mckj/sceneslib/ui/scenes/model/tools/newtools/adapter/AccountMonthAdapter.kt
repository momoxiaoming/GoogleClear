package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import android.graphics.Color
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AccountInfoAdapterItemBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.AccountInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.AccountHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.DateUtil
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.SpanStringHelper

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter
 * @data  2022/4/12 17:48
 */
class AccountMonthAdapter:BaseQuickAdapter<Pair<String,MutableList<AccountInfo>>,BaseDataBindingHolder<AccountInfoAdapterItemBinding>>(R.layout.account_info_adapter_item) {
    override fun convert(
        holder: BaseDataBindingHolder<AccountInfoAdapterItemBinding>,
        item:Pair<String,MutableList<AccountInfo>>
    ) {
        holder.dataBinding?.apply {
            val sum = AccountHelper.sumMoney(item.second)
            accountDayMoney.text = "支出：${sum.toDouble()}"

            val week = DateUtil.getWeekOfStamp(item.second[0].payTimeDate)
            val content = SpanStringHelper()
                .setContent("${item.first}  $week")
                .setTextSizeSpan(13, week)
                .setForeColorSpan(Color.parseColor("#999999"),week)
                .build()
            accountDate.text =content


            accountMonthContainer.layoutManager = LinearLayoutManager(context)
            val accountDayAdapter = AccountDayAdapter()
            accountMonthContainer.adapter =accountDayAdapter
            val list = item.second
            accountDayAdapter.setList(list)
            if (list.size>0)   accountDayAdapter.setGonePosition(list.size-1)


        }
    }

}