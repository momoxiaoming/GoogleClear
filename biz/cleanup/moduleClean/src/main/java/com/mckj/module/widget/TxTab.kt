package com.mckj.module.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import com.mckj.baselib.util.SizeUtil
import com.mckj.module.bean.SortBean
import com.mckj.moduleclean.R
import org.jetbrains.anko.backgroundResource

/**
 * @author leix
 * @version 1
 * @createTime 2021/9/15 14:29
 * @desc
 */
class TxTab : LinearLayout {
    private var mTabEntity: TabEntity? = null

    private var mTabNameTv: TextView? = null
    private var mTabNameIv: ImageView? = null


    private var mPopWindow: SortPopWindow? = null


    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        val view =
            LayoutInflater.from(context).inflate(R.layout.cleanup_tabs, this, true)
        mTabNameTv = view.findViewById(R.id.tab_name)
        mTabNameIv = view.findViewById(R.id.expand_icon)
        mPopWindow = SortPopWindow(context)
        mPopWindow?.setOnDismissListener {
            mTabNameIv?.backgroundResource = R.drawable.cleanup_icon_arrow_down_white
        }
    }

    fun setupWithTabLayout( tabEntity: TabEntity?) {
        this.mTabEntity = tabEntity
        mTabNameTv?.text = mTabEntity?.tabName
        mTabEntity?.sortList?.let {
            mPopWindow?.setData(it)
        }
    }

    fun popWindow() {
        mPopWindow?.showAsDropDown(this, 0, SizeUtil.dp2px(18f))
        mTabNameIv?.backgroundResource = R.drawable.cleanup_icon_arrow_up_white
    }

    data class TabEntity(var tabName: String, var sortList: MutableList<SortBean>)
}