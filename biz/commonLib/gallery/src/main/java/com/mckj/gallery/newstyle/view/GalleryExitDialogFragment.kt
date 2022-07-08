package com.mckj.gallery.newstyle.view

import android.content.DialogInterface
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.baselib.util.SizeUtil
import com.mckj.gallery.databinding.CleanupxDialogGalleryExitBinding
import com.org.openlib.utils.onceClick

class GalleryExitDialogFragment : LightDialogBindingFragment(){

    private lateinit var binding:CleanupxDialogGalleryExitBinding
    override val dialogWindowHeight: Int
        get() = SizeUtil.dp2px(265f)
    override val dialogWindowWidth: Int
        get() = SizeUtil.dp2px(311f)

    var confirm:(()->Unit)? = null
    var cancel:(()->Unit)? = null

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): CleanupxDialogGalleryExitBinding? {
        binding = CleanupxDialogGalleryExitBinding.inflate(inflater, container, false)
        initView()
        cancelBack()
        return binding
    }

    private fun initView() {
        binding.exitCancelTv.onceClick {
            dismissAllowingStateLoss()
            cancel?.invoke()
        }
        binding.exitConfirmTv.onceClick {
            dismissAllowingStateLoss()
            confirm?.invoke()
        }
    }

    private fun cancelBack() {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setOnKeyListener(object : DialogInterface.OnKeyListener {
            override fun onKey(p0: DialogInterface?, keyCode: Int, p2: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true
                }
                return false
            }
        })
    }
}