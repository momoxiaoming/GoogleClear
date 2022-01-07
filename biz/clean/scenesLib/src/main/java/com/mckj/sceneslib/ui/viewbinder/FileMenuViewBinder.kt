package com.mckj.sceneslib.ui.viewbinder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.baselib.base.databinding.DataBindingViewBinder
import com.dn.baselib.ext.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemFileMenuBinding
import com.mckj.sceneslib.entity.FileMenuItem
import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class FileMenuViewBinder : DataBindingViewBinder<FileMenuItem>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_item_file_menu, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<FileMenuItem, ScenesItemFileMenuBinding>(
            itemView
        ) {
        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as FileMenuItem
                    onItemClick(it, position, item)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bindData(t: FileMenuItem) {
            mBinding.itemDescIv.text = t.desc
            mBinding.itemTitleIv.text = t.title
            mBinding.itemIconIv.imageResource = t.iconRes
        }
    }
}