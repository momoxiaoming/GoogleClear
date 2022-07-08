package com.mckj.sceneslib.ui.scenes.landing

import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.org.openlib.help.Consumer
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesLandingStyle
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.databinding.ScenesFragmentLandingBinding
import com.mckj.sceneslib.manager.scenes.ScenesManager


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

    //落地页样式
    val style = getLandingStyle()

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
//        mBinding.titleLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.titleLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            setNavigationOnClickListener {
//                if(style == ScenesLandingStyle.NEWS){
//                    ARouter.getInstance().build("/app/main").navigation()
//                }else {
//                }
                activity?.onBackPressed()
            }
        }
        showLandingView()
    }


    /**
     * 显示落地页view
     */
    private fun showLandingView() {
        //添加headerView
        mModel.getScenes().addLandingHeaderView(requireActivity(), mBinding.headerLayout, style)

        //添加contentView
        mModel.getScenes().addLandingContentView(requireActivity(), mBinding.contentLayout, style)
    }

    private fun getLandingStyle(): ScenesLandingStyle {
        val list = mutableListOf<ScenesLandingStyle>()
        if (ScenesManager.getInstance().isRegisterWifiBody()) {
            list.add(ScenesLandingStyle.SPEED)
        }

        if (ScenesManager.getInstance().isRegisterCleanerBody()
            ||ScenesManager.getInstance().isRegisterPowerBody()) {
            list.add(ScenesLandingStyle.CLEAN)
        }

//        list.add(ScenesLandingStyle.NEWS)
        return list.random()
    }

    private inner class MyBackPressCallback : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
//            if (style == ScenesLandingStyle.NEWS) {
//                ARouter.getInstance().build("/app/main").navigation()
//                return
//            }

            if (mConsumer == null) {
                mModel.isFinish.value = true
            } else {
                mConsumer?.accept(true)
                mConsumer = null
            }
        }
    }

}