package com.mckj.module.viewbinder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mckj.api.client.JunkConstants
import com.mckj.api.entity.JunkInfo
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.manager.CleanManager
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupItemMediaChildBinding
import org.jetbrains.anko.backgroundResource


/**
 * @author xx
 * @version 1
 * @createTime 2021/9/2 11:38
 * @desc
 */
class MediaChildItemViewBinder(var block: ((junkDetail: JunkInfo) -> Unit)?) :
    DataBindingViewBinder<JunkInfo>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_media_child, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<JunkInfo, CleanupItemMediaChildBinding>(
            itemView
        ) {
        init {
            itemView.setOnClickListener {
                block?.invoke(adapterItems[layoutPosition] as JunkInfo)
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bindData(t: JunkInfo) {
            var contains = false
            if (t.fileType == JunkConstants.JunkFileType.VIDEO) {
                mBinding.playIcon.show()
            } else {
                mBinding.playIcon.gone()
            }
            Glide.with(mBinding.cover)
                .asBitmap()
                .thumbnail(0.3f)
                .load(t.mediaBean?.originalPath)
                .into(mBinding.cover)
            if (CleanManager.instance.mSelectedList.contains(t)) {
                contains = true
                mBinding.checkIcon.backgroundResource = R.drawable.cleanup_box_recovery_selet
            } else {
                mBinding.checkIcon.backgroundResource = R.drawable.cleanup_box_recovery_nor
            }
            mBinding.checkIcon.setOnClickListener {
                if (contains){
                    mBinding.checkIcon.backgroundResource = R.drawable.cleanup_box_recovery_nor
                }else{
                    mBinding.checkIcon.backgroundResource = R.drawable.cleanup_box_recovery_selet
                }
                CleanManager.instance.selectedJunk(t)
                adapter.notifyItemChanged(layoutPosition)
            }
            t.junkSize.let {
                mBinding.lengthTv.text =
                    "${FileUtil.getFileSizeNumberText(it)}${FileUtil.getFileSizeUnitText(it)}"
            }
        }
    }
}