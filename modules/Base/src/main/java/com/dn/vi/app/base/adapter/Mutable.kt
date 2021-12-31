package com.dn.vi.app.base.adapter

/**
 * 可变的的内容
 * @author holmes
 * *
 * *
 * @param <T>
</T> */
interface Mutable<T> {

    /**
     * 刷新
     * @param data
     */
    fun refresh(data: MutableList<T>?)

    /**
     * 追加一个
     * @param data
     */
    fun append(data: T)

    /**
     * 追加一个于某位置
     * @param data
     */
    fun append(position: Int, data: T)

    /**
     * 批量追加
     * @param data
     */
    fun append(data: List<T>)

    /**
     * 批量从一个位插入
     */
    fun appendAllAt(position: Int, data: List<T>)

    /**
     * 移除一个Item
     * @param item
     * *
     * @return 被移除的item
     */
    fun remove(item: T): T

    /**
     * 通过位置移除一个Item
     * @param position
     * *
     * @return 被移除的item
     */
    fun remove(position: Int): T?

    /**
     * 清楚全部
     */
    fun removeAll()

}
