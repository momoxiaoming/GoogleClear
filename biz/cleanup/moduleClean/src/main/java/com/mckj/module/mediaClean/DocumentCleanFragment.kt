package com.mckj.module.mediaClean

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.mckj.api.client.JunkConstants
import com.dn.vi.app.cm.utils.FileUtil
import com.google.android.material.tabs.TabLayout
import com.mckj.api.entity.JunkInfo
import com.mckj.api.util.FileUtils
import com.mckj.api.util.RFileUtils
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.view.ToastUtil
import com.mckj.module.event.UpdateAppJunkEvent
import com.mckj.module.manager.CleanManager
import com.mckj.module.utils.EventTrack
import com.mckj.module.utils.McConstants
import com.mckj.module.utils.OpenFileUtil
import com.mckj.module.utils.UIHelper
import com.mckj.module.viewmodel.MediaCleanViewModel
import com.mckj.module.widget.TxTab
import com.mckj.module.widget.ViewPagerAdapter
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupFragmentDocumentCleanBinding
import org.greenrobot.eventbus.EventBus

class DocumentCleanFragment :
    DataBindingFragment<CleanupFragmentDocumentCleanBinding, MediaCleanViewModel>() {

    private var mCurrentPosition: Int = 0

    private var mTabs = mutableListOf<TxTab>()
    private var mUIHelper = UIHelper()
    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_document_clean
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
        subscribeUi()
    }


    override fun initView() {
        buildTabs()
        registerListener()
    }

    private fun subscribeUi() {
        CleanManager.instance.mSelectedLiveData.observe(this, {
            if (it.isEmpty()) {
                mBinding.share.isClickable = false
                mBinding.share.alpha = 0.5f
                mBinding.delete.isClickable = false
                mBinding.delete.alpha = 0.5f
            } else {
                mBinding.share.isClickable = true
                mBinding.share.alpha = 1.0f
                mBinding.delete.isClickable = true
                mBinding.delete.alpha = 1.0f
            }
        })
    }

    private fun buildTabs() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(DocumentChildFragment.newInstance(mUIHelper.getDateSortList()))
        fragmentList.add(DocumentChildFragment.newInstance(mUIHelper.getSizeSortList(), false))
        fragmentList.add(
            DocumentChildFragment.newInstance(
                mUIHelper.getFileTypeFilterList(),
                false
            )
        )
        mBinding.viewpager.adapter =
            ViewPagerAdapter(fragmentList, requireActivity().supportFragmentManager)
        mBinding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                mCurrentPosition = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                (tab?.customView as TxTab).popWindow()
            }
        })
        mBinding.tabLayout.setupWithViewPager(mBinding.viewpager)

        val count = mBinding.viewpager.adapter?.count ?: 0
        mTabs.add(
            mUIHelper.buildTabItem(
                "文件日期",
                mUIHelper.buildDateSortList(newest = {
                    EventTrack.stFileDateLatelyClick(mModel.mType)
                    mModel.mSortType.postValue(McConstants.Sort.SORT_NEWEST)
                }, last = {
                    EventTrack.stFileDateOldestClick(mModel.mType)
                    mModel.mSortType.postValue(McConstants.Sort.SORT_LAST)
                })
            )
        )
        mTabs.add(mUIHelper.buildTabItem("文件大小", mUIHelper.buildSizeSortList(max = {
            EventTrack.stFileSizeLargestClick(mModel.mType)
            mModel.mSortType.postValue(McConstants.Sort.SORT_MAX)
        }, min = {
            EventTrack.stFileSizeSmallestClick(mModel.mType)
            mModel.mSortType.postValue(McConstants.Sort.SORT_MIN)
        })))
        mTabs.add(
            mUIHelper.buildTabItem(
                "文件类型",
                mUIHelper.buildMimeTypeSortList(word = {
                    EventTrack.stFileTypeClick(mModel.mType)
                    mModel.mSortType.postValue(McConstants.Filter.FILTER_WORD)
                }, excel = {
                    EventTrack.stFileTypeClick(mModel.mType)
                    mModel.mSortType.postValue(McConstants.Filter.FILTER_EXCEL)
                }, ppt = {
                    EventTrack.stFileTypeClick(mModel.mType)
                    mModel.mSortType.postValue(McConstants.Filter.FILTER_PPT)
                }, pdf = {
                    EventTrack.stFileTypeClick(mModel.mType)
                    mModel.mSortType.postValue(McConstants.Filter.FILTER_PDF)
                }, txt = {
                    EventTrack.stFileTypeClick(mModel.mType)
                    mModel.mSortType.postValue(McConstants.Filter.FILTER_TXT)
                }, other = {
                    EventTrack.stFileTypeClick(mModel.mType)
                    mModel.mSortType.postValue(McConstants.Filter.FILTER_OTHER)
                })
            )
        )
        for (i in 0 until count) {
            val tab = mBinding.tabLayout.getTabAt(i)
            tab?.customView = mTabs[i]
        }
        mBinding.viewpager.offscreenPageLimit = 3
        mBinding.viewpager.currentItem = 0
    }


    private fun registerListener() {
        //打开文件
        mBinding.share.setOnClickListener {
            val selectedList = CleanManager.instance.mSelectedList
            if (selectedList.isEmpty()) return@setOnClickListener
            if (selectedList.size > 1) {
                ToastUtil.postShow("该操作只能选择1个文件")
                return@setOnClickListener
            }
            val junkInfo = selectedList[0]
            if (junkInfo.uri != null) {
                EventTrack.stFileShare(mModel.mType)
                OpenFileUtil.shareFile(requireActivity(), junkInfo.uri!!)
            } else {
                EventTrack.stFileShare(mModel.mType)
                OpenFileUtil.shareFile(requireActivity(), junkInfo.path)
            }
        }
        //删除
        mBinding.delete.setOnClickListener {
            val selectedList = CleanManager.instance.mSelectedList
            if (selectedList.isEmpty()) return@setOnClickListener
            EventTrack.stFileDelete(mModel.mType)
            mUIHelper.showDocumentDeleteDialog(requireActivity(), selectedList, {
                EventTrack.stFileDeletePopDeleteClick(mModel.mType)
                selectedList.forEach { junkDetailEntity ->
                    delete(junkDetailEntity)
                    if (mModel.mAppJunk?.junks?.contains(junkDetailEntity) == true) {
                        (mModel.mAppJunk?.junks as MutableList).remove(junkDetailEntity)
                        mModel.mAppJunk?.junkSize =
                            mModel.mAppJunk?.junkSize!! - junkDetailEntity.junkSize
                        mModel.mAppJunkLiveData.postValue(mModel.mAppJunk)
                    }
                    EventBus.getDefault().post(
                        UpdateAppJunkEvent(
                            type = JunkConstants.AppCacheType.DOCUMENT_CACHE,
                            mModel.mAppJunk!!
                        )
                    )
                }
                CleanManager.instance.clearSelectedData(selectedList)
            }, {
                EventTrack.stFileDeletePopCancleClick(mModel.mType)
            })
        }
    }

    private fun delete(junkInfo: JunkInfo): Boolean {
        junkInfo.uri?.let {
            return RFileUtils.deleteFile(it)
        } ?: let {
            return FileUtils.delete(junkInfo.path)
        }
    }
}