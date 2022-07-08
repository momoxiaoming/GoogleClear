package com.mckj.baselib.view.adapter.binder

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.drakeet.multitype.ItemViewBinder
import com.mckj.baselib.view.adapter.holder.AbsViewHolder

import com.mckj.baselib.view.adapter.ItemChildClickListener
import com.mckj.baselib.view.adapter.ItemChildLongClickListener
import com.mckj.baselib.view.adapter.ItemClickListener
import com.mckj.baselib.view.adapter.ItemLongClickListener
import com.mckj.baselib.view.state.findView
import java.util.LinkedHashSet

/**
 * BaseItemViewBinder
 *
 * @author mmxm
 * @date 2021/6/29 16:47
 */
abstract class AbsViewBinder<T,VH: AbsViewHolder<T>>(@LayoutRes val layoutId: Int) :
    ItemViewBinder<T, VH>() {

    /**
     * 用于保存需要设置点击事件的 item
     */
    private val childClickViewIds = LinkedHashSet<Int>()

    private var mOnItemChildClickListener: ItemChildClickListener?=null
    private var mOnItemChildLongClickListener: ItemChildLongClickListener?=null
    private var mOnItemClickListener: ItemClickListener?=null
    private var mOnItemLongClickListener: ItemLongClickListener?=null

    var onItemClick: ((View, Int, T) -> Unit)? = null

    var onLongItemClick: ((View, Int, T) -> Boolean)? = null

    override fun onBindViewHolder(holder: VH, item: T) {
        holder.bindData(item)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
        Log.i("TAG", "onCreateViewHolder: ")
        val itemV = inflater.inflate(layoutId, parent, false)
        val viewHolder=onCreateHolder(itemV)
        bindViewClickListener(viewHolder)
        return viewHolder
    }

    protected abstract fun onCreateHolder(itemView: View): VH

    /**
     * 设置item子view点击事件
     */
    fun setOnItemChildClickListener(mOnItemChildClickListener: ItemChildClickListener){
        this.mOnItemChildClickListener=mOnItemChildClickListener
    }
    /**
     * 设置item子view长按事件
     */
    fun setOnItemChildLongClickListener(mOnItemChildLongClickListener: ItemChildLongClickListener){
        this.mOnItemChildLongClickListener=mOnItemChildLongClickListener
    }
    /**
     * 设置item点击事件
     */
    fun setOnItemClickListener(mOnItemClickListener: ItemClickListener){
        this.mOnItemClickListener=mOnItemClickListener
    }
    /**
     * 设置item长按事件
     */
    fun setOnItemLongClickListener(mOnItemLongClickListener: ItemLongClickListener){
        this.mOnItemLongClickListener=mOnItemLongClickListener
    }


    /**
     * 添加item的可点击id
     * @param ids IntArray
     */
    fun addChildClickViewIds(@IdRes vararg ids:Int){
        ids.forEach {
            childClickViewIds.add(it)
        }
    }

    /**
     * 绑定item事件
     * @param viewHolder VH
     */
    private fun bindViewClickListener(viewHolder: VH){
        mOnItemChildClickListener?.let { listener->
            for(id in childClickViewIds){
                viewHolder.itemView.findView<View>(id)?.let { childView->
                    if(!childView.isClickable) childView.isClickable=true
                    childView.setOnClickListener { v->
                        listener.onItemChildClick(adapter,v,viewHolder.adapterPosition)
                    }
                }
            }
        }
        mOnItemChildLongClickListener?.let { listener->
            for(id in childClickViewIds){
                viewHolder.itemView.findView<View>(id)?.let { childView->
                    if(!childView.isClickable) childView.isClickable=true
                    childView.setOnLongClickListener { v->
                        listener.onItemChildLongClick(adapter,v,viewHolder.adapterPosition)
                    }
                }
            }
        }
        mOnItemClickListener?.let { listener->
            viewHolder.itemView.setOnClickListener {
                listener.onItemClick(adapter,it,viewHolder.adapterPosition)
            }
        }
        mOnItemLongClickListener?.let { listener->
            viewHolder.itemView.setOnLongClickListener {
                listener.onItemLongClick(adapter,it,viewHolder.adapterPosition)
            }
        }

    }


}