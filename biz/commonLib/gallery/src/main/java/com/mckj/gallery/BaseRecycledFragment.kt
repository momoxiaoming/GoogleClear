package com.mckj.gallery

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.job.GalleryManager
import com.mckj.gallery.viewbinder.RecycledViewBinder
import com.mckj.gallery.viewmodel.RecycledViewModel
import com.mckj.gallery.bean.ARouterPath
import com.mckj.gallery.databinding.CleanupxFragmentRecycledBinding

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/21 19:24
 * @desc
 */
class BaseRecycledFragment :
    DataBindingFragment<CleanupxFragmentRecycledBinding, RecycledViewModel>() {

    companion object {
        private const val MIME_TYPE = "MIME_TYPE"
        fun newInstance(mimeType: String): BaseRecycledFragment {
            val args = Bundle()
            args.putString(MIME_TYPE, mimeType)
            val fragment = BaseRecycledFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mimeType: String
    private val mAdapter = MultiTypeAdapter()
    private val mData = mutableListOf<RecycledBean>()
    private val mSelectedData = ArrayList<RecycledBean>()
    private var mChecked: Boolean = false

    override fun getLayoutId(): Int {
        return R.layout.cleanupx_fragment_recycled
    }

    override fun getViewModel(): RecycledViewModel {
        val model: RecycledViewModel by activityViewModels()
        return model
    }

    override fun initData() {
        arguments?.let {
            mimeType = it.getString(MIME_TYPE,"")
            val tip: String = if (mimeType.startsWith("image")) {
                getString(R.string.gallery_tips_7_image)
            } else {
                getString(R.string.gallery_tips_7_video)
            }
            mBinding.tip.text = tip
            mBinding.emptyTip.text = tip

        }
        subscribeData()
    }


    override fun initView() {
        initRecycleView()
        mBinding.checkboxAll.setOnClickListener {
            mChecked = !mChecked
            if (mChecked) {
                mBinding.checkboxAll.background =
                    ResourceUtil.getDrawable(R.drawable.cleanupx_checkbox_selected)
            } else {
                mBinding.checkboxAll.background =
                    ResourceUtil.getDrawable(R.drawable.cleanupx_checkbox_white_unselectd)
            }
            GalleryManager.instance.selectedAllRecycledBean(
                mData,
                !mChecked
            )
        }
    }

    private fun subscribeData() {
        val recycledMimeTypeData = mModel.getRecycledMimeTypeData(mimeType)
        recycledMimeTypeData.observe(requireActivity(), {
            mData.clear()
            mData.addAll(it)
            if (mData.isNullOrEmpty()) {
                mBinding.emptyView.show()
                mBinding.tip.gone()
                mBinding.countLayout.gone()
                mBinding.recycleList.gone()
            } else {
                mBinding.emptyView.gone()
                mBinding.tip.show()
                mBinding.countLayout.show()
                mBinding.recycleList.show()
            }
            mAdapter.items = mData
            mAdapter.notifyDataSetChanged()
        })
        GalleryManager.instance.mSelectedData.observe(this, {
            mSelectedData.clear()
            mSelectedData.addAll(it)
            var count = 0
            for (bean in mData) {
                if (it.contains(bean) && bean.mimeType == mimeType) {
                    if (bean.mimeType == mimeType) count++
                }
            }
            mBinding.count.text = String.format(getString(R.string.gallery_selected_x),count)
            if (count == mData.size && !mChecked && mData.isNotEmpty()) {
                mBinding.checkboxAll.background =
                    ResourceUtil.getDrawable(R.drawable.cleanupx_checkbox_selected)
                mChecked = true
            } else if (count != mData.size && mChecked) {
                mBinding.checkboxAll.background =
                    ResourceUtil.getDrawable(R.drawable.cleanupx_checkbox_white_unselectd)
                mChecked = false
            }
            mAdapter.notifyDataSetChanged()
        })
    }

    private fun initRecycleView() {
        mAdapter.register(RecycledBean::class, RecycledViewBinder().also {
            it.itemClickListener =
                object : AbstractViewBinder.OnItemClickListener<RecycledBean> {
                    override fun onItemClick(view: View, position: Int, t: RecycledBean) {
                        val bundle = Bundle()
                        bundle.putParcelable(PreviewActivity.SELECTED_BEAN, t)
                        bundle.putString(PreviewActivity.TYPE, mimeType)
                        ARouter.getInstance().build(ARouterPath.GALLERY_PREVIEW_PATH)
                            .withBundle(PreviewActivity.BUNDLE_KEY, bundle)
                            .navigation()
                    }
                }
            it.itemCheckListener =
                object : RecycledViewBinder.OnItemCheckCListener<RecycledBean> {
                    override fun onCheckItem(view: View, position: Int, t: RecycledBean) {
                        GalleryManager.instance.checkChangeRecycledBean(t)
                    }

                }
        })
        val manager = GridLayoutManager(requireActivity(), 3)
        mBinding.recycleList.layoutManager = manager
        initDecoration()
        mBinding.recycleList.adapter = mAdapter
    }

    //处理分割线
    private fun initDecoration() {
        mBinding.recycleList.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                RecyclerView.HORIZONTAL
            ).also { decor ->
                decor.setDrawable(DividerDrawable(SizeUtil.dp2px(8f)).also { })
            })
    }

}