package com.mckj.gallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.cm.utils.FileUtil
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.api.util.ScopeHelper
import com.mckj.gallery.utils.InjectUtils
import com.mckj.gallery.utils.SpanStringProducer
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.baselib.util.TextUtils
import com.mckj.gallery.dialog.BucketDialogFragment
import com.mckj.gallery.dialog.GalleryGuideDialogFragment
import com.mckj.gallery.event.RegainEvent
import com.mckj.gallery.view.SupplyTouchCallback
import com.mckj.gallery.job.GalleryManager
import com.mckj.gallery.job.JobChain
import com.mckj.gallery.job.recycled.RecycledJodCreate
import com.mckj.gallery.view.GalleryLayoutManager
import com.mckj.gallery.view.ScaleTransformer
import com.mckj.gallery.viewbinder.GalleryPreviewViewBinder
import com.mckj.gallery.viewbinder.GalleryViewBinder
import com.mckj.gallery.viewmodel.GalleryViewModel
import com.mckj.gallery.bean.*
import com.mckj.gallery.databinding.CleanupxActivityGalleryBinding
import com.mckj.gallery.utils.GalleryInteraction
import com.mckj.gen.St
import com.org.openlib.utils.onceClick
import com.mckj.utils.EventTrack
import com.org.openlib.utils.SystemUiUtil
import com.tbruyelle.rxpermissions3.RxPermissions
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Runnable
import kotlin.math.abs
import kotlin.properties.Delegates

/**
 *@author xx
 *@version 1
 *@createTime 2021/7/20
 *@desc
 */
//@Route(path = ARouterPath.GALLERY_PATH)
class GalleryActivity : DataBindingActivity<CleanupxActivityGalleryBinding, GalleryViewModel>() {
    val TAG = "GalleryActivity"
    var bucketName: String by Delegates.observable(ResourceUtil.getString(R.string.gallery_albums), { _, _, newName ->
        mBinding.bucketName.text = newName
    })

    var bucketId: String = Int.MIN_VALUE.toString()
    var mimeType: String = MediaConstant.MEDIA_TYPE_ALL

    private lateinit var mBottomLayoutManager: GalleryLayoutManager
    private lateinit var mTopLayoutManager: GalleryLayoutManager
    private var mDialog: BucketDialogFragment? = null
    private lateinit var mGalleryAdapter: MultiTypeAdapter
    private lateinit var mGalleryPreviewAdapter: MultiTypeAdapter
    private val mMediaData = mutableListOf<MediaBean>()//?????????
    private val mRecycledData = mutableListOf<MediaBean>()//??????????????????????????????

    /**
     * ????????????????????????????????????
     * ???????????????????????????????????????????????????None
     * @see smoothPhotoToPosition
     */
    enum class GalleryDrag {
        NONE, PREVIEW, THUMBNAIL, DELETE
    }

    //??????????????????
    private var dragTag = GalleryDrag.NONE

    private var mInteractionBean: InteractionBean? = null//????????????
    private var removeMediaBlock: (() -> Unit?)? = null
    private var mPosition: Int = 0
    override fun getViewModel(): GalleryViewModel {
        return ViewModelProvider(
            this,
            GalleryViewModel.GalleryViewModelFactory(InjectUtils.getGalleryRepository())
        ).get(
            GalleryViewModel::class.java
        )
    }

    override fun getLayoutId(): Int {
        return R.layout.cleanupx_activity_gallery
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        GalleryInteraction.clearInteractionData()
    }

    override fun initView() {
        SystemUiUtil.immersiveSystemUi(window)
        initAdapter()

        //init gallery
        initPreviewRecycleView()
        initBottomRecycleView()
        initPreviewScrollListener(mBinding.previewRecycler)

        initBottomScrollListener(mBinding.galleryRecycler)

        initDecoration()
        subscribeData()
        setListener()
        requestPermissions()
        mBinding.galleryRecycler.postDelayed({
            mBottomLayoutManager.scrollToPosition(mPosition)
            mBinding.galleryRecycler.smoothScrollToPosition(mPosition)
        }, 100)
    }

