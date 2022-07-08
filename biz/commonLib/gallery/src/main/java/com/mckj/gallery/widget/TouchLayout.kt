package com.mckj.gallery.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout
import androidx.core.view.MotionEventCompat

class TouchLayout :
    LinearLayout{
    constructor(context: Context?) : super(context)
    constructor(context: Context?,attrs:AttributeSet) : super(context,attrs)
    constructor(context: Context?,attrs:AttributeSet,defStyleAttr:Int) : super(context,attrs,defStyleAttr)
    constructor(context: Context?,attrs:AttributeSet,defStyleAttr:Int,defStyleRes:Int) : super(context,attrs,defStyleAttr,defStyleRes)

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action: Int? = event?.action

        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("TouchLayout", "Action was DOWN")
                true
            }
            MotionEvent.ACTION_MOVE -> {
                Log.d("TouchLayout", "Action was MOVE,X:${event.x},Y:${event.y}")
                true
            }
            MotionEvent.ACTION_UP -> {
                Log.d("TouchLayout", "Action was UP")
                true
            }
            MotionEvent.ACTION_CANCEL -> {
                Log.d("TouchLayout", "Action was CANCEL")
                true
            }
            MotionEvent.ACTION_OUTSIDE -> {
                Log.d("TouchLayout", "Movement occurred outside bounds of current screen element")
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

}