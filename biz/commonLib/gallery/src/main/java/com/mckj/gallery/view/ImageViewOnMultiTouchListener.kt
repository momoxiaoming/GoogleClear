package com.mckj.gallery.view


import android.graphics.Matrix
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt
import android.widget.ImageView

/**
 * @Description
 * @CreateTime 2022/3/24 10:47
 * @Author
 */
class ImageViewOnMultiTouchListener:View.OnTouchListener {
    companion object{
        const val NONE = 0
        const val MOVE = 1
        const val ZOOM = 2
        const val DRAG = 3
    }
    private var mode = NONE
    private val matrix = Matrix()
    private val savedMatrix = Matrix()
    private val start = PointF()
    private val mid = PointF()
    private var oldDistance = 0.0f
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val view = v as ImageView
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                matrix.set(view.imageMatrix)
                savedMatrix.set(matrix)
                start[event.x] = event.y
                mode = DRAG
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDistance = spacing(event)
                if (oldDistance > 5f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, event)
                    mode = ZOOM
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> mode = NONE
            MotionEvent.ACTION_MOVE -> if (mode === DRAG) {
                matrix.set(savedMatrix)
                matrix.postTranslate(
                    event.x - start.x, event.y
                            - start.y
                )
            } else if (mode === ZOOM) {
                val newDist = spacing(event)
                if (newDist > 5f) {
                    matrix.set(savedMatrix)
                    val scale = newDist / oldDistance
                    matrix.postScale(scale, scale, mid.x, mid.y)
                }
            }
        }

        view.imageMatrix = matrix
        view.scaleType = ImageView.ScaleType.MATRIX
        view.setPadding(3, 5, 3, 5)

        return true


    }


    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt(x * x + y * y)
    }
    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point[x / 2] = y / 2
    }
}

