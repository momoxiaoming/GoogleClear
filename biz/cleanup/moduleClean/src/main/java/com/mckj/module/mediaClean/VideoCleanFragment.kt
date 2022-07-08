package com.mckj.module.mediaClean

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.mckj.api.client.JunkConstants
import com.mckj.api.entity.JunkInfo
import com.mckj.gallery.impl.VideoImpl
import com.dn.vi.app.base.helper.StepRunner
import com.google.android.material.tabs.TabLayout
import com.mckj.api.util.ScopeHelper
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.helper.getApplicationContext
import com.mckj.gallery.bean.GalleryConstants
import com.mckj.gallery.job.JobChain
import com.mckj.gallery.job.recycled.RecycledJodCreate
import com.mckj.module.bean.ChildFragment
import com.mckj.module.event.UpdateAppJunkEvent
import com.mckj.module.manager.CleanManager
import com.mckj.module.queue.MdRemover
import com.mckj.module.utils.EventTrack
import com.mckj.module.utils.McConstants
import com.mckj.module.utils.UIHelper
import com.mckj.module.viewmodel.MediaCleanViewModel
import com.mckj.module.widget.TxTab
import com.mckj.module.widget.ViewPagerAdapter
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupFragmentVideoCleanBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import kotlin.coroutines.resume

