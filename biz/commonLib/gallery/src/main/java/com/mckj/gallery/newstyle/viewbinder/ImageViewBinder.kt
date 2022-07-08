package com.mckj.gallery.newstyle.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.SizeUtil
import com.mckj.gallery.R
import com.mckj.gallery.bean.MediaBean
import com.mckj.gallery.databinding.CleanupxItemImageGalleryBinding
import kotlin.math.round

class ImageViewBinder: DataBindingViewBinder<MediaBean>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ImageViewHolder(inflater.inflate(R.layout.cleanupx_item_image_gallery, parent, false))
    }

    inner class ImageViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MediaBean, CleanupxItemImageGalleryBinding>(itemView) {
        override fun bindData(t: MediaBean) {
            val option = RequestOptions()
                .transform(CenterCrop(),RoundedCorners(SizeUtil.dp2px(8f)))
            Glide.with(mBinding.galleryContentIv)
                .asBitmap()
                .apply(option)
                .load(t.originalPath)
                .thumbnail(0.5f)
                .into(mBinding.galleryContentIv)
        }

    }
}