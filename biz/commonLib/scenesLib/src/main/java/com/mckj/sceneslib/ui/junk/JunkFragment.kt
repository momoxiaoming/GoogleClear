package com.mckj.sceneslib.ui.junk

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dn.vi.app.cm.log.VLog
import com.dn.vi.app.cm.utils.DAttrUtil
import com.dn.vi.app.cm.utils.FileUtil
import com.dn.vi.app.cm.utils.TextUtils
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.api.client.JunkConstants
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.helper.showToast
import com.mckj.baselib.util.ResourceUtil
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentJunkBinding
import com.mckj.sceneslib.entity.LocalAdStatus
import com.mckj.sceneslib.entity.MenuJunkChild
import com.mckj.sceneslib.entity.MenuJunkParent
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.ui.LottieFragment
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory
import com.mckj.sceneslib.ui.scenes.landing.ScenesLandingWarnDialogFragment
import com.mckj.sceneslib.ui.viewbinder.JunkDetailChildViewHolder
import com.mckj.sceneslib.ui.viewbinder.JunkDetailParentViewHolder
import com.mckj.sceneslib.ui.viewbinder.JunkLoadingViewHolder
import com.mckj.sceneslib.util.ARGBUtils
import com.mckj.sceneslib.util.CountDownTimer
import com.org.openlib.help.Consumer
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundResource


/**
 * Describe:
 *
 * Created By yangb on 2021/6/1
 */
class JunkFragment : LottieFragment<ScenesFragmentJunkBinding, ScenesViewModel>() {

    private val mScenesLandingWarnDialogFragment by lazy {
        ScenesLandingWarnDialogFragment()
    }
    private var ctx: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (mModel.mLocalAdStatus == LocalAdStatus.SHOW) {
                //广告展示时屏蔽返回键
                return@addCallback
            }
            if (showDangerArea) {
            //    showDangerPage()
                return@addCallback
            }
            isEnabled = false
            activity?.onBackPressed()
            isEnabled = true
        }
    }


    /**
     * 显示危险页
     */
    private fun showDangerPage() {
        //Toast.makeText(requireContext(), "展示危险页", Toast.LENGTH_SHORT).show()
        setDangerLandDesc()
        mScenesLandingWarnDialogFragment.rxShow(
            requireActivity().supportFragmentManager,
            ScenesLandingWarnDialogFragment.TAG
        ).subscribeBy { it ->
            if (it == ScenesLandingWarnDialogFragment.FINISH_STATE) {
                // showToast("倒计时结束关闭了页面")
                runningConsumer?.let {
                    onSceneButtonClick(it)
                }
            }
        }
    }

    companion object {
        const val TAG = "JunkFragment"
        fun newInstance(consumer: Consumer<Boolean>): JunkFragment {
            return JunkFragment().also {
                it.mConsumer = consumer
            }
        }
    }


    data class LoadingBean(val name: String, var loading: Boolean = true)

    //是否是危险页可弹出时间段（需要配合广告状态决定）
    private var showDangerArea: Boolean = false

    private val loadingList = mutableListOf<LoadingBean>(
        LoadingBean(ResourceUtil.getString(R.string.scenes_app_cache)),
        LoadingBean(ResourceUtil.getString(R.string.scenes_useless_package)),
        LoadingBean(ResourceUtil.getString(R.string.scenes_download_cache)),
        LoadingBean(ResourceUtil.getString(R.string.scenes_log_files)),
        LoadingBean(ResourceUtil.getString(R.string.scenes_ad_spam)),
        LoadingBean(ResourceUtil.getString(R.string.scenes_empty_folder))
    )


    private val log by lazy {
        VLog.scoped(TAG)
    }

    private var mConsumer: Consumer<Boolean>? = null

    private val mJunkModel by lazy {
        ViewModelProvider(requireActivity(), JunkViewModelFactory()).get(
            JunkViewModel::class.java
        )
    }

    private var argbAnimator: ValueAnimator? = null


    private val progressAnimator by lazy {
        ValueAnimator.ofInt()
    }

    private val mAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(MenuJunkParent::class.java, JunkDetailParentViewHolder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuJunkParent> {
                override fun onItemClick(view: View, position: Int, t: MenuJunkParent) {
                    when (view.id) {
                        R.id.item_check_iv -> {
                            //选中
                            mJunkModel.select(t)
                        }
                        else -> {
                            mJunkModel.expand(t)
                        }
                    }
                }
            }
        })
        adapter.register(MenuJunkChild::class, JunkDetailChildViewHolder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<MenuJunkChild> {
                override fun onItemClick(view: View, position: Int, t: MenuJunkChild) {
                    mJunkModel.select(t)
                }
            }
        })

        adapter.register(LoadingBean::class, JunkLoadingViewHolder())
        adapter
    }

    override fun getLayoutId() = R.layout.scenes_fragment_junk

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

