package com.mckj.sceneslib.ui.scenes.landing.content

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter

import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentSpeedBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.viewbinder.LandingGuideViewBinder
import com.org.proxy.EvAgent

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingContentSpeedFragment :
    DataBindingFragment<ScenesFragmentLandingContentSpeedBinding, ScenesViewModel>() {

    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(AbstractScenes::class, LandingGuideViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<AbstractScenes> {
                override fun onItemClick(view: View, position: Int, t: AbstractScenes) {
                    St.stLevelGuideClick(mModel.getScenesData().key)
                    stItemRecommend(position)

                    t.jumpPage(requireContext()) { accept ->
                        if (accept) mModel.isFinish.value = true
                    }
                    //yaoshen
//                    mModel.isFinish.value = true
                }
            }
        })
//        adapter.register(NativeAdEntity::class, NativeAdViewBinder(requireActivity()) { adStatus ->
//        })
        adapter
    }

    override fun getLayoutId() = R.layout.scenes_fragment_landing_content_speed

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
        val list = mutableListOf<Any>()
        //添加引导页列表
        val guideScenesDataList = mModel.getGuideScenesList()
        // 广告模板
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
        showAd()
    }

    override fun initView() {
        mBinding.speedRecycler.layoutManager = LinearLayoutManager(context)
    }

    private fun setAdapter(list: List<Any>) {
        if (mBinding.speedRecycler.adapter == null) {
            mBinding.speedRecycler.adapter = mAdapter
        }
        mAdapter.items = list
        if (list.isEmpty()) {
            mBinding.recommond.visibility = View.GONE
        }
        mAdapter.notifyDataSetChanged()
    }

    //单个推荐菜单点击打点
    private fun stItemRecommend(position: Int) {
        val scenes = mModel.getScenes()
        val scenesData = scenes.getData()
        val map = HashMap<String, String>()
        var landTypes = ""
        scenes.landRecommendTypes.forEach { it ->
            landTypes = "$landTypes${scenes.getNameByType(it)}+"
        }
        when (scenes.landContentType) {
            ScenesType.LandType.TYPE_NEWS -> map["what"] = "新闻"
            ScenesType.LandType.TYPE_RECOMMEND -> map["what"] = landTypes
        }
        map["from"] = scenesData.name
        map["type"] = scenes.getNameByType(scenes.landRecommendTypes[position])

        EvAgent.sendEventMap("landing_page_show", map)
    }

    private fun showAd() {
//        AdManager.getInstance().showAd(
//            mModel.mJumpData.landingAdName ?: "",
//            ViewGroupAdContainer(mBinding.adLayout),
//            this,
//            OSDefaultNativeRender(),
//        ) {
//            when (it.adStatus) {
//                AdStatus.SHOW_SUCCESS -> {
//                    St.stLevelMsgShow()
//                    mBinding.adLayout.show()
//                }
//                AdStatus.ERROR -> {
//                    mBinding.adLayout.gone()
//                }
//                AdStatus.CLICK -> {
//
//                }
//                AdStatus.CLOSE -> {
//                    mBinding.adLayout.gone()
//                }
//            }
//        }
    }
}