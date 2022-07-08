package com.mckj.template.style

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.TextUtils
import com.mckj.template.R
import com.mckj.template.abs.BaseViewContainer
import com.mckj.template.databinding.CleanupDayUsedBinding


class DefaultDayUsedStyle(context: Context) : BaseViewContainer<Int>(context) {


    private lateinit var mBinding: CleanupDayUsedBinding

    override fun initView(context: Context): View {
        mBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            getLayoutResId(),
            null,
            false
        )
        rootView = mBinding.root
        return rootView
    }

    override fun updateUI(context: Context, data: Int) {
        getTip().let {
            val key = (data+1).toString()
            val content = String.format(
                ResourceUtil.getString(R.string.scenes_x_day_partner_with_you),
                ResourceUtil.getText(R.string.app_name), key
            )
            it.text = TextUtils.string2SpannableStringForColor(
                content,
                key,
                color = Color.parseColor("#333333")
            )
        }
    }


    protected open fun getLayoutResId(): Int {
        return R.layout.cleanup_day_used
    }

    protected open fun getTip(): TextView {
        return mBinding.dayUsed
    }

}