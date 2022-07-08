package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.landhead

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import com.dn.vi.app.base.app.DatabindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.NotifyFragmentHeadBinding
import kotlinx.coroutines.*

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.dust
 * @data  2022/3/8 11:13
 */
class NotifyHeadFragment : DatabindingFragment<NotifyFragmentHeadBinding>() {

    companion object {
        fun getInstance() = NotifyHeadFragment()
    }

    override fun onCreateDatabinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): NotifyFragmentHeadBinding {
        return NotifyFragmentHeadBinding.inflate(inflater, container, false)
    }

    override fun initLayout() {
        val text = AnimationUtils.loadAnimation(requireContext(), R.anim.notify_anim_text)
        val icon = AnimationUtils.loadAnimation(requireContext(), R.anim.notify_anim_icon)
        icon.setAnimationListener(object : Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {
                viewScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO){
                        delay(200)
                    }
                }
                startAnimation(binding.notifyFinishText,text)
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        startAnimation(binding.notifyFinishIcon,icon)
    }

    private fun startAnimation(view: View, animation: Animation) {
        view.isVisible=true
        view.startAnimation(animation)
    }

}