package com.mckj.gallery.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mckj.gallery.bean.BucketBean
import com.dn.vi.app.base.view.drawable.DividerDrawable
import com.dn.vi.app.scaffold.LightDialogBindingFragment
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.baselib.base.AbstractViewBinder.*
import com.mckj.baselib.util.SizeUtil
import com.mckj.gallery.databinding.CleanupxDialogAlbumBinding
import com.mckj.gallery.viewbinder.BucketViewBinder
import com.mckj.gen.St
import com.mckj.utils.EventTrack

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/21 19:24
 * @desc 图片文件夹选择dialog
 */
class BucketDialogFragment : LightDialogBindingFragment() {

    private lateinit var binding: CleanupxDialogAlbumBinding
    override val dialogWindowHeight: Int
        get() = SizeUtil.dp2px(473f)

    override val dialogWindowWidth: Int
        get() = SizeUtil.dp2px(312f)
    private var mData = mutableListOf<BucketBean>()
    private var mItemClickListener: OnItemClickListener<BucketBean>? = null
    private val mBucketAdapter by lazy {
        val adapter = MultiTypeAdapter()
        adapter.register(BucketBean::class.java, BucketViewBinder().also {
            it.itemClickListener = object : OnItemClickListener<BucketBean> {
                override fun onItemClick(view: View, position: Int, t: BucketBean) {
                    EventTrack.stClassifyPopAlbumClick(t.bucketName)
                    dismiss()
                    if (t.cover.isNotEmpty()) {
                        mItemClickListener?.onItemClick(view, position, t)
                    }
                }
            }
        })
        adapter
    }

    companion object {
        fun newInstance(): BucketDialogFragment {
            return BucketDialogFragment()
        }
    }

    override fun onCreateRootBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): ViewDataBinding? {
        binding = CleanupxDialogAlbumBinding.inflate(inflater, container, false)
        initView()
        return binding
    }


    private fun initView() {
        binding.close.setOnClickListener {
            EventTrack.stClassifyPopCloseClick()
            dismiss()
        }
        binding.recycleView.layoutManager = GridLayoutManager(context, 2)
        binding.recycleView.addItemDecoration(
            DividerItemDecoration(
                activity,
                RecyclerView.HORIZONTAL
            ).also { decor ->
                decor.setDrawable(DividerDrawable(SizeUtil.dp2px(7f)).also { })
            })
        binding.recycleView.adapter = mBucketAdapter
    }

    fun setData(list: MutableList<BucketBean>) {
        mData.clear()
        mData.addAll(list)
        mBucketAdapter.items = mData
        mBucketAdapter.notifyDataSetChanged()
    }

    fun registerItemClickListener(listener: OnItemClickListener<BucketBean>) {
        this.mItemClickListener = listener
    }
}