package com.mckj.module.mediaClean

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mckj.api.entity.JunkInfo
import com.mckj.gallery.bean.MediaBean
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.bean.Category
import com.mckj.module.manager.CleanManager
import com.mckj.module.utils.EventTrack
import com.mckj.module.utils.McConstants
import com.mckj.module.utils.McUtils
import com.mckj.module.viewbinder.CatogryViewBinder
import com.mckj.module.viewbinder.MediaChildItemViewBinder
import com.mckj.module.viewmodel.MediaCleanViewModel
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupFragmentMediaCleanBinding
import org.jetbrains.anko.backgroundResource

class MediaCleanFragment :
    DataBindingFragment<CleanupFragmentMediaCleanBinding, MediaCleanViewModel>() {

    private var mIsCheckAll: Boolean = false

    private var mAllImgJunkList = ArrayList<Any>()//all Junk

    private var mSortList = ArrayList<Int>()

    private var mPackageData: Boolean = true

    private var junkList: List<JunkInfo>? = null//原数据

    private var mMediaList = ArrayList<MediaBean>()

    private var mimeType: String = IMG

    var mType: Int = 0//qq,wx(埋点区分)

    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_media_clean
    }

    override fun getViewModel(): MediaCleanViewModel {
        return ViewModelProvider(
            requireActivity(),
            MediaCleanViewModel.MediaCleanViewModelFactory()
        ).get(
            MediaCleanViewModel::class.java
        )
    }

    override fun initData() {
        mSortList = arguments?.getIntegerArrayList(SORT_LIST) as ArrayList<Int>//排序规则
        mPackageData = arguments?.getBoolean(PACKAGE_DATA) ?: true
        mimeType = arguments?.getString(MIME_TYPE, IMG) ?: IMG
        subscribeUi()
        mModel.mAppJunkLiveData.postValue(mModel.mAppJunk)
        when (mimeType) {
            IMG -> mBinding.selectedNum.text = "已选${0}张"
            VIDEO -> mBinding.selectedNum.text = "已选${0}个"
        }
    }


    override fun initView() {
        mBinding.recycleView.layoutManager = GridLayoutManager(context, 3).also {
            it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (mAdapter.items[position]) {
                        is Category -> 3
                        else -> 1
                    }
                }
            }
        }

        mBinding.checkboxAll.setOnClickListener {
            if (mIsCheckAll) {
                CleanManager.instance.clearSelectedData()
            } else {
                CleanManager.instance.selectedAllJunk(junkList!!, false)
            }
        }

    }

    private fun subscribeUi() {
        CleanManager.instance.mSelectedLiveData.observe(this, {
            when (mimeType) {
                IMG -> mBinding.selectedNum.text = "已选${it.size}张"
                VIDEO -> mBinding.selectedNum.text = "已选${it.size}个"
            }

            if (it.isNotEmpty() && it.size == junkList?.size) {
                mBinding.checkboxAll.backgroundResource = R.drawable.cleanup_box_select_all_selet
                mIsCheckAll = true
            } else {
                mBinding.checkboxAll.backgroundResource = R.drawable.cleanup_box_select_all_nor
                mIsCheckAll = false
            }
            mAdapter.notifyDataSetChanged()
        })
        mModel.mSortType.observe(requireActivity(), {
            if (mSortList.contains(it)) {
                refreshBySort(it)
            }
        })
        mModel.mAppJunkLiveData.observe(requireActivity(), {
            junkList = mModel.mAppJunk?.junks
            if (junkList.isNullOrEmpty()) {
                showEmptyView()
            } else {
                showDataView()
            }
            refreshBySort(mSortList[0])
        })
    }

    private fun refreshBySort(sortType: Int) {
        when (sortType) {
            McConstants.Sort.SORT_MIN -> {
                junkList = junkList?.sortedBy {
                    it.junkSize
                }
            }
            McConstants.Sort.SORT_MAX -> {
                junkList = junkList?.sortedByDescending {
                    it.junkSize
                }
            }
            McConstants.Sort.SORT_LAST -> {
                junkList = junkList?.sortedBy {
                    it.createTime
                }
            }
            McConstants.Sort.SORT_NEWEST -> {
                junkList = junkList?.sortedByDescending {
                    it.createTime
                }
            }
        }
        packageData(junkList)
    }

    private fun packageData(junkList: List<JunkInfo>?) {
        if (mBinding.recycleView.adapter == null) {
            mBinding.recycleView.adapter = mAdapter
        }
        mMediaList.clear()
        val map = junkList?.groupBy {
            McUtils.getTimeLimit(it.createTime)
        }
        mAllImgJunkList.clear()
        map?.entries?.forEach { (key, value) ->
            if (mPackageData) {
                mAllImgJunkList.add(Category(true, key, value))
            }
            mAllImgJunkList.addAll(value)
        }
        mAdapter.items = mAllImgJunkList
        mAdapter.notifyDataSetChanged()
    }


    private val mAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(
            Category::class.java,
            CatogryViewBinder { isExpand, category, layoutPosition ->
                expand(isExpand, category, layoutPosition)
            })
        adapter.register(JunkInfo::class.java, MediaChildItemViewBinder() {
//                jumpToGallery(it)
        })
        adapter
    }

    private fun expand(
        isExpand: Boolean,
        category: Category,
        position: Int
    ) {
        if (isExpand) {
            mAllImgJunkList.addAll(position + 1, category.childList)
        } else {
            mAllImgJunkList.removeAll(category.childList)
        }
        mAdapter.items = mAllImgJunkList
        mAdapter.notifyDataSetChanged()
    }


    private fun showEmptyView() {
        when (mimeType) {
            IMG -> {
                EventTrack.stManualCleanPictureEmptyShow(mModel.mType)
                mBinding.emptyTip.text = "暂无图片"
                mBinding.emptyIcon.background =
                    ResourceUtil.getDrawable(R.drawable.cleanup_images_recycle_empty)
            }
            VIDEO -> {
                EventTrack.stManualCleanVideoEmptyShow(mModel.mType)
                mBinding.emptyTip.text = "暂无视频"
                mBinding.emptyIcon.background =
                    ResourceUtil.getDrawable(R.drawable.cleanup_video_recycle_empty)
            }
        }
        mBinding.emptyView.show()
        mBinding.topLl.gone()
        mBinding.recycleView.gone()
    }

    private fun showDataView() {
        mBinding.emptyView.gone()
        mBinding.topLl.show()
        mBinding.recycleView.show()
    }

    companion object {
        private const val SORT_LIST = "SORT_LIST"
        private const val PACKAGE_DATA = "PACKAGE_DATA"
        private const val MIME_TYPE = "mimeType"
        const val IMG = "IMG"
        const val VIDEO = "VIDEO"
        fun newInstance(
            sortList: ArrayList<Int>,
            packageData: Boolean = true,
            mimeType: String
        ): MediaCleanFragment {
            val args = Bundle()
            args.putIntegerArrayList(SORT_LIST, sortList)
            args.putBoolean(PACKAGE_DATA, packageData)
            args.putString(MIME_TYPE, mimeType)
            val fragment = MediaCleanFragment()
            fragment.arguments = args
            return fragment
        }
    }
}