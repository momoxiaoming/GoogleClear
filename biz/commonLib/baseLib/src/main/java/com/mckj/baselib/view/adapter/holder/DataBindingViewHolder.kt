package com.mckj.baselib.view.adapter.holder

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * AbsViewHolder
 *
 * @author mmxm
 * @date 2021/6/29 17:01
 */
abstract class DataBindingViewHolder<T, DB : ViewDataBinding>(itemView: View) :
    AbsViewHolder<T>(itemView) {
    val mBinding = DataBindingUtil.bind<DB>(itemView)!!
    override fun bindData(data: T) {
        bindingData(data)
        mBinding.executePendingBindings()
    }
    abstract fun bindingData(data: T)
}