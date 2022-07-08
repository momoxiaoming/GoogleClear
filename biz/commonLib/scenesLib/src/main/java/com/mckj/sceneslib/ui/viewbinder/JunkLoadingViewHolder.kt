package com.mckj.sceneslib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.CleanupJunkLoadingItemBinding
import com.mckj.sceneslib.ui.junk.JunkFragment

/**
 * Describe:
 *
 * Created By yangb on 2021/3/3
 */
class JunkLoadingViewHolder : DataBindingViewBinder<JunkFragment.LoadingBean>() {

//    private val rotate by lazy {
//        val rotateAnimation = RotateAnimation(0f, 1f)
//        rotateAnimation.repeatMode =  Animation.RESTART
//        rotateAnimation.repeatCount = Animation.INFINITE
//        rotateAnimation
//    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanup_junk_loading_item, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<JunkFragment.LoadingBean, CleanupJunkLoadingItemBinding>(
            itemView
        ) {

        override fun bindData(t: JunkFragment.LoadingBean) {
            mBinding.itemNameTv.text = t.name
            if (t.loading){
//                mBinding.itemIv.startAnimation(rotate)
                mBinding.itemIv.setImageResource(R.drawable.scenes_task_loading_progress_green)

            }else{
                mBinding.itemIv.setImageResource(R.drawable.cleanup_icon_check_sel)
            }
        }
    }
}