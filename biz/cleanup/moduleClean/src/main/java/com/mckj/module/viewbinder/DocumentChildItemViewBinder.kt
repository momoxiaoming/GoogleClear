package com.mckj.module.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.api.entity.JunkInfo
import com.mckj.api.util.FileUtils
import com.dn.vi.app.cm.utils.FileUtil
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.module.manager.CleanManager
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupItemChildDocumentBinding
import org.jetbrains.anko.backgroundResource


/**
 * @author leix
 * @version 1
 * @createTime 2021/9/2 11:38
 * @desc
 */
class DocumentChildItemViewBinder(var ducumentBlock: ((path: String) -> Unit)? = null) :
    DataBindingViewBinder<JunkInfo>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_child_document, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<JunkInfo, CleanupItemChildDocumentBinding>(
            itemView
        ) {

        override fun bindData(t: JunkInfo) {
            mBinding.documentName.text = t.name
            if (CleanManager.instance.mSelectedList.contains(t)) {
                mBinding.checkIcon.backgroundResource = R.drawable.cleanup_box_round_sel
            } else {
                mBinding.checkIcon.backgroundResource = R.drawable.cleanup_box_round_nor
            }
            mBinding.checkIconLl.setOnClickListener {
                CleanManager.instance.selectedJunk(t)
                adapter.notifyItemChanged(layoutPosition)
            }
            itemView.setOnClickListener {
                ducumentBlock?.invoke(t.path!!)
            }
            t.description?.let {
                when {
                    FileUtils.isWord(it) ->
                        mBinding.documentIcon.backgroundResource = R.drawable.cleanup_icon_doc

                    FileUtils.isPPT(it) ->
                        mBinding.documentIcon.backgroundResource = R.drawable.cleanup_icon_ppt

                    FileUtils.isExcel(it) ->
                        mBinding.documentIcon.backgroundResource = R.drawable.cleanup_icon_xsl

                    FileUtils.isPdf(it) ->
                        mBinding.documentIcon.backgroundResource = R.drawable.cleanup_icon_pdf
                    FileUtils.isTxt(it) ->
                        mBinding.documentIcon.backgroundResource = R.drawable.cleanup_icon_txt
                    else ->
                        mBinding.documentIcon.backgroundResource = R.drawable.cleanup_icon_other
                }
            }
            mBinding.documentSize.text =
                "${FileUtil.getFileSizeNumberText(t.junkSize)}${FileUtil.getFileSizeUnitText(t.junkSize)}"
        }
    }
}