package com.mckj.module.wifi.ui.viewBinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.ListUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.module.wifi.R
import com.mckj.module.wifi.databinding.WifiItemWifiInfoMoreBinding
import com.mckj.module.wifi.entity.WifiInfoMoreEntity
import com.mckj.openlib.helper.onceClick
import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
class WifiInfoMoreViewBinder : DataBindingViewBinder<WifiInfoMoreEntity>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): WifiInfoMoreViewHolder {
        return WifiInfoMoreViewHolder(
            inflater.inflate(
                R.layout.wifi_item_wifi_info_more,
                parent,
                false
            )
        )
    }

    inner class WifiInfoMoreViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<WifiInfoMoreEntity, WifiItemWifiInfoMoreBinding>(
            itemView
        ) {

        private val wifiInfoAdapter by lazy {
            val adapter = MultiTypeAdapter()
            val wifiInfoViewBinder = WifiInfoViewBinder()
            wifiInfoViewBinder.itemClickListener = wifiItemClickListener
            adapter.register(WifiInfo::class, wifiInfoViewBinder)
            adapter
        }

        init {
            mBinding.itemWifiInfoRecycler.layoutManager = LinearLayoutManager(itemView.context)
            mBinding.itemWifiInfoMoreLayout.onceClick {
                val list = adapter.items
                val any = ListUtil.getItem(list, layoutPosition)
                if (any is WifiInfoMoreEntity) {
                    any.isShowMore = !any.isShowMore
                    adapter.notifyItemChanged(layoutPosition)
                }
            }
        }

        override fun bindData(t: WifiInfoMoreEntity) {
            var list = t.list
            if (list.isNullOrEmpty()) {
                mBinding.itemWifiInfoRecycler.visibility = LinearLayout.GONE
                mBinding.itemWifiInfoMoreLayout.visibility = LinearLayout.GONE
                return
            }
            mBinding.itemWifiInfoRecycler.visibility = LinearLayout.VISIBLE
            if (list.size <= 3) {
                mBinding.itemWifiInfoMoreLayout.visibility = LinearLayout.GONE
            } else {
                mBinding.itemWifiInfoMoreLayout.visibility = LinearLayout.VISIBLE
                val layoutParams =
                    mBinding.itemWifiInfoMoreLayout.layoutParams as RelativeLayout.LayoutParams
                if (t.isShowMore) {
                    mBinding.itemWifiInfoMoreTv.text = "收起WiFi"
                    mBinding.itemWifiInfoMoreIv.imageResource = R.drawable.wifi_arrow_up_green
                    layoutParams.topMargin = 0
                } else {
                    list = list.subList(0, 3)
                    mBinding.itemWifiInfoMoreTv.text = "更多WiFi"
                    mBinding.itemWifiInfoMoreIv.imageResource = R.drawable.wifi_arrow_down_green
                    layoutParams.topMargin = SizeUtil.dp2px(-60f)
                }
            }
            setAdapter(list)
        }

        private fun setAdapter(list: List<WifiInfo>) {
            wifiInfoAdapter.items = list
            mBinding.itemWifiInfoRecycler.adapter = wifiInfoAdapter
        }

        private val wifiItemClickListener =
            object : OnItemClickListener<WifiInfo> {
                override fun onItemClick(view: View, position: Int, t: WifiInfo) {
                    mChildItemClickListener?.onItemClick(view, position, t)
                }
            }
    }

    private var mChildItemClickListener: OnItemClickListener<WifiInfo>? = null

    fun setChildItemClickListener(listener: OnItemClickListener<WifiInfo>) {
        mChildItemClickListener = listener
    }

}