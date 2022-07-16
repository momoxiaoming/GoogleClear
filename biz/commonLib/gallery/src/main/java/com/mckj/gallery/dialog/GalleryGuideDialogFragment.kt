package com.mckj.gallery.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieDrawable
import com.alibaba.android.arouter.launcher.ARouter
import com.dn.vi.app.base.app.UI
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.gallery.R
import com.mckj.gallery.bean.ARouterPath
import com.mckj.gallery.utils.SpanStringProducer
import com.mckj.gallery.bean.MediaConstant
import com.mckj.gallery.databinding.CleanupxDialogAlbumGuideBinding
import com.mckj.gen.GallerySp
import com.mckj.utils.EventTrack
import kotlin.properties.Delegates

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/23 10:50
 * @desc 图库使用引导
 */
class GalleryGuideDialogFragment : LightDialogBindingFragment() {

    private lateinit var binding: CleanupxDialogAlbumGuideBinding
    override val dialogWindowHeight: Int
        get() = UI.screenHeight

    override val dialogWindowWidth: Int
        get() = UI.screenWidth

    override val dialogBackgroundDrawable: Drawable
        get() = SimpleRoundDrawable(context, 0x00000000).also {

        }

    private var mCurrentProgress: Int by Delegates.observable(-1, { _, _, progress ->
        updateGuide()
    })

    companion object {
        fun newInstance(): GalleryGuideDialogFragment {
            return GalleryGuideDialogFragment()
        }
    }

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        binding = CleanupxDialogAlbumGuideBinding.inflate(inflater, container, false)
        mCurrentProgress = GallerySp.instance.galleryGuideProgress
        initView()
        return binding
    }


    private fun initView() {
        binding.rootLayout.setOnClickListener {
            mCurrentProgress++
            GallerySp.instance.galleryGuideProgress = mCurrentProgress
        }
        binding.back.setOnClickListener {
            ARouter.getInstance().build("/app/main").navigation()
            requireActivity().finish()
        }
        binding.refGuideSee.setOnClickListener {
            ARouter.getInstance().build(ARouterPath.GALLERY_RECYCLED_PATH).navigation()
            mCurrentProgress++
            GallerySp.instance.galleryGuideProgress = mCurrentProgress
            dismiss()
        }

        val spanStringProducer = SpanStringProducer()
        val spanStringBuilder = SpannableStringBuilder()
        spanStringBuilder.append(
            spanStringProducer.getColorSpan(
                getString(R.string.gallery_7day_find1),
                Color.parseColor("#040000")
            )
        ).append(
            spanStringProducer.getColorSpan(
                getString(R.string.gallery_7day_find2),
                Color.parseColor("#40A7FF")
            )
        ).append(
            spanStringProducer.getColorSpan(
                getString(R.string.gallery_7day_find3),
                Color.parseColor("#040000")
            )
        )
        binding.tip.text = spanStringBuilder
        binding.tip.text = spanStringBuilder

    }

    private fun updateGuide() {
        when (mCurrentProgress) {
            MediaConstant.GUIDE_01 -> {
                EventTrack.stPhotoCleanUpGuidanceShow()
                binding.recycleGroup.gone()
                binding.animGroup.show()
                binding.refDesc.text = getString(R.string.gallery_swipe_up_to_delete)
                startAnim()
            }
            MediaConstant.GUIDE_02->{
                EventTrack.stPhotoCleanBothSidesGuidanceShow()
                binding.recycleGroup.gone()
                binding.animGroup.show()
                binding.refDesc.text = getString(R.string.gallery_left_right_change_between_photos)
                startAnim()
            }
            MediaConstant.GUIDE_03 -> {
                EventTrack.stPhotoCleanRecycleShow()
                binding.recycleGroup.show()
                binding.animGroup.gone()
            }
            else -> {
                dismiss()
            }
        }
    }


    private fun startAnim() {
        binding.refAnimView.apply {
            if (isAnimating) cancelAnimation()
            repeatCount = LottieDrawable.INFINITE
            repeatMode = LottieDrawable.RESTART
            addAnimatorListener(object : AnimatorListenerAdapter() {
                override fun onAnimationCancel(animation: Animator?) {
                    super.onAnimationCancel(animation)
                    removeAllAnimatorListeners()
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    removeAllAnimatorListeners()
                }
            })
            when (mCurrentProgress) {
                MediaConstant.GUIDE_01 -> {
//                    imageAssetsFolder = "lottieFiles/guideLeft/images"
//                    setAnimation("lottieFiles/guideLeft/data.json")
                    setAnimation(R.raw.lottie_files_guideleft)
                }
                else -> {
//                    imageAssetsFolder = "lottieFiles/guideUp/images"
//                    setAnimation("lottieFiles/guideUp/data.json")
                    setAnimation(R.raw.lottie_files_guideup)
                }
            }
            playAnimation()
        }
    }

    fun showGuide(manager: FragmentManager) {
        val galleryGuideProgress = GallerySp.instance.galleryGuideProgress
        if (galleryGuideProgress > MediaConstant.GUIDE_03) return
        rxShow(manager, "GalleyGuideDialogFragment")
    }
}