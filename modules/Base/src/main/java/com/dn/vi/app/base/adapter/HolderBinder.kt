package com.dn.vi.app.base.adapter

/**
 * 绑定Adapter ItemHolder和ItemData
 * Created by holmes on 17-12-29.
 */
interface HolderBinder<in T> {

    /**
     * 绑定 [item] Data
     */
    fun onBindViewHolder(item: T, position: Int)

}