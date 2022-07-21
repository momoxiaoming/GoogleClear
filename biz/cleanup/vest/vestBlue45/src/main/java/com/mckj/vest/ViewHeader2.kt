package com.mckj.vest

import android.content.Context
import android.view.View
import android.widget.TextView
import com.mckj.template.header.clean.img.AbsCleanImgHeader
import com.mckj.template.impl.IAction
import com.mckj.vest.databinding.CleanupFragmentHomeHeaderBinding

/**
 * ViewHeader2
 *
 * @author mmxm
 * @date 2022/7/19 22:16
 */
class ViewHeader2(
    context: Context,
    iAction: IAction
) : AbsCleanImgHeader<CleanupFragmentHomeHeaderBinding>(context, iAction) {
    override fun getResId(): Int {
        return R.layout.cleanup_fragment_home_header
    }

    override fun getAnimView(): View? {
        return null
    }

    override fun getScanSize(): TextView? {
        return null
    }

    override fun getScanUnit(): TextView? {
        return null
    }

    override fun getJunkDesc(): TextView? {
        return null
    }

    override fun getScanDesc(): TextView? {
        return null
    }

    override fun getScanStatus(): TextView? {
        return null
    }

    override fun getScanAction(): TextView? {
        return null
    }

    override fun getScanEmptyView(): View? {
        return null
    }
}