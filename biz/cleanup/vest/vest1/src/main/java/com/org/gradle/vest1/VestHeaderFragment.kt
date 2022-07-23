package com.org.gradle.vest1

import android.text.format.Formatter
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.app.AppMod
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.template.BaseHomeViewModel

import com.org.gradle.vest1.databinding.CleanupFragmentHeaderBinding
import com.org.gradle.vest1.vm.VestHomeViewModel

/**
 * VestHeader
 *
 * @author mmxm
 * @date 2022/7/21 16:36
 */
class VestHeaderFragment : DataBindingFragment<CleanupFragmentHeaderBinding, VestHomeViewModel>(){

    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_header
    }

    override fun getViewModel(): VestHomeViewModel {
        return ViewModelProvider(requireActivity()).get(
            VestHomeViewModel::class.java
        )
    }

    override fun initData() {
        mModel.ramLiveData.observe(viewLifecycleOwner){
            val size=Formatter.formatFileSize(activity,it.first)
            mBinding.ramSizeText.text="$size"
            mBinding.csProgress.startProgress(0f,it.first/it.second.toFloat(),1000)
        }
    }

    override fun initView() {
        //获取内存使用量
    }

    override fun onResume() {
        super.onResume()
        mModel.getRamInfo()
    }

}