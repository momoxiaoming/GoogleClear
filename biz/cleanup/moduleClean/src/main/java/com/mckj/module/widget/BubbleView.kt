package com.mckj.module.widget

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mckj.baselib.util.SizeUtil
import com.mckj.moduleclean.R
import org.jetbrains.anko.layoutInflater

/**
 * 气泡View
 */
class BubbleView(
) {
    fun showBubble(
        rootLayout: ViewGroup,
        targetView: View,
        desc: String,
        paddingSize: Float = 5f
    ): View {
        val rootLocation = IntArray(2)
        val targetLocation = IntArray(2)
        rootLayout.getLocationInWindow(rootLocation)
        targetView.getLocationInWindow(targetLocation)
        val bubbleView = createBubbleView(rootLayout.context, desc)
        val width = SizeUtil.dp2px(110f)
        val height = SizeUtil.dp2px(20f)
        bubbleView.x =
            (targetLocation[0] - (targetView.width / 2)).toFloat()
        bubbleView.y =
            (targetLocation[1] - targetView.height / 2).toFloat()
        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(width, height)
        rootLayout.addView(bubbleView, layoutParams)
        return bubbleView
    }

    fun remove(bubble: View?) {
        val parent = bubble?.parent ?: return
        if (parent is ViewGroup) {
            parent.removeView(bubble)
        }
    }

    private fun createBubbleView(context: Context, desc: String): View {
        val view = context.layoutInflater.inflate(R.layout.cleanup_view_bubble, null)
        val descTv = view.findViewById<TextView>(R.id.desc)
        descTv.text = desc
        return view
    }

}