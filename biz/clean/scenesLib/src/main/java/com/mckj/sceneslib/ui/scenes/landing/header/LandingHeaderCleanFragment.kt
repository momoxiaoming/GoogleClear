package com.mckj.sceneslib.ui.scenes.landing.header

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.dn.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderCleanBinding

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingHeaderCleanFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderCleanBinding, ScenesViewModel>() {


    override fun getLayoutId() = R.layout.scenes_fragment_landing_header_clean

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        startAnim()
        mBinding.landingHeaderNameTv.text = mModel.getScenesData().landingName
        mBinding.landingHeaderDescTv.text = mModel.getScenesData().landingDesc
    }

    private fun startAnim() {
        mBinding.landingHeaderNameTv.gone()
        mBinding.landingHeaderDescTv.gone()

        mBinding.landingHeaderLottie.apply {
            imageAssetsFolder = "scenes/lottieFiles/landing_header_clean/images"
            setAnimation("scenes/lottieFiles/landing_header_clean/data.json")
            speed = 1f
            repeatCount = 1

            setMinAndMaxFrame(0, 30)
            addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator?) {
                    super.onAnimationCancel(animation)
                    removeAllAnimatorListeners()
                    endAnim()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    removeAllAnimatorListeners()
                    endAnim()
                }
            })
            playAnimation()
        }
    }

    private fun endAnim() {
        mBinding.landingHeaderNameTv.show()
        mBinding.landingHeaderDescTv.show()
        mBinding.landingHeaderLottie.apply {
            setMinAndMaxFrame(30, 268)
            playAnimation()
        }
    }

}