//    override fun initData() {
//        val list = DataTransport.getInstance().getAs<List<JunkInfo>>("junk_list")
//        mJunkModel.initJunkData(list)
//        super.initData()
//    }

    override fun initView() {
        initHeader()
        initRecycleView()
        initCleanBtn()
    }

    private var runningConsumer: Consumer<Boolean>? = null
    private fun initCleanBtn() {
        cleanButtonCanClick(false)
        mBinding.scenesButton.onceClick {
            runningConsumer?.let {
                onSceneButtonClick(it)
            }
        }
    }

    /**
     * 清理按钮点击事件
     */
    private fun onSceneButtonClick(consumer: Consumer<Boolean>, auto: Boolean = false) {
        mTimer.stopTimer()
        downTime = 0
        setCleanBtnText()
        runningConsumer = null
        showDangerArea = false
        cleanButtonCanClick(false)
        cleanStart = true
        cleanJunks(consumer)
        val text = if (auto) {
            "AutoClick"
        } else {
            "Button"
        }
        St.stOptimizeLevelClick(text)
    }

    private val mTimer by lazy {
        CountDownTimer(this)
    }

    private var cleanStart = false

    /**
     * 设置清理按钮是否可点击
     */
    private fun cleanButtonCanClick(click: Boolean) {
        var mClick = click
        if (cleanStart) {
            mClick = false
        }
        mBinding.scenesButton.isEnabled = mClick
    }

    private fun initHeader() {
        mBinding.headerLayout.headerLayout.backgroundResource = android.R.color.transparent
        mBinding.headerLayout.headerToolbar.apply {
            title = mModel.getScenesData().name
            navigationIcon =null
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }


    }

    override fun initObserver() {
        super.initObserver()
        mJunkModel.mJunkSizeLiveData.observe(viewLifecycleOwner, Observer {
            val number = FileUtil.getFileSizeNumberText(it)
            val unit = FileUtil.getFileSizeUnitText(it)
            val text =
                TextUtils.string2SpannableStringForSize(number + unit, unit, sizeDip = 21)
            mBinding.junkSizeTv.text = text
        })

        mJunkModel.mScanPathLiveData.observe(viewLifecycleOwner) {

            mBinding.tvFilePath.text =
                String.format("%s %s", ResourceUtil.getString(R.string.scenes_junk_scanning), it)
        }

        mJunkModel.mDetailLiveData.observe(viewLifecycleOwner, Observer {
            setAdapter(it)
        })
        mJunkModel.mSelectSizeLiveData.observe(viewLifecycleOwner, Observer {
            cleanButtonCanClick(it != 0L)
            setCleanBtnText()
        })

        mJunkModel.mScanProgress.observe(viewLifecycleOwner) {
            setViewStatus(it)
            refreshLoadList(it)
            if (it == 100) {
                showInsertAd(false)
            }
        }
    }

    var downTime = 3
    private fun setCleanBtnText() {
        val cleanText = ResourceUtil.getString(R.string.scenes_clean_x_file)
        val it = mJunkModel.mSelectSizeLiveData.value ?: 0L
        var junkSizeText = ""
        var timeText = ""
        if (it != 0L) {
            junkSizeText = FileUtil.getFileSizeText(it)
        }
        if (downTime > 0) {
            timeText = "(${downTime}s)"
        }
        mBinding.scenesButton.text = String.format("%s%s%s", cleanText, junkSizeText, timeText)

    }


