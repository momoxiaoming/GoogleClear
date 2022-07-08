package com.mckj.module.cleanup.ui.point

import androidx.lifecycle.ViewModelProvider
import com.mckj.baselib.base.databinding.DataBindingFragment
import com.mckj.module.cleanup.R
import com.mckj.module.cleanup.databinding.CleanupFragmentPointBinding

/**
 * DeskToolFragment
 *
 * @author mmxm
 * @date 2021/3/3 20:25
 */
class DeskToolFragment : DataBindingFragment<CleanupFragmentPointBinding,PointViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.cleanup_fragment_point_tool
    }

    override fun getViewModel(): PointViewModel {
        return ViewModelProvider(this).get(PointViewModel::class.java)
    }

    override fun initData() {
    }

    override fun initView() {
    }

}