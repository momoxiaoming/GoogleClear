package com.mckj.template.abs

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dn.vi.app.base.view.gone
import com.dn.vi.app.base.view.show
import com.drakeet.multitype.MultiTypeAdapter
import com.mckj.api.util.ScopeHelper
import com.mckj.template.entity.DataCenter
import com.mckj.template.entity.UIMenuBean

/**
 *  author : leix
 *  date : 2022/3/5 11:25
 *  description :
 */
abstract class AbsMenuView(
    context: Context
) :
    BaseViewContainer<UIMenuBean>(context) {

    protected var mStyle: AbsMenuStyle? = null

    protected var mDecoration: RecyclerView.ItemDecoration? = null

    override fun initView(context: Context): View {
        return getRoot()
    }


    abstract fun getRoot(): View

    abstract fun getContainerView(): View?

    abstract fun getMenuList(): RecyclerView?

    abstract fun getMenuAdapter(): MultiTypeAdapter?

    open fun getCategory(): TextView? {
        return null
    }

    fun bindStyle(style: AbsMenuStyle) {
        mStyle = style
    }

    fun setDecoration(decoration: RecyclerView.ItemDecoration) {
        mDecoration = decoration
    }

    fun onItemClick(type: Int) {
        ScopeHelper.launch {
            val dataCenter = DataCenter()
            dataCenter.updateRecommend(type)
        }
    }

    override fun updateUI(context: Context, data: UIMenuBean) {
        getCategory()?.apply {
            if (data.category.isEmpty())
                gone()
            else {
                show()
                text = data.category
            }
        }
        getMenuList()?.apply {
            getMenuAdapter()?.let { adapter ->
                adapter.items = data.menuList!!
                this.adapter = adapter
                this.adapter?.notifyDataSetChanged()
            }
        }
    }
}