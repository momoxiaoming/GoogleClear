package com.mckj.module.cleanup.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.mckj.api.util.ScopeHelper
import com.mckj.baselib.util.ResourceUtil
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.databinding.CleanupFragmentHomeGuideBinding
import com.mckj.module.cleanup.gen.CleanupSp
import com.org.openlib.utils.onceClick
import com.org.openlib.utils.SystemUiUtil
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import kotlinx.coroutines.delay

class OsHomeGuideFragment : DialogFragment() {

    companion object {

        @JvmStatic
        fun newInstance(sceneType: Int): OsHomeGuideFragment {
            val args = Bundle()
            args.putInt("scene", sceneType)
            val fragment = OsHomeGuideFragment()

            fragment.setStyle(STYLE_NORMAL, R.style.cleanupDialogHomeFullScreen)
            fragment.arguments = args
            fragment.isCancelable = false
            return fragment
        }

    }

    lateinit var mBinding: CleanupFragmentHomeGuideBinding

//    private val scenesViewModel =
//        ViewModelProvider(requireContext(), ScenesViewModelFactory()).get(
//            ScenesViewModel::class.java
//        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.let { SystemUiUtil.immersiveSystemUi(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = CleanupFragmentHomeGuideBinding.inflate(inflater, container, false)
        initData()
        return mBinding.rootLayout
    }


//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        mBinding = CleanupFragmentHomeRecommendBinding.inflate(inflater, container, false)
//        initView()
//        return mBinding.rootLayout
//    }

    fun initData() {
        CleanupSp.instance.cleanupHomeGuidePopTime = System.currentTimeMillis()
//        scenesViewModel.isFinish.observe(viewLifecycleOwner, {
//            initView(it)
//        })
        val type = arguments?.getInt("scene")
//        val isCoolDown = ScenesUtils.isClickGuide
        if (type != null) {
            initView(type)
        }
    }

    fun initView(sceneType: Int) {
        mBinding.skip.let {
            it.postDelayed({
                if (isAdded) {
                        it.visibility = View.VISIBLE
                    }
                },
                3000)
        }

        mBinding.button.let {
            it.postDelayed({
                if (isAdded) {
                    St.stMaskClick(sceneType.toString(),"AutoButton")
                    ScenesManager.getInstance().getScenes(sceneType)?.jumpPage(requireContext())
                    ScopeHelper.launch {
                        delay(500)
                        dismissAllowingStateLoss()
                    }
                }
            }, 5000)
        }

//        ScopeHelper.launch {
//            withContext(Dispatchers.IO) {
//                delay(3000)
//            }
//        }


        when (sceneType) {
            ScenesType.TYPE_COOL_DOWN -> {
                mBinding.image.background =
                    ResourceUtil.getDrawable(R.drawable.cleanup_img_home_guide_cool_down)!!
                mBinding.desc.text = "电池温度过高\n寿命预计缩短30天"
                mBinding.btnText = "立即降温"
            }
            ScenesType.TYPE_PHONE_SPEED -> {
                mBinding.image.background =
                    ResourceUtil.getDrawable(R.drawable.cleanup_img_home_guide_phone_speed)!!
                mBinding.desc.text = "15+应用过度占用资源"
                mBinding.btnText = "立即加速"
            }
        }
        registerClick(sceneType)
    }


    private fun registerClick(scene: Int) {
        mBinding.button.onceClick {
            val isFirst = (arguments?.get("isFirst") == true)
            St.stMaskClick(scene.toString(),"Button")
            ScenesManager.getInstance().getScenes(scene)?.jumpPage(requireContext())
            ScopeHelper.launch {
                delay(500)
                dismissAllowingStateLoss()
            }
        }

        mBinding.skip.onceClick {
            St.stMaskClick(scene.toString(),"Close")
            dismissAllowingStateLoss()
            St.stDesktopWidgetPop()
        }
    }
}