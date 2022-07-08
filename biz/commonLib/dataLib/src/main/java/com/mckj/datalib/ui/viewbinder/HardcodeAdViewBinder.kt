package com.mckj.datalib.ui.viewbinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.app.AppMod
import com.drakeet.multitype.ItemViewBinder
import com.mckj.datalib.databinding.DataHardCodeAdBinding
import com.mckj.datalib.entity.HardcodeAdEntity


/**
 * 首页底部 放个小广告
 * Created by holmes on 2021/5/24.
 **/
class HardcodeAdViewBinder : ItemViewBinder<HardcodeAdEntity, HardcodeAdViewBinder.H>() {


    class H(val binding: DataHardCodeAdBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        override fun onClick(v: View?) {
            val context = v?.context ?: return
//            binding.item?.also { item ->
//                WebHelper.openWeb(context, WebHelper.Request(
//                    webUrl = item.dpUrl
//                ))
//            }
        }

    }

    override fun onBindViewHolder(holder: H, item: HardcodeAdEntity) {
        holder.binding.item = item
        AppMod.appComponent.getGlide()
            .load(item.pic)
            .into(holder.binding.adPic)
        holder.binding.root.setOnClickListener(holder)
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): H {
        return H(DataHardCodeAdBinding.inflate(inflater, parent, false))
    }
}