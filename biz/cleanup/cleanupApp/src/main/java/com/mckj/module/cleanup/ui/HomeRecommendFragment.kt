package com.mckj.module.cleanup.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import com.dn.vi.app.base.databinding.bindActive
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.cm.log.VLog

import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.view.anim.ZoomAnimator
import com.mckj.baselib.view.anim.playZoom
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.anima.AlphaAnimator
import com.mckj.module.cleanup.databinding.FragmentHomeRecommendBinding
import com.mckj.module.cleanup.entity.AbsHomeRecommendScenes
import com.mckj.module.cleanup.entity.scenes.HomeRecommendManager
import com.mckj.module.cleanup.gen.CleanupSp
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager


class HomeRecommendFragment : DialogFragment() {

    companion object {
        private const val SCENES_TYPE = "scenesType"
        private const val AD_NAME = "recommend_msg"
        const val TAG = "HomeRecommendFragment"

        @JvmStatic
        fun newInstance(scenesType: Int): HomeRecommendFragment {
            val args = Bundle()
            args.putInt(SCENES_TYPE, scenesType)
            val fragment = HomeRecommendFragment()
            fragment.arguments = args
            fragment.isCancelable = false
            return fragment
        }

    }

    private val log by lazy {
        VLog.scoped("OsHomeRecommend")
    }
    private lateinit var mBinding: FragmentHomeRecommendBinding
    lateinit var mScenes: AbsHomeRecommendScenes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogHomeFullScreen)
//        dialog?.window?.decorView?.systemUiVisibility  = View.SYSTEM_UI_FLAG_VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentHomeRecommendBinding.inflate(inflater, container, false)
        initData()
        initView()
        initEvent()
        return mBinding.root
    }


    fun initData() {
        val scenesType = arguments?.getInt(SCENES_TYPE, -1) ?: -1
        val scenes = HomeRecommendManager.getInstance().getScenes(scenesType)
        if (scenes == null) {
            log.e("init error: scenes is null, type[${scenesType}]")
            dismiss()
        } else {
            mScenes = scenes
        }
    }


    fun initView() {
        showAd()
        initSystemUI()
        initToolBar()
        //边缘闪烁动画
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.cleanup_splash)
        //按钮动画
        mBinding.btnLottile.setAnimation(R.raw.btnlottile)
        //启动放大缩小动画
        val zoomAnimator = ZoomAnimator(
            lifecycle,
            mBinding.rcScenesBt,
            1f,
            1.1f,
            1f,
            time = 1000L
        )
        val zoomAnimator1 = ZoomAnimator(
            lifecycle,
            mBinding.btnLottile,
            1f,
            1.1f,
            1f,
            time = 1000L
        )
        val alphaAnimator = AlphaAnimator(
            lifecycle,
            mBinding.frameAnim,
            0f,
            1f,
            0f,
            time = 1000L
        )
        zoomAnimator.play()
        zoomAnimator1.play()
        alphaAnimator.play()
        mBinding.btnLottile.playAnimation()
    }

    private fun initToolBar() {
        val scenesName = mScenes.getData().name
        St.stRecommendCardShow(scenesName)
        mBinding.apply {
            toolbar.navigationIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_back_landing, null)
            toolbar.title = scenesName
            toolbar.setTitleTextColor(ResourceUtil.getColor(R.color.black))
            toolbar.setNavigationOnClickListener {
                St.stRecommendCardClick("跳过")
                //设置退出时机，用于onResume弹窗CD检测
                CleanupSp.instance.skipTimestamp = System.currentTimeMillis()
                dismissAllowingStateLoss()
            }
        }
    }

    private fun initSystemUI() {
        //状态栏字体黑色，同时styles里设白色背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog?.window?.let {
                it.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

    private fun initEvent() {
        mBinding.rcScenesBt.text = mScenes.getData().btnText
        mBinding.rcScenesDes.text = mScenes.getData().desc
        mBinding.rcScenesTitle.text = mScenes.getData().title
        mBinding.rcScenesIcon.setImageResource(mScenes.getIconResId())
        mBinding.rcScenesBt.onceClick {
            St.stRecommendCardClick("button")
            mScenes.jumpPage(requireContext())
            dismissAllowingStateLoss()
        }
    }

    private fun showAd() {
        val adName = mScenes.getData().adName ?: ""
//        if (adName.isNotEmpty()) {
//            AdManager.getInstance().showAd(
//                adName,
//                ViewGroupAdContainer(mBinding.adLayout),
//                this,
//                OSDefaultNativeRender(),
//            ) {
//                when (it.adStatus) {
//                    AdStatus.SHOW_SUCCESS -> {
//                        mBinding.adLayout.show()
//                    }
//                    AdStatus.ERROR -> {
//                        mBinding.adLayout.gone()
//                    }
//                    AdStatus.CLICK -> {
//
//                    }
//                    AdStatus.CLOSE -> {
//                        mBinding.adLayout.gone()
//                    }
//                }
//            }
//        } else {
//            mBinding.adLayout.gone()
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding.btnLottile.cancelAnimation()
        mBinding.btnLottile.clearAnimation()
        mBinding.rcScenesBt.clearAnimation()
        mBinding.frameAnim.clearAnimation()
    }
}