package com.mckj.gallery.view

import android.graphics.Canvas
import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/23 10:26
 * @desc item滑动检测
 */
class SupplyTouchCallback(
    private val onSupplyCallback: (Int) -> Unit,
    private val onSwipe: () -> Unit
) :
    ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val idle = ItemTouchHelper.UP
        val swipe = ItemTouchHelper.UP or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        return (makeFlag(ItemTouchHelper.ACTION_STATE_IDLE, idle)
                        or makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, swipe))

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.2f
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //图片删除时还原View的动效改变
        viewHolder.itemView.rotation = 0f
        viewHolder.itemView.scaleX = 1f
        viewHolder.itemView.scaleY = 1f
        viewHolder.itemView.translationX = 0f
        viewHolder.itemView.translationY = 0f
        onSwipe()
        onSupplyCallback(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        //用户拖动图片时，根据偏移设置各种动效
        val abs = abs(dY)
        viewHolder.itemView.translationX = dX
        viewHolder.itemView.translationY = dY
        viewHolder.itemView.rotation = abs / 24
        if (abs< 500) {
            val scala = (1000 - abs) / 1000f
            viewHolder.itemView.scaleX = scala
            viewHolder.itemView.scaleY = scala
        }
    }

    //用户将图片拖出边界，偏移相关量计算
    override fun interpolateOutOfBoundsScroll(
        recyclerView: RecyclerView,
        viewSize: Int,
        viewSizeOutOfBounds: Int,
        totalSize: Int,
        msSinceStartScroll: Long
    ): Int {
        //不偏移item，直接返回0
        return 0
    }
}