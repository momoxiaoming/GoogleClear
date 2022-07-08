package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.mckj.sceneslib.R

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils
 * @data  2022/3/3 16:15
 */
object LottieHelper {
        //newtools.lottieFiles
      val DUST_ING_LOTTIE = R.raw.dust_ing
      val DUST_END_LOTTIE = R.raw.dust_end
      val AUDIO_LOTTIE =R.raw.tool_audio
      val NOTIFY_CLEAN_LOTTIE=R.raw.notify_clean_finish
      val NOTIFY_FINGER_LOTTIE=R.raw.notify_clean_finish


    fun preLottie(lottieView: LottieAnimationView,idResRaw:Int,  loopCount:Int=LottieDrawable.INFINITE,end:()->Unit={}){
        lottieView.apply {
            isVisible=true
            setAnimation(idResRaw)
            repeatCount = loopCount
            repeatMode = LottieDrawable.RESTART
            addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    end()
                }
            })
        }
    }

    fun startLottie(lottieView: LottieAnimationView){
        if (lottieView.progress > 0f) {
            lottieView.resumeAnimation()
        } else {
            lottieView.playAnimation()
        }
    }


    fun stopLottie(lottieView: LottieAnimationView) {
        if (lottieView.isAnimating) {
            lottieView.cancelAnimation()
        }
    }

    fun pauseLottie(lottieView: LottieAnimationView) {
        if (lottieView.isAnimating) {
            lottieView.pauseAnimation()
        }
    }

}