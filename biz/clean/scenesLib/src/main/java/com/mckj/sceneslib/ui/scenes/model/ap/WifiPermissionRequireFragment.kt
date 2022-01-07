package com.mckj.sceneslib.ui.scenes.model.ap

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.sceneslib.databinding.ScenesLayoutSettingPerRequireBinding

/**
 * wifi需要权限
 * Created by holmes on 2021/4/23.
 **/
class WifiPermissionRequireFragment : LightDialogBindingFragment() {

    companion object {
        @JvmStatic
        fun newInstance(): WifiPermissionRequireFragment {
            val args = Bundle()

            val fragment = WifiPermissionRequireFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding: ScenesLayoutSettingPerRequireBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        binding = ScenesLayoutSettingPerRequireBinding.inflate(inflater, container, false)
        return binding
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.titleText = "权限申请"
        binding.bodyText = "开启热点需要\"设置权限\"，是否立即开启?"
        binding.ok.setOnClickListener {
            dispatchButtonClickObserver(DialogInterface.BUTTON_POSITIVE)
            dismissAllowingStateLoss()
        }
        binding.cancel.setOnClickListener {
            dispatchButtonClickObserver(DialogInterface.BUTTON_NEGATIVE)
            dismissAllowingStateLoss()
        }

    }

}