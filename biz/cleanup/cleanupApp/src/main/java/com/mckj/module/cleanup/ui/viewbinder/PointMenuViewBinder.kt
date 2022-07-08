package com.mckj.module.cleanup.ui.viewbinder

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.databinding.CleanupItemPointBinding
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.entity.OptItem

/**
 * Describe:
 *
 * Created By yangb on 2021/1/11
 */
class PointMenuViewBinder : DataBindingViewBinder<OptItem>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_point, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<OptItem, CleanupItemPointBinding>(
            itemView
        ) {
        init {
            mBinding.fixitBtn.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as OptItem
                    onItemClick(it, position, item)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        override fun bindData(t: OptItem) {

            when (t.type) {
                OptItem.AUTO_CLEAN -> {
                    mBinding.iconIv.setImageResource(R.drawable.cleanup_icon_point_auto_clean)
                    mBinding.titleTv.text="自动清理"
                    mBinding.subTitleTv.text = "开启手机壁纸可修复"
                }
                OptItem.DESK_TOOL -> {
                    mBinding.iconIv.setImageResource(R.drawable.cleanup_icon_point_notify)
                    mBinding.titleTv.text="桌面小工具"
                    mBinding.subTitleTv.text = "手机清理加速提升60%"
                }
                OptItem.OPEN_NOT -> {
                    mBinding.iconIv.setImageResource(R.drawable.cleanup_icon_point_clean_alert)
                    mBinding.titleTv.text="通知读取"
                    mBinding.subTitleTv.text = "通知清理的重要权限"
                }
                OptItem.OPEN_FLOAT -> {
                    mBinding.iconIv.setImageResource(R.drawable.cleanup_icon_point_win)
                    mBinding.titleTv.text="悬浮窗失效"
                    mBinding.subTitleTv.text = "修复后清理速度更快"
                }
                OptItem.OPEN_AUTHORITY -> {
                    mBinding.iconIv.setImageResource(R.drawable.cleanup_icon_point_win)
                    mBinding.titleTv.text="权限修复"
                    mBinding.subTitleTv.text = "修复后功能更全面"
                }

            }

        }
    }
}