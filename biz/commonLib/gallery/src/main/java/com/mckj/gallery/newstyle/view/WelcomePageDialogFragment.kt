package com.mckj.gallery.newstyle.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dn.vi.app.base.app.UI
import com.dn.vi.app.cm.utils.FileUtil
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.baselib.util.SizeUtil
import com.mckj.gallery.R
import com.mckj.gallery.databinding.CleanupxDialogWelcomePageBinding
import com.mckj.gallery.newstyle.local.LocalDataSource
import com.mckj.gen.St
import com.org.openlib.utils.onceClick

class WelcomePageDialogFragment : LightDialogBindingFragment(){

    override val dialogWindowWidth: Int
        get() = UI.screenWidth
    override val dialogWindowHeight: Int
        get() = UI.screenHeight
    private lateinit var binding: CleanupxDialogWelcomePageBinding

    override val dialogBackgroundDrawable: Drawable
        get() = ColorDrawable(Color.TRANSPARENT)

    var enter:(()->Unit)? = null
    var cancel:(()->Unit)? = null

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): CleanupxDialogWelcomePageBinding? {
        binding = CleanupxDialogWelcomePageBinding.inflate(inflater, container, false)
        initView()
        return binding
    }

    private fun initView() {
        if (LocalDataSource.getAgain() == LocalDataSource.AGAIN){
            dismissAllowingStateLoss()
            LocalDataSource.deleteAgain()
            return
        }
        St.stPhotoCleanPopupShow()
        binding.welcomeGoBtn.onceClick {
            St.stPhotoCleanPopupClick("go")
            dismissAllowingStateLoss()
            enter?.invoke()
        }
        binding.welcomeClose.onceClick {
            St.stPhotoCleanPopupClick("close")
            cancel?.invoke()
        }
        var size = LocalDataSource.getCleanSize()
        if (size > 0){
            binding.welcomeCleanUpSize.visibility = View.VISIBLE
            binding.welcomeCleanUpSize.text = getString(R.string.welcome_dialog_clean_up_size,
                "${FileUtil.getFileSizeNumberText(size)}${FileUtil.getFileSizeUnitText(size)}")
        }else{
            binding.welcomeCleanUpSize.visibility = View.GONE
        }
    }
}