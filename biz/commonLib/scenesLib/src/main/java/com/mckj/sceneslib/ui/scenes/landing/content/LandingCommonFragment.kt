package com.mckj.sceneslib.ui.scenes.landing.content

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter

import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.ResourceUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentSpeedBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.model.ScenesLandingCommonViewModel
import com.mckj.sceneslib.ui.viewbinder.LandingGuideViewBinder
import com.org.proxy.EvAgent
@Route(path = ARouterPath.Cleanupx.FRAGMENT_SCENES_COMMON)
class LandingCommonFragment : DataBindingFragment<ScenesFragmentLandingContentSpeedBinding, ScenesLandingCommonViewModel>() {
    override fun getLayoutId() = R.layout.scenes_fragment_landing_content_speed

    override fun getViewModel(): ScenesLandingCommonViewModel {
        return ViewModelProvider(requireActivity()).get(
            ScenesLandingCommonViewModel::class.java
        )
    }

    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(AbstractScenes::class, LandingGuideViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<AbstractScenes> {
                override fun onItemClick(view: View, position: Int, t: AbstractScenes) {
                    St.stLevelGuideClick("album_clean")
                    stItemRecommend(position)

                    t.jumpPage(requireContext()) { accept ->
                        if (accept) mModel.isFinish.value = true
                    }

                }
            }
        })
//        adapter.register(NativeAdEntity::class, NativeAdViewBinder(requireActivity()) { adStatus ->
//        })
        adapter
    }


    override fun initData() {
        val list = mutableListOf<Any>()
        //添加引导页列表
        val guideScenesDataList = mModel.getGuideScenesList()
        if (!guideScenesDataList.isNullOrEmpty()) {
            list.addAll(guideScenesDataList)
        }
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
        val map = HashMap<String, String>()

        map["from"] = ResourceUtil.getString(R.string.scenes_photo_cleaner)
        map["type"] = ScenesType.TYPE_ALBUM_CLEAN.toString()

        EvAgent.sendEventMap("landing_page_show", map)
    }

    private fun showAd() {
//        AdManager.getInstance().showAd(
//            "clean_landing",
//            ViewGroupAdContainer(mBinding.adLayout),
//            this,
//            OSDefaultNativeRender(),
//        ) {
//            when (it.adStatus) {
//                AdStatus.SHOW_SUCCESS -> {
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