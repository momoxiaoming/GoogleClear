package com.mckj.sceneslib.ui.scenes.landing.header

import androidx.lifecycle.ViewModelProvider
import com.mckj.sceneslib.R
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.dn.baselib.base.databinding.DataBindingFragment
import com.dn.vi.app.cm.utils.TextUtils
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderSpeedBinding

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
            imageAssetsFolder = "scenes/lottieFiles/landing_header_speed/images"
            setAnimation("scenes/lottieFiles/landing_header_speed/data.json")
            speed = 1f
            repeatCount = 1
            playAnimation()
        }
        mBinding.landingHeaderNameTv.text = getString(R.string.scenes_phone_speed_os)

        val key = (20..30).random().toString()
        val content = "$key%"
        mBinding.landingHeaderDescTv.text =
            TextUtils.string2SpannableStringForSize(content, key, sizeDip = 44)
    }

}