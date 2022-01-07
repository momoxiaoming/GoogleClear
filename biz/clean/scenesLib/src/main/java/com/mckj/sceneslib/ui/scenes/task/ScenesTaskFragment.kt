package com.mckj.sceneslib.ui.scenes.task

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.sceneslib.R
import com.mckj.sceneslib.entity.ScenesTask
import com.mckj.sceneslib.entity.ScenesTaskData
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.viewbinder.ScenesTaskViewBinder
import com.dn.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.databinding.ScenesFragmentTaskBinding

/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class ScenesTaskFragment : DataBindingFragment<ScenesFragmentTaskBinding, ScenesViewModel>() {

    companion object {
        const val TAG = "ScenesAnimFragment"
    }

    private val mTaskAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(ScenesTask::class, ScenesTaskViewBinder())
        adapter
    }

    override fun getLayoutId() = R.layout.scenes_fragment_task

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        mBinding.taskRecycler.layoutManager = LinearLayoutManager(context)
    }

    override fun initObserver() {
        super.initObserver()
        mModel.taskDataLiveData.observe(viewLifecycleOwner) { data ->
            setData(data)
        }
    }

    private fun setData(data: ScenesTaskData?) {
        if (data == null) {
            return
        }
        mBinding.taskTitleTv.text = data.title
        val desc = data.desc
        if (desc.isEmpty()) {
            mBinding.taskDescTv.gone()
        } else {
            mBinding.taskDescTv.text = desc
            mBinding.taskDescTv.show()
        }
        setAdapter(data.taskList)
    }

    private fun setAdapter(list: List<ScenesTask>?) {
        if (mBinding.taskRecycler.adapter == null) {
            mBinding.taskRecycler.adapter = mTaskAdapter
        }
        mTaskAdapter.items = list ?: emptyList()
        mTaskAdapter.notifyDataSetChanged()
    }

}