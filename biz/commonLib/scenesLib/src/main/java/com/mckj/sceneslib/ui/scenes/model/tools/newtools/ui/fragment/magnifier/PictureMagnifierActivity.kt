package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.magnifier


import android.Manifest
import android.app.Activity
import android.app.PendingIntent.getActivity
import android.app.RecoverableSecurityException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.repo.kv.KvSp
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.api.util.ScopeHelper
import com.mckj.baselib.base.databinding.DataBindingActivity
import com.mckj.baselib.view.ToastUtil
import com.mckj.datalib.entity.ARouterPath
import com.mckj.gallery.bean.*
import com.mckj.gallery.event.RegainEvent
import com.mckj.gallery.job.GalleryManager
import com.mckj.gallery.job.JobChain
import com.mckj.gallery.job.recycled.RecycledJodCreate
import com.mckj.gallery.utils.GalleryInteraction
import com.mckj.gallery.utils.InjectUtils
import com.mckj.gallery.view.GalleryLayoutManager
import com.mckj.gen.St
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.PictureMagnifierActivityBinding
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.viewbinder.PicMagnifierPreviewViewBinder
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.Constants
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.PictureMagnifierViewModel
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs
import android.provider.MediaStore
import android.content.ContentResolver
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.net.toFile
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.cm.utils.FileUtil
import com.github.chrisbanes.photoview.PhotoView
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.FilesUtils
import okio.IOException
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream


