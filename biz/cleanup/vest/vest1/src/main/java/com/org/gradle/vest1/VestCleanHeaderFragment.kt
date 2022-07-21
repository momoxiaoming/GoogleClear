package com.org.gradle.vest1

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

    }

}