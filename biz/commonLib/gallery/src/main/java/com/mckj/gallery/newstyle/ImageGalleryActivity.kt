package com.mckj.gallery.newstyle

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.dn.vi.app.cm.utils.FileUtil
import com.drakeet.multitype.MultiTypeAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*

import com.mckj.baselib.base.databinding.DataBindingActivity
import com.mckj.baselib.util.SizeUtil
import com.mckj.baselib.view.adapter.register
import com.mckj.gallery.R
import com.mckj.gallery.bean.*
import com.mckj.gallery.databinding.CleanupxActivityImageGalleryBinding
import com.mckj.gallery.databinding.CleanupxItemGalleryCleanBinding
import com.mckj.gallery.job.JobChain
import com.mckj.gallery.job.recycled.RecycledJodCreate
import com.mckj.gallery.newstyle.lib.CardConfig
import com.mckj.gallery.newstyle.lib.CardItemTouchHelperCallback
import com.mckj.gallery.newstyle.lib.CardLayoutManager
import com.mckj.gallery.newstyle.lib.OnSwipeListener
import com.mckj.gallery.newstyle.view.GalleryExitDialogFragment
import com.mckj.gallery.newstyle.view.WelcomePageDialogFragment
import com.mckj.gallery.newstyle.viewbinder.ImageViewBinder
import com.mckj.gallery.newstyle.viewmodel.ImageGalleryViewModel
import com.mckj.gallery.utils.InjectUtils
import com.mckj.gen.St
import com.org.openlib.utils.onceClick
import com.org.openlib.utils.SystemUiUtil
import com.tbruyelle.rxpermissions3.RxPermissions
import java.util.*
import kotlin.math.abs


