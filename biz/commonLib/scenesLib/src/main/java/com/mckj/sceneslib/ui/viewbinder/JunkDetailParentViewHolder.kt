package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.sceneslib.entity.MenuJunkParent
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.CleanupItemJunkDetailParentBinding

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class JunkDetailParentViewHolder : DataBindingViewBinder<MenuJunkParent>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_junk_detail_parent, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuJunkParent, CleanupItemJunkDetailParentBinding>(
            itemView), View.OnClickListener {

        init {
            itemView.onceClick(this)
            mBinding.itemCheckIv.onceClick(this)
        }

        override fun bindData(t: MenuJunkParent) {
            mBinding.item = t
            mBinding.itemSizeTv.text = FileUtil.getFileSizeText(t.size)
        }

        override fun onClick(v: View) {
            itemClickListener?.apply {
                val position = layoutPosition
                val item = adapter.items[position] as MenuJunkParent
                onItemClick(v, position, item)
            }
        }
    }
}