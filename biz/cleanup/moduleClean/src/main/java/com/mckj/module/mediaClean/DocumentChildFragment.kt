package com.mckj.module.mediaClean

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mckj.api.data.Constants
import com.mckj.api.entity.JunkInfo
import com.mckj.api.util.FileUtils
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.module.bean.Category
import com.mckj.module.bean.UIMediaCleanBean
import com.mckj.module.manager.CleanManager
import com.mckj.module.utils.EventTrack
import com.mckj.module.utils.McConstants
import com.mckj.module.utils.McUtils
import com.mckj.module.utils.OpenFileUtil
import com.mckj.module.viewbinder.CatogryViewBinder
import com.mckj.module.viewbinder.DocumentChildItemViewBinder
import com.mckj.module.viewmodel.MediaCleanViewModel
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupFragmentDocumentSortCleanBinding
import org.jetbrains.anko.backgroundResource

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/15 14:09
 * @desc
 */
class DocumentChildFragment :
    DataBindingFragment<CleanupFragmentDocumentSortCleanBinding, MediaCleanViewModel>() {
    private var mSortList = ArrayList<Int>()
    private var mIsCheckAll: Boolean = false
    private var mPackageData: Boolean = true
    private var junkList: List<JunkInfo>? = null
    private var mAllJunkList = ArrayList<Any>()//all Junk
    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_document_sort_clean
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
        subscribeUi()
        mModel.mAppJunkLiveData.postValue(mModel.mAppJunk)
    }

    override fun initView() {
        val manager = LinearLayoutManager(requireActivity())
        mBinding.recycleView.apply {
            layoutManager = manager
            adapter = mAdapter
        }
        mBinding.checkboxAll.setOnClickListener {
            if (mIsCheckAll) {
                CleanManager.instance.clearSelectedData()
            } else {
                CleanManager.instance.selectedAllJunk(junkList!!, false)
            }
        }
    }

    private fun refreshBySort(sortType: Int) {
        var tempList: List<JunkInfo>? = null
        when (sortType) {
            McConstants.Sort.SORT_MIN -> {
                tempList = junkList?.sortedBy {
                    it.junkSize
                }
            }
            McConstants.Sort.SORT_MAX -> {
                tempList = junkList?.sortedByDescending {
                    it.junkSize
                }
            }
            McConstants.Sort.SORT_LAST -> {
                tempList = junkList?.sortedBy {
                    it.createTime
                }
            }
            McConstants.Sort.SORT_NEWEST -> {
                tempList = junkList?.sortedByDescending {
                    it.createTime
                }
            }
            McConstants.Filter.FILTER_WORD -> {
                tempList = junkList?.filter {
                    FileUtils.isWord(it.description)
                }
            }
            McConstants.Filter.FILTER_EXCEL -> {
                tempList = junkList?.filter {
                    FileUtils.isExcel(it.description)
                }
            }
            McConstants.Filter.FILTER_PPT -> {
                tempList = junkList?.filter {
                    FileUtils.isPPT(it.description)
                }
            }
            McConstants.Filter.FILTER_PDF -> {
                tempList = junkList?.filter {
                    FileUtils.isPdf(it.description)
                }
            }
            McConstants.Filter.FILTER_TXT -> {
                tempList = junkList?.filter {
                    FileUtils.isTxt(it.description)
                }
            }
            McConstants.Filter.FILTER_OTHER -> {
                tempList = junkList?.filter {
                    !FileUtils.isWord(it.description)
                            && !FileUtils.isExcel(it.description)
                            && !FileUtils.isPPT(it.description)
                            && !FileUtils.isPdf(it.description)
                            && !FileUtils.isPPT(it.description)
                            && !FileUtils.isTxt(it.description)
                }
            }
            McConstants.Filter.FILTER_ALL -> {
                tempList = junkList
            }
        }
        packageData(tempList)
    }

    private fun packageData(junkList: List<JunkInfo>?) {
        mAllJunkList.clear()
        val map = junkList?.groupBy {
            McUtils.getTimeLimit(it.createTime)
        }
        map?.entries?.forEach { (key, value) ->
            if (mPackageData) {
                mAllJunkList.add(Category(true, key, value))
            }
            mAllJunkList.addAll(value)
        }
        mAdapter.items = mAllJunkList
        mAdapter.notifyDataSetChanged()
    }

    private fun subscribeUi() {
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
        CleanManager.instance.mSelectedLiveData.observe(this, {
            mBinding.selectedNum.text = "已选${it.size}个"
            if (it.isNotEmpty() && it.size == junkList?.size) {
                mBinding.checkboxAll.backgroundResource = R.drawable.cleanup_box_select_all_selet
                mIsCheckAll = true
            } else {
                mBinding.checkboxAll.backgroundResource = R.drawable.cleanup_box_select_all_nor
                mIsCheckAll = false
            }
            mAdapter.notifyDataSetChanged()
        })
    }

    private val mAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(
            Category::class.java,
            CatogryViewBinder { isExpand, category, layoutPosition ->
                expand(isExpand, category, layoutPosition)
            })
        adapter.register(JunkInfo::class.java, DocumentChildItemViewBinder(ducumentBlock = {
            EventTrack.stFileOpenPopAppClick(mModel.mType)
            OpenFileUtil.openFile(it, requireActivity())
        }))
        adapter
    }

    private fun expand(
        isExpand: Boolean,
        category: Category,
        position: Int
    ) {
        if (isExpand) {
            mAllJunkList.addAll(position + 1, category.childList)
        } else {
            mAllJunkList.removeAll(category.childList)
        }
        mAdapter.items = mAllJunkList
        mAdapter.notifyDataSetChanged()
    }

    private fun showEmptyView() {
        EventTrack.stManualCleanFileEmptyShow(mModel.mType)
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
        fun newInstance(
            sortList: ArrayList<Int>,
            packageData: Boolean = true
        ): DocumentChildFragment {
            val args = Bundle()
            args.putIntegerArrayList(SORT_LIST, sortList)
            args.putBoolean(PACKAGE_DATA, packageData)
            val fragment = DocumentChildFragment()
            fragment.arguments = args
            return fragment
        }
    }
}