package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.ScenesItemSecurityCheckBinding
import com.mckj.sceneslib.entity.SecurityCheckEntity
import com.mckj.baselib.base.databinding.DataBindingViewBinder

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class SecurityCheckViewBinder : DataBindingViewBinder<SecurityCheckEntity>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): SecurityCheckViewHolder {
        return SecurityCheckViewHolder(
            inflater.inflate(
                R.layout.scenes_item_security_check,
                parent,
                false
            )
        )
    }

    inner class SecurityCheckViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<SecurityCheckEntity, ScenesItemSecurityCheckBinding>(
            itemView
        ) {

        override fun bindData(t: SecurityCheckEntity) {
            mBinding.entity = t
            t.position = adapterPosition
        }
    }

}