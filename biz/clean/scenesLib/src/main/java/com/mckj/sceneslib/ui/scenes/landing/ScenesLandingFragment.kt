package com.mckj.sceneslib.ui.scenes.landing

import androidx.activity.OnBackPressedCallback
import androidx.core.util.Consumer
import androidx.lifecycle.ViewModelProvider
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesLandingStyle
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.dn.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.databinding.ScenesFragmentLandingBinding
import org.jetbrains.anko.backgroundResource

/**
 * Describe:
 *
 * 管理落地页展示内容，及 步骤
 *
 * 先落地页前广告->落地页内容->落地页后广告
 *
 * Created By yangb on 2021/5/20
 */
class ScenesLandingFragment :
    DataBindingFragment<ScenesFragmentLandingBinding, ScenesViewModel>() {

    companion object {

        const val TAG = "ScenesLandingFragment"

        fun newInstance(consumer: Consumer<Boolean>): ScenesLandingFragment {
            return ScenesLandingFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    override fun getLayoutId() = R.layout.scenes_fragment_landing

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        activity?.onBackPressedDispatcher?.addCallback(MyBackPressCallback())
        mBinding.titleLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.titleLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        showLandingView()
    }

    /**
     * 显示落地页view
     */
    private fun showLandingView() {
        //落地页样式
        val style = getLandingStyle()
        //添加headerView
        mModel.getScenes().addLandingHeaderView(requireActivity(), mBinding.headerLayout, style)

        //添加contentView
        mModel.getScenes().addLandingContentView(requireActivity(), mBinding.contentLayout, style)
    }

    private fun getLandingStyle(): ScenesLandingStyle {
        val list = mutableListOf<ScenesLandingStyle>()
        list.add(ScenesLandingStyle.DEFAULT)
        list.add(ScenesLandingStyle.CLEAN)
        return list.random()
    }

    private inner class MyBackPressCallback : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            if (mConsumer == null) {
                mModel.isFinish.value = true
            } else {
                mConsumer?.accept(true)
                mConsumer = null
            }
        }
    }

}