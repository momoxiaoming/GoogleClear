package com.mckj.module.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.bean.TxMenuItem
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupItemMenuBinding
import com.org.openlib.utils.onceClick
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.backgroundResource


/**
 * @author leix
 * @version 1
 * @createTime 2021/9/2 11:38
 * @desc
 */
class TxMenuViewBinder : DataBindingViewBinder<TxMenuItem>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_menu, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<TxMenuItem, CleanupItemMenuBinding>(
            itemView
        ) {
        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as TxMenuItem
                    onItemClick(it, position, item)
                }
            }
        }

        override fun bindData(t: TxMenuItem) {
            mBinding.menuIcon.setBackgroundResource(t.iconRes)
            if (t.appJunk == null || t.appJunk?.junkSize == 0L) {
                cleanStatus(t)
            } else {
                junkStatus(t)
            }
            mBinding.menuDesc.text = t.desc
        }

        private fun cleanStatus(t: TxMenuItem) {
            mBinding.menuTitle.text = t.title
            mBinding.menuAction.text = "很干净"
            mBinding.menuAction.background = null
            itemView.isClickable = false
        }

        private fun junkStatus(t: TxMenuItem) {
            t.appJunk?.let {
                mBinding.menuTitle.text =
                    "${t.title}   ${FileUtil.getFileSizeNumberText(it.junkSize)}${
                        FileUtil.getFileSizeUnitText(it.junkSize)
                    }"
            }
            itemView.isClickable = true
            mBinding.menuAction.text = "去清理"
            mBinding.menuAction.backgroundResource = R.drawable.cleanup_shape_btn_sel
        }
    }
}