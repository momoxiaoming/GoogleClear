package com.mckj.sceneslib.ui.scenes.landing

import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.cm.utils.DAttrUtil
import com.org.openlib.help.Consumer
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingNewsBinding
import com.mckj.sceneslib.entity.ScenesLandingStyle
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.org.proxy.EvAgent
import org.jetbrains.anko.backgroundResource

/**
 * Describe:
 *
 * 新的管理落地页展示内容（可滑动），及 步骤
 *
 * 先落地页前广告->落地页内容->落地页后广告
 *
 * Created By yangb on 2021/5/20
 */
class ScenesLandingScrollFragment :
    DataBindingFragment<ScenesFragmentLandingNewsBinding, ScenesViewModel>() {

    //落地页样式
    val style = getLandingStyle()

    companion object {

        const val TAG = "ScenesLandingFragment"

        fun newInstance(consumer: Consumer<Boolean>): ScenesLandingScrollFragment {
            return ScenesLandingScrollFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    /**
     * 回调
     */
    private var mConsumer: Consumer<Boolean>? = null

    override fun getLayoutId() = R.layout.scenes_fragment_landing_news

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        activity?.onBackPressedDispatcher?.addCallback(MyBackPressCallback())
        mBinding.titleLayout.headerLayout.backgroundResource =
            DAttrUtil.getPrimaryColorId(requireContext())
        mBinding.titleLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            setNavigationOnClickListener {
                // stBackKey()
                St.stLevelReturnClose()
                ARouter.getInstance().build("/app/main").navigation()
                mModel.isClickLandBack.value = true
                requireActivity().finish()
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

        if (ScenesManager.getInstance().isRegisterCleanerBody() ||
            ScenesManager.getInstance().isRegisterPowerBody()) {
            list.add(ScenesLandingStyle.CLEAN)
        }

//        list.add(ScenesLandingStyle.NEWS)
        return list.random()
    }

    private inner class MyBackPressCallback : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            stBackKey()
            ARouter.getInstance().build("/app/main").navigation()
            requireActivity().finish()
            mModel.isClickLandBack.value = true
            return
        }
    }

    //返回键打点
    private fun stBackKey(){
        val scenes = mModel.getScenes()
        val scenesData = scenes.getData()
        val map = HashMap<String,String>()
        var landTypes = ""
        scenes.landRecommendTypes.forEach { it->
            landTypes = "$landTypes${scenes.getNameByType(it)}+"
        }
        when(scenes.landContentType){
            ScenesType.LandType.TYPE_NEWS -> map["what"] = "新闻"
            ScenesType.LandType.TYPE_RECOMMEND -> map["what"] = landTypes
        }
        map["from"] = scenesData.name
        map["type"] = "返回"
        EvAgent.sendEventMap("level_return_click",map)
        EvAgent.sendEventMap("landing_page_show",map)
    }

}