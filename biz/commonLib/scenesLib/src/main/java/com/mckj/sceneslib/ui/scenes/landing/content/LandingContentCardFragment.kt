package com.mckj.sceneslib.ui.scenes.landing.content

import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter

import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentCardBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.viewbinder.LandingGuideCardViewBinder

/**
 * Describe:
 *
 * Created By yangb on 2021/5/24
 */
class LandingContentCardFragment :
    DataBindingFragment<ScenesFragmentLandingContentCardBinding, ScenesViewModel>() {

    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(AbstractScenes::class, LandingGuideCardViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<AbstractScenes> {
                override fun onItemClick(view: View, position: Int, t: AbstractScenes) {
                    St.stLevelGuideClick(mModel.getScenesData().key)
                    t.jumpPage(requireContext())
                    mModel.isFinish.value = true
                }
            }
        })
//        adapter.register(NativeAdEntity::class, NativeAdViewBinder(requireActivity()) { adStatus ->
//
//        })
        adapter
    }

    override fun getLayoutId() = R.layout.scenes_fragment_landing_content_card

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
        val list = mutableListOf<Any>()
        //添加引导页列表
        val guideList = mModel.getGuideScenesList()
        if (!guideList.isNullOrEmpty()) {
            list.addAll(guideList)
        }
        val adName = mModel.getJumpData().landingAdName ?: ""
//        val render = CardBottomButtonImageTemplate()
//        when (list.size) {
//            2 -> list.add(1, NativeAdEntity(adName, render))
//            else -> list.add(NativeAdEntity(adName, render))
//        }
        setAdapter(list)
    }

    override fun initView() {
        mBinding.cardRecycler.layoutManager = LinearLayoutManager(context)
    }

    private fun setAdapter(list: List<Any>) {
        if (mBinding.cardRecycler.adapter == null) {
            mBinding.cardRecycler.adapter = mAdapter
        }
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }
}