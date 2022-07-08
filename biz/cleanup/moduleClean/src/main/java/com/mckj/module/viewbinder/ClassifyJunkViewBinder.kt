package com.mckj.module.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.bean.GroupJunkInfo
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupItemJunkClassifyBinding
import com.org.openlib.utils.onceClick


/**
 * @author leix
 * @version 1
 * @createTime 2021/9/2 11:38
 * @desc
 */
class ClassifyJunkViewBinder : DataBindingViewBinder<GroupJunkInfo>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_junk_classify, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<GroupJunkInfo, CleanupItemJunkClassifyBinding>(
            itemView
        ), View.OnClickListener {
        init {
            mBinding.iconCheck.onceClick(this)
        }

        override fun bindData(t: GroupJunkInfo) {
            if (t.selected) {
                mBinding.iconCheck.setBackgroundResource(R.drawable.cleanup_box_round_sel)
            } else {
                mBinding.iconCheck.setBackgroundResource(R.drawable.cleanup_box_round_nor)
            }
            mBinding.classifyName.text = t.name
            mBinding.classifyDesc.text =
                "可省${FileUtil.getFileSizeNumberText(t.junkSize)}${FileUtil.getFileSizeUnitText(t.junkSize)}"

        }

        override fun onClick(v: View) {
            itemClickListener?.apply {
                val position = layoutPosition
                val item = adapter.items[position] as GroupJunkInfo
                onItemClick(v, position, item)
            }
        }
    }
}