//    private val insertAdConsumer: (AdResult<AdItem>) -> Unit = {
//        if (it.adStatus == AdStatus.LOAD_END) {
//            showInsertAd(true)
//        }
//    }

    /**
     * 广告与垃圾扫描进度检查
     * 只有全部执行时才
     */
    private fun statusCheck() {
        if (mJunkModel.mScanProgress.value == 100 && mModel.mLocalAdStatus == LocalAdStatus.STOP) {
            nextStep()
            startConsumer?.accept(true)
        }
    }

    /**
     * 在广告未加载情况下展示广告
     * @param isLoadEnd 是否是广告加载结束的情况下
     */
    private fun showInsertAd(isLoadEnd: Boolean) {
        if (mModel.mLocalAdStatus < LocalAdStatus.SHOW) {
            mModel.showInsertAd(context) {
                if (!it && !isLoadEnd){
                    mModel.mLocalAdStatus = LocalAdStatus.STOP
                }
                statusCheck()
            }
        } else if (mModel.mLocalAdStatus > LocalAdStatus.SHOW) {
            if (!isLoadEnd) {
                statusCheck()
            }
        }
    }

    private fun nextStep() {
        startConsumer?.accept(true)
    }

    private var startConsumer: Consumer<Boolean>? = null
    override fun startAnim(consumer: Consumer<Boolean>) {
        showDangerArea = false
        startConsumer = consumer
//        mModel.loadInsertAd(insertAdConsumer)
        setAdapter(loadingList)
        mBinding.tvFilePath.text = ResourceUtil.getString(R.string.scenes_junk_scanning)
        St.stLevelFlashShow(mModel.enterFlag, mModel.getScenesData().name)
        mJunkModel.getScanTask()
    }

    override fun runningAnim(consumer: Consumer<Boolean>) {
        showDangerArea = true
        mBinding.progress.visibility = View.INVISIBLE
        cleanButtonCanClick(true)
        startTimer()
        mBinding.tvFilePath.text = String.format(
            "%s %s", ResourceUtil.getString(R.string.app_name),
            ResourceUtil.getString(R.string.scenes_booster_not_delete_private)
        )

        val junkList = mJunkModel.junkList
        //初始化垃圾劣表
        mJunkModel.initRecycleData(junkList)
        //如果垃圾列表为空
        if (mJunkModel.getDetailList().isEmpty()) {
            consumer.accept(true)
        }
        runningConsumer = consumer
    }

    private fun startTimer() {
        mTimer.setTimerAction(
            ing = {
                downTime = it
                setCleanBtnText()
            },
            end = {
                runningConsumer?.let {
                    val mSelectSize = mJunkModel.mSelectSizeLiveData.value ?: 0L
                    if (mSelectSize > 0L) {
                        onSceneButtonClick(it, true)
                    } else {
                        //没有选中时啥也不做
                        downTime = 0
                        setCleanBtnText()
                    }
                }
            }
        )
    }


    override fun endAnim(consumer: Consumer<Boolean>) {
        showDangerArea = false
        setLandDesc()
        consumer.accept(true)
    }

    private fun setDangerLandDesc() {
        val mSelectSize = mJunkModel.mSelectSizeLiveData.value ?: 0
        if (mJunkModel.mTotalSize == 0L) {
            setEmptyDesc()
            return
        }
        val desc = "${
            FileUtil.getFileSizeNumberText(mSelectSize)
        }${
            FileUtil.getFileSizeUnitText(mSelectSize)
        }"
        mModel.getScenesData().landingName = "仍有缓存待清理"
        mModel.getScenesData().landingDesc =
            if (ScenesManager.getInstance().isRegisterCleanerBody()) {
                "占用手机${desc}内存"
            } else {
                ResourceUtil.getString(R.string.scenes_best_optimized_for_mobile)
            }

        St.stOptimizeLevelShow("Junk Clean", desc)
    }


    private fun setLandDesc() {
        val mSelectSize = mJunkModel.mSelectSizeLiveData.value ?: 0
        if (mJunkModel.mTotalSize == 0L) {
            setEmptyDesc()
            return
        }
        val desc = "${
            FileUtil.getFileSizeNumberText(mSelectSize)
        }${
            FileUtil.getFileSizeUnitText(mSelectSize)
        }"
        mModel.getScenesData().landingName = ResourceUtil.getString(R.string.scenes_junk_clean_finish)
        mModel.getScenesData().landingDesc =
            if (ScenesManager.getInstance().isRegisterCleanerBody()
                ||ScenesManager.getInstance().isRegisterPowerBody()) {
                String.format(ResourceUtil.getString(R.string.scenes_x_freed), desc)
            } else {
                ResourceUtil.getString(R.string.scenes_best_optimized_for_mobile)
            }

        St.stOptimizeLevelShow("Junk Clean", desc)
    }

    private fun cleanJunks(consumer: Consumer<Boolean>) {
        var index = 0
        //count总垃圾执行器的数量
        //result当前垃圾执行器的结果
        //item执行垃圾的对象
        mJunkModel.cleanJunk { count, result, item ->
            if (result == JunkConstants.ScanStatus.CLEAN) {
                index++
            }
            log.d("junk clean count:$count -> index:$index")
            viewScope.launch(context = Dispatchers.Main) {
                if (count == 0 || index >= count) {
                    consumer.accept(true)
                } else {
                    item?.let {
                        mBinding.tvFilePath.text = String.format(
                            "%s %s",
                            ResourceUtil.getString(R.string.scenes_clean_ing),
                            it.path
                        )
                    }
                }
            }
        }
    }


    /**
     * 设置空的清理文案
     */
    private fun setEmptyDesc() {
        mModel.getScenesData().landingDesc = ResourceUtil.getString(R.string.scenes_have_rest)
        mModel.getScenesData().landingName = getString(R.string.scenes_junk_very_clean)
    }

    override fun preFinish(result: Boolean) {
        mConsumer?.accept(result)
    }

    private fun initRecycleView() {
        mBinding.scenesRecyclerview.apply {
            layoutManager = LinearLayoutManager(this.context)
        }
    }

    private fun setAdapter(list: List<Any>) {
        if (mBinding.scenesRecyclerview.adapter == null) {
            mBinding.scenesRecyclerview.adapter = mAdapter
        }
        mAdapter.items = list
        mAdapter.notifyDataSetChanged()
    }


    private fun refreshLoadList(progress: Int) {
        val position = (progress / 100.0 * loadingList.size).toInt()
        var index = 0
        while (index < position) {
            val loadingBean = loadingList[index]
            loadingBean.loading = false
            loadingList[index] = loadingBean
            index++
        }
        setAdapter(loadingList)
    }

    private fun setViewStatus(it: Int) {
        val junkStatusView = mBinding.viewJunkStatus
        val viewBackgroundColor = getViewBackgroundColor(junkStatusView)
        val evaluate = ARGBUtils.evaluate(it / 100f, viewBackgroundColor, 0xFFF54E29.toInt())
        if (argbAnimator == null) {
            argbAnimator = ValueAnimator.ofArgb(viewBackgroundColor, 0xff3198F4.toInt())
        }
        changeValueByAnimator(
            argbAnimator!!,
            viewBackgroundColor,
            evaluate,
            500
        ) {
            junkStatusView.backgroundColor = it
        }

        val progress = mBinding.progress
        changeValueByAnimator(
            progressAnimator,
            progress.progress,
            it,
            500
        ) {
            progress.progress = it
        }
    }

    private fun changeValueByAnimator(
        animator: ValueAnimator,
        startValue: Int,
        endValue: Int,
        time: Long = 1000,
        listen: (Int) -> Unit
    ) {
//        animator.removeAllUpdateListeners()
        animator.duration = time
        animator.setIntValues(startValue, endValue)
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            listen(value)
        }
        animator.start()
    }

    private fun getViewBackgroundColor(view: View): Int {
        val background = view.background
        var bgColor = DAttrUtil.getPrimaryColor(view.context)
        if (background is ColorDrawable) {
            bgColor = background.color
        }
        return bgColor
    }

    private fun removeAnimator(animator: Animator) {
        animator.removeAllListeners()
        animator.cancel()
    }

    override fun onDestroyView() {
        removeAnimator(progressAnimator)
        argbAnimator?.let { removeAnimator(it) }
        super.onDestroyView()
    }


}