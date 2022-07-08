package com.mckj.sceneslib.ui.viewbinder

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemLandingGuideSpecialBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.bean.GuideScenesBean
import com.mckj.sceneslib.util.CountDownTimer
import com.mckj.sceneslib.util.dp
import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2021/5/21
 */
class LandingGuideViewSpecialBinder(val fragment: Fragment) :
    DataBindingViewBinder<GuideScenesBean>() {


    private val FINGER_LOTTIE = R.raw.tool_finger


    private var isSafetyState = true

    fun setSafetyState(state: Boolean) {
        isSafetyState = state
    }

    fun getSafetyState() = isSafetyState

    private val mTimer by lazy {
        CountDownTimer(fragment)
    }


    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder {
        return ViewHolder(
            inflater.inflate(
                R.layout.scenes_item_landing_guide_special,
                parent,
                false
            )
        )
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<GuideScenesBean, ScenesItemLandingGuideSpecialBinding>(
            itemView
        ) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as GuideScenesBean
                    onItemClick(it, position, item)
                    mTimer.stopTimer()
                }
            }
            mBinding.scenesLandingTopBt.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as GuideScenesBean
                    onItemClick(it, position, item)
                    mTimer.stopTimer()

                }
            }

        }

        override fun bindData(t: GuideScenesBean) {
            mBinding.apply {
                t.scenes.apply {
                    scenesLandingTopIcon.imageResource = getGuideIconResId()
                    scenesLandingTopTitle.text = getData().name
                    scenesLandingTopHint.text = getData().recommendDesc

                    scenesLandingTopLottieView.isVisible = isSafetyState
                    if (isSafetyState) {
                        mTimer.setTimerAction(ing = {
                            scenesLandingTopBt.text = "${getData().btnText}（${it + 1}s）"
                        }, end = {
                            scenesLandingTopBt.text = "${getData().btnText}"
                            enterFlag = "落地页"
//                            jumpPage(scenesLandingTopBt.context)
                            itemView.performClick()
                            St.stLevelRecommendClickAuto(getData().name)
                        })
                        scenesLandingTopLottieView.apply {
                            setAnimation(FINGER_LOTTIE)
                            repeatCount = LottieDrawable.INFINITE
                            repeatMode = LottieDrawable.RESTART
                            playAnimation()
                        }
                    } else {
                        scenesLandingTopBt.text = getData().btnText
                        val layoutParams =
                            specialContainer.layoutParams as LinearLayout.LayoutParams
                        layoutParams.bottomMargin = 32.dp
                        specialContainer.layoutParams = layoutParams
                    }

                }
            }
        }
    }
}
