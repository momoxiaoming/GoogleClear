package com.dn.vi.app.base.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView

/**
 * RecycleView 的简化 adapter
 */
abstract class RecycleMutableAdapter<Holder : RecyclerView.ViewHolder, T>(
    context: Context,
    data: MutableList<T>?
) : RecyclerView.Adapter<Holder>(), Mutable<T> {

    /** Context  */
    var context: Context
        private set

    /** adapter 数据集  */
    var data: MutableList<T>?
        private set

    /** 用于解析布局  */
    protected var inflater: LayoutInflater

    init {
        this.context = context
        this.data = data
        inflater = LayoutInflater.from(context)
    }


    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    /**
     * 是否是个空的
     * @return
     */
    val isEmpty: Boolean
        get() = itemCount == 0


    /**
     * 获取通过位置获取item内容
     * @param position
     * *
     * @return
     */
    //兼容处理．
    fun getItem(position: Int): T? {
        data?.also {
            if (position >= 0 && position < it.size) {
                return it[position]
            }
        }
        return null
    }

    override fun refresh(data: MutableList<T>?) {
        if (this.data != data) {
            this.data?.clear()
            this.data = null
            this.data = data
        }
        notifyDataSetChanged()
    }

    override fun append(data: T) {
        this.data?.also {
            it.add(data)
            notifyItemInserted(it.size - 1)
        }
    }

    override fun append(position: Int, data: T) {
        this.data?.add(position, data)
        notifyItemInserted(position)
    }

    override fun append(data: List<T>) {
        val d = this.data
        if (d != null) {
            val oldSize = d.size
            d.addAll(data)
            notifyItemRangeInserted(oldSize, data.size)
        } else {
            this.data = data.toMutableList()
            notifyItemRangeInserted(0, this.data?.size ?: 0)
        }

    }

    override fun appendAllAt(position: Int, data: List<T>) {
        val d = this.data
        if (d != null) {
            var oldSize = position
            if (position >= d.size) {
                oldSize = d.size
                d.addAll(data)
            } else {
                d.addAll(position, data)
            }
            notifyItemRangeInserted(oldSize, data.size)
        } else {
            this.data = data.toMutableList()
            notifyItemRangeInserted(0, this.data?.size ?: 0)
        }
    }

    fun append(position: Int, data: List<T>) {
        val d = this.data
        if (d != null) {
            val oldSize = d.size
            d.addAll(position, data)
            notifyItemRangeInserted(position, data.size)
        } else {
            this.data = data.toMutableList()
            notifyItemRangeInserted(0, this.data?.size ?: 0)
        }

    }

    override fun remove(item: T): T {
        this.data?.also {
            val i = it.indexOf(item)
            if (i >= 0) {
                it.removeAt(i)
                notifyItemRemoved(i)
            }
        }
        return item
    }

    override fun remove(position: Int): T? {
        this.data?.also {
            if (position >= 0 && position < it.size) {
                val item = it[position]
                it.removeAt(position)
                notifyItemRemoved(position)
                return item
            }
        }
        return null
    }

    override fun removeAll() {
        this.data?.clear()
        notifyDataSetChanged()
    }

}
