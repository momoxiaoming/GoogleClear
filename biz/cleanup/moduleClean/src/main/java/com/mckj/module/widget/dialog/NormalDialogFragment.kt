package com.mckj.module.widget.dialog

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.moduleclean.databinding.CleanupDialogNormalBinding
import java.io.Serializable

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/21 19:24
 * @desc
 */
class NormalDialogFragment : LightDialogBindingFragment() {

    companion object {
        private const val DIALOG_BEAN = "BEAN"
        fun newInstance(dialogBean: DialogBean): NormalDialogFragment {
            val args = Bundle()
            args.putSerializable(DIALOG_BEAN, dialogBean)
            val fragment = NormalDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val dialogBackgroundDrawable: Drawable
        get() = SimpleRoundDrawable(context, 0x00000000).also {

        }
    private lateinit var binding: CleanupDialogNormalBinding

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        binding = CleanupDialogNormalBinding.inflate(inflater, container, false)
        arguments?.let {
            (it.getSerializable(DIALOG_BEAN) as DialogBean).apply {
                binding.title.text = title
                binding.content.text = content
                binding.positiveBtn.text = positiveDesc
                binding.negativeBtn.text = negativeDesc
                binding.positiveBtn.setOnClickListener {
                    positive.invoke()
                    dismiss()
                }
                binding.negativeBtn.setOnClickListener {
                    negative.invoke()
                    dismiss()
                }
            }
        }
        return binding
    }

    data class DialogBean(
        var title: String,
        var content: SpannableStringBuilder,
        var positiveDesc:String = "确定",
        var negativeDesc:String = "取消",
        var positive: () -> Unit,
        var negative: () -> Unit
    ) : Serializable
}