package com.mckj.baselib.view.loading

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.mckj.baselib.R

/**
 * Describe:LoadingDialog
 *
 * Created By yangb on 2020/9/25
 */
class LoadingDialog(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private var textTv: TextView? = null
    private var text: String? = null

    constructor(context: Context) : this(context, R.style.BaseLoadingDialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        if (window != null) {
            val attr = window.attributes
            if (attr != null) {
                attr.height = ViewGroup.LayoutParams.WRAP_CONTENT
                attr.width = ViewGroup.LayoutParams.WRAP_CONTENT
                attr.gravity = Gravity.CENTER //设置dialog 在布局中的位置
            }
        }
        val view = View.inflate(context, R.layout.base_progress_loading, null)
        textTv = view.findViewById(R.id.loading_text_tv)

        val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        params.gravity = Gravity.CENTER
        setContentView(view, params)
        setMessage(text)
    }

    fun setMessage(text: String?) {
        this.text = text
        textTv?.let {
            it.visibility = if (!TextUtils.isEmpty(text)) {
                it.text = text
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

}