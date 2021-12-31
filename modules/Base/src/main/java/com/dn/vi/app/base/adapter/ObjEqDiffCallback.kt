package com.dn.vi.app.base.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

/**
 * 普通 obj diff 规则。 依赖 equals
 * Created by holmes on 2020/5/21.
 **/
class ObjEqDiffCallback : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }

}