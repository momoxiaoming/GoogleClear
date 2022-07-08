package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify

import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.base.app.UI
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.NotifyDialogPermissionBinding
import com.mckj.sceneslib.gen.St

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.fragment.notify
 * @data  2022/3/21 15:51
 */
class NotifyPermissionDialogFragment:LightDialogBindingFragment(){

    companion object {
        const val TAG = "NotifyPermissionDialogFragment"

        @JvmStatic
        fun newInstance(): NotifyPermissionDialogFragment {
            return NotifyPermissionDialogFragment()
        }
    }
    private lateinit var mBinding: NotifyDialogPermissionBinding

    override val gravity: Int
        get() = Gravity.BOTTOM

    override val dialogWindowWidth: Int
        get() = UI.screenWidth

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        mBinding=NotifyDialogPermissionBinding.inflate(inflater,container,false)
        initView()
       return mBinding
    }




    private fun initView() {
        mBinding.apply {
            notifyPermissionHint.text ="${AppMod.getString(R.string.app_name)}需要获取权限 才可以使用此功能"
            notifyPermissionCancel.setOnClickListener {
                dismissAllowingStateLoss()
            }

            notifyPermissionSure.setOnClickListener {
                dispatchButtonClickObserver(DialogInterface.BUTTON_POSITIVE)
                dismissAllowingStateLoss()
            }
        }
    }

}