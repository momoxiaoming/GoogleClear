package com.mckj.sceneslib.helper

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.drawable.DividerDrawable

/**
 * InlineExt
 *
 * @author mmxm
 * @date 2021/3/5 11:57
 */
/**
 * 添加一个默认的 vertical方向上的，横分割线
 */
 fun RecyclerView.addVerticalDividerLine( block:(()-> DividerDrawable)) {
    this.addItemDecoration(
        DividerItemDecoration(
            this.context,
            RecyclerView.VERTICAL
        ).also { decor ->
            decor.setDrawable(block())
        })
}

