package com.mckj.sceneslib.ui.scenes.landing.header

import androidx.lifecycle.ViewModelProvider
import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderSpeedBinding
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingHeaderSpeedFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderSpeedBinding, ScenesViewModel>() {


    override fun getLayoutId() = R.layout.scenes_fragment_landing_header_speed

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        mBinding.landingHeaderLottie.apply {
            setAnimation(R.raw.landing_header_speed)
            speed = 1f
            repeatCount = 1
            playAnimation()
        }
        mBinding.landingHeaderNameTv.text = mModel.getScenesData().landingName
        mBinding.landingHeaderDescTv.text = mModel.getScenesData().landingDesc

        if(mModel.getScenesData().type == ScenesType.TYPE_JUNK_CLEAN
            &&(ScenesManager.getInstance().isRegisterCleanerBody()
                    ||ScenesManager.getInstance().isRegisterPowerBody())){
            val lp = mBinding.landingHeaderLayout.layoutParams
            lp.height = 300
            mBinding.landingHeaderLayout.layoutParams = lp
            mBinding.landingHeaderLayout.requestLayout()
        }

    }

}