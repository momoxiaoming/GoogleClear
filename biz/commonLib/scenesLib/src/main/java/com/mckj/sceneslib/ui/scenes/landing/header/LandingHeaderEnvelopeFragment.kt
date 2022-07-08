package com.mckj.sceneslib.ui.scenes.landing.header

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingHeaderEnvelopeBinding
import com.mckj.sceneslib.entity.DelayTestTaskData
import com.mckj.sceneslib.manager.scenes.model.EnvelopeScenes
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.viewbinder.HostDelayTaskViewBinder

class LandingHeaderEnvelopeFragment :
    DataBindingFragment<ScenesFragmentLandingHeaderEnvelopeBinding, ScenesViewModel>() {


    private var taskList: List<DelayTestTaskData>? = null

    override fun initData() {
        val scenes = mModel.getScenes()
        if (scenes is EnvelopeScenes) {
            taskList = scenes.taskList
        }
    }

    override fun initView() {
        mBinding.rvList.layoutManager = LinearLayoutManager(requireContext())

        //列表适配器
        val mTaskAdapter: MultiTypeAdapter by lazy {
            val binder = HostDelayTaskViewBinder()
            binder.finish = true
            val adapter = MultiTypeAdapter()
            adapter.register(DelayTestTaskData::class, binder)
            adapter
        }
        mBinding.rvList.adapter = mTaskAdapter
        if (!taskList.isNullOrEmpty()) {
            val list = taskList!!.toMutableList()
            list.sort()
            mTaskAdapter.items = list
        }
        mTaskAdapter.notifyDataSetChanged()
    }


    override fun getLayoutId(): Int {
        return R.layout.scenes_fragment_landing_header_envelope
    }

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }
}