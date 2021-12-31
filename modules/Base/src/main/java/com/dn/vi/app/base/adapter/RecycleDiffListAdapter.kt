package com.dn.vi.app.base.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * 默认Equals实现的。
 * 配合LiveData使用的 ListAdapter
 * Created by holmes on 3/24.
 */
abstract class RecycleDiffListAdapter<T, VH : RecyclerView.ViewHolder?> : ListAdapter<T, VH> {

    /**
     * match item equals
     */
    private class SimpleDiffItemCallback<T> : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
            return oldItem == newItem
        }

    }

    /** 用于解析布局  */
    protected var inflater: LayoutInflater

    constructor(context: Context, diffCallback: DiffUtil.ItemCallback<T>) : super(diffCallback) {
        inflater = LayoutInflater.from(context)
    }

    constructor(context: Context, config: AsyncDifferConfig<T>) : super(config) {
        inflater = LayoutInflater.from(context)
    }

    constructor(context: Context) : this(context, SimpleDiffItemCallback())

    /**
     * 是否是个空的
     * @return
     */
    val isEmpty: Boolean
        get() = itemCount == 0

}