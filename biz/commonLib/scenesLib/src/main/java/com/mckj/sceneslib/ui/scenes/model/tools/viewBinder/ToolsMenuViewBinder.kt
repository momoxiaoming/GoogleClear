package com.mckj.sceneslib.ui.scenes.model.tools.viewBinder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.ResourceUtil
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_ALARM_CLOCK
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_ALBUM
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_CALENDAR
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_FLASHLIGHT
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_FLOW_USING
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_FONT_SIZE
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_PHONE_PARAMETERS
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_WEATHER
import com.mckj.sceneslib.ui.scenes.model.tools.data.entity.MenuToolsItem
import com.mckj.sceneslib.ui.scenes.model.tools.utils.FlashUtils
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.R
import com.mckj.sceneslib.databinding.CleanupxToolsItemMenuBinding
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_ACCOUNT
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_CHECK_NET_WORK
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_DAYS
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_DUST
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_FONT_SCALE
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_INCREASE_VOICE
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_MAGNIFIER
import com.mckj.sceneslib.ui.scenes.model.tools.data.ToolsType.TYPE_NOTIFY_CLEAR
import org.jetbrains.anko.imageResource

/**
 * Describe:
 *
 * Created By yangb on 2020/10/15
 */
class ToolsMenuViewBinder : DataBindingViewBinder<MenuToolsItem>() {

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.cleanupx_tools_item_menu, parent, false))
    }

    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuToolsItem, CleanupxToolsItemMenuBinding>(
            itemView
        ) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as MenuToolsItem
                    onItemClick(it, position, item)
                }
            }
        }

        override fun bindData(t: MenuToolsItem) {
            mBinding.menuIv.imageResource = t.resId
            mBinding.menuNameTv.text = t.name

            when (t.type) {
                TYPE_WEATHER -> {
                    mBinding.bg.setBackgroundColor(Color.parseColor("#FEFAF2"))
                }
                TYPE_CALENDAR -> {
                    mBinding.bg.setBackgroundColor(Color.parseColor("#EFF9F7"))
                }
                TYPE_FLOW_USING -> {
                    mBinding.bg.setBackgroundColor(Color.parseColor("#F2F6FE"))
                }
                TYPE_PHONE_PARAMETERS -> {
                    mBinding.bg.setBackgroundColor(Color.parseColor("#FDF4F4"))
                }
                TYPE_FLASHLIGHT -> {
                    mBinding.bg.setBackgroundColor(Color.parseColor("#FEFAF2"))
                    if (FlashUtils.open_status) {
                        mBinding.menuIv.imageResource = t.resId
                    } else {
                        mBinding.menuIv.imageResource =
                            R.drawable.cleanup_icon_tools_flashlight_close
                    }
                }
                TYPE_FONT_SIZE -> {
                    mBinding.bg.setBackgroundColor(Color.parseColor("#F4F2FE"))
                }
                TYPE_ALARM_CLOCK -> {
                    mBinding.bg.setBackgroundColor(Color.parseColor("#F2FBFE"))
                }
                TYPE_ALBUM -> {
                    mBinding.bg.setBackgroundColor(Color.parseColor("#F1F9EF"))
                }
                TYPE_MAGNIFIER -> {
                    mBinding.menuNameTv.text = t.name
                    mBinding.bg.setBackgroundColor(Color.parseColor("#FEFAF2"))
                }
                TYPE_DUST -> {
                    mBinding.menuNameTv.text =  t.name
                    mBinding.bg.setBackgroundColor(Color.parseColor("#FDF4F4"))
                }
                TYPE_NOTIFY_CLEAR -> {
                    mBinding.menuNameTv.text =  t.name
                    mBinding.bg.setBackgroundColor(Color.parseColor("#EFF9F7"))
                }
                TYPE_INCREASE_VOICE -> {
                    mBinding.menuNameTv.text = t.name
                    mBinding.bg.setBackgroundColor(Color.parseColor("#F2F6FE"))
                }
                TYPE_ACCOUNT -> {
                    mBinding.menuNameTv.text = t.name
                    mBinding.bg.setBackgroundColor(Color.parseColor("#FEFAF2"))
                }
                TYPE_CHECK_NET_WORK -> {
                    mBinding.menuNameTv.text = t.name
                    mBinding.bg.setBackgroundColor(Color.parseColor("#F4F2FE"))
                }

                TYPE_DAYS -> {
                    mBinding.menuNameTv.text = t.name
                    mBinding.bg.setBackgroundColor(Color.parseColor("#F2F6FE"))
                }

                TYPE_FONT_SCALE -> {
                    mBinding.menuNameTv.text = t.name
                    mBinding.bg.setBackgroundColor(Color.parseColor("#FDF4F4"))
                }
                else -> {
                    mBinding.menuNameTv.text = ResourceUtil.getString(R.string.scenes_tools_unknown)
                    mBinding.bg.setBackgroundColor(Color.parseColor("#F1F9EF"))
                }
            }
        }
    }
}