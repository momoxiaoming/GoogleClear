package com.mckj.module.cleanup.ui.appManager.widget

import android.app.Dialog
import android.content.Context
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import com.mckj.module.cleanup.R

class AppManagerLoadingDialog(context: Context) : Dialog(context, R.style.LoadingDialogTheme) {

    private val iv_loading: ImageView

    private val rotateAnimation: RotateAnimation

    init {
        setContentView(R.layout.cleanup_app_manager_dialog_loading)
        iv_loading = findViewById(R.id.iv_loading)
        rotateAnimation = RotateAnimation(
            0f,
            360f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 1000
        rotateAnimation.repeatCount = -1
        rotateAnimation.interpolator = LinearInterpolator()
        rotateAnimation.repeatMode = Animation.RESTART

    }

    fun start(cancelable: Boolean = false, canceledOnTouchOutside: Boolean = false) {
        setCancelable(cancelable)
        setCanceledOnTouchOutside(canceledOnTouchOutside)
        iv_loading.clearAnimation()
        iv_loading.startAnimation(rotateAnimation)
        show()
    }

    override fun cancel() {
        super.cancel()
        rotateAnimation.cancel()
        iv_loading.clearAnimation()
    }

    override fun dismiss() {
        super.dismiss()
        rotateAnimation.cancel()
        iv_loading.clearAnimation()
    }

}