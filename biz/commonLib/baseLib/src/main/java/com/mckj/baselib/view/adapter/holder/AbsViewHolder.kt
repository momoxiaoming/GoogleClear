package com.mckj.baselib.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * AbsViewHolder
 *
 * @author mmxm
 * @date 2021/6/29 18:06
 */
abstract class AbsViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindData(data: T)

}