class VideoCleanFragment :
    DataBindingFragment<CleanupFragmentVideoCleanBinding, MediaCleanViewModel>() {
    private var mCurrentPosition: Int = 0

    private var mTabs = mutableListOf<TxTab>()
    private var mUIHelper = UIHelper()
    private var removeBlock: (() -> Unit?)? = null
    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_video_clean
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
        mBinding.delete.setOnClickListener {
            val selectedList = CleanManager.instance.mSelectedList
            if (selectedList.isEmpty()) return@setOnClickListener
            EventTrack.stVideoDeleteClick(mModel.mType, selectedList.size.toString())
            mUIHelper.showVideoDeleteDialog(requireActivity(), selectedList, {
                EventTrack.stVideoPopDeleteClick(mModel.mType)
                showLoading("正在删除中...")
                setCanceledOnTouchOutside(false)
                removeMedia()
            }, negative = {
                EventTrack.stVideoPopCancelClick(mModel.mType)
            })
        }

        mBinding.save.setOnClickListener {
            val selectedList = CleanManager.instance.mSelectedList
            if (selectedList.isEmpty()) return@setOnClickListener
            EventTrack.stVideoSaveClick(mModel.mType, selectedList.size.toString())
            showLoading("正在保存...")
            setCanceledOnTouchOutside(false)
            StepRunner.runner(scope) {
                contStep {
                    viewScope.launch(Dispatchers.IO) {
                        val iterator = selectedList.iterator()
                        while (iterator.hasNext()) {
                            val next = iterator.next()
                            VideoImpl().insertMedia(getApplicationContext(), next.path!!)
                        }
                        delay(2000)
                        it.resume(true)
                    }
                }
                contStep {
                    viewScope.launch {
                        withContext(Dispatchers.Main)
                        {
                            showLoading("成功保存到相册!")
                        }
                        delay(1000)
                        it.resume(true)
                    }
                }
                contStep {
                    hideLoading()
                    it.resume(true)
                }
            }
        }
    }

    private fun buildTabs() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(
            MediaCleanFragment.newInstance(
                mUIHelper.getDateSortList(),
                mimeType = MediaCleanFragment.VIDEO
            )
        )
        fragmentList.add(
            MediaCleanFragment.newInstance(
                mUIHelper.getSizeSortList(),
                false,
                mimeType = MediaCleanFragment.VIDEO
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
        mBinding.tabLayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {
                (tab?.customView as TxTab).popWindow()
            }
        })
        mBinding.tabLayout.setupWithViewPager(mBinding.viewpager)

        val count = mBinding.viewpager.adapter?.count ?: 0
        mTabs.add(mUIHelper.buildTabItem("文件日期", mUIHelper.buildDateSortList(newest = {
            EventTrack.stVideoDateLatelyClick(mModel.mType)
            mModel.mSortType.postValue(McConstants.Sort.SORT_NEWEST)
        }, last = {
            EventTrack.stVideoDateOldestClick(mModel.mType)
            mModel.mSortType.postValue(McConstants.Sort.SORT_LAST)
        })))
        mTabs.add(mUIHelper.buildTabItem("文件大小", mUIHelper.buildSizeSortList(max = {
            EventTrack.stVideoSizeLargestClick(mModel.mType)
            mModel.mSortType.postValue(McConstants.Sort.SORT_MAX)
        }, min = {
            EventTrack.stVideoSizeSmallestClick(mModel.mType)
            mModel.mSortType.postValue(McConstants.Sort.SORT_MIN)
        })))
        for (i in 0 until count) {
            val tab = mBinding.tabLayout.getTabAt(i)
            tab?.customView = mTabs[i]
        }
        mBinding.viewpager.offscreenPageLimit = 2
        mBinding.viewpager.currentItem = 0
    }

    private fun subscribeUi() {
        CleanManager.instance.mSelectedLiveData.observe(this, {
            if (it.isEmpty()) {
                mBinding.save.isClickable = false
                mBinding.save.alpha = 0.5f
                mBinding.delete.isClickable = false
                mBinding.delete.alpha = 0.5f
            } else {
                mBinding.save.isClickable = true
                mBinding.save.alpha = 1.0f
                mBinding.delete.isClickable = true
                mBinding.delete.alpha = 1.0f
            }
        })
        mModel.mActivityResultLiveData.observe(this, {
            if (it.tag == ChildFragment.VIDEO) {
                if (it.isOk()) {
                    removeBlock?.invoke()
                }
                removeBlock = null
            }
        })
    }

    private fun preRemove(junkInfo: JunkInfo, block: (send: Boolean) -> Unit) {
        val mediaBean = junkInfo.mediaBean!!
        JobChain.newInstance()
            .addJob(RecycledJodCreate(mediaBean, requireActivity(), block = {
                when (it) {
                    GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION -> {
                        removeBlock = {
                            preRemove(junkInfo, block)
                        }
                        block.invoke(false)
                    }
                    GalleryConstants.RemoveStatus.REMOVED -> {
                        block.invoke(true)
                    }
                }
            }).create())
    }

    private var mRemoveRunnable: Runnable? = null
    private var mdRemover: MdRemover? = null
    private fun removeMedia() {
        mRemoveRunnable = Runnable {
            try {
                val selectedList = mutableListOf<JunkInfo>()
                selectedList.addAll(CleanManager.instance.mSelectedList)
                mdRemover = MdRemover()
                mdRemover?.removeJunks(requireActivity(), selectedList) { md ->
                    val iterator = selectedList.iterator()
                    ScopeHelper.launch {
                        withContext(Dispatchers.Main) {
                            while (iterator.hasNext()) {
                                val next = iterator.next()
                                if (md == null) {
                                    continue
                                }
                                if (next.mediaBean!!.originalPath == md.originalPath) {
                                    val appJunk = mModel.mAppJunk
                                    appJunk?.junkSize = appJunk?.junkSize!! - next.junkSize
                                    (appJunk.junks as MutableList).remove(next)
                                    mModel.mAppJunkLiveData.value = appJunk
                                    CleanManager.instance.removeJunk(next)
                                    EventBus.getDefault().post(
                                        UpdateAppJunkEvent(
                                            type = JunkConstants.AppCacheType.IMG_CACHE,
                                            mModel.mAppJunk!!
                                        )
                                    )
                                }
                            }
                            hideLoading()
                        }
                    }
                }
            } catch (e: Exception) {
            }
        }
        mRemoveRunnable?.run()
    }

    fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mdRemover?.onActivityResult(requestCode, resultCode, data)
    }
}