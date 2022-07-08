package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.audio

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.dn.vi.app.base.app.DatabindingFragment
import com.org.openlib.help.Consumer
import com.mckj.datalib.entity.ARouterPath
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.AudioFragmentBinding
import com.mckj.sceneslib.gen.St
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.adapter.AudioAdapter
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.bean.VoiceLevel
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust.DustFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.magnifier.MagnifierFragment
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.AudioHelper
import com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils.LottieHelper
import kotlin.math.roundToInt

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment
 * @data  2022/3/3 18:06
 */
@Route(path = ARouterPath.NewTools.NEW_TOOLS_AUDIO)
class AudioFragment : DatabindingFragment<AudioFragmentBinding>() {
    private var mConsumer: Consumer<Boolean>? = null
    companion object {
        private const val TITLE = "智能音量"
        private const val LOTTIE_MAX_FRAME = 71f
        fun newInstance() = AudioFragment()
    }

    private val audioAdapter by lazy { AudioAdapter() }
    private val audioList by lazy {
        arrayListOf(
            VoiceLevel(
                R.drawable.ic_volume_video,
                "观看视频",
                "建议音量80%",
                0.8f
            ),
            VoiceLevel(
                R.drawable.ic_volume_song,
                "聆听歌曲",
                "建议音量60%",
                0.6f
            ),
            VoiceLevel(
                R.drawable.ic_volume_work,
                "工作时刻",
                "建议音量20%",
                0.2f
            ),
            VoiceLevel(
                R.drawable.ic_volume_rest,
                "休息时刻",
                "建议音量0%  ",
                0f
            ),
        )
    }

    private val audioHelper by lazy { AudioHelper(requireContext()) }

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AudioFragmentBinding {
        return AudioFragmentBinding.inflate(inflater, container, false)
    }

    override fun initLayout() {
        initView()
        initEvent()
        lifecycle.addObserver(audioHelper)
    }

    private fun initView() {
        St.stSetVolumeShow()
        binding.apply {
            audioLottie.setAnimation(LottieHelper.AUDIO_LOTTIE)
            audioNewTitleBar.apply {
                setTitle(TITLE, R.color.back)
                setBackColor()
                setTitleBarBgColor()
                setTitleBarListener(backAction = {
                    requireActivity().finish()
                })
            }

            val voiceLevel =
                (audioHelper.getCurrentVolume() / audioHelper.getMaxSystemVolume()) * 100
            setLottieProgress(voiceLevel.roundToInt())

            audioContainer.layoutManager = LinearLayoutManager(requireContext())
            audioContainer.adapter = audioAdapter
            audioAdapter.setList(audioList)
        }

    }

    private fun initEvent() {
        audioHelper.setOnVolumeListener {
            setLottieProgress(it)

        }

        binding.maxVoiceBt.setOnClickListener {
            St.stSetVolumeClick("一键扩音")
            audioHelper.setLevelStreamVolume()
        }

        audioAdapter.setOnItemChildClickListener { adapter, view, position ->
            val voiceLevel = adapter.data[position] as VoiceLevel
            St.stSetVolumeClick(voiceLevel.title)
            audioHelper.setLevelStreamVolume(voiceLevel.level)
        }
    }

    private var oldVolume = 0
    private var animator: Animator? = null

    private fun setLottieProgress(volume: Int) {
        val realVolume = when (volume) {
            in 18..21 -> 20
            in 60..63 -> 60
            in 80..82 -> 80
            else -> volume
        }
        var maxFrame = binding.audioLottie.maxFrame
        maxFrame = if (maxFrame == 0f) LOTTIE_MAX_FRAME else maxFrame
        animator = ValueAnimator.ofInt(oldVolume, realVolume).apply {
            addUpdateListener {
                val animatedValue = it.animatedValue as Int
                val value = ((animatedValue / 100f) * maxFrame).roundToInt()
                binding.audioLottie.frame = value
                binding.currentVolume.text = "$animatedValue%"
            }
            doOnEnd {
                oldVolume = realVolume
            }
            duration = 500
        }
        animator?.start()
    }
}