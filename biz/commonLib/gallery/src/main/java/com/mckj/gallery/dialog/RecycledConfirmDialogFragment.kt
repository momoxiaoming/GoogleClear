package com.mckj.gallery.dialog

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.dn.vi.app.base.view.drawable.SimpleRoundDrawable
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.mckj.gallery.utils.SpanStringProducer
import com.mckj.baselib.util.TextUtils
import com.mckj.gallery.R
import com.mckj.gallery.databinding.CleanupxDialogRecycledConfirmBinding
import com.mckj.gallery.datebase.entity.RecycledBean
import com.mckj.gallery.job.JobChain
import com.mckj.gallery.job.recycled.CompletelyDeleteJodCreate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author xx
 * @version 1
 * @createTime 2021/7/21 19:24
 * @desc
 */
class RecycledConfirmDialogFragment : LightDialogBindingFragment() {

    companion object {
        const val DIALOG_TYPE_REGAIN = "regain"
        const val DIALOG_TYPE_COMPLETELY_DELETE = "CompletelyDelete"

        fun newInstance(): RecycledConfirmDialogFragment {
            return RecycledConfirmDialogFragment()
        }
    }

    override val dialogBackgroundDrawable: Drawable
        get() = SimpleRoundDrawable(context, 0x00000000).also {

        }
    private lateinit var binding: CleanupxDialogRecycledConfirmBinding
    private val mData = mutableListOf<RecycledBean>()
    private var mType: String = ""
    private var mCompletelyDeleteListener: CompletelyDeleteListener? = null
    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        binding = CleanupxDialogRecycledConfirmBinding.inflate(inflater, container, false)
        initView()
        return binding
    }


    private fun initView() {
        when (mType) {
            DIALOG_TYPE_REGAIN -> showRegainView()
            DIALOG_TYPE_COMPLETELY_DELETE -> showCompletelyDeleteView()
        }
        binding.cancel.setOnClickListener {
            dismiss()
        }
        binding.delete.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                for (bean in mData) {
                    JobChain.newInstance().addJob(CompletelyDeleteJodCreate(bean).create())
                }
                withContext(Dispatchers.Main) {
                    mCompletelyDeleteListener?.delete()
                    dismiss()
                }
            }
        }
        binding.iSee.setOnClickListener {
            mCompletelyDeleteListener?.iSee()
            dismiss()
        }
        if (mData.isNullOrEmpty()){
            return
        }
        buildContent(mData.size, mData[0].mimeType!!)
    }

    fun setData(list: MutableList<RecycledBean>?, type: String) {
        this.mType = type
        mData.clear()
        list?.let {
            mData.addAll(it)
        }
    }


    private fun buildContent(size: Int, mimeType: String) {
        var tip: String = if (mimeType.startsWith("image")) {
            getString(R.string.gallery_x_images)
        } else {
            getString(R.string.gallery_x_videos)
        }
        var titleTip: String = if (mimeType.startsWith("image")) {
            getString(R.string.gallery_photos)
        } else {
            getString(R.string.gallery_video)
        }
        binding.remindTitle.text =
            String.format(getString(R.string.gallery_x_restored_successfully), titleTip)
        binding.confirmTitle.text =
            String.format(getString(R.string.gallery_x_delete_successfully), titleTip)

        val spanStringProducer = SpanStringProducer()

        val content = String.format(getString(R.string.gallery_x_image_deleted_sure), "${size}$tip")
        val confirmText = TextUtils.string2SpannableStringForColor(
            content,
            "${size}$tip",
            color = Color.parseColor("#40A7FF")
        )

        val remind =
            String.format(getString(R.string.gallery_x_restored_successfully_tips), "${size}$tip")
        val remindText = TextUtils.string2SpannableStringForColor(
            remind,
            "${size}$tip",
            color = Color.parseColor("#40A7FF")
        )

        binding.confirmContent.text = confirmText
        binding.remindContent.text = remindText
    }

    private fun showRegainView() {
        binding.remindLl.show()
        binding.confirmLl.gone()
    }

    private fun showCompletelyDeleteView() {
        binding.confirmLl.show()
        binding.remindLl.gone()
    }

    fun registerCompletelyDeleteListener(listener: CompletelyDeleteListener) {
        this.mCompletelyDeleteListener = listener
    }

    interface CompletelyDeleteListener {
        fun delete()
        fun iSee()
    }

/*    fun showAd() {
        AdManager.getInstance().showAd(
            "game_clean",
            binding.adGroup.toAdContainer(),
            this,
            GalleryNativeRender(),
        ) { adResult ->
            when (adResult.adStatus) {
                AdStatus.SHOW_SUCCESS -> {
                    binding.adGroup.show()
                }
                AdStatus.ERROR -> {
                    binding.adGroup.gone()
                }
                else -> {
                }
            }
        }
    }*/
}