    private fun initPreviewRecycleView() {
        mTopLayoutManager = GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL)
        mTopLayoutManager.setSnapHelper(PagerSnapHelper())
        mTopLayoutManager.attach(mBinding.previewRecycler)
        mTopLayoutManager.setOnItemSelectedListener { recyclerView, item, position ->
            St.stPhotoCleanPageBothSides()
        }
        mBinding.previewRecycler.adapter = mGalleryPreviewAdapter
    }

    private fun initBottomRecycleView() {
        mBottomLayoutManager = GalleryLayoutManager(GalleryLayoutManager.HORIZONTAL)
        mBottomLayoutManager.setItemTransformer(ScaleTransformer())
        mBottomLayoutManager.setSnapHelper(PagerSnapHelper())
        mBottomLayoutManager.attach(mBinding.galleryRecycler)
        mBottomLayoutManager.setOnItemSelectedListener { _, _, position ->
            St.stPhotoCleanThumbnailSlide()
        }
        mBinding.galleryRecycler.adapter = mGalleryAdapter
    }

    private fun initPreviewScrollListener(rv: RecyclerView) {
        var tempScroll = 0
        //????????????????????????????????????
        //??????????????????????????????????????????
        var remain = 0f

        //????????????(????????????)
        var itemWidth = getDisplayWidth(rv.context)
        //????????????
        val bottomSize = dp2Px(50f, rv.context)

        //????????????????????????????????????
        var pro = 0f
        pro = if (itemWidth != 0) {
            itemWidth.toFloat() / bottomSize
        } else {
            //??????1080??????
            1080 / bottomSize
        }

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //??????????????????????????????????????????????????????
                if (dragTag == GalleryDrag.PREVIEW) {
                    //????????????????????????????????????????????????????????????
                    tempScroll += dx
                    if (abs(tempScroll) > pro) {
                        //??????????????????????????????+??????????????????????????????
                        val fl = tempScroll / pro + remain
                        //??????????????????????????????
                        val scrollX = fl.toInt()
                        //???????????????????????????
                        remain = fl - scrollX
                        //????????????
                        mBinding.galleryRecycler.scrollBy(scrollX, 0)
                        tempScroll = 0
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //???????????????????????????????????????????????????
                //????????????????????????????????????onScrolled()???????????????
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    dragTag = GalleryDrag.PREVIEW
                }
            }
        })
    }

    private fun initBottomScrollListener(rv: RecyclerView) {
        var itemWidth = getDisplayWidth(rv.context)
        val bottomSize = dp2Px(50f, rv.context)
        val bottomWidth = bottomSize

        var remain = 0f
        var pro = 0f
        pro = if (itemWidth != 0) {
            itemWidth.toFloat() / bottomWidth
        } else {
            //??????1080??????
            1080 / bottomWidth
        }


        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dragTag == GalleryDrag.THUMBNAIL) {
                    val fl = dx * pro + remain
                    val scrollX = fl.toInt()
                    remain = fl - scrollX
                    mBinding.previewRecycler.scrollBy(scrollX, 0)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
//                Log.i(TAG, "bottom->onScrollStateChanged: $newState")

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    dragTag = GalleryDrag.THUMBNAIL
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (dragTag == GalleryDrag.THUMBNAIL) {
                        val curSelectedPosition = mBottomLayoutManager.curSelectedPosition
                        smoothPhotoToPosition(curSelectedPosition)
                    }
                }
            }
        })
    }

    private fun getDisplayWidth(ctx: Context?): Int {
        val r: Resources = if (ctx == null) {
            Resources.getSystem()
        } else {
            ctx.resources
        }
        return r.displayMetrics.widthPixels
    }


    private fun dp2Px(dp: Float, ctx: Context?): Float {
        val r: Resources = if (ctx == null) {
            Resources.getSystem()
        } else {
            ctx.resources
        }
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
    }

    /**
     * ???????????????
     */
    private fun subscribeData() {
        //???????????????
        mModel.mediaData.observe(this, {
            mMediaData.clear()
            mMediaData.addAll(it)
            checkViewStatus()
            mGalleryAdapter.items = mMediaData
            mGalleryPreviewAdapter.items = mMediaData
            mGalleryAdapter.notifyDataSetChanged()
            mGalleryPreviewAdapter.notifyDataSetChanged()

        })
        //???????????????
        mModel.mBucketData.observe(this, {
            if (mDialog == null) {
                mDialog = BucketDialogFragment.newInstance()
                mDialog!!.registerItemClickListener(object :
                    AbstractViewBinder.OnItemClickListener<BucketBean> {
                    override fun onItemClick(view: View, position: Int, t: BucketBean) {
                        bucketName = t.bucketName
                        mimeType = when {
                            t.isImage() -> {
                                MediaConstant.MEDIA_TYPE_IMG
                            }
                            t.isVideo() -> {
                                MediaConstant.MEDIA_TYPE_VIDEO
                            }
                            else -> {
                                MediaConstant.MEDIA_TYPE_ALL
                            }
                        }
                        bucketId = t.bucketId
                        requestMediaData()
                    }
                })
            }
            mDialog?.setData(it)
        })
    }

    /**
     * ????????????
     */
    private fun requestMediaData() {
        mModel.getMediaData(this, bucketId, requestType = mimeType)
    }

    private fun getBuckets() {
        mModel.getBuckets(this)
    }

    //???????????????
    private fun initDecoration() {
        mBinding.galleryRecycler.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.HORIZONTAL
            ).also { decor ->
                decor.setDrawable(DividerDrawable(SizeUtil.dp2px(2f)).also { })
            })
    }


    private fun setListener() {
        mBinding.albumIcon.setOnClickListener {
            EventTrack.stPhotoCleanClassifyClick()
            St.stClassifyPopShow()
            mDialog?.rxShow(supportFragmentManager, "BucketDialog")
        }
        mBinding.back.setOnClickListener {
            finish()
        }
        mBinding.recycleIcon.onceClick {
            EventTrack.stPhotoCleanRecycleClick()
            ARouter.getInstance().build(ARouterPath.GALLERY_RECYCLED_PATH).navigation()
        }

        val supplyCallback: (Int) -> Unit = {
            mMediaData[it].apply {
                preRemove(this)
            }
        }
        val onSwipe: () -> Unit = {
            if (dragTag != GalleryDrag.DELETE) {
                dragTag = GalleryDrag.DELETE
            }
        }
        val itemTouchHelper = ItemTouchHelper(SupplyTouchCallback(supplyCallback, onSwipe))
        itemTouchHelper.attachToRecyclerView(mBinding.previewRecycler)

    }

    private fun preRemove(bean: MediaBean) {
        if (!mMediaData.contains(bean)) {
            return
        }
        JobChain.newInstance()
            .addJob(RecycledJodCreate(bean, this@GalleryActivity) { status ->
                when (status) {
                    GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION -> {
                        removeMediaBlock = {
                            preRemove(bean)
                        }
                    }
                    GalleryConstants.RemoveStatus.REMOVED -> {
                        remove(bean)
                    }
                }
            }.create())
    }

    private fun remove(bean: MediaBean) {
        if (!mMediaData.contains(bean)) {
            return
        }
        ScopeHelper.launch {
            withContext(Dispatchers.Main) {
                mMediaData.remove(bean)
                checkViewStatus()
                mGalleryAdapter.items = mMediaData
                mGalleryPreviewAdapter.items = mMediaData
                mGalleryPreviewAdapter.notifyDataSetChanged()
                mGalleryAdapter.notifyDataSetChanged()
                St.stPhotoCleanPageUpGlide()
                mRecycledData.add(bean)
                updateRemindTip(mRecycledData)
                mInteractionBean?.removeBlock?.invoke(bean)
                getBuckets()
            }
        }
    }


    private fun initAdapter() {
        mGalleryAdapter = MultiTypeAdapter()
        mGalleryAdapter.register(MediaBean::class.java, GalleryViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MediaBean> {
                override fun onItemClick(view: View, position: Int, t: MediaBean) {
                    if (mBottomLayoutManager.curSelectedPosition == position) return
                    St.stPhotoCleanThumbnailClick()
                    smoothPhotoToPosition(position)
                }
            }
        })
        mGalleryPreviewAdapter = MultiTypeAdapter()
        mGalleryPreviewAdapter.register(MediaBean::class.java, GalleryPreviewViewBinder())

    }

    /**
     * ???????????????????????????
     */
    private fun smoothPhotoToPosition(position: Int) {
        //??????????????????????????????????????????????????????
        dragTag = GalleryDrag.NONE
        mBinding.galleryRecycler.smoothScrollToPosition(position)
        mBinding.previewRecycler.smoothScrollToPosition(position)
    }

    private fun scrollPhotoToPosition(position: Int) {
        dragTag = GalleryDrag.NONE
        mBinding.galleryRecycler.scrollToPosition(position)
        mBinding.previewRecycler.scrollToPosition(position)
    }

    private fun updateRemindTip(list: List<MediaBean>) {
        if (list.isNullOrEmpty()) {
            mBinding.title.text = getString(R.string.gallery_swipe_up_to_delete_pictures)
            return
        }
        var totalSize: Long = 0
        for (bean in list) {
            bean.length?.let {
                totalSize += it
            }
        }
        val fileSizeNumberText = FileUtil.getFileSizeNumberText(totalSize)
        val fileSizeUnitText = FileUtil.getFileSizeUnitText(totalSize)

        val content = String.format(getString(R.string.gallery_save), fileSizeNumberText, fileSizeUnitText)
        val value = fileSizeNumberText+fileSizeUnitText
        val descTwo = TextUtils.string2SpannableStringForColor(
            content,
            value,
            color = Color.parseColor("#00D8FF")
        )
        mBinding.title.text = descTwo
    }

    private fun checkViewStatus() {
        if (mMediaData.isNullOrEmpty()) {
            mBinding.emptyView.show()
            mBinding.previewRecycler.gone()
            mBinding.galleryRecycler.gone()
        } else {
            mBinding.emptyView.gone()
            mBinding.previewRecycler.show()
            mBinding.galleryRecycler.show()
        }
    }


    private fun requestPermissions() {
        GalleryGuideDialogFragment.newInstance().showGuide(supportFragmentManager)
        doBeforeGallery()
        parseIntent()
    }

    private fun parseIntent() {
        mInteractionBean = GalleryInteraction.interactionBean
        if (mInteractionBean == null) {
            requestMediaData()
            getBuckets()
        } else {
            mModel.useOutSideData(mInteractionBean?.mediaList as MutableList)
            mPosition = mInteractionBean?.position ?: 0
        }
    }

    private fun doBeforeGallery() {
        //????????????????????????
        GalleryManager.instance.checkRecycledData()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRegainEvent(event: RegainEvent) {
        GlobalScope.launch(Dispatchers.IO) {
            delay(500)
            requestMediaData()
            getBuckets()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GalleryConstants.REQUEST_CODE_SECURITY -> {
                if (resultCode == RESULT_OK) {
                    removeMediaBlock?.invoke()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}