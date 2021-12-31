package com.mckj.vest.defaultwifi.ui

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.module.wifi.entity.HomeMenuData
import com.mckj.module.wifi.ui.wifi.WifiViewModel
import com.mckj.module.wifi.ui.wifi.WifiViewModelFactory
import com.mckj.openlib.util.FragmentUtil
import com.mckj.openlib.util.SystemUiUtil
import com.mckj.sceneslib.entity.MenuBusinessItem
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.entity.MenuJumpItem
import com.mckj.sceneslib.ui.viewbinder.BusinessFooterViewBinder
import com.mckj.sceneslib.ui.viewbinder.BusinessMenuViewBinder
import com.mckj.sceneslib.ui.viewbinder.HomeMenuViewBinder
import com.mckj.sceneslib.ui.viewbinder.JumpMenuViewBinder
import com.mckj.vest.defaultwifi.R
import com.mckj.vest.defaultwifi.databinding.WifiFragmentHomeBinding

/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
@Route(path = ARouterPath.Wifi.FRAGMENT_HOME)
open class HomeFragment : DataBindingFragment<WifiFragmentHomeBinding, WifiViewModel>() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val mHomeModel by lazy {
        ViewModelProvider(requireActivity(), HomeViewModelFactory()).get(
            HomeViewModel::class.java
        )
    }

    private val mHomeAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(MenuItem::class.java, HomeMenuViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuItem> {
                override fun onItemClick(view: View, position: Int, t: MenuItem) {
                    mModel.menuClickListener(requireActivity(), t)
                    mHomeModel.updateMenuConfig(adapter, position, t)
                }
            }
        })
        adapter.register(MenuBusinessItem::class, BusinessMenuViewBinder().also {
            it.itemClickListener =
                object : AbstractViewBinder.OnItemClickListener<MenuBusinessItem> {
                    override fun onItemClick(view: View, position: Int, t: MenuBusinessItem) {
                        mModel.menuClickListener(requireActivity(), t)
                        mHomeModel.updateMenuConfig(adapter, position, t)
                    }
                }
        })
        adapter.register(MenuJumpItem::class, JumpMenuViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuJumpItem> {
                override fun onItemClick(view: View, position: Int, t: MenuJumpItem) {
                    mModel.menuClickListener(requireActivity(), t)
                    mHomeModel.updateMenuConfig(adapter, position, t)
                }
            }
        })
        adapter.register(Integer::class, BusinessFooterViewBinder())
        adapter
    }

    override fun getLayoutId() = R.layout.wifi_fragment_home

    override fun getViewModel(): WifiViewModel {
        return ViewModelProvider(requireActivity(), WifiViewModelFactory()).get(
            WifiViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
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
        //wifi首页
        FragmentUtil.show(childFragmentManager, HomeHeaderFragment(), containerResId)
        block(SizeUtil.dp2px(361f))
    }

    override fun initObserver() {
        super.initObserver()
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
        mHomeModel.mHomeDataLiveData.observe(viewLifecycleOwner, Observer {
            setHomeAdapter(it)
        })
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