@Route(path = ARouterPath.GALLERY_PATH)
class ImageGalleryActivity : DataBindingActivity<CleanupxActivityImageGalleryBinding, ImageGalleryViewModel>() {
    override fun getViewModel(): ImageGalleryViewModel {
        return ViewModelProvider(
            this,
            ImageGalleryViewModel.GalleryViewModelFactory(InjectUtils.getGalleryRepository())
        ).get(
            ImageGalleryViewModel::class.java
        )
    }
    override fun getLayoutId(): Int {
        return R.layout.cleanupx_activity_image_gallery
    }
    private lateinit var behavior : BottomSheetBehavior<ConstraintLayout>
    private val mMediaData = LinkedList<MediaBean>()//数据源
    private val mRemoveData = LinkedList<MediaBean>()//待删除数据
    private val mCleanData = LinkedList<CleanMediaBen>()//待删除数据
    private val mSaveData = LinkedList<MediaBean>()//不做操作数据
    private val mHistoryData = LinkedList<Int>()//操作记录
    private val adName = "level_win"
    private var from = ""//打点来源
    private val callback by lazy {
        CardItemTouchHelperCallback(adapter, mMediaData)
    }
    val adapter by lazy {
        MultiTypeAdapter().apply {
            register(MediaBean::class.java, ImageViewBinder().also {

            })
        }
    }
    var cleanSize = 0 //清除的照片个数
    //是否划完
    var isFinish = false
    /**
     * 上滑 界面RecyclerView的adapter
     */
    val cleanAdapter by lazy {
        MultiTypeAdapter().apply {
            register<CleanMediaBen,CleanupxItemGalleryCleanBinding>(R.layout.cleanupx_item_gallery_clean,{
            })
            {data ,binding ->
                val option = RequestOptions()
                    .transform(CenterCrop(), RoundedCorners(SizeUtil.dp2px(4f)))
                Glide.with(binding.cleanItemContentIv)
                    .asBitmap()
                    .apply(option)
                    .load(data.mediaBean.originalPath)
                    .into(binding.cleanItemContentIv)
                if(data.isSelect){
                    binding.cleanItemSelectCb.setImageResource(R.drawable.cleanupx_gallery_select_sel)
                }else{
                    binding.cleanItemSelectCb.setImageResource(R.drawable.cleanupx_gallery_select_nol)
                }
                binding.cleanItemRoot.onceClick {
                    updateCleanSize(data)
                }
            }
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        subscribeData()
        SystemUiUtil.setBarColor(window, Color.WHITE)
        SystemUiUtil.setSystemUiFlag(window,View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        hideBottomUIMenu()
        St.stPhotoCleanShow()
    }


    private fun setNoImgGroupVisibility(visible:Int){
        mBinding.galleryNoImageIv.visibility = visible
        mBinding.galleryNoImageTv.visibility = visible
    }

    private fun setNormalGroupVisibility(visible:Int){
        mBinding.mainGalleryRv.visibility = visible
        mBinding.galleryPreBtn.visibility = visible
        mBinding.galleryCancelBtn.visibility = visible
        mBinding.galleryNextBtn.visibility = visible
        mBinding.gallerySwipeUpTv.visibility = visible
    }

    private fun setCompleteGroupVisibility(visible:Int){
        mBinding.galleryCompleteBg.visibility = visible
        mBinding.galleryAnimLottie.visibility = visible
        mBinding.galleryTaskNameTv.visibility = visible
    }


    private fun subscribeData() {
        mModel.mediaData.observe(this) {
            if (it.size == 0) {
                setNoImgGroupVisibility(View.VISIBLE)
                setNormalGroupVisibility(View.GONE)
                mBinding.galleryCleanRoot.visibility = View.GONE
                St.stPhotoCleanNoPhoto()
            } else if (it.size > 0) {
                setNoImgGroupVisibility(View.GONE)
                setNormalGroupVisibility(View.VISIBLE)
                mMediaData.clear()
                mMediaData.addAll(it)
                adapter.items = mMediaData
                callback.setDataList(mMediaData)
                adapter.notifyDataSetChanged()
            }
        }
        mModel.totalData.observe(this,{
            mBinding.galleryCleanBtn.text =
                "${getString(R.string.gallery_clean_btn_content)} ${FileUtil.getFileSizeNumberText(it)}${FileUtil.getFileSizeUnitText(it)}"
        })
    }

    override fun initView() {
        requestPermissions()
        mBinding.mainGalleryRv.itemAnimator = DefaultItemAnimator()
        callback.setOnSwipedListener(swipeListener)
        var touchHelper = ItemTouchHelper(callback)
        mBinding.mainGalleryRv.layoutManager = CardLayoutManager(mBinding.mainGalleryRv, touchHelper)
        mBinding.mainGalleryRv.adapter = adapter
        touchHelper.attachToRecyclerView(mBinding.mainGalleryRv)

        initListener()
        behavior = BottomSheetBehavior.from(mBinding.galleryCleanRoot)
        behavior.setBottomSheetCallback(bottomSheetCallback)
        var layoutParams = mBinding.galleryCleanRoot.layoutParams
        layoutParams.height = SizeUtil.dp2px(55f)
        mBinding.galleryCleanRoot.layoutParams = layoutParams

        mBinding.galleryCleanRv.layoutManager = GridLayoutManager(this@ImageGalleryActivity,3)
        mBinding.galleryCleanRv.adapter = cleanAdapter
        updateLayoutParams()
    }

    private fun initListener() {
        mBinding.galleryPreBtn.onceClick {
            St.stPhotoCleanAction("Remove")
            var pre = mMediaData.pollFirst()
            mRemoveData.addLast(pre)
            mCleanData.addLast(pre.convertClean())
            callback.setDataList(mMediaData)
            mHistoryData.add(CardConfig.SWIPED_LEFT)
            adapter.notifyDataSetChanged()
            if (mMediaData.size == 0){
                isFinish = true
            }
            checkCleanGallerySize()
            saveCurrImg()
        }
        mBinding.galleryCancelBtn.onceClick {
            St.stPhotoCleanAction("reset")
            if (mHistoryData.size <= 0){
                return@onceClick
            }
            var direction = mHistoryData.pollLast()
            var data:MediaBean
            if (direction == CardConfig.SWIPED_LEFT){
                data = mRemoveData.pollLast()
                mCleanData.pollLast()
                updateLayoutParams()
            }else{
                data = mSaveData.pollLast()
            }
            mMediaData.addFirst(data)
            adapter.notifyDataSetChanged()
            saveCurrImg()
        }
        mBinding.galleryNextBtn.onceClick {
            St.stPhotoCleanAction("Keep")
            var next = mMediaData.pollFirst()
            mSaveData.addLast(next)
            callback.setDataList(mMediaData)
            mHistoryData.add(CardConfig.SWIPED_RIGHT)
            adapter.notifyDataSetChanged()
            saveCurrImg()
        }
        mBinding.galleryCleanBtn.onceClick {
            clean()
        }
        mBinding.galleryCleanTitleTv.onceClick {
            if (cleanAdapter.items.size == 27 || isFinish){
                showExit()
            }else{
                behavior.state = STATE_COLLAPSED
            }
        }
        mBinding.galleryClose.onceClick {
            if (mMediaData.size == 0){
                finish()
                St.stPhotoCleanAction("close")
            }else{
                showExit()
            }
        }
    }

    private fun showExit(confirmResult:(()->Unit)? = null,cancelResult:(()->Unit)? = null){
        GalleryExitDialogFragment().apply {
            cancel = if (cancelResult != null){
                cancelResult
            }else{
                {

                }
            }
            confirm = if (confirmResult != null){
                confirmResult
            }else{
                {
                    St.stPhotoCleanAction("close")
                    finish()
                }
            }
        }.rxShow(supportFragmentManager,"GalleryExitDialogFragment")
    }

    /**
     * 点击清理图片后，更新数据
     */
    private fun updateCleanSize(data:CleanMediaBen){
        var list = cleanAdapter.items.toMutableList() as MutableList<CleanMediaBen>
        list.forEach {
            if (it.mediaBean.id == data.mediaBean.id){
                it.isSelect = !it.isSelect
                mModel.cleanTotalStorage(if (it.isSelect) data.mediaBean.length else -data.mediaBean.length)
            }
        }
        cleanAdapter.items = list
        cleanAdapter.notifyDataSetChanged()
    }

    /**
     * 点击清理
     */
    private fun clean() {
        var list = mutableListOf<MediaBean>()
        cleanAdapter.items.forEach {
            if (it as? CleanMediaBen != null){
                if (it.isSelect){
                    list.add(it.mediaBean)
                }
            }
        }
        if(list.size == 0){
            return
        }else{
            removeGallery(list)

            St.stPhotoSelectClick(from,list.size.toString())
        }
    }

    private fun removeGallery(list:List<MediaBean>){
        cleanSize = list.size
        JobChain.newInstance()
            .addJob(RecycledJodCreate(list,this){status ->
                when(status){
                    GalleryConstants.RemoveStatus.REJECT_BY_PERMISSION -> {
                        Log.e("RecycledJob", "removeGallery: REJECT_BY_PERMISSION" )
                    }
                    GalleryConstants.RemoveStatus.REMOVED -> {
                        Log.e("RecycledJob", "removeGallery: REMOVED")
                        removeFinish()
                    }
                }
            }.create())
    }

    private fun requestPermissions() {
        val rp = RxPermissions(this)
        rp.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).subscribe() {
            if (it) {
                WelcomePageDialogFragment().apply {
                    enter = {

                    }
                    cancel = {
                        finish()
                    }
                }.rxShow(supportFragmentManager,WelcomePageDialogFragment::class.java.name)
                mModel.getMediaData(this)
            } else {
                finish()
            }
        }
    }

    /**
     * 监听左右滑动
     */
    val swipeListener = object:OnSwipeListener<MediaBean>{
        override fun onSwiping(viewHolder: RecyclerView.ViewHolder?, ratio: Float, direction: Int) {
            checkHolder(viewHolder){
                Log.e(TAG, "onSwiping: ratio ${ratio}")
                it.mBinding.galleryRoot.alpha = 1 - abs(ratio) * 0.15f
                when(direction){
                    CardConfig.SWIPING_LEFT -> {
                        if(ratio == 0f){
                            it.mBinding.galleryRemoveIv.alpha = 0f
                        }else{
                            it.mBinding.galleryRemoveIv.alpha = abs(ratio) + 0.5f
                            it.mBinding.galleryLikeIv.alpha = 0f
                        }
                    }
                    CardConfig.SWIPING_RIGHT -> {
                        if(ratio == 0f){
                            it.mBinding.galleryLikeIv.alpha = 0f
                        }else{
                            it.mBinding.galleryRemoveIv.alpha = 0f
                            it.mBinding.galleryLikeIv.alpha = abs(ratio) + 0.5f
                        }
                    }
                    CardConfig.SWIPING_NONE ->{
                        it.mBinding.galleryLikeIv.alpha = 0f
                        it.mBinding.galleryRemoveIv.alpha = 0f
                    }
                }
            }
        }
        //左滑、右滑后回调
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, t: MediaBean?, direction: Int) {
            checkHolder(viewHolder){
                it.mBinding.galleryRoot.alpha = 1f
                it.mBinding.galleryRemoveIv.alpha = 0f
                it.mBinding.galleryLikeIv.alpha = 0f
                //添加上一次操作(左滑、右滑)
                mHistoryData.add(direction)
                St.stPhotoCleanAction("bothside")
                when(direction){
                    CardConfig.SWIPED_LEFT -> {
                        St.stPhotoCleanAction("Remove")
                        //清理
                        mRemoveData.addLast(t)
                        mCleanData.addLast(t!!.convertClean())
                        mModel.cleanTotalStorage(t!!.length)
                        checkCleanGallerySize()
                    }
                    CardConfig.SWIPED_RIGHT -> {
                        St.stPhotoCleanAction("Keep")
                        mSaveData.addLast(t)
                    }
                }
                saveCurrImg()
            }
        }
        //全部数据弄完后回调
        override fun onSwipedClear() {
            from = "最后一张"
            St.stPhotoSelectShow(from)
            //全部图片完成
            //去掉本地保存
            mModel.cleanLocalGallery()
            if (mRemoveData.size == 0){
                //跳回调页面
                showAnim()
            }else{
                //跳选择图片页面
                isFinish = true
                checkCleanGallerySize()
            }
        }
    }

