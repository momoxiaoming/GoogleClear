package com.mckj.sceneslib.permission

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.baselib.util.LocationUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.sceneslib.databinding.ScenesPermissionDialogBinding
import com.mckj.sceneslib.gen.St


class DPermissionDialogFragment : LightDialogBindingFragment() {

    private lateinit var binding: ScenesPermissionDialogBinding
    override val dialogWindowHeight: Int
        get() = SizeUtil.dp2px(361f)

    override val dialogWindowWidth: Int
        get() = SizeUtil.dp2px(311f)

    override val dialogBackgroundDrawable: Drawable
        get() = SimpleRoundDrawable(context, 0x00000000).also {

        }

    private var what: String? = ""
    private var mBlock: ((accept: Boolean) -> Unit)? = null

    companion object {
        private const val TITLE = "title"
        private const val DESC = "desc"
        fun newInstance(
            title: String,
            desc: String,
            block: (accept: Boolean) -> Unit
        ): DPermissionDialogFragment {
            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(DESC, desc)
            val fragment = DPermissionDialogFragment()
            fragment.mBlock = block
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        binding = ScenesPermissionDialogBinding.inflate(inflater, container, false)
        initView()
        return binding
    }

    private fun initView() {
        arguments?.getString(TITLE)?.let {
            binding.title.text = it
        }

        arguments?.getString(DESC)?.let {
            binding.desc.text = it
        }
        binding.forwardSettings.setOnClickListener {
            LocationUtil.openSelfSetting()
            St.stPermissionSysDialogDismiss(what!!, "去设置")
            dismiss()
            mBlock?.invoke(false)
        }
        binding.close.setOnClickListener {
            St.stPermissionSysDialogDismiss(what!!, "关闭")
            dismiss()
            mBlock?.invoke(false)
        }

        dialog?.setOnDismissListener {
            if (DPermissionUtils.hasStoragePermission(requireActivity())) {
                mBlock?.invoke(true)
            } else {
                mBlock?.invoke(false)
            }
            dismiss()
        }
    }


    fun showDialog(manager: FragmentManager, what: String? = "", from: String? = "") {
        what?.let {
            this.what = it
            St.stPermissionSysDialogShow(what = it, from = from!!)
        }
        rxShow(manager, "DPermissionDialogFragment")
    }
}