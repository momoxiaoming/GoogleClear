package com.mckj.module.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mckj.api.client.JunkConstants
import com.mckj.api.entity.JunkInfo
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.SizeUtil
import com.mckj.module.bean.UIMediaCleanBean
import com.mckj.module.utils.McUtils
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupItemMediaCleanBinding
import kotlin.properties.Delegates


/**
 * @author leix
 * @version 1
 * @createTime 2021/9/2 11:38
 * @desc
 */
class MediaCleanViewBinder(
    var block: ((junkDetail: JunkInfo) -> Unit)? = null,
    var ducumentBlock: ((path: String) -> Unit)? = null
) :
    DataBindingViewBinder<UIMediaCleanBean>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_item_media_clean, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<UIMediaCleanBean, CleanupItemMediaCleanBinding>(
            itemView
        ) {
        private var mIsExpand: Boolean by Delegates.observable(true, { _, _, isExpand ->
            if (isExpand) {
                mBinding.recycleView.show()
                mBinding.expandStatus.setBackgroundResource(R.drawable.cleanup_icon_arrow_down_black)
            } else {
                mBinding.recycleView.gone()
                mBinding.expandStatus.setBackgroundResource(R.drawable.cleanup_icon_arrow_up_black)
            }
        })

        override fun bindData(t: UIMediaCleanBean) {
            mBinding.topLl.setOnClickListener {
                mIsExpand = !mIsExpand
            }
            when (t.timeGroup) {
                McUtils.ONE_WEEK -> mBinding.timeLimit.text = "一周内"
                McUtils.ONE_MONTH -> mBinding.timeLimit.text = "一个月内"
                McUtils.ONE_YEAR -> mBinding.timeLimit.text = "一年内"
                McUtils.EARLY -> mBinding.timeLimit.text = "更早"
            }
            if (t.mimeType == JunkConstants.JunkFileType.DOCUMENT) {
                buildDocumentAdapter(t)
            } else {
                buildImgAdapter(t)
            }
        }

        private fun buildImgAdapter(t: UIMediaCleanBean) {
            val manager = GridLayoutManager(AppMod.app, 3)
            val mAdapter = MultiTypeAdapter()
            mAdapter.register(JunkInfo::class.java, MediaChildItemViewBinder(block))
            mBinding.recycleView.apply {
                layoutManager = manager
                addItemDecoration(DividerItemDecoration(
                    AppMod.app,
                    RecyclerView.HORIZONTAL
                ).also { decor ->
                    decor.setDrawable(DividerDrawable(SizeUtil.dp2px(8f)))
                })
                mAdapter.items = t.dataList
                adapter = mAdapter
            }
        }

        private fun buildDocumentAdapter(t: UIMediaCleanBean) {
            val manager = LinearLayoutManager(AppMod.app)
            val mAdapter = MultiTypeAdapter()
            mAdapter.register(JunkInfo::class.java, DocumentChildItemViewBinder(ducumentBlock))
            mBinding.recycleView.apply {
                layoutManager = manager
                mAdapter.items = t.dataList
                adapter = mAdapter
            }
        }

        fun notifyItemChange(position: Int) {
            mBinding.recycleView.adapter?.notifyItemRemoved(position)
        }
    }


}