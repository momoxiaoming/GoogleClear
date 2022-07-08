package com.mckj.sceneslib.ui.scenes.model.tools

import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.repo.kv.KvSp
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.sceneslib.ui.scenes.model.tools.data.entity.MenuToolsItem
import com.mckj.sceneslib.ui.scenes.model.tools.data.entity.ToolsMenuData
import com.mckj.sceneslib.ui.scenes.model.tools.viewBinder.ToolsMenuViewBinder
import com.mckj.sceneslib.ui.scenes.model.tools.utils.FlashUtils
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.CleanupxFragmentToolsBinding
import com.org.openlib.utils.Log


/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
@Route(path = ARouterPath.Cleanupx.FRAGMENT_TOOLS)
class ToolsFragment : DataBindingFragment<CleanupxFragmentToolsBinding, ToolsViewModel>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val mToolsAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(MenuToolsItem::class.java, ToolsMenuViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuToolsItem> {
                override fun onItemClick(view: View, position: Int, t: MenuToolsItem) {
                    mModel.menuClickListener(requireContext(), t)
                    mModel.updateMenuConfig(adapter, position, t)
                }
            }
        })
        adapter
    }

    override fun getLayoutId() = R.layout.cleanupx_fragment_tools

    override fun getViewModel(): ToolsViewModel {
        return ViewModelProvider(requireActivity(), ToolsViewModelFactory()).get(
            ToolsViewModel::class.java
        )
    }

    override fun initData() {
        Log.i(TAG, "initData: this:$this")
    }

    override fun initView() {
        KvSp.putBool("Magnifier_Tab",false)
        Log.i(TAG, "initView: this:$this")
        //手电筒服务
        FlashUtils.init()
        FlashUtils.open_status = false

        mBinding.headerLayout.headerLayout.setBackgroundColor(Color.parseColor("#40A7FF"))
        mBinding.headerLayout.headerToolbar.apply {
            title = getString(R.string.scenes_tools_box)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
        mBinding.toolsRecycler.layoutManager = GridLayoutManager(context, 2)
    }

    override fun initObserver() {
        super.initObserver()
        Log.i(TAG, "initObserver: this:$this")
        mModel.mToolsDataLiveData.observe(viewLifecycleOwner, Observer {
            setToolsAdapter(it)
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()

        //不可见时关闭手电筒
        FlashUtils.close()
        mToolsAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //不在工具界面关闭手电筒
        FlashUtils.close()
        mToolsAdapter.notifyDataSetChanged()
    }



    private fun setToolsAdapter(toolsData: ToolsMenuData) {
        if (mBinding.toolsRecycler.adapter == null) {
            mBinding.toolsRecycler.adapter = mToolsAdapter
        }
        val list = mutableListOf<Any>()
        toolsData.toolsList?.let {
            list.addAll(it)
        }
        mToolsAdapter.items = list
        mToolsAdapter.notifyDataSetChanged()
    }
}