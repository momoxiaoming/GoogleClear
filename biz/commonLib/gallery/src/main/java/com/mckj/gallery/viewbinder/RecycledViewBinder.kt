package com.mckj.gallery.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.gallery.utils.GalleryToolsUtil
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.gallery.R
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.databinding.CleanupxItemRecycledBinding
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.job.GalleryManager
import com.org.openlib.utils.onceClick

/**
 *@author leix
 *@version 1
 *@createTime 2021/7/24 22:17
 *@desc 图库底部Item
 */
class RecycledViewBinder : DataBindingViewBinder<RecycledBean>() {

    var itemCheckListener: OnItemCheckCListener<RecycledBean>? = null
    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanupx_item_recycled, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<RecycledBean, CleanupxItemRecycledBinding>(
            itemView
        ) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as RecycledBean
                    onItemClick(it, position, item)
                }
            }
            mBinding.checkStatusLl.onceClick {
                itemCheckListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as RecycledBean
                    onCheckItem(it, position, item)
                }
            }
        }

        override fun bindData(t: RecycledBean) {
            val selectedData = GalleryManager.instance.mSelectedData.value
            if (selectedData?.contains(t) == true) {
                mBinding.checkStatus.setBackgroundResource(R.drawable.cleanupx_checkbox_selected)
            } else {
                mBinding.checkStatus.setBackgroundResource(R.drawable.cleanupx_checkbox_unselected)
            }
            t.recycledTime?.let {
                mBinding.passDay.text = String.format(ResourceUtil.getString(R.string.x_day),GalleryToolsUtil.checkRecycledTime(t.recycledTime))
            }
//            val corners = RoundedCorners(SizeUtil.dp2px(4f))
//            val options = RequestOptions.bitmapTransform(corners)
//                .override(SizeUtil.dp2px(300f), SizeUtil.dp2px(300f))
            Glide.with(mBinding.photo)
                .asBitmap()
                .load(t.originalPath)
                .into(mBinding.photo)
            if (t.mimeType == MediaConstant.MEDIA_TYPE_VIDEO) {
                mBinding.playIcon.show()
            } else {
                mBinding.playIcon.gone()
            }
        }
    }

    interface OnItemCheckCListener<T> {
        fun onCheckItem(view: View, position: Int, t: T)
    }
}