package com.org.gradle.vest1

import androidx.lifecycle.ViewModelProvider
import com.mckj.baselib.base.databinding.DataBindingFragment

import com.org.gradle.vest1.databinding.CleanupFragmentHeaderBinding

/**
 * VestHeader
 *
 * @author mmxm
 * @date 2022/7/21 16:36
 */
class VestHeaderFragment : DataBindingFragment<CleanupFragmentHeaderBinding,BaseHomeViewModel>(){

    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_header
    }

    override fun getViewModel(): BaseHomeViewModel {
        return ViewModelProvider(requireActivity()).get(
            BaseHomeViewModel::class.java
        )
    }

    override fun initData() {

    }

    override fun initView() {
        //获取内存使用量
    }


}