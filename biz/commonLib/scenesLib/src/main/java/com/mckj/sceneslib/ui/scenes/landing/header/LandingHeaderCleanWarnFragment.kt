package com.mckj.sceneslib.ui.scenes.landing.header

import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieDrawable
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show

import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderCleanWarnBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.util.CountDownTimer

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingHeaderCleanWarnFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderCleanWarnBinding, ScenesViewModel>() {


    companion object {
        private const val FINGER_LOTTIE = "scenes/lottieFiles/tool_finger.zip"
        const val FINISH_EVENT = 0
        const val CLICK_EVENT = 1

    }

    private var eventCallback:(Int)->Unit={}

    fun setFinishAction(action:(Int)->Unit){
        eventCallback=action
    }

    private val mTimer by lazy {
        CountDownTimer(this)
    }

    override fun getLayoutId() = R.layout.scenes_fragment_landing_header_clean_warn

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
//        startAnim()
        showAd()
        mBinding.apply {
            val scenesData = mModel.getScenesData()
            landingHeaderImage.setImageResource(R.drawable.ic_result_dangerous)
            landingHeaderNameTvClean.text = scenesData.landingName
            landingHeaderDescTvClean.text = scenesData.landingDesc

            speedWarnLottie.apply {
                setAnimation(FINGER_LOTTIE)
                repeatCount = LottieDrawable.INFINITE
                repeatMode = LottieDrawable.RESTART
                playAnimation()
            }
            mTimer.setTimerAction(ing = {
                speedWarnClean.text = "立即清理（${it+1}s）"
            }, end = {

                speedWarnClean.text = "立即清理"
                eventCallback(FINISH_EVENT)
                St.stExitConfirmThreatenClick("AutoKeep")
            })

            speedWarnClean.setOnClickListener {
                mTimer.stopTimer()
                eventCallback(CLICK_EVENT)
                St.stExitConfirmThreatenClick("Keep")
            }
        }
    }

    private fun showAd() {
//        AdManager.getInstance().showAd(
//            mModel.mJumpData.landingAdName ?: "",
//            ViewGroupAdContainer(mBinding.adLayout),
//            this,
//            OSDefaultNativeRender(),
//        ) {
//            when (it.adStatus) {
//                AdStatus.SHOW_SUCCESS -> {
//                    val name = mModel.getScenesData().name
//                    St.stAdMsgThreatenShow(name,"in")
//                    mBinding.adLayout.show()
//                }
//                AdStatus.ERROR -> {
//                    mBinding.adLayout.gone()
//                }
//                AdStatus.CLICK -> {
//
//                }
//                AdStatus.CLOSE -> {
//                    mBinding.adLayout.gone()
//                }
//            }
//        }
    }
}

//    private fun startAnim() {
//        mBinding.landingHeaderNameTv.gone()
//        mBinding.landingHeaderDescTv.gone()

//        mBinding.landingHeaderLottie.apply {
//            imageAssetsFolder = "scenes/lottieFiles/landing_header_clean/images"
//            setAnimation("scenes/lottieFiles/landing_header_clean/data.json")
//            speed = 1f
//            repeatCount = 1
//
//            setMinAndMaxFrame(0, 30)
//            addAnimatorListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationCancel(animation: Animator?) {
//                    super.onAnimationCancel(animation)
//                    removeAllAnimatorListeners()
//                    endAnim()
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    super.onAnimationEnd(animation)
//                    removeAllAnimatorListeners()
//                    endAnim()
//                }
//            })
//            playAnimation()
//        }

//    private fun endAnim() {
//        mBinding.landingHeaderNameTv.show()
//        mBinding.landingHeaderDescTv.show()
//        mBinding.landingHeaderLottie.apply {
//            setMinAndMaxFrame(30, 268)
//            playAnimation()
//        }
//    }