package com.dn.vi.app.base.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * RecycleView GridLayout 间距 Decoration
 * Created by holmes on 18-1-29.
 */
class GridDividerItemDecoration(val dividerSize: Int, val gridSize: Int) :
    RecyclerView.ItemDecoration() {

    var verticalDivider = dividerSize
    private var mNeedLeftSpacing = false

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        var itemIndex = itemPosition
        var itemIndexOffset = 0

        val gridInLine = gridSize
        var itemSpanIndex = itemIndex

        val glp = parent.layoutManager as? GridLayoutManager
        if (glp != null) {
            val totalSpan = glp.spanCount
            val lookup = glp.spanSizeLookup
            if (!lookup.isSpanIndexCacheEnabled) {
                lookup.isSpanIndexCacheEnabled = true
            }
            val itemSize = lookup.getSpanSize(itemIndex)
            if (itemSize == totalSpan) {
                // full
                return
            }
            itemSpanIndex = lookup.getSpanIndex(itemIndex, totalSpan)
            itemIndexOffset = (itemPosition % totalSpan) - itemSpanIndex

            itemIndex = itemSpanIndex
        }

        val frameWidth =
            ((parent.width - dividerSize.toFloat() * (gridInLine - 1)) / gridInLine).toInt()
        val padding = parent.width / gridInLine - frameWidth

        if (itemIndexOffset == 0 && itemPosition < gridInLine) {
            outRect.top = 0
        } else {
            outRect.top = verticalDivider
        }

        if (itemIndex % gridInLine == 0) {
            outRect.left = 0
            outRect.right = padding
            mNeedLeftSpacing = true
        } else if ((itemIndex + 1) % gridInLine == 0) {
            mNeedLeftSpacing = false
            outRect.right = 0
            outRect.left = padding
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false
            outRect.left = dividerSize - padding
            if ((itemIndex + 2) % gridInLine == 0) {
                outRect.right = dividerSize - padding
            } else {
                outRect.right = dividerSize / 2
            }
        } else if ((itemIndex + 2) % gridInLine == 0) {
            mNeedLeftSpacing = false
            outRect.left = dividerSize / 2
            outRect.right = dividerSize - padding
        } else {
            mNeedLeftSpacing = false
            outRect.left = dividerSize / 2
            outRect.right = dividerSize / 2
        }
        outRect.bottom = 0
    }

}