package com.org.gradle.vest1

import android.text.format.Formatter
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.app.AppMod
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.template.BaseHomeViewModel

import com.org.gradle.vest1.databinding.CleanupFragmentHeaderBinding
import com.org.gradle.vest1.vm.VestHomeViewModel
import com.org.openlib.helper.NavigationHelper

/**
 * VestHeader
 *
 * @author mmxm
 * @date 2022/7/21 16:36
 */
class VestHeaderFragment : DataBindingFragment<CleanupFragmentHeaderBinding, VestHomeViewModel>() {

    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_header
    }

    override fun getViewModel(): VestHomeViewModel {
        return ViewModelProvider(requireActivity()).get(
            VestHomeViewModel::class.java
        )
    }

    override fun initData() {
        mModel.ramLiveData.observe(viewLifecycleOwner) {
            val size = Formatter.formatFileSize(activity, it.first)
            mBinding.ramSizeText.text = "$size"
            mBinding.csProgress.startProgress(0f, (it.first / it.second.toFloat()) * 100, 1000)
        }
    }

    override fun initView() {
        //获取内存使用量
        mBinding.button.setOnClickListener {
            ScenesManager.getInstance().getScenes(ScenesType.TYPE_PHONE_SPEED)
                ?.jumpPage(requireContext())
        }
        
        mBinding.setting.setOnClickListener {
            NavigationHelper.openAbout(requireContext())
        }
    }

    override fun onResume() {
        super.onResume()
        mModel.getRamInfo()
    }

}