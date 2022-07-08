package com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.NotifyAdapterItemContentBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.NotifyInfo
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.MultipleAdapterHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.notify.AppStateProvider

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter
 * @data  2022/3/4 18:24
 */
class NotifyAdapter:BaseQuickAdapter<NotifyInfo,BaseDataBindingHolder<NotifyAdapterItemContentBinding>>(
    R.layout.notify_adapter_item_content), MultipleAdapterHelper.IAdapterHelper<NotifyInfo> {

    private val multipleAdapterHelper by lazy { MultipleAdapterHelper(this) }

    private var clickAction:(Int)->Unit= {}


    fun setAdapterClickAction (action:(Int)->Unit){
        clickAction=action
    }

    override fun convert(
        holder: BaseDataBindingHolder<NotifyAdapterItemContentBinding>,
        item: NotifyInfo
    ) {
        holder.dataBinding?.apply {
            notifyContentTitle.text=item.title
            notifyContentIcon.setImageDrawable(AppStateProvider.getIconAppMap()[item.packageName])
            notifyContentHint.text=item.contentText
            multipleAdapterHelper.changeViewState(item, doContains = {
                notifyContentSelect.setImageResource(R.drawable.ic_notify_select)
            }, doRemoved = {
                notifyContentSelect.setImageResource(R.drawable.ic_notify_normal)
            })

            multipleAdapterHelper.notifySelectView(notifyItemContainer, item) {
                clickAction(it)
            }
        }
    }

    override fun selectAll() {
       multipleAdapterHelper.selectAll()
    }

    override fun removeSelectAll() {
        multipleAdapterHelper.removeSelectAll()
    }

    override fun getSrcList(): List<NotifyInfo> = data

    override fun notifyAdapter() {
        notifyDataSetChanged()
    }

    override fun notifyAdapter(position: Int) {
        notifyItemChanged(position)
    }

    override fun getSelectList(): List<NotifyInfo> =multipleAdapterHelper.selectList


    override fun removePartSelect(list: List<NotifyInfo>) {
        data.removeAll(list)
        multipleAdapterHelper.removePartSelect(list)
        notifyDataSetChanged()
    }


}