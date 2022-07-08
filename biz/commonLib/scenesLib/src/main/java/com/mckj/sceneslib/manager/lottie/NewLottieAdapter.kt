package com.mckj.sceneslib.manager.lottie

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesLottieData
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.manager.strategy.helper.WeightsScenesProvider.checkIsFirstEchelon
import org.jetbrains.anko.backgroundColor

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.manager.lottie
 * @data  2022/5/25 10:18
 */
class NewLottieAdapter(val scenesType: Int) {

    /**
     * 筛选动画适配View
     * @param oldLottieView LottieAnimationView
     * @param newLottieView LottieAnimationView
     * @return LottieAnimationView
     */
    fun filterLottieView(
        oldLottieView: LottieAnimationView,
        newLottieView: LottieAnimationView
    ): LottieAnimationView {
        return if (checkIsFirstEchelon(scenesType)) {
            oldLottieView.isVisible = false
            newLottieView
        } else {
            newLottieView.isVisible = false
            oldLottieView
        }
    }

    /**
     * 渐变层
     * @param bgView View
     */
    fun setNewLottieBgColor(bgView: View,maskView: View) {
        if (!checkIsFirstEchelon(scenesType)) return
        maskView.isVisible=true
        bgView.backgroundColor = filterNewLottieBgColor()
    }


    private fun filterNewLottieBgColor(): Int {
        return when (scenesType) {
            ScenesType.TYPE_JUNK_CLEAN -> Color.parseColor("#FF8014")
            ScenesType.TYPE_COOL_DOWN -> Color.parseColor("#0097EC")
            ScenesType.TYPE_PHONE_SPEED, ScenesType.TYPE_POWER_SAVE -> Color.parseColor("#00C57F")
            else -> Color.BLUE
        }
    }


    fun doNewLottieAnimationStep(
        lottieView: LottieAnimationView,
        lottieData: ScenesLottieData?,
        isStartPre: Boolean,
        breakAction:()->Unit={},
    ) {
        if (lottieData != null) {
            if (lottieData.startPreFrame != null&&lottieData.runPreFrame!=null) {
                doPreLottie(lottieView,lottieData,isStartPre)
            }else{
                breakAction()
            }
        }else{
            breakAction()
        }
    }


    private fun doPreLottie(
        lottieView: LottieAnimationView,
        lottieData: ScenesLottieData,
        isStartLottieFrame:Boolean
    ) {
        lottieView.apply {
            setAnimation(lottieData.fileRaw)
            //动画播放一次结束
           val startFrame=if (isStartLottieFrame) lottieData.startPreFrame!!.start else lottieData.runPreFrame!!.start
           val endFrame = if (isStartLottieFrame) lottieData.startPreFrame!!.end else lottieData.runPreFrame!!.end
            setMinAndMaxFrame(startFrame, endFrame)
            speed = 1f
            repeatCount = 0
            repeatMode = LottieDrawable.REVERSE
            addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    removeAllAnimatorListeners()
                    doRunLottie(lottieView, lottieData,isStartLottieFrame)
                }
            })
            playAnimation()
        }
    }

    private fun doRunLottie(
        lottieView: LottieAnimationView,
        lottieData: ScenesLottieData,
        isStart: Boolean
    ) {
        lottieView.apply {
            //动画播放一次结束
            val startFrame=if (isStart) lottieData.startFrame!!.start else lottieData.runFrame!!.start
            val endFrame = if (isStart) lottieData.startFrame!!.end else lottieData.runFrame!!.end
            setMinAndMaxFrame(startFrame, endFrame)
            speed = 1f
            repeatCount = LottieDrawable.INFINITE
            repeatMode = LottieDrawable.RESTART
            playAnimation()
        }
    }


}