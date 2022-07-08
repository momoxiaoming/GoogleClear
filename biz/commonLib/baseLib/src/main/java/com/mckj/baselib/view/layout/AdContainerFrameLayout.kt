package com.mckj.baselib.view.layout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout

/**
 * CustomFragment
 * 当容器高度不为0时,回调具体高度
 * @author mmxm
 * @date 2021/7/3 17:44
 */
class AdContainerFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    var onSizeChanged:(AdContainerFrameLayout.(w:Int,h:Int)->Unit)?=null
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(h>0){
            onSizeChanged?.invoke(this,w,h)
        }
    }
}