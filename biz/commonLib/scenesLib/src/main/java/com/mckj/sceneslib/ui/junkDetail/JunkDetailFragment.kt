package com.mckj.sceneslib.ui.junkDetail

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.kt.getAs
import com.dn.vi.app.base.helper.DataTransport
import com.dn.vi.app.cm.utils.FileUtil
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.api.entity.AppJunk
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.datalib.entity.ARouterPath
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.CleanupFragmentJunkDetailBinding
import com.mckj.sceneslib.entity.MenuJunkChild
import com.mckj.sceneslib.entity.MenuJunkParent
import com.mckj.sceneslib.ui.viewbinder.JunkDetailChildViewHolder
import com.mckj.sceneslib.ui.viewbinder.JunkDetailParentViewHolder

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
        activity?.title = getString(R.string.scenes_junk_details)
        val appJunk = DataTransport.getInstance().getAs<MutableList<AppJunk>>("junk_list")
        mModel.init(appJunk)
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
            mBinding.junkDetailBtn.text = String.format(getString(R.string.scenes_clean_x_file),FileUtil.getFileSizeText(it))
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