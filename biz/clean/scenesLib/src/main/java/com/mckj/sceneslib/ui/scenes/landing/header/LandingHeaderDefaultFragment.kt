package com.mckj.sceneslib.ui.scenes.landing.header

import androidx.lifecycle.ViewModelProvider
import com.dn.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderDefaultBinding
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory

/**
 * Describe:
 *
 * Created By yangb on 2021/5/24
 */
class LandingHeaderDefaultFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderDefaultBinding, ScenesViewModel>() {

    override fun getLayoutId() = R.layout.scenes_fragment_landing_header_default

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        mBinding.landingHeaderDetailNameTv.text = mModel.getScenesData().landingName
        mBinding.landingHeaderDetailDescTv.text = mModel.getScenesData().landingDesc
    }
}