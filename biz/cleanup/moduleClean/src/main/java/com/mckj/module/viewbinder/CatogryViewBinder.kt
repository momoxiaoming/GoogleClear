package com.mckj.module.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.bean.Category
import com.mckj.module.utils.McUtils
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupItemMediaCatogryBinding


/**
 * @author xx
 * @version 1
 * @createTime 2021/9/2 11:38
 * @desc
 */
class CatogryViewBinder(
    var block: (
        isExpand: Boolean,
        category: Category,
        position: Int
    ) -> Unit
) :
    DataBindingViewBinder<Category>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_media_catogry, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<Category, CleanupItemMediaCatogryBinding>(
            itemView
        ) {

        override fun bindData(t: Category) {
            if (t.isExpand) {
                mBinding.expandStatus.setBackgroundResource(R.drawable.cleanup_icon_arrow_down_black)
            } else {
                mBinding.expandStatus.setBackgroundResource(R.drawable.cleanup_icon_arrow_up_black)
            }
            mBinding.topLl.setOnClickListener {
                t.isExpand = !t.isExpand
                block.invoke(
                    t.isExpand,
                    adapterItems[layoutPosition] as Category,
                    layoutPosition
                )
            }
            when (t.category) {
                McUtils.ONE_WEEK -> mBinding.timeLimit.text = "一周内"
                McUtils.ONE_MONTH -> mBinding.timeLimit.text = "一个月内"
                McUtils.ONE_YEAR -> mBinding.timeLimit.text = "一年内"
                McUtils.EARLY -> mBinding.timeLimit.text = "更早"
            }
        }


    }
}