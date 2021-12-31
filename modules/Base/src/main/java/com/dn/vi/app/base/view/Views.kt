/**
 * View的一些扩展
 * Created by holmes on 2020/5/21.
 **/

@file:Suppress("NOTHING_TO_INLINE")

package com.dn.vi.app.base.view

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager


/**
 * View跟随键盘弹起，bottomView为弹起时最底部view
 */
fun View.popKeyboard(
    bottomView: View,
    popListener: (() -> Unit)? = null,
    dismissListener: (() -> Unit)? = null
) {
    this.viewTreeObserver
        .addOnGlobalLayoutListener {
            var rect = Rect()
            getWindowVisibleDisplayFrame(rect)
            var screenHeight = rootView.height
            var mainInvisibleHeight = rootView.height - rect.bottom
            if (mainInvisibleHeight > screenHeight / 4) {
                var location = IntArray(2)
                bottomView.getLocationInWindow(location);
                var scrollHeight = (location[1] + bottomView.height) - rect.bottom;
                scrollTo(0, scrollHeight)
                popListener?.invoke()
            } else {
                dismissListener?.invoke()
                scrollTo(0, 0)
            }
        }
}

/**
 * 如果有parent的话,从view的parent上直接移掉view
 * @return 如果可以移掉就true, 不用移就反false
 */
fun View.removeFromParent(): Boolean {
    val parent = this.parent
    if (parent != null && parent is ViewGroup) {
        parent.removeView(this)
        return true
    }
    return false
}

/**
 * View gone 掉
 */
inline fun View.gone() {
    this.visibility = View.GONE
}

/**
 * View 显示
 */
inline fun View.show() {
    this.visibility = View.VISIBLE
}


/**
 * 在RecycleView的ViewHolder里面，如果是有效的Position，则调用 [block]。
 * @param block 执行参数为有效position的代码块
 * @return 当前的postion
 */
inline fun androidx.recyclerview.widget.RecyclerView.ViewHolder.onPosition(block: (position: Int) -> Unit): Int {
    val position = this.adapterPosition
    if (position != androidx.recyclerview.widget.RecyclerView.NO_POSITION) {
        block(position)
    }
    return position
}

/**
 * 重新动态处理View的LayoutParam Size (width, height)
 * @param block 处理[ViewGroup.LayoutParams]
 * @see [updateLayoutParams] in androidx
 */
inline fun View.resizeView(block: (lp: ViewGroup.LayoutParams) -> ViewGroup.LayoutParams): ViewGroup.LayoutParams? {
    var lp = this.layoutParams
    if (lp != null) {
        lp = block(lp)
        this.layoutParams = lp
        this.requestLayout()
    }
    return lp
}

/**
 * 打开键盘
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}

/**
 * 关闭键盘
 */
fun View.closeKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


