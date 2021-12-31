package com.dn.vi.app.base.view

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

/**
 * 原来的[DefaultItemAnimator]，在某一版本后，居然没有实现
 * [setSupportsChangeAnimations], 照成无法取消change动画。
 * 所以这里兼容一下
 *
 * [参考](https://stackoverflow.com/questions/35766497/disable-onchange-animations-on-itemanimator-for-recyclerview)
 *
 * Created by holmes on 2021/4/23.
 **/
open class DefaultItemAnimatorCompat : DefaultItemAnimator() {

    /**
     * 绑定到[recyclerView]上。
     * @return 如果[recyclerView]本来就是[DefaultItemAnimatorCompat]则返回原来的，
     *  否则就设置[this]并返回[this]
     */
    fun attach(recyclerView: RecyclerView): DefaultItemAnimatorCompat {
        val itemAnimator = recyclerView.itemAnimator
        if (itemAnimator is DefaultItemAnimatorCompat) {
            return itemAnimator
        }
        recyclerView.itemAnimator = this
        return this
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder,
        preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        if (supportsChangeAnimations) {
            return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY)
        } else {
            if (oldHolder == newHolder) {
                if (oldHolder != null) {
                    //if the two holders are equal, call dispatch change only once
                    dispatchChangeFinished(oldHolder, true)
                }
            } else {
                //else call dispatch change once for every non-null holder
                if (oldHolder != null) {
                    dispatchChangeFinished(oldHolder, true)
                }
                if (newHolder != null) {
                    dispatchChangeFinished(newHolder, false)
                }
            }
            return false
        }
    }

}