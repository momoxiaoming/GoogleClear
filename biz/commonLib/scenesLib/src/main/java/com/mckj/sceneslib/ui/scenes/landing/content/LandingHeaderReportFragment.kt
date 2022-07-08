package com.mckj.sceneslib.ui.scenes.landing.content

import androidx.lifecycle.ViewModelProvider
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentReportBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory

class LandingHeaderReportFragment :
    DataBindingFragment<ScenesFragmentLandingContentReportBinding, ScenesViewModel>() {
    override fun initData() {

    }

    override fun initView() {
        val type = mModel.getScenes().getGuideTypes()?.get(0)
        val name = type?.let { mModel.getScenes().getNameByType(it) }
        mBinding.clItem.text = name
        mBinding.clItem.setOnClickListener {
            //网络测速_抢红包测速_点击
            if (type == ScenesType.TYPE_ENVELOPE_TEST) {
                St.stSpeedRedPacketClick()
            }

            if (type != null) {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), type)
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.scenes_fragment_landing_content_report
    }

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

}
