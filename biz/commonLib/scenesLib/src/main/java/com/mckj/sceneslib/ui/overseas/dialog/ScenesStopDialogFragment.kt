package com.mckj.sceneslib.ui.overseas.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.dn.vi.app.base.app.UI
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.sceneslib.databinding.ScenesStopDialogLayoutBinding
import com.mckj.sceneslib.ui.scenes.ScenesViewModel
import kotlin.math.roundToInt

class ScenesStopDialogFragment : LightDialogBindingFragment(){

    companion object {

        @JvmStatic
        fun newInstance(): ScenesStopDialogFragment {
            val args = Bundle()
            val fragment = ScenesStopDialogFragment()
            fragment.arguments = args
            fragment.isCancelable = false
            return fragment
        }

    }

    private val vm: ScenesViewModel by lazy {
        ViewModelProvider(requireActivity()).get(ScenesViewModel::class.java)
    }

    override val dialogWindowWidth: Int
        get() = (UI.screenWidth * 0.87F).roundToInt()




    private lateinit var binding:ScenesStopDialogLayoutBinding

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        binding = ScenesStopDialogLayoutBinding.inflate(inflater,container,false)
        initView()
        return binding
    }

    private fun initView() {
        binding.tvConfirm.setOnClickListener {
            dispatchButtonClickObserver(DialogInterface.BUTTON_POSITIVE)
            dismissAllowingStateLoss()
        }

        binding.tvCancel.setOnClickListener {
            dispatchButtonClickObserver(DialogInterface.BUTTON_NEGATIVE)
            dismissAllowingStateLoss()
        }
    }

}