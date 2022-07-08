package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.kt.transportData
import com.dn.vi.app.cm.utils.DAttrUtil
import com.org.openlib.help.Consumer
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.datalib.entity.ARouterPath

import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.DustFragmentBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.landhead.DustHeadFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.DustHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.LottieHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.DustViewModel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.vm.obtainViewModel
import com.mckj.sceneslib.util.Log
import org.jetbrains.anko.backgroundColor

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment
 * @data  2022/3/2 17:27
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_DUST)
class DustFragment : DataBindingFragment<DustFragmentBinding, DustViewModel>() {

    companion object {
        const val TAG = "DustFragment"
        const val AD_NAME = "htend_plaque"
        private const val TITLE = "声波除尘"
        fun newInstance(consumer: Consumer<Boolean>): DustFragment {
            return DustFragment().also {
                it.mConsumer = consumer
            }
        }
    }

    private var mConsumer: Consumer<Boolean>? = null
    private var currentMode = DustViewModel.DUST_MODE_EARPIECE
    private var isEnd = false
    private val dustHelper by lazy {
        DustHelper(requireContext())
    }

    override fun getLayoutId() = R.layout.dust_fragment

    override fun getViewModel(): DustViewModel {
        return obtainViewModel()
    }


    override fun initData() {
        lifecycle.addObserver(dustHelper)
//        NewsAdHelper.loadAd(AD_NAME)
    }

    override fun initView() {
        mBinding.apply {
            newTitleBar.setTitle(TITLE)
            preLottieSrc()
        }
        initEvent()
    }

    private fun DustFragmentBinding.preLottieSrc() {
        LottieHelper.preLottie(dustIngLottie, LottieHelper.DUST_ING_LOTTIE)
        LottieHelper.preLottie(dustFingerLottie, LottieHelper.NOTIFY_FINGER_LOTTIE)
        LottieHelper.startLottie(dustFingerLottie)
        LottieHelper.preLottie(
            dustFinishLottie,
            LottieHelper.DUST_END_LOTTIE,
            loopCount = 0,
            end = {
//                val cacheAd = NewsAdHelper.isCacheAd(AD_NAME)
//                NewsAdHelper.showAd(requireContext(), AD_NAME, false) { adStatus ->
//                    Log.i(
//                        TAG,
//                        "${StringHexConfig.showad} name:$AD_NAME status:$adStatus-----$cacheAd"
//                    )
//                    when (adStatus) {
//                        AdStatus.LOAD_END -> {
//                            St.stLevelBackAdShow(AD_NAME)
//                        }
//                        AdStatus.CLICK -> {
//                            St.stLevelBackAdClick(AD_NAME)
//                        }
//                        AdStatus.CLOSE -> {
//                            mConsumer?.accept(true)
//                        }
//                        AdStatus.ERROR -> {
//                            mConsumer?.accept(true)
//                        }
//                        else -> {
//                        }
//                    }
//                }

            })
    }

    private var position = 0

    private fun initEvent() {
        mBinding.apply {

            newTitleBar.setTitleBarListener(backAction = {
                requireActivity().finish()
            })

            speakerDust.setOnClickListener {
                if (position == 1) {
                    dustHelper.changeMode()
                    position = 0
                }
                mModel.changeDustModel(DustViewModel.DUST_MODE_SPEAKER)
            }

            earpieceDust.setOnClickListener {
                if (position == 0) {
                    dustHelper.changeMode()
                    position = 1
                }
                mModel.changeDustModel(DustViewModel.DUST_MODE_EARPIECE)
            }

            dustBt.setOnClickListener {
                dustHelper.doVibrateWork()
                if (currentMode == DustViewModel.DUST_MODE_SPEAKER)
                    St.stDustSpeakerStartClick()
                else
                    St.stDustReceiverStartClick()
            }

            dustHelper.setOnVibrateListener(
                start = {
                    changeInitView(false)
                    dustFingerLottie.isVisible = false
                    LottieHelper.startLottie(dustIngLottie)
                    LottieHelper.stopLottie(dustFingerLottie)
                },
                ing = {
                    progressValue.text = "${it}%"
                    dustBt.text = "正在除尘..."
                },
                stop = {
                    dustBt.text = "继续除尘"
                    LottieHelper.pauseLottie(dustIngLottie)
                },
                end = {
                    isEnd = true
                    progressValue.text = "100%"
                    dustBt.visibility = View.INVISIBLE
                    dustIngLottie.isVisible = false
                    dustFinishContainer.isVisible = true
                    LottieHelper.stopLottie(dustIngLottie)
                    LottieHelper.startLottie(dustFinishLottie)
                },
                reset = {
                    if (!isEnd) {
                        changeInitView(true)
                    }
                })

            transportData {
                val again = get(DustHeadFragment.DUST_AGAIN) as? Boolean
                again?.let {
                    if (it) {
                        dustHelper.changeMode()
                        dustHelper.doVibrateWork()
                    }
                }
            }
        }
    }

    private fun DustFragmentBinding.changeInitView(isReset: Boolean) {
        val themeColor = DAttrUtil.getPrimaryColor(requireContext())
        dustContainer.backgroundColor = if (isReset) Color.WHITE else themeColor
        dustBt.background =
            ContextCompat.getDrawable(
                requireContext(),
                if (isReset) R.drawable.shape_dust_bt_select else R.drawable.shape_dust_bt_normal
            )
        dustBt.setTextColor(if (isReset) Color.WHITE else themeColor)
        progressValue.text = ""
        dustBt.text = "开始除尘"
        initContainer.isVisible = isReset
    }


    override fun initObserver() {
        super.initObserver()
        val that = this
        mBinding.apply {
            mModel.apply {
                dustModel.observe(that) {
                    St.stDustReceiverClick()
                    currentMode = it
                    speakerDust.background = ContextCompat.getDrawable(
                        requireContext(),
                        if (it == DustViewModel.DUST_MODE_SPEAKER) R.drawable.shape_dust_change_select else R.drawable.shape_dust_change_normal
                    )
                    speakerDust.setTextColor(
                        if (it == DustViewModel.DUST_MODE_SPEAKER) Color.WHITE else Color.parseColor(
                            "#333333"
                        )
                    )

                    earpieceDust.background = ContextCompat.getDrawable(
                        requireContext(),
                        if (it == DustViewModel.DUST_MODE_EARPIECE) R.drawable.shape_dust_change_select else R.drawable.shape_dust_change_normal
                    )
                    earpieceDust.setTextColor(
                        if (it == DustViewModel.DUST_MODE_EARPIECE) Color.WHITE else Color.parseColor(
                            "#333333"
                        )
                    )
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        LottieHelper.stopLottie(mBinding.dustIngLottie)
    }


}