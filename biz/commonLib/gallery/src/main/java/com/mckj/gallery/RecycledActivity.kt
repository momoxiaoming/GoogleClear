package com.mckj.gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.helper.StepRunner
import com.mckj.gallery.utils.InjectUtils
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.mckj.baselib.view.loading.LoadingDialog
import com.mckj.gallery.bean.ARouterPath
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.dialog.RecycledConfirmDialogFragment
import com.mckj.gallery.event.RegainEvent
import com.mckj.gallery.job.GalleryManager
import com.mckj.gallery.job.JobChain
import com.mckj.gallery.job.recycled.RegainJodCreate
import com.mckj.gallery.viewmodel.RecycledViewModel
import com.mckj.gallery.databinding.CleanupxActivityRecycledBinding
import com.mckj.gen.St
import com.org.openlib.utils.SystemUiUtil
import com.mckj.utils.EventTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import kotlin.coroutines.resume
import kotlin.properties.Delegates


@Route(path = ARouterPath.GALLERY_RECYCLED_PATH)
class RecycledActivity : DataBindingActivity<CleanupxActivityRecycledBinding, RecycledViewModel>() {

    private val mSelectedData = mutableListOf<RecycledBean>()
    private val mImgSelectedData = mutableListOf<RecycledBean>()
    private val mVideoSelectedData = mutableListOf<RecycledBean>()

    var mCurrentMimeType: String by Delegates.observable(
        MediaConstant.MEDIA_TYPE_IMG,
        { _, _, newName ->
            checkBtnStatus()
        })

    override fun getViewModel(): RecycledViewModel {
        return ViewModelProvider(
            this,
            RecycledViewModel.RecycledViewModelFactory(InjectUtils.getRecycledRepository())
        ).get(
            RecycledViewModel::class.java
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.cleanupx_activity_recycled
    }

    override fun onDestroy() {
        super.onDestroy()
        GalleryManager.instance.clearSelectedData()
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun initView() {
//        mBinding.titleLayout.headerToolbar.apply {
//            title = getString(R.string.gallery_trash)
//            setNavigationOnClickListener {
//                finish()
//            }
//        }
        SystemUiUtil.immersiveSystemUi(window)
        buildTab()
        registerListener()
        subscribeData()
    }

    private fun subscribeData() {
        GalleryManager.instance.mSelectedData.observe(this, {
            mSelectedData.clear()
            mSelectedData.addAll(it)
            checkBtnStatus()
        })
    }

    private fun registerListener() {
        mBinding.resumeMedia.setOnClickListener {
            val selectedData = getActionData()
            showLoading(getString(R.string.gallery_restoreing))
            setCanceledOnTouchOutside(false)
            StepRunner.runner(scope) {
                contStep {
                    EventTrack.stRecycleRecoverClick()
                        lifecycleScope.launch(Dispatchers.IO) {
                            for (bean in selectedData) {
                                JobChain.newInstance().addJob(RegainJodCreate(bean).create())
                            }
                            it.resume(true)
                        }
                }
                contStep {
                    lifecycleScope.launch (Dispatchers.Main){
                        hideLoading()
                        showRegainDialog(selectedData)
                        it.resume(true)
                    }
                }
            }
        }
        mBinding.deleteMedia.setOnClickListener {
            val selectedData = getActionData()
            selectedData.let {
                EventTrack.stRecycleDeleteClick()
                showRecycledConfirmDialog(it)
            }
        }
    }


    private fun showRegainDialog(data: List<RecycledBean>) {
        val list = mutableListOf<RecycledBean>()
        list.addAll(data)
        val dialog = RecycledConfirmDialogFragment.newInstance()
        dialog.setData(list, RecycledConfirmDialogFragment.DIALOG_TYPE_REGAIN)
        dialog.rxShow(supportFragmentManager, "RecycledConfirmDialog")
        GalleryManager.instance.selectedAllRecycledBean(data, true)
        EventBus.getDefault().post(RegainEvent(true))
    }

    private fun showRecycledConfirmDialog(data: List<RecycledBean>) {
        val list = mutableListOf<RecycledBean>()
        list.addAll(data)
        val dialog = RecycledConfirmDialogFragment.newInstance()
        dialog.setData(list, RecycledConfirmDialogFragment.DIALOG_TYPE_COMPLETELY_DELETE)
        dialog.registerCompletelyDeleteListener(object :
            RecycledConfirmDialogFragment.CompletelyDeleteListener {
            override fun delete() {
                GalleryManager.instance.selectedAllRecycledBean(list, true)
                EventBus.getDefault().post(RegainEvent(true))
            }

            override fun iSee() {

            }
        })
        dialog.rxShow(supportFragmentManager, "RecycledConfirmDialog")
    }

    private fun checkBtnStatus() {
        if (mSelectedData.isEmpty()) {
            isEnable(false)
            return
        }
        mImgSelectedData.clear()
        mVideoSelectedData.clear()
        for (bean in mSelectedData) {
            if (bean.isImage()) {
                mImgSelectedData.add(bean)
            } else {
                mVideoSelectedData.add(bean)
            }
        }
        if (mCurrentMimeType == MediaConstant.MEDIA_TYPE_IMG && mImgSelectedData.isEmpty()) {
            isEnable(false)
        } else if (mCurrentMimeType == MediaConstant.MEDIA_TYPE_VIDEO && mVideoSelectedData.isEmpty()) {
            isEnable(false)
        } else {
            isEnable(true)
        }
    }

    private fun isEnable(enable: Boolean) {
        if (enable) {
            mBinding.resumeMedia.isClickable = true
            mBinding.deleteMedia.isClickable = true
            mBinding.resumeMedia.alpha = 1.0f
            mBinding.deleteMedia.alpha = 1.0f
        } else {
            mBinding.resumeMedia.isClickable = false
            mBinding.deleteMedia.isClickable = false
            mBinding.resumeMedia.alpha = 0.5f
            mBinding.deleteMedia.alpha = 0.5f
        }
    }

    private fun getActionData(): List<RecycledBean> {
        return when (mCurrentMimeType) {
            MediaConstant.MEDIA_TYPE_IMG -> mImgSelectedData
            else -> mVideoSelectedData
        }
    }

    private fun buildTab() {
        val fragmentList = ArrayList<BaseRecycledFragment>()
        val tabs = mutableListOf<String>()
        tabs.add(getString(R.string.gallery_photos))
        tabs.add(getString(R.string.gallery_video))

        mBinding.tabLayout.apply {
            for (title in tabs) {
                val newTab = this.newTab()
                newTab.text = title
                addTab(newTab)
            }
            fragmentList.add(BaseRecycledFragment.newInstance(MediaConstant.MEDIA_TYPE_IMG))
            fragmentList.add(BaseRecycledFragment.newInstance(MediaConstant.MEDIA_TYPE_VIDEO))
        }
        mBinding.recycleViewpager.adapter = object : FragmentPagerAdapter(
            supportFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getCount(): Int {
                return fragmentList.size
            }

            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return tabs[position]
            }
        }
        mBinding.recycleViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                mCurrentMimeType = if (position == 0) {
                    MediaConstant.MEDIA_TYPE_IMG
                } else {
                    MediaConstant.MEDIA_TYPE_VIDEO
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        mBinding.tabLayout.setupWithViewPager(mBinding.recycleViewpager)
    }
}