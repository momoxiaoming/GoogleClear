package com.mckj.module.mediaClean

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.mckj.api.client.JunkConstants
import com.mckj.api.entity.JunkInfo
import com.mckj.gallery.impl.PictureImpl
import com.dn.vi.app.base.helper.StepRunner
import com.mckj.api.util.ScopeHelper
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.helper.getApplicationContext
import com.mckj.module.event.UpdateAppJunkEvent
import com.mckj.module.manager.CleanManager
import com.mckj.module.queue.MdRemover
import com.mckj.module.utils.EventTrack
import com.mckj.module.utils.McConstants
import com.mckj.module.utils.UIHelper
import com.mckj.module.viewmodel.MediaCleanViewModel
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupFragmentImgCleanBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import java.lang.Exception
import kotlin.coroutines.resume

/**
 * @author xx
 * @version 1
 * @createTime 2021/9/15 11:53
 * @desc
 */
class ImgCleanFragment :
    DataBindingFragment<CleanupFragmentImgCleanBinding, MediaCleanViewModel>() {

    private var mUIHelper: UIHelper? = null


    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_img_clean
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
        mUIHelper = UIHelper()
        subscribeUi()
    }

    override fun initView() {
        val sorts = ArrayList<Int>()
        sorts.add(McConstants.Sort.SORT_NEWEST)
        val fragment = MediaCleanFragment.newInstance(sorts, mimeType = MediaCleanFragment.IMG)
        requireActivity()
            .supportFragmentManager
            .beginTransaction()
            .add(R.id.fra_container, fragment)
            .commit()
        registerListener()
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


    private fun registerListener() {
        mBinding.delete.setOnClickListener {
            val selectedList = CleanManager.instance.mSelectedList
            if (selectedList.isEmpty()) return@setOnClickListener
            EventTrack.stPictureDeleteClick(mModel.mType, selectedList.size.toString())
            mUIHelper?.showImgDeleteDialog(requireActivity(), selectedList, {
                EventTrack.stPicturePopDeleteClick(mModel.mType)
                showLoading("正在删除中...")
                setCanceledOnTouchOutside(false)
                removeMedia()
            }, negative = {
                EventTrack.stPicturePopCancelClick(mModel.mType)
            })
        }
        mBinding.save.setOnClickListener {
            val selectedList = CleanManager.instance.mSelectedList
            if (selectedList.isEmpty()) return@setOnClickListener
            EventTrack.stPictureSaveCilck(mModel.mType, selectedList.size.toString())
            showLoading("正在保存...")
            setCanceledOnTouchOutside(false)
            StepRunner.runner(scope) {
                contStep {
                    viewScope.launch(Dispatchers.IO) {
                        val iterator = selectedList.iterator()
                        while (iterator.hasNext()) {
                            val next = iterator.next()
                            PictureImpl().insertMedia(getApplicationContext(), next.path!!)
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
    }

    fun activityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mdRemover?.onActivityResult(requestCode, resultCode, data)
    }
}