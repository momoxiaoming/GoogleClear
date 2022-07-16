package com.mckj.template.abs

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.mckj.baselib.base.databinding.DataBindingViewBinder
import com.mckj.baselib.util.ResourceUtil
import com.mckj.baselib.util.SizeUtil
import com.mckj.template.impl.IMenuStyle
import com.org.openlib.utils.onceClick
import com.mckj.sceneslib.entity.MenuItem
import com.mckj.sceneslib.manager.scenes.ScenesManager
import com.mckj.template.R
import com.mckj.template.databinding.CleanupMenuItemBinding
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColor

/**
 * Describe:
 *
 * Created By xx on 2022/3/3
 */
open class AbsMenuStyle : DataBindingViewBinder<MenuItem>(), IMenuStyle {


    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
    ): RecyclerView.ViewHolder {
        return ViewHolder(getRootView(inflater, parent)!!)
    }


    private inner class ViewHolder(itemView: View) :
        DataBindingViewBinder.DataBindingViewHolder<MenuItem, CleanupMenuItemBinding>(
            itemView
        ) {

        init {
            itemView.onceClick {
                itemClickListener?.apply {
                    val position = layoutPosition
                    val item = adapter.items[position] as MenuItem
                    onItemClick(it, position, item)
                }
            }
        }

        override fun bindData(t: MenuItem) {
            val scenes = ScenesManager.getInstance().getScenes(t.type)
            scenes?.let {
                getItemIcon()?.imageResource = t.resId
                /**
                 * 菜单名称
                 */
                getItemName()?.let { menuName ->
                    menuName.text = splitItemName(it.getData().name)
                    //设置大小
                    getItemNameSize().let { size ->
                        menuName.textSize = size
                    }
                    //设置颜色
                    getItemNameColor().let { color ->
                        menuName.textColor = ResourceUtil.getColor(color)
                    }
                    //设置粗细
                    getItemNameStyle().let { type ->
                        menuName.setTypeface(Typeface.DEFAULT, type)
                    }
                }
                /**
                 * 菜单描述
                 */
                it.getData().desc.let { desc ->
                    getItemDesc()?.let { menuDesc ->
                        if (desc.isEmpty()) menuDesc.gone() else menuDesc.show()
                        if (forceHideDesc()) menuDesc.gone() else menuDesc.show()
                        menuDesc.text = it.getData().desc
                        //设置大小
                        getItemDescSize().let { size ->
                            menuDesc.textSize = size
                        }
                        //设置颜色
                        getItemDescColor().let { color ->
                            menuDesc.textColor = ResourceUtil.getColor(color)
                        }
                    }
                }

                getRecommendView()?.let { recommendView ->
                    if (t.isRecommend) {
                        if (it.getData().tip.isNotEmpty()) {
                            if (recommendView is TextView) {
                                recommendView.text = it.getData().tip
                            }
                            recommendView.show()
                        } else {
                            recommendView.gone()
                        }
                    } else {
                        recommendView.gone()
                    }
                }

                getMenuArrowView()?.let { menuArrowView ->
                    menuArrowView.text = it.getData().btnText
                }
                invokeItem(t)
            }
        }
    }

    protected open fun forceHideDesc(): Boolean {
        return false
    }

    override fun getRootView(inflater: LayoutInflater, parent: ViewGroup): View? {
        return null
    }

    override fun getItemName(): TextView? {
        return null
    }

    override fun getItemIcon(): ImageView? {
        return null
    }

    override fun getItemDesc(): TextView? {
        return null
    }

    override fun getItemTip(): TextView? {
        return null
    }

    override fun getRecommendView(): View? {
        return null
    }

    override fun getMenuArrowView(): TextView? {
        return null
    }

    protected open fun getItemBg(): Int? {
        return null
    }

    /**
     * 图片大小:直接传dp
     */
    protected open fun getIconSize(): Float? {
        return null
    }

    /**
     * item name 配置
     */
    protected open fun getItemNameColor(): Int {
        return R.color.color_333333
    }

    protected open fun getItemNameSize(): Float {
        return 15f
    }

    protected open fun getItemNameStyle(): Int {
        return Typeface.NORMAL
    }

    /**
     * item Desc 配置
     */
    protected open fun getItemDescColor(): Int {
        return R.color.color_999999
    }

    protected open fun getItemDescSize(): Float {
        return 11f
    }


    /**
     * margin
     */
    protected open fun getNameTopMargin(): Float {
        return 18f
    }

    protected open fun getDescTopMargin(): Float {
        return 7f
    }

    protected open fun invokeItem(t: MenuItem) {

    }
    //如果需要拆分单词的话可以使用此方法
    protected open fun splitItemName(name: String):String {
        return name
    }


}