package com.mckj.baselib.view.adapter

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.view.adapter.binder.AbsViewBinder
import com.mckj.baselib.view.adapter.binder.CommonViewBinder
import com.mckj.baselib.view.adapter.holder.AbsViewHolder
import com.mckj.baselib.view.adapter.holder.DataBindingViewHolder
//import com.q.j.ad.uONt


/**
 * 快速注册一个databing viewHolder
 * @receiver MultiTypeAdapter
 * @param layoutId Int
 * @param binderBlock 持有[AbsViewBinder]对象,可以在此设置各种点击事件
 * @param block 持有[AbsViewHolder]对象,可以处理item的数据
 * @author mmxm
 * @date 2021/7/2 16:23
 */
inline fun <reified T, DB : ViewDataBinding> MultiTypeAdapter.register(
    @LayoutRes layoutId: Int,
    crossinline binderBlock: AbsViewBinder<T,AbsViewHolder<T>>.()->Unit,
    crossinline block: AbsViewHolder<T>.(data: T,binding:DB) -> Unit
) {
    register(T::class.java, object : CommonViewBinder<T>(layoutId) {
        override fun onCreateHolder(itemView: View): AbsViewHolder<T> {
            return object : DataBindingViewHolder<T, DB>(itemView) {
                override fun bindingData(data: T) {
                    block(data,mBinding)
                }
            }
        }
    }.apply {
        binderBlock()
    })
}