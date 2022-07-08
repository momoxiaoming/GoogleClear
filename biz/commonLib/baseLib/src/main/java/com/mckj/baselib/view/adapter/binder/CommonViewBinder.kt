package com.mckj.baselib.view.adapter.binder

import com.mckj.baselib.view.adapter.holder.AbsViewHolder

/**
 * CommonViewBinder
 * 通用的bind,当你不想写binder时,可实现此类 类似下面写法, 剩下就是专注写[AbsViewHolder]即可
 *  adapter.register(String::class.java,object : CommonViewBinder<String>(R.layout.item_list_view){
        override fun onCreateHolder(itemView: View): AbsViewHolder<String> {
            return TestCommonViewHolder(itemView)
        }
    }.apply {
        addChildClickViewIds()
        setOnItemClickListener{ multiTypeAdapter: MultiTypeAdapter, view: View, i: Int ->
        }
    })
 *
 *   author : mmxm
 * @date 2021/6/30 11:23
 */
abstract class CommonViewBinder<T>(layoutId: Int) : AbsViewBinder<T, AbsViewHolder<T>>(layoutId) {

}