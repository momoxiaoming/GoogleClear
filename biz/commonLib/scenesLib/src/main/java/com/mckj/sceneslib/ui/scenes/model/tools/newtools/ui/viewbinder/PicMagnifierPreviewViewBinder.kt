package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mckj.gallery.bean.MediaBean
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.gallery.bean.MediaConstant

import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesCleanupxItemGalleryPreviewBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.Constants


/**
 *@author xx
 *@version 1
 *@createTime 2021/7/20 14:22
 *@desc 图库大图Item
 */
class PicMagnifierPreviewViewBinder : DataBindingViewBinder<MediaBean>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.scenes_cleanupx_item_gallery_preview, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MediaBean, ScenesCleanupxItemGalleryPreviewBinding>(
            itemView
        ) {
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
            if (!Constants.state){
                val mAttacher: PhotoViewAttacher
                mAttacher = PhotoViewAttacher(mBinding.photo)
                mAttacher.update()
                Constants.state = true
            }
            Glide.with(mBinding.photo)
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