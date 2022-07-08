package com.mckj.gallery

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.cm.utils.FileUtil
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.gallery.utils.InjectUtils
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.mckj.gallery.bean.ARouterPath
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.dialog.RecycledConfirmDialogFragment
import com.mckj.gallery.dialog.VideoPlayDialogFragment
import com.mckj.gallery.event.RegainEvent
import com.mckj.gallery.job.GalleryManager
import com.mckj.gallery.job.JobChain
import com.mckj.gallery.job.recycled.RegainJodCreate
import com.mckj.gallery.viewbinder.RecycledPreviewViewBinder
import com.mckj.gallery.viewmodel.RecycledViewModel
import com.mckj.gallery.databinding.CleanupxActivityPreviewBinding
import com.mckj.gen.St
import com.org.openlib.utils.SystemUiUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus

@Route(path = ARouterPath.GALLERY_PREVIEW_PATH)
class PreviewActivity : DataBindingActivity<CleanupxActivityPreviewBinding, RecycledViewModel>() {

    companion object {
        const val BUNDLE_KEY = "bundle"
        const val SELECTED_BEAN = "selected_bean"
        const val TYPE = "TYPE"
    }

    private val mPreviewAdapter = MultiTypeAdapter()
    var mData = mutableListOf<RecycledBean>()
    var mPosition = 0
    var mCurrentBean: RecycledBean? = null
    private lateinit var mimeType: String
    override fun getViewModel(): RecycledViewModel {
        return ViewModelProvider(
            this,
            RecycledViewModel.RecycledViewModelFactory(InjectUtils.getRecycledRepository())
        ).get(
            RecycledViewModel::class.java
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.cleanupx_activity_preview
    }

    override fun initData(savedInstanceState: Bundle?) {
        St.stRecyclePhotoPreview()
        mCurrentBean = intent.getBundleExtra(BUNDLE_KEY)?.getParcelable(SELECTED_BEAN)
        mimeType = intent.getBundleExtra(BUNDLE_KEY)?.getString(TYPE).toString()
    }

    override fun initView() {
        SystemUiUtil.immersiveSystemUi(window)
        initRecycleView()
        registerListener()
        subscribeData()
    }

    private fun subscribeData() {
        val recycledMimeTypeData = mModel.getRecycledMimeTypeData(mimeType)
        recycledMimeTypeData.observe(this, {
            if (it.isEmpty()) finish()
            mData.clear()
            mData.addAll(it)
            mPreviewAdapter.items = mData
            for (bean in mData.withIndex()) {
                if (mCurrentBean == bean.value) {
                    mPosition = bean.index
                    mBinding.viewPager.setCurrentItem(mPosition,false)
//                    mBinding.viewPager.currentItem = mPosition
                    refreshTopUi()
                    break
                }
            }
            mPreviewAdapter.notifyDataSetChanged()
        })
    }

    //统计位置和媒体资源大小
    private fun refreshTopUi() {
        GlobalScope.launch(Dispatchers.Main) {
           if (mData.isNullOrEmpty()) return@launch
            mBinding.positionTip.text = "${mPosition + 1}/${mData.size}"
            mData[mPosition].size?.let {
                val fileSizeNumberText = FileUtil.getFileSizeNumberText(it)
                val fileSizeUnitText = FileUtil.getFileSizeUnitText(it)
                mBinding.sizeTotal.text = fileSizeNumberText + fileSizeUnitText
            }
        }
    }

    private fun initRecycleView() {
        mPreviewAdapter.register(RecycledBean::class.java, RecycledPreviewViewBinder().also {
            it.itemClickListener =
                object : AbstractViewBinder.OnItemClickListener<RecycledBean> {
                    override fun onItemClick(view: View, position: Int, t: RecycledBean) {
                        VideoPlayDialogFragment.newInstance(mData[position].originalPath!!)
                            .rxShow(supportFragmentManager, "VideoDialog")
                    }
                }
        })
        mBinding.viewPager.offscreenPageLimit = 5
        mBinding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                mPosition = position
                refreshTopUi()
            }
        })
        mBinding.viewPager.adapter = mPreviewAdapter
    }

    private fun registerListener() {
        mBinding.resumeMedia.setOnClickListener {
            GlobalScope.launch {
                JobChain.newInstance().addJob(RegainJodCreate(mData[mPosition]).create())
                withContext(Dispatchers.Main) {
                    val recycledBean = mData[mPosition]
                    GalleryManager.instance.removeRecycledBean(recycledBean)
                    EventBus.getDefault().post(RegainEvent(true))
                    showRecycledDialog(RecycledConfirmDialogFragment.DIALOG_TYPE_REGAIN, {}, {})
                }
            }
        }
        mBinding.deleteMedia.setOnClickListener {
            showRecycledDialog(RecycledConfirmDialogFragment.DIALOG_TYPE_COMPLETELY_DELETE, {
                val recycledBean = mData[mPosition]
                GalleryManager.instance.removeRecycledBean(recycledBean)
            }, {})
        }
        mBinding.back.setOnClickListener {
            finish()
        }
    }

    private fun showRecycledDialog(type: String, delete: () -> Unit?, iSee: () -> Unit?) {
        val list = mutableListOf<RecycledBean>()
        list.add(mData[mPosition])
        val dialog = RecycledConfirmDialogFragment.newInstance()
        dialog.setData(list, type)
        dialog.registerCompletelyDeleteListener(object :
            RecycledConfirmDialogFragment.CompletelyDeleteListener {
            override fun delete() {
                delete.invoke()
            }

            override fun iSee() {
                iSee.invoke()
            }
        })
        dialog.rxShow(supportFragmentManager, "RecycledConfirmDialog")
    }
}