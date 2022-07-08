package com.mckj.vest

import com.mckj.template.BaseHomeViewModel
import com.mckj.template.CleanHeaderFragment
import com.mckj.template.header.clean.AbsCleanHeader

class VestCleanHeaderFragment : CleanHeaderFragment() {

    override fun getHeader(): AbsCleanHeader {
        return VestHeader(requireActivity(), iAction)
    }

    override fun getEmptyViewHeight(): Float {
        return 48f
    }

    override fun initView() {
        super.initView()
        subscribeStatus()
    }
    private fun subscribeStatus() {
        mModel.uiStatus.observe(requireActivity()) {
            when (it) {
                BaseHomeViewModel.NORMAL -> {
                    mBinding.rootLayout.setBackgroundResource(R.drawable.img_home_bg)
                }
                BaseHomeViewModel.END -> {
                    mBinding.rootLayout.setBackgroundResource(R.drawable.img_home_top_bg_red)
                }
                BaseHomeViewModel.WARN -> {
                    mBinding.rootLayout.setBackgroundResource(R.drawable.img_home_bg)
                }
            }
        }
    }

}