package com.dn.vi.app.base.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.BaseAdapter

/**
 * 可变内容的Adapter<br></br>
 * 子类可以直接使用 数据集[.data], Context [.context], View解析器[.inflater]
 * @author holmes
 * *
 * *
 * @param <T>
 */
abstract class MutableAdapter<T>(
    context: Context,
    data: MutableList<T>?
) : BaseAdapter(), Mutable<T> {
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
        inflater = LayoutInflater.from(context)
        this.data = data
    }

    override fun getCount(): Int {
        return data?.size ?: 0
    }

    override fun getItem(position: Int): T? {
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
        this.data?.add(data)
        notifyDataSetChanged()
    }

    override fun append(position: Int, data: T) {
        this.data?.add(position, data)
        notifyDataSetChanged()
    }

    override fun append(data: List<T>) {
        this.data?.addAll(data)
        notifyDataSetChanged()
    }

    override fun appendAllAt(position: Int, data: List<T>) {
        this.data?.addAll(position, data)
        notifyDataSetChanged()
    }

    override fun remove(item: T): T {
        this.data?.remove(item)
        notifyDataSetChanged()
        return item
    }

    override fun remove(position: Int): T? {
        val d = data
        if (d != null && position >= 0 && position < d.size) {
            val item = d[position]
            d.removeAt(position)
            notifyDataSetChanged()
            return item

        }
        return null
    }

    /**
     * 移除所有数据.
     * data.clear()
     * 注这个会清理数据集的对象
     */
    override fun removeAll() {
        this.data?.clear()
        notifyDataSetChanged()
    }

    /**
     * 通过ID获取位置
     * @param id
     * *
     * @return 位置，如果没有此id，此返回-1
     */
    fun getPositionById(id: Long): Int {
        for (i in 0 until count) {
            if (getItemId(i) == id) {
                return i
            }
        }
        return -1
    }
}