    fun checkCleanGallerySize(){
        val size = mRemoveData.size
        if (size % 9 == 0 || isFinish){
            behavior.state = STATE_EXPANDED
        }
        updateLayoutParams()
    }

    /**
     * 更新下拉界面布局大小
     */
    private fun updateLayoutParams(){
        val size = mRemoveData.size
        var layoutParams = mBinding.galleryCleanRoot.layoutParams
        when{
            size == 0 -> {
                layoutParams.height = SizeUtil.dp2px(55f)
                mBinding.galleryCleanMenuIv.visibility = View.GONE
                mBinding.gallerySwipeUpTv.visibility = View.GONE
                mBinding.gallerySwipeUpTv.alpha = 0f
            }
            size <= 3-> {
                layoutParams.height = SizeUtil.dp2px(288f)
                mBinding.galleryCleanMenuIv.visibility = View.VISIBLE
                mBinding.gallerySwipeUpTv.visibility = View.VISIBLE
                mBinding.gallerySwipeUpTv.alpha = 1f
            }
            size <= 6 -> {
                layoutParams.height = SizeUtil.dp2px(392f)
                mBinding.gallerySwipeUpTv.visibility = View.VISIBLE
                mBinding.gallerySwipeUpTv.alpha = 1f
            }
            size > 6 -> {
                layoutParams.height = SizeUtil.dp2px(496f)
                mBinding.gallerySwipeUpTv.visibility = View.VISIBLE
                mBinding.gallerySwipeUpTv.alpha = 1f
            }
        }
        mBinding.galleryCleanRoot.layoutParams = layoutParams
    }

