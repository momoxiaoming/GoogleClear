package com.mckj.gallery.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dn.vi.app.base.app.UI
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.gallery.databinding.CleanupxDialogVideoBinding

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/21 19:24
 * @desc
 */
class VideoPlayDialogFragment(val path: String) : LightDialogBindingFragment() {

    companion object {
        fun newInstance(path: String): VideoPlayDialogFragment {
            return VideoPlayDialogFragment(path)
        }
    }
    override val dialogWindowHeight: Int
        get() = UI.screenHeight

    override val dialogWindowWidth: Int
        get() = UI.screenWidth

    private lateinit var binding: CleanupxDialogVideoBinding

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        binding = CleanupxDialogVideoBinding.inflate(inflater, container, false)
        initView()
        return binding
    }

    override fun onPause() {
        super.onPause()
        if (binding.videoView.isPlaying) {
            binding.videoView.pause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.videoView.stopPlayback()
        binding.videoView.suspend()
    }

    private fun initView() {
        binding.videoView.apply {
            setVideoPath(path)
            this.setOnPreparedListener {
                start()
            }
            this.setOnErrorListener { mp, what, extra ->
                this@VideoPlayDialogFragment.dismiss()
                false
            }
            this.setOnClickListener {
                binding.videoView.stopPlayback()
                binding.videoView.suspend()
                this@VideoPlayDialogFragment.dismiss()
            }

            this.setOnCompletionListener {
                binding.videoView.suspend()
                this@VideoPlayDialogFragment.dismiss()
            }
        }
    }
}