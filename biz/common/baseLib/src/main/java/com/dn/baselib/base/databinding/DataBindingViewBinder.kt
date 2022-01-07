package com.dn.baselib.base.databinding

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.dn.baselib.base.AbstractViewBinder

/**
 * Describe:BaseViewBinder
 *
 * Created By yangb on 2020/10/15
 */
abstract class DataBindingViewBinder<T> : AbstractViewBinder<T, RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: T) {
        @Suppress("UNCHECKED_CAST") val viewHolder = holder as DataBindingViewHolder<T, ViewDataBinding>
        viewHolder.bindData(item)
        viewHolder.mBinding.executePendingBindings()
    }

    abstract class DataBindingViewHolder<T, DB : ViewDataBinding>(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val mBinding = DataBindingUtil.bind<DB>(itemView)!!

        abstract fun bindData(t: T)

    }

}