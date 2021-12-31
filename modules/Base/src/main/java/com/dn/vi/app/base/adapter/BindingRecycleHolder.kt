package com.dn.vi.app.base.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * 一般配合Databinding的 RecycleView
 * Created by holmes on 18-1-29.
 */
abstract class BindingRecycleHolder<Data, Binding : ViewDataBinding>(val binding: Binding) :
    RecyclerView.ViewHolder(binding.root), HolderBinder<Data> {

    open val context: Context
        get() = itemView.context

}