    val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback(){
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when(newState){
                STATE_EXPANDED -> {
                    //完全展开
                    totalSize()
                    cleanAdapter.items = mCleanData
                    cleanAdapter.notifyDataSetChanged()
                    if (cleanAdapter.items.isEmpty()){
                        behavior.state = STATE_COLLAPSED
                        return
                    }
                    mBinding.galleryMask.isClickable = true
                    if (cleanAdapter.items.size == 27 || isFinish){
                        mBinding.galleryCleanSizeTv.text = getString(R.string.gallery_clean_size_content_finish)
                    }else{
                        mBinding.galleryCleanSizeTv.text = Html.fromHtml(getString(R.string.gallery_clean_size_content,cleanAdapter.items.size))
                    }
                    mBinding.galleryCleanMenuIv.alpha = 0f
                    when(mRemoveData.size){
                        9 ->{
                            from = "9张自动弹出"
                        }
                        18 -> {
                            from = "18张自动弹出"
                        }
                        27 -> {
                            from = "27张自动弹出"
                        }
                        else ->{
                            from = "手动拉起"
                        }
                    }
                    St.stPhotoSelectShow(from)
                }
                STATE_COLLAPSED -> {
                    Log.e("====", ": 关闭")
                    mBinding.galleryMask.isClickable = false
                    if (cleanAdapter.items.size == 27 || isFinish){
                        showExit(cancelResult = {
                            behavior.state = STATE_EXPANDED
                        })
                    }
                    updateRemoveToSave()
                }
                STATE_DRAGGING -> {
                    Log.e("====", ": 过渡")
                }
                STATE_SETTLING -> {
                    Log.e("====", ": 视图从脱离手指自由滑动到最终停下的这一小段时间")
                }
                STATE_HIDDEN  -> {
                    Log.e("====", ": 默认无此状态")
                    behavior.state = STATE_COLLAPSED
                }
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
            mBinding.galleryMask.alpha = slideOffset
            mBinding.galleryCleanMenuIv.alpha = abs(slideOffset - 1)
            mBinding.galleryCleanTitleTv.alpha = slideOffset
        }

    }

