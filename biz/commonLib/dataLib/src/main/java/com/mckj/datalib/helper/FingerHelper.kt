package com.mckj.datalib.helper

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.R

/**
 * Describe:动态显示手指工具类
 *
 * Created By yangb on 2020/10/19
 */
class FingerHelper (
    var imagesFile:String = "data/lottieFiles/finger/images",
    var animFile:String = "data/lottieFiles/finger/data.json",
    private var isLoop:Boolean = true
)
{

    private var idResRaw:Int = 0

    constructor(idResRaw: Int) : this() {
        this.idResRaw = idResRaw
    }

    init {
        if(imagesFile.equals("data/lottieFiles/finger/images") && animFile.equals("data/lottieFiles/finger/data.json")) {
            idResRaw = R.raw.lottiefilesfinger
        }
    }

    /**
     * @param rootLayout 父控件
     * @param rootLayout 相对区域控件
     * @param paddingX x轴边距指，默认位置居中
     * @param paddingY y轴边距指，默认位置居中
     * @param fingerSizeDp 手指控件大小
     */
    fun showFinger(
        rootLayout: ViewGroup,
        targetView: View,
        paddingX: Int = 0,
        paddingY: Int = 0,
        fingerSizeDp: Float = 60f,
    ): View {
        val rootLocation = IntArray(2)
        val targetLocation = IntArray(2)
        rootLayout.getLocationInWindow(rootLocation)
        targetView.getLocationInWindow(targetLocation)

        val fingerView = createFingerView(rootLayout.context)
        val width = SizeUtil.dp2px(fingerSizeDp)
        val height = SizeUtil.dp2px(fingerSizeDp)

        fingerView.x =
            targetLocation[0] - rootLocation[0].toFloat() + (targetView.width / 2) + paddingX
        fingerView.y =
            targetLocation[1] - rootLocation[1].toFloat() + ((targetView.height - height) / 2) + paddingY

        val layoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(width, height)
        rootLayout.addView(fingerView, layoutParams)
        return fingerView
    }

    fun remove(fingerView: View?) {
        val parent = fingerView?.parent ?: return
        if (parent is ViewGroup) {
            parent.removeView(fingerView)
        }
    }

    /**
     * 创建手指动画控件
     */
    private fun createFingerView(context: Context): View {
        val fingerView = LottieAnimationView(context)
        if(idResRaw == 0) {
            fingerView.imageAssetsFolder = imagesFile
            fingerView.setAnimation(animFile)
        } else {
            fingerView.setAnimation(idResRaw)
        }

        if (isLoop) {
            fingerView.repeatCount = LottieDrawable.INFINITE
        }

        fingerView.playAnimation()
        return fingerView
    }

//    fun setLottieData(imagesFile: String, animFile: String) {
//        this.imagesFile = imagesFile
//        this.animFile = animFile
//    }
}