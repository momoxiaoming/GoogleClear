package com.mckj.module.widget

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.module.bean.SortBean
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupSortItemBinding

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/14 18:16
 * @desc
 */
class SortPopWindow(var context: Context?) : PopupWindow(context) {
    private var mSortRecycleView: RecyclerView? = null
    private val mAdapter = MultiTypeAdapter()

    private var mCurrentSortName: String = ""

    init {
        initView()
    }

    private fun initView() {
        val view = LayoutInflater.from(context).inflate(R.layout.cleanup_sort_pop_window, null)
        mSortRecycleView = view.findViewById(R.id.recycle_view)
        mAdapter.register(SortBean::class.java, SortViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<SortBean> {
                override fun onItemClick(view: View, position: Int, t: SortBean) {
                    mCurrentSortName = t.sortName
                    (mAdapter.items as List<SortBean>).forEach { sortBean ->
                        sortBean.selected = mCurrentSortName == sortBean.sortName
                    }
                    t.block.invoke()
                    mAdapter.notifyDataSetChanged()
                    dismiss()
                }
            }
        })
        mSortRecycleView?.apply {
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.VERTICAL
            layoutManager = manager
            adapter = mAdapter
        }
        initPopupWindow(view)
    }

    /**
     * 初始化PopupWindow的一些属性
     */
    private fun initPopupWindow(view: View) {
        contentView = view
        width = SizeUtil.dp2px(136f)
        setBackgroundDrawable(ColorDrawable())
        isOutsideTouchable = true
        isFocusable = true
        setTouchInterceptor { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_OUTSIDE) {
                dismiss()
            }
            false
        }
    }

    fun setData(list: List<SortBean>) {
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }


    class SortViewBinder : DataBindingViewBinder<SortBean>() {

        override fun onCreateViewHolder(
            inflater: LayoutInflater,
            parent: ViewGroup,
        ): RecyclerView.ViewHolder {
            return ViewHolder(inflater.inflate(R.layout.cleanup_sort_item, parent, false))
        }

        private inner class ViewHolder(itemView: View) :
            DataBindingViewBinder.DataBindingViewHolder<SortBean, CleanupSortItemBinding>(
                itemView
            ) {
            init {
                itemView.setOnClickListener {
                    itemClickListener?.apply {
                        val position = layoutPosition
                        val item = adapter.items[position] as SortBean
                        onItemClick(it, position, item)
                    }
                }
            }

            override fun bindData(t: SortBean) {
                mBinding.sortName.text = t.sortName
                if (t.selected) {
                    mBinding.sortName.setTextColor(ResourceUtil.getColor(R.color.cleanup_junk_health))
                    mBinding.sortIcon.visibility = View.VISIBLE
                } else {
                    mBinding.sortName.setTextColor(ResourceUtil.getColor(R.color.cleanup_subtitle))
                    mBinding.sortIcon.visibility = View.INVISIBLE
                }
            }
        }
    }
}