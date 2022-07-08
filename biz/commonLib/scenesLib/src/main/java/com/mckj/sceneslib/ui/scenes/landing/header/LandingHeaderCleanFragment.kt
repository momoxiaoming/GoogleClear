package com.mckj.sceneslib.ui.scenes.landing.header

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.view.show

import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderCleanBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.strategy.helper.WeightsScenesProvider


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
//        startAnim()
        showAd()
        val scenesData = mModel.getScenesData()
        val strategy = mModel.getStrategy()
        val checkSafeTimeState = strategy.checkScenesSafeTimeState(scenesData.type)
        val weightsScenes = strategy.isWeightsScenes(scenesData.type)
        if (weightsScenes) {
            val landingSafetyHeaderName = scenesData.landingSafetyHeaderName
            val landingSafetyHeaderDes = scenesData.landingSafetyHeaderDes
            mBinding.landingHeaderNameTvClean.text =if (checkSafeTimeState && landingSafetyHeaderName.isNotBlank()) landingSafetyHeaderName else  scenesData.landingName
            mBinding.landingHeaderDescTvClean.text =if (checkSafeTimeState && landingSafetyHeaderDes.isNotBlank()) landingSafetyHeaderDes else scenesData.landingDesc
            strategy.refreshScenesPopTime(scenesData.type)
        }else{
            mBinding.landingHeaderNameTvClean.text = scenesData.landingName
            mBinding.landingHeaderDescTvClean.text = scenesData.landingDesc
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
//                    St.stLevelMsgShow()
//                    mBinding.adLayout.show()
//                }
//                AdStatus.ERROR -> {
//                    mBinding.adLayout.visibility = View.INVISIBLE
//                }
//                AdStatus.CLICK -> {
//
//                }
//                AdStatus.CLOSE -> {
//                    mBinding.adLayout.visibility = View.INVISIBLE
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