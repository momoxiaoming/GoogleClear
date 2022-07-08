package com.mckj.sceneslib.ui.scenes.landing.header

import androidx.lifecycle.ViewModelProvider
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderDefaultBinding
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderNewsBinding
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory

/**
 * Describe:
 *
 * Created By yangb on 2021/5/24
 */
class LandingHeaderNewsFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderNewsBinding, ScenesViewModel>() {

    override fun getLayoutId() = R.layout.scenes_fragment_landing_header_news

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
    }
}