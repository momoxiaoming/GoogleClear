package com.mckj.gallery.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mckj.gallery.bean.MediaBean
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.gallery.R
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.databinding.CleanupxItemGalleryBinding
import com.org.openlib.utils.onceClick

/**
 *@author leix
 *@version 1
 *@createTime 2021/7/20 14:22
 *@desc 图库底部Item
 */
class GalleryViewBinder : DataBindingViewBinder<MediaBean>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanupx_item_gallery, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MediaBean, CleanupxItemGalleryBinding>(itemView) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as MediaBean
                    onItemClick(it, position, item)
                }
            }
        }

        override fun bindData(t: MediaBean) {
            Glide.with(mBinding.photo)
                .asBitmap()
                .load(t.originalPath)
                .thumbnail(0.33f)
                .centerCrop()
                .into(mBinding.photo)
            if (t.mimeType == MediaConstant.MEDIA_TYPE_VIDEO) {
                mBinding.playIcon.show()
            } else {
                mBinding.playIcon.gone()
            }
        }

    }
}