    /**
     * 下拉后把remove的数据更新到save里面
     */
    fun updateRemoveToSave(){
//        mRemoveData.removeAll(mTempData)
//        mSaveData.addAll(mTempData)
//        mTempData.clear()
    }

    /**
     * 判空和转化viewHolder
     */
    fun checkHolder(holder:RecyclerView.ViewHolder?,result:(ImageViewBinder.ImageViewHolder)->Unit){
        val imageHolder = holder as? ImageViewBinder.ImageViewHolder
        if (imageHolder!=null){
            result(imageHolder)
        }
    }

    fun totalSize(){
        var size = 0L
        mCleanData.forEach {
            if (it.isSelect){
                size += it.mediaBean.length
            }
        }
        mModel.setTotalStorage(size)
    }

    fun MediaBean.convertClean():CleanMediaBen{
        return CleanMediaBen(this)
    }

    /**
     * 删除照片完成
     */
    private fun removeFinish(){
        saveCurrImg()
        isFinish = false //图片删完了 到这一部可以取消

        showAnim()

        mModel.saveCleanSize(mModel.totalData.value!!)
    }

    private fun saveCurrImg(){
        if(mMediaData.size > 0){
            //先保存当前相册首张图片，下次进来到这张照片
            mModel.saveLocalGalleryData(mMediaData.first)
        }else{
            //图片已经删完，删除本地数据，重新开始清理图片
            mModel.cleanLocalGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GalleryConstants.REQUEST_CODE_SECURITY -> {
                if (resultCode == RESULT_OK) {
                    Log.d("RecycledJob", "onActivityResult: RESULT_OK")
                    //删除完成
                    removeFinish()
                }else{
                    //用户拒绝
                    Log.e("RecycledJob", "onActivityResult: 用户拒绝")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        if (mMediaData.size == 0){
            super.onBackPressed()
        }else{
            showExit()
        }
    }

    private fun showAnim(){
//        AdManager.getInstance().loadAd(adName)
        mBinding.galleryCleanRoot.visibility = View.GONE
        setNormalGroupVisibility(View.GONE)
        setNoImgGroupVisibility(View.GONE)
        setCompleteGroupVisibility(View.VISIBLE)

        mBinding.galleryPreBtn.alpha = 0f
        mBinding.galleryCancelBtn.alpha = 0f
        mBinding.galleryNextBtn.alpha = 0f
        mBinding.galleryCompleteBg.isClickable = true
        mBinding.galleryAnimLottie.addAnimatorListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                showAd()
            }
        } )
        mBinding.galleryAnimLottie.playAnimation()

    }

    private fun showAd(){
//        AdManager.getInstance().showAd(
//            adName,
//            ViewGroupAdContainer(mBinding.constraintLayout),
//            this
//        ) {
//            when (it.adStatus) {
//                AdStatus.SHOW_SUCCESS -> {
//
//                }
//                AdStatus.ERROR -> {
//                    Log.e("====", "showAd: ERROR ")
//                    goResult()
//                }
//                AdStatus.CLICK -> {
//
//                }
//                AdStatus.CLOSE -> {
//                    Log.e("====", "showAd: CLOSE ")
//                    goResult()
//                }
//            }
//        }
        goResult()
    }

    private fun goResult(){
        finish()
        //跳转到回调页
        var intent = Intent(this@ImageGalleryActivity,GalleryResultActivity::class.java)
        intent.putExtra(GalleryResultActivity.PHOTOS,cleanSize)
        intent.putExtra(GalleryResultActivity.SIZE,mModel.totalData.value)
        startActivity(intent)
    }

    private fun hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        val params: WindowManager.LayoutParams = window.attributes
        params.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  or View.SYSTEM_UI_FLAG_IMMERSIVE
        window.attributes = params

    }
}