/**
 * @Description
 * @CreateTime 2022/3/22 16:59
 * @Author
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_PICTURE_MAGNIFIER)
class PictureMagnifierActivity :
    DataBindingActivity<PictureMagnifierActivityBinding, PictureMagnifierViewModel>() {
    val TAG = "PictureMagnifierActivity"

    var bucketId: String = Int.MIN_VALUE.toString()
    var mimeType: String = MediaConstant.MEDIA_TYPE_ALL

    private lateinit var mTopLayoutManager: GalleryLayoutManager
    private lateinit var mGalleryPreviewAdapter: MultiTypeAdapter
    private val mMediaData = mutableListOf<MediaBean>()//数据源
    private val mRecycledData = mutableListOf<MediaBean>()//当前页面被回收的数据
    private var curSelectedPosition = 0
    private lateinit var outputDirectory: File
    private val context: Context? = null

    /**
     * 引起页面滑动原因的枚举类
     * 手动调用相册滚动一定要把标志设置成None
     * @see smoothPhotoToPosition
     */
    enum class GalleryDrag {
        NONE, PREVIEW, THUMBNAIL, DELETE
    }

    //标记滑动原因
    private var dragTag = GalleryDrag.NONE

    private var mInteractionBean: InteractionBean? = null//交互数据
    private var removeMediaBlock: (() -> Unit?)? = null
    private var mPosition: Int = 0
    override fun getViewModel(): PictureMagnifierViewModel {
        return ViewModelProvider(
            this,
            PictureMagnifierViewModel.GalleryViewModelFactory(InjectUtils.getGalleryRepository())
        ).get(
            PictureMagnifierViewModel::class.java
        )
    }


    override fun getLayoutId(): Int {
        return R.layout.picture_magnifier_activity
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
        initPreviewRecycleView()
        initPreviewScrollListener(mBinding.previewRecycler)
        subscribeData()
        setListener()
        requestPermissions()
        outputDirectory = FilesUtils.getOutputDirectory(this)
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


    private fun initPreviewScrollListener(rv: RecyclerView) {
        var tempScroll = 0
        //存储每次滚动后裁剪掉的值
        //在存储到可滚动阈值时补充滚动
        var remain = 0f

        //预览宽度(屏幕宽度)
        var itemWidth = getDisplayWidth(rv.context)
        //缩略宽度
        val bottomSize = dp2Px(50f, rv.context)

        //计算预览与缩略图滚动系数
        var pro = 0f
        pro = if (itemWidth != 0) {
            itemWidth.toFloat() / bottomSize
        } else {
            //默认1080处理
            1080 / bottomSize
        }

        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //如果拖拽标记为预览拖拽，缩略需要联动
                if (dragTag == GalleryDrag.PREVIEW) {
                    //如果当前偏移量超过最小偏移量，则可以偏移
                    tempScroll += dx
                    if (abs(tempScroll) > pro) {
                        //将存储的之前截取的值+本次偏移为新的存储值
                        val fl = tempScroll / pro + remain
                        //计算需要偏移的像素值
                        val scrollX = fl.toInt()
                        //存储余下误差的像素
                        remain = fl - scrollX
                        tempScroll = 0
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //当列表滚动是因为拖拽预览列表列表时
                //将拖拽标记为预览拖拽，在onScrolled()中进行联动
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    curSelectedPosition = mTopLayoutManager.curSelectedPosition
                    dragTag = GalleryDrag.PREVIEW
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
     * 订阅数据源
     */
    private fun subscribeData() {
        //媒体库数据
        mModel.mediaData.observe(this, {
            mMediaData.clear()
            transportData {
                val code = get("urlCode")
                if (code == 2) {
                    val url = get("picturePath") as Uri
                    VLog.d("链接:uri=${url}")
                    val bean = MediaBean(originalPath = url.toString())
                    it.add(index = 0, bean)
                }
            }
            mMediaData.addAll(it)
            checkViewStatus()
            mGalleryPreviewAdapter.items = mMediaData
            mGalleryPreviewAdapter.notifyDataSetChanged()

        })

    }

    /**
     * 请求数据
     */
    private fun requestMediaData() {
        mModel.getMediaData(this, bucketId, requestType = mimeType)
    }

    private fun getBuckets() {
        mModel.getBuckets(this)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun setListener() {
        mBinding.tvPicBack.setOnClickListener {
            finish()
        }
        mBinding.tvPicDel.setOnClickListener {
            if (mMediaData.isNullOrEmpty()) {
                mBinding.emptyView.show()
                mBinding.previewRecycler.gone()
                mBinding.ibPicPreviousPage.gone()
                mBinding.ibPicNextPage.gone()
            } else {
                mBinding.emptyView.gone()
                mBinding.previewRecycler.show()
                val urlPath = mMediaData[curSelectedPosition].originalPath
                if (urlPath != null) {
                    try {
                        FileUtil.delete(urlPath)
                        mMediaData.removeAt(curSelectedPosition)
                        curSelectedPosition -= 1
                        if (curSelectedPosition < 0) {
                            curSelectedPosition = 0
                        }
                        mGalleryPreviewAdapter.items = mMediaData
                        mGalleryPreviewAdapter.notifyDataSetChanged()
                    } catch (e: Exception) {
                        if (isAboveQ() && e is RecoverableSecurityException) {
                            if (context is Activity) {
                                val bundle = Bundle()
                                bundle.putString("uri", urlPath)
                                context.startIntentSenderForResult(
                                    e.userAction.actionIntent.intentSender,
                                    GalleryConstants.REQUEST_CODE_SECURITY,
                                    null, 0,
                                    0,
                                    0,
                                    bundle
                                )
                            }
                        }
                    }

                }
            }


        }
        mBinding.ibPicNextPage.setOnClickListener {
            curSelectedPosition += 1
            if (curSelectedPosition >= mMediaData.size) {
                curSelectedPosition = mMediaData.size -1
            }
            smoothPhotoToPosition(curSelectedPosition)
        }
        mBinding.ibPicPreviousPage.setOnClickListener {
            curSelectedPosition -= 1
            if (curSelectedPosition < 0) {
                curSelectedPosition = 0
            }
            smoothPhotoToPosition(curSelectedPosition)
        }
        mBinding.btnSave.setOnClickListener {
            if (Constants.state) {
                try {
                    val urlPath = mMediaData[curSelectedPosition].originalPath
                    if (urlPath != null) {
                        FileUtil.delete(urlPath)
                    }
                    val photo = mBinding.previewRecycler.getChildAt(curSelectedPosition)
                        .findViewById<PhotoView>(R.id.photo)
                    photo.isDrawingCacheEnabled = true
                    val obmp = Bitmap.createBitmap(photo.drawingCache)
                    photo.isDrawingCacheEnabled = false
                    saveBitmap(obmp)
                    Constants.state = false
                    finish()
                }catch (e:Exception){
                    e.printStackTrace()
                }
                
            } else {
                finish()
            }
        }

    }

    private fun saveBitmap(bitmap: Bitmap) {
        val photoFile =
            FilesUtils.createFile(outputDirectory, Constants.DATE_FORMAT, Constants.PHOTO_EXTENSION)
        try {
            val fos = FileOutputStream(photoFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val uri = Uri.fromFile(photoFile)
        intent.data = uri
        this.sendBroadcast(intent)

    }

    private fun preRemove(bean: MediaBean) {
        if (!mMediaData.contains(bean)) {
            return
        }
        JobChain.newInstance()
            .addJob(RecycledJodCreate(bean, this@PictureMagnifierActivity) { status ->
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
                mGalleryPreviewAdapter.items = mMediaData
                mGalleryPreviewAdapter.notifyDataSetChanged()
                St.stPhotoCleanPageUpGlide()
                mRecycledData.add(bean)
                mInteractionBean?.removeBlock?.invoke(bean)
                getBuckets()
            }
        }
    }


    private fun initAdapter() {
        mGalleryPreviewAdapter = MultiTypeAdapter()
        mGalleryPreviewAdapter.register(MediaBean::class.java, PicMagnifierPreviewViewBinder())

    }

    /**
     * 滚动图片到指定位置
     */
    private fun smoothPhotoToPosition(position: Int) {
        //标记滚动为代码滚动，列表滑动联动断开
        dragTag = GalleryDrag.NONE
        mBinding.previewRecycler.smoothScrollToPosition(position)
    }

    private fun scrollPhotoToPosition(position: Int) {
        dragTag = GalleryDrag.NONE
        mBinding.previewRecycler.scrollToPosition(position)
    }


    private fun checkViewStatus() {
        if (mMediaData.isNullOrEmpty()) {
            mBinding.emptyView.show()
            mBinding.previewRecycler.gone()
        } else {
            mBinding.emptyView.gone()
            mBinding.previewRecycler.show()
        }
    }


    private fun requestPermissions() {
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
        //检查回收站的资源
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == Constants.REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {

            } else {
                ToastUtil.show("请您打开必要权限")
                finish()
            }
        }

    }

    private fun allPermissionsGranted() = Constants.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
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

    private fun isAboveQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
    }
}


