package com.mckj.baselib.view.adapter

import android.view.View
import com.drakeet.multitype.MultiTypeAdapter

/**
 * 关于MultiTypeAdapter的item 监听事件 支持SAM
 *
 * @author mmxm
 * @date 2021/6/30 9:52
 */
fun interface ItemChildClickListener{
    fun  onItemChildClick(adapter: MultiTypeAdapter, view: View, position: Int)
}

fun interface ItemChildLongClickListener{
    fun  onItemChildLongClick(adapter: MultiTypeAdapter, view: View, position: Int):Boolean
}

fun interface ItemClickListener{
    fun  onItemClick(adapter: MultiTypeAdapter, view: View, position: Int)
}

fun interface ItemLongClickListener{
    fun  onItemLongClick(adapter: MultiTypeAdapter, view: View, position: Int):Boolean
}
