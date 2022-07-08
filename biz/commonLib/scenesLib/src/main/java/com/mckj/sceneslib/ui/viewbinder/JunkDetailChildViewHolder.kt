package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.dn.vi.app.cm.utils.FileUtil
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.CleanupItemJunkDetailChildBinding
import com.mckj.sceneslib.entity.MenuJunkChild

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class JunkDetailChildViewHolder : DataBindingViewBinder<MenuJunkChild>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_junk_detail_child, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuJunkChild, CleanupItemJunkDetailChildBinding>(
            itemView
        ), View.OnClickListener {

        init {
            mBinding.itemCheckIv.onceClick(this)
        }

        override fun bindData(t: MenuJunkChild) {
            mBinding.item = t
            mBinding.itemAvatarIv.setImageDrawable(t.iJunkEntity.icon)
            mBinding.itemNameTv.text = t.iJunkEntity.appName
            val desc = t.iJunkEntity.junkDescription?.description ?: ""
            if (desc.isNotEmpty()) {
                mBinding.itemDescTv.show()
                mBinding.itemDescTv.text = desc
            } else {
                mBinding.itemDescTv.gone()
            }
            mBinding.itemSizeTv.text = FileUtil.getFileSizeText(t.iJunkEntity.junkSize)
        }

        override fun onClick(v: View) {
            itemClickListener?.apply {
                val position = layoutPosition
                val item = adapter.items[position] as MenuJunkChild
                onItemClick(v, position, item)
            }
        }
    }
}