package com.mckj.sceneslib.ui.scenes.landing.content

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.view.View
import androidx.core.view.doOnLayout
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.cm.utils.TextUtils
import com.drakeet.multitype.MultiTypeAdapter

import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.datalib.helper.FingerHelper
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesFragmentLandingContentOneKeyCleanBinding
import com.mckj.sceneslib.entity.ScenesData
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.sceneslib.manager.scenes.ScenesType
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import com.mckj.sceneslib.ui.scenes.ScenesViewModelFactory

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingContentOneKeyCleanFragment :
    DataBindingFragment<ScenesFragmentLandingContentOneKeyCleanBinding, ScenesViewModel>() {

    /**
     * 手指帮助类
     */
    private val mFingerHelper by lazy { FingerHelper() }

    /**
     * 手指view
     */
    private var mFingerView: View? = null

    private val mAdapter: MultiTypeAdapter by lazy {
        val adapter = MultiTypeAdapter()
//        adapter.register(NativeAdEntity::class, NativeAdViewBinder(requireActivity()) { adStatus ->
//        })
        adapter
    }

    override fun getLayoutId() = R.layout.scenes_fragment_landing_content_one_key_clean

    override fun getViewModel(): ScenesViewModel {
        return ViewModelProvider(requireActivity(), ScenesViewModelFactory()).get(
            ScenesViewModel::class.java
        )
    }

    override fun initData() {
    }

    override fun initView() {
        startAnim()
        mBinding.landingHeaderNameTv.text = mModel.getScenesData().landingName
        mBinding.landingHeaderDescTv.text = mModel.getScenesData().landingDesc
        var guideSceneData: ScenesData? = null
        if (!mModel.getGuideScenesList().isNullOrEmpty()) {
            guideSceneData = mModel.getGuideScenesList()?.get(0)?.getData()
        }
        var type: Int? = mModel.getScenes().getRecommendTypes()?.get(0)
        if (!mModel.getScenes().getGuideTypes().isNullOrEmpty()) {
            type = mModel.getScenes().getGuideTypes()?.get(0)
        }
        val name = type?.let { mModel.getScenes().getNameByType(it) }
        mBinding.landingContentBtn.text = name

        mBinding.landingContentBtn.setOnClickListener {
            //网络测速_抢红包测速_点击
            if (type == ScenesType.TYPE_ENVELOPE_TEST) {
                St.stSpeedRedPacketClick()
            }

            if (type != null) {
                ScenesManager.getInstance()
                    .jumpPage(requireContext(), type) {
                        if (it) {
                            activity?.finish()
                        }
                    }
            }
        }

        //设置场景描述文字
        val size = (400..700).random()
        mBinding.landingContentDescTv.text = when (type) {
            ScenesType.TYPE_COOL_DOWN -> {
                TextUtils.string2SpannableStringForSize(
                    String.format(ResourceUtil.getString(R.string.scenes_overheating_affects_performance)),
                    getString(R.string.scenes_performance),
                    sizeDip = 24,
                )
            }
            ScenesType.TYPE_WX_CLEAN -> {
                TextUtils.string2SpannableString(
                    String.format(getString(R.string.scenes_to_be_cleaned_up2), "$size"),
                    String.format(getString(R.string.scenes_mb), "$size"),
                    sizeDip = 24,
                    Color.parseColor("#FA3C32"),
                    false
                )
            }
            ScenesType.TYPE_POWER_SAVE -> {
                TextUtils.string2SpannableStringForSize(
                    String.format(getString(R.string.scenes_apps_draining_battery_background)),
                    getString(R.string.scenes_draining_battery),
                    sizeDip = 24,
                )
            }
            ScenesType.TYPE_ANTIVIRUS -> {
                TextUtils.string2SpannableStringForSize(
                    getString(R.string.scenes_mobile_phone_virus_will_threaten_internet_security2),
                    getString(R.string.scenes_internet_safety),
                    sizeDip = 24,
                )
            }
            ScenesType.TYPE_SHORT_VIDEO_CLEAN -> {
                TextUtils.string2SpannableStringForSize(
                    getString(R.string.scenes_clear_short_video_cache),
                    getString(R.string.scenes_special_cleaning),
                    sizeDip = 24,
                )
            }
            ScenesType.TYPE_CAMERA_CHECK -> {
                TextUtils.string2SpannableStringForSize(
                    getString(R.string.scenes_check_privacy_risk),
                    getString(R.string.scenes_privacy_leak),
                    sizeDip = 24,
                )
            }
            ScenesType.TYPE_QQ_CLEAN -> {
                TextUtils.string2SpannableString(
                    String.format(getString(R.string.scenes_to_be_cleaned_up), "$size"),
                    String.format(getString(R.string.scenes_mb), "$size"),
                    sizeDip = 24,
                    Color.parseColor("#FA3C32"),
                    false
                )
            }
            ScenesType.TYPE_NETWORK_TEST -> {
                TextUtils.string2SpannableStringForSize(
                    String.format( getString(R.string.scenes_look_at_your_current_internet_speed)),
                    getString(R.string.scenes_defeat),
                    sizeDip = 24,
                )
            }
            ScenesType.TYPE_ENVELOPE_TEST -> {
                TextUtils.string2SpannableStringForSize(
                    getString(R.string.scenes_grab_the_red_envelope),
                    getString(R.string.scenes_slow),
                    sizeDip = 24,
                )
            }
            else -> {
                guideSceneData?.desc
            }
        }
    }

    private fun startAnim() {
        mBinding.landingHeaderNameTv.gone()
        mBinding.landingHeaderDescTv.gone()

        mBinding.landingHeaderLottie.apply {
            setAnimation(R.raw.landing_header_clean)
            speed = 1f
            repeatCount = 1

            setMinAndMaxFrame(0, 30)
            addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator?) {
                    super.onAnimationCancel(animation)
                    removeAllAnimatorListeners()
                    endAnim()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    removeAllAnimatorListeners()
                    endAnim()
                }
            })
            playAnimation()
        }

        //手指动画
        mBinding.landingContentBtn.doOnLayout {
            mFingerHelper.remove(mFingerView)
            mFingerView = mFingerHelper.showFinger(
                mBinding.landingHeaderLayout,
                mBinding.landingContentBtn,
                9, SizeUtil.dp2px(5f), 150f
            )
        }
    }

    private fun endAnim() {
        mBinding.landingHeaderNameTv.show()
        mBinding.landingHeaderDescTv.show()
        mBinding.landingHeaderLottie.apply {
            setMinAndMaxFrame(30, 268)
            playAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mFingerHelper.remove(mFingerView)
    }

}