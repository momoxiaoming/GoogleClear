package com.dn.vi.app.scaffold

import android.content.Context
import android.util.SparseIntArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *  ViewPager2的adapter
 *
 *  一些通用方法可以参数[ViewPagers.kt]扩展,
 *  如[associatePagerAndBottomNavigation]
 *
 * Created by holmes on 2020/7/13.
 **/
open class PagerItemAdapter2 : FragmentStateAdapter {

    private val pagerProvider: PagerItemProvider
    private val context: Context

    val pagers: MutableList<PagerItem>

    /**
     * 通过Pager的Id，查对应位置的[pagers]中index
     */
    val idIndex: SparseIntArray


    constructor(
        fragmentActivity: FragmentActivity,
        pagerProvider: PagerItemProvider
    ) : super(fragmentActivity) {
        context = fragmentActivity
        this.pagerProvider = pagerProvider

        pagers = pagerProvider.getPagerItems(context)
        idIndex = pagers.convertIdIndex()
    }

    constructor(
        fragment: Fragment,
        pagerProvider: PagerItemProvider
    ) : super(fragment) {
        context = fragment.requireContext()
        this.pagerProvider = pagerProvider

        pagers = pagerProvider.getPagerItems(context)
        idIndex = pagers.convertIdIndex()
    }

    fun getPageItem(position: Int): PagerItem = pagers[position]

    override fun getItemCount(): Int {
        return pagers.size
    }

    override fun createFragment(position: Int): Fragment {
        return pagers[position].fragment
    }

    fun getItem(position: Int): Fragment = pagers[position].fragment

}