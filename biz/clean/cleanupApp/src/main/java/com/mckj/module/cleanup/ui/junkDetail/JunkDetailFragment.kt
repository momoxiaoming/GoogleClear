package com.mckj.module.cleanup.ui.junkDetail

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route

import com.dn.baselib.base.AbstractViewBinder
import com.dn.vi.app.cm.utils.FileUtil
import com.drakeet.multitype.MultiTypeAdapter
import com.dn.baselib.base.databinding.DataBindingFragment
import com.dn.baselib.ext.idToString
import com.dn.baselib.ext.onceClick
import com.dn.datalib.ARouterPath

import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.databinding.CleanupFragmentJunkDetailBinding
import com.mckj.module.cleanup.entity.MenuJunkChild
import com.mckj.module.cleanup.entity.MenuJunkParent
import com.mckj.module.cleanup.ui.viewbinder.JunkDetailChildViewHolder
import com.mckj.module.cleanup.ui.viewbinder.JunkDetailParentViewHolder

import com.mckj.sceneslib.manager.AutoScanManager

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
@Route(path = ARouterPath.Cleanup.FRAGMENT_JUNK_DETAIL)
class JunkDetailFragment :
    DataBindingFragment<CleanupFragmentJunkDetailBinding, JunkDetailViewModel>() {

    companion object {
        const val TAG = "JunkDetailFragment"
    }

    private val mAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(MenuJunkParent::class.java, JunkDetailParentViewHolder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuJunkParent> {
                override fun onItemClick(view: View, position: Int, t: MenuJunkParent) {
                    when (view.id) {
                        R.id.item_check_iv -> {
                            //选中
                            mModel.select(t)
                        }
                        else -> {
                            mModel.expand(t)
                        }
                    }
                }
            }
        })
        adapter.register(MenuJunkChild::class, JunkDetailChildViewHolder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuJunkChild> {
                override fun onItemClick(view: View, position: Int, t: MenuJunkChild) {
                    mModel.select(t)
                }
            }
        })
        adapter
    }

    override fun getLayoutId() = R.layout.cleanup_fragment_junk_detail

    override fun getViewModel(): JunkDetailViewModel {
        return ViewModelProvider(this, JunkDetailViewModelFactory()).get(
            JunkDetailViewModel::class.java
        )
    }

    override fun initData() {
        activity?.title = idToString(R.string.cleanup_junk_detail_title)
        mModel.init(AutoScanManager.getInstance().getScanData().list)
    }

    override fun initView() {
        mBinding.junkDetailRecycler.layoutManager = LinearLayoutManager(context)

        mBinding.junkDetailBtn.onceClick {
            mModel.clean(requireContext())
        }
    }

    override fun initObserver() {
        super.initObserver()
        mModel.mDetailLiveData.observe(viewLifecycleOwner, Observer {
            setAdapter(it)
        })
        mModel.mSelectSizeLiveData.observe(viewLifecycleOwner, Observer {
            mBinding.junkDetailBtn.text = " ${FileUtil.getFileSizeText(it)}"
        })
    }

    private fun setAdapter(list: List<Any>) {
        if (mBinding.junkDetailRecycler.adapter == null) {
            mBinding.junkDetailRecycler.adapter = mAdapter
        }
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }

}