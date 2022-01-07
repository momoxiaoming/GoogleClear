package com.mckj.sceneslib.ui.scenes.landing.content

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dn.baselib.base.AbstractViewBinder
import com.drakeet.multitype.MultiTypeAdapter

import com.dn.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentSpeedBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.viewbinder.LandingGuideViewBinder

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
                    t.jumpPage(requireContext())
                    mModel.isFinish.value = true
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
        if (!guideScenesDataList.isNullOrEmpty()) {
            list.addAll(guideScenesDataList)
        }
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
//        val adName = mModel.getJumpData().landingAdName ?: ""
//        //添加广告位
//        list.add(NativeAdEntity(adName, render))
        setAdapter(list)
    }

    override fun initView() {
        mBinding.speedRecycler.layoutManager = LinearLayoutManager(context)
    }

    private fun setAdapter(list: List<Any>) {
        if (mBinding.speedRecycler.adapter == null) {
            mBinding.speedRecycler.adapter = mAdapter
        }
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }

}