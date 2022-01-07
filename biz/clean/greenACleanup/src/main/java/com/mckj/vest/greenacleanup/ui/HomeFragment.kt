package com.mckj.vest.greenacleanup.ui

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.baselib.base.AbstractViewBinder
import com.dn.baselib.base.databinding.DataBindingFragment
import com.dn.datalib.ARouterPath
import com.dn.openlib.utils.FragmentUtil
import com.dn.baselib.util.SizeUtil
import com.dn.openlib.utils.SystemUiUtil
import com.drakeet.multitype.MultiTypeAdapter

import com.mckj.module.cleanup.entity.HomeMenuData
import com.mckj.module.cleanup.util.Log

import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem

import com.mckj.sceneslib.entity.MenuJumpItem
import com.mckj.sceneslib.ui.viewbinder.BusinessFooterViewBinder
import com.mckj.sceneslib.ui.viewbinder.BusinessMenuViewBinder
import com.mckj.sceneslib.ui.viewbinder.HomeMenuViewBinder
import com.mckj.sceneslib.ui.viewbinder.JumpMenuViewBinder
import com.mckj.vest.greenacleanup.R
import com.mckj.vest.greenacleanup.databinding.CleanupFragmentHomeBinding

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
@Route(path = ARouterPath.Cleanup.FRAGMENT_HOME)
open class HomeFragment : DataBindingFragment<CleanupFragmentHomeBinding, HomeViewModel>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val mHomeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(MenuItem::class.java, HomeMenuViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuItem> {
                override fun onItemClick(view: View, position: Int, t: MenuItem) {
                    mModel.menuClickListener(requireContext(), t)
                    mModel.updateMenuConfig(adapter, position, t)
                }
            }
        })
        adapter.register(MenuBusinessItem::class, BusinessMenuViewBinder().also {
            it.itemClickListener =
                object : AbstractViewBinder.OnItemClickListener<MenuBusinessItem> {
                    override fun onItemClick(view: View, position: Int, t: MenuBusinessItem) {
                        mModel.menuClickListener(requireContext(), t)
                        mModel.updateMenuConfig(adapter, position, t)
                    }
                }
        })
        adapter.register(MenuJumpItem::class, JumpMenuViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuJumpItem> {
                override fun onItemClick(view: View, position: Int, t: MenuJumpItem) {
                    mModel.menuClickListener(requireContext(), t)
                    mModel.updateMenuConfig(adapter, position, t)
                }
            }
        })
        adapter.register(Integer::class, BusinessFooterViewBinder())
        adapter
    }

    override fun getLayoutId() = R.layout.cleanup_fragment_home

    override fun getViewModel(): HomeViewModel {
        return ViewModelProvider(requireActivity(), HomeViewModelFactory()).get(
            HomeViewModel::class.java
        )
    }

    override fun initData() {
        Log.i(TAG, "initData: this:$this")
    }

    override fun initView() {
        Log.i(TAG, "initView: this:$this")
        mBinding.homeRecycler.layoutManager = GridLayoutManager(context, 3).also {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val item = mHomeAdapter.items[position]
                    return if (item is MenuBusinessItem || item is MenuJumpItem || item is Int) {
                        3
                    } else {
                        1
                    }
                }
            }
        }
    }

    protected open fun initHeader(containerResId: Int, block: (Int) -> Unit) {
        Log.i(TAG, "initHeader: this:$this")
        //清理首页
        FragmentUtil.show(childFragmentManager, HomeHeaderFragment(), R.id.content_layout)
        block(SizeUtil.dp2px(361f))
    }

    override fun initObserver() {
        super.initObserver()
        Log.i(TAG, "initObserver: this:$this")
        initHeader(R.id.content_layout) {
            val emptyParams = mBinding.emptyView.layoutParams
            emptyParams.height = it
            mBinding.emptyView.layoutParams = emptyParams
            mBinding.emptyView.minimumHeight =
                SizeUtil.dp2px(52f) + SystemUiUtil.getStatusBarHeight(requireContext())

            val contentParams = mBinding.contentLayout.layoutParams
            contentParams.height = it
            mBinding.contentLayout.layoutParams = contentParams
        }
        mModel.mHomeDataLiveData.observe(viewLifecycleOwner, Observer {
            setHomeAdapter(it)
        })
    }

    override fun onResume() {
        super.onResume()
        whatCanIDo()
    }

    protected open fun whatCanIDo() {
        mModel.whatCanIDo(requireContext(), mBinding.emptyLayout)
    }

    private fun setHomeAdapter(homeData: HomeMenuData) {
        if (mBinding.homeRecycler.adapter == null) {
            mBinding.homeRecycler.adapter = mHomeAdapter
        }
        val list = mutableListOf<Any>()
        homeData.homeList?.let {
            list.addAll(it)
        }
        homeData.businessList?.let {
            list.addAll(it)
        }
        homeData.jumpList?.let {
            list.addAll(it)
        }
        list.add(homeData.useDays)
        mHomeAdapter.items = list
        mHomeAdapter.notifyDataSetChanged()
    }

}