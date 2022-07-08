package com.mckj.gallery.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mckj.gallery.bean.BucketBean
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.SizeUtil
import com.mckj.gallery.R
import com.mckj.gallery.databinding.CleanupxItemBucketBinding
import com.org.openlib.utils.onceClick


/**
 *@author leix
 *@version 1
 *@createTime 2021/7/22 17:52
 *@desc Bucket
 */
class BucketViewBinder : DataBindingViewBinder<BucketBean>() {
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanupx_item_bucket, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<BucketBean, CleanupxItemBucketBinding>(itemView) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as BucketBean
                    onItemClick(it, position, item)
                }
            }
        }

        override fun bindData(t: BucketBean) {
            val corners = RoundedCorners(SizeUtil.dp2px(8f))
            val options = RequestOptions.bitmapTransform(corners)
                .override(SizeUtil.dp2px(300f), SizeUtil.dp2px(300f))
            if (t.cover.isNullOrEmpty()) {
                mBinding.emptyLl.show()
                mBinding.coverLl.gone()
            } else {
                mBinding.emptyLl.gone()
                mBinding.coverLl.show()
                Glide.with(mBinding.cover)
                    .asBitmap()
                    .load(t.cover)
                    .apply(options)
                    .placeholder(R.drawable.cleanup_gallery_images_placeholder)
                    .error(R.drawable.cleanup_gallery_images_placeholder)
                    .into(mBinding.cover)
            }
            mBinding.displayName.text = t.bucketName
            if (t.isVideo()) {
                mBinding.playIcon.show()
            } else {
                mBinding.playIcon.gone()
            }
        }

    }
}