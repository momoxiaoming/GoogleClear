package com.mckj.gallery.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.gallery.R
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.databinding.CleanupxItemGalleryPreviewBinding
import com.mckj.gallery.datebase.entity.RecycledBean

/**
 *@author leix
 *@version 1
 *@createTime 2021/7/20 14:22
 *@desc 图库大图Item
 */
class RecycledPreviewViewBinder : DataBindingViewBinder<RecycledBean>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanupx_item_gallery_preview, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<RecycledBean, CleanupxItemGalleryPreviewBinding>(
            itemView
        ) {

        init {
//            itemView.onceClick {
//                itemClickListener?.apply {
//                    val position = layoutPosition
//                    val item = adapter.items[position] as RecycledBean
//                    onItemClick(it, position, item)
//                }
//            }
        }

        override fun bindData(t: RecycledBean) {
            Glide.with(mBinding.photo)
                .load(t.originalPath)
                .thumbnail(0.33f)
                .centerCrop()
                .into(mBinding.photo)
            if (t.mimeType == MediaConstant.MEDIA_TYPE_VIDEO) {
                mBinding.playIcon.show()
                mBinding.playIcon.setOnClickListener {
                    itemClickListener?.apply {
                        val position = layoutPosition
                        val item = adapter.items[position] as RecycledBean
                        onItemClick(it, position, item)
                    }
                }
            } else {
                mBinding.playIcon.gone()
            }
        }
    }
}