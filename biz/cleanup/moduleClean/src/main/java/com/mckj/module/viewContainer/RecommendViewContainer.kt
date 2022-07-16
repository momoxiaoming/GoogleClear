package com.mckj.module.viewContainer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder
import com.mckj.baselib.util.SizeUtil
import com.mckj.moduleclean.R
import com.mckj.moduleclean.databinding.CleanupTxReconmendBinding
import com.mckj.sceneslib.manager.scenes.AbstractScenes
import com.mckj.sceneslib.ui.viewbinder.LandingGuideViewBinder

/**
 * @author xx
 * @version 1
 * @createTime 2021/9/7 16:48
 * @desc
 */
class RecommendViewContainer(context: Context) : BaseViewContainer<List<AbstractScenes>>(context) {

    private lateinit var mBinding: CleanupTxReconmendBinding
    private lateinit var mAdapter: MultiTypeAdapter
    override fun initView(context: Context): View {
        rootView = LayoutInflater.from(context).inflate(R.layout.cleanup_tx_reconmend, null, false)
        mBinding = DataBindingUtil.bind(rootView)!!
        val layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
//        layoutParams.leftMargin = SizeUtil.dp2px(11f)
//        layoutParams.rightMargin = SizeUtil.dp2px(11f)
//        layoutParams.topMargin = SizeUtil.dp2px(9f)
        rootView.layoutParams = layoutParams
        mBinding.recycleView.apply {
            buildAdapter()
            val manager = LinearLayoutManager(context)
            manager.orientation = LinearLayoutManager.VERTICAL
//            addItemDecoration(
//                DividerItemDecoration(
//                    context,
//                    RecyclerView.VERTICAL
//                ).also { decor ->
//                    val divider = DividerDrawable(SizeUtil.dp2px(15f))
//                    divider.color = android.R.color.transparent
//                    decor.setDrawable(divider)
//                })
            layoutManager = manager
            adapter = mAdapter
        }
        return rootView
    }

    override fun updateUI(context: Context, data: List<AbstractScenes>) {
        mAdapter.items = data
        mAdapter.notifyDataSetChanged()
    }

    private fun buildAdapter() {
        mAdapter = MultiTypeAdapter()
        mAdapter.register(AbstractScenes::class, LandingGuideViewBinder().also {
            it.itemClickListener = object : AbstractViewBinder.OnItemClickListener<AbstractScenes> {
                override fun onItemClick(view: View, position: Int, t: AbstractScenes) {
                    t.jumpPage(context)
                }
            }
        })
    }
}