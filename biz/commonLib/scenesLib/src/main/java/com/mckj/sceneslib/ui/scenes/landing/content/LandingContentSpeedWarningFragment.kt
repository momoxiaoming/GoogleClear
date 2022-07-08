package com.mckj.sceneslib.ui.scenes.landing.content

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager


import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentSpeedWarnBinding
import com.mckj.sceneslib.ui.bean.GuideScenesBean
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.util.ScenesAdapterProvider
import com.org.openlib.utils.FragmentUtil
import com.org.proxy.AppProxy
import com.org.proxy.EvAgent

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingContentSpeedWarningFragment :
    DataBindingFragment<ScenesFragmentLandingContentSpeedWarnBinding, ScenesViewModel>() {


    private val mAdapter by lazy {
        ScenesAdapterProvider.crateAdapter(this,mModel,{
            it.setSafetyState(false)
        },{
            it.setSafetyState(false)
        })
    }


    override fun getLayoutId() = R.layout.scenes_fragment_landing_content_speed_warn

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
        val list = mutableListOf<Any>()
        //添加引导页列表
        val guideScenesDataList = mModel.getGuideScenesList()
        if (!guideScenesDataList.isNullOrEmpty()) {
            for (index  in  guideScenesDataList.indices){
                val type =if (index ==0)  GuideScenesBean.SPECIAL_TYPE else  GuideScenesBean.NORMAL_TYPE
                list.add(GuideScenesBean(type,guideScenesDataList[index]))
            }
        }
        val hasGuide = list.isNotEmpty()
        showCurrentStateView(hasGuide)
//        // 广告模板
//        val render = when (guideScenesDataList?.size ?: 0) {
//            0 -> {
//                TwoButtonImageTemplate(object : View.OnClickListener {
//                    override fun onClick(v: View?) {
//                        // tip("否,下一步")
//                        activity?.onBackPressed()
//                    }
//                })
//            }
//            1 -> {
//                CardButtonImageTemplate()
//            }
//            else -> {
//                MiniTileTemplate()
//            }
//        }
        setAdapter(list)
    }

    override fun initView() {
        mBinding.apply {
            speedWarnRecycler.layoutManager = LinearLayoutManager(context)

        }
    }


    private fun showCurrentStateView(state: Boolean) {
        val showNewsState = !false && !state
        mBinding.landingContentNews.isVisible = showNewsState
        mBinding.speedWarnRecycler.isVisible=state
        if (showNewsState) updateNewLayout()

    }

    private fun setAdapter(list: List<Any>) {
        if (mBinding.speedWarnRecycler.adapter == null) {
            mBinding.speedWarnRecycler.adapter = mAdapter
        }
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }

    private fun updateNewLayout() {
//        val newsContentFm =
//            NewsManager.getInstance().getFragment(NewsConfig.NEWS_TYPE_ALL, NewsFrom.FROM_APP_IN)
//        newsContentFm?.let {
//            FragmentUtil.show(
//                childFragmentManager,
//                it,
//                R.id.landing_content_news,
//                "LandingContentNewsFragment"
//            )
//            stNewsLoadSuccess()
//        }
    }

    //新闻显示成功后打点
    private fun stNewsLoadSuccess() {
        val scenes = mModel.getScenes()
        val scenesData = scenes.getData()
        val map = HashMap<String, String>()
        map["what"] = "新闻"
        map["from"] = scenesData.name
        map["type"] = "新闻"

        EvAgent.sendEventMap("landing_page_show", map)
    }


}