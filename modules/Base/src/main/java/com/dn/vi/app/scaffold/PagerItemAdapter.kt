package com.dn.vi.app.scaffold

import android.content.Context
import android.os.Parcelable
import android.util.SparseIntArray
import androidx.core.util.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager.widget.PagerAdapter
import com.dn.vi.app.base.app.AppMod

/**
 * 方便管理ViewPager里面的每个Fragment,的adapter
 */
open class PagerItemAdapter(
    context: Context? = null,
    fm: FragmentManager,
    val pagerProvider: PagerItemProvider
) :
    DefaultFragmentPagerAdapter(fm) {

    val pagers: ArrayList<PagerItem>

    /**
     * 通过Pager的Id，查对应位置的[pagers]中index
     */
    val idIndex: SparseIntArray

    var context: Context? = context
        get() {
            if (field == null) {
                field = AppMod.app
            }
            return field
        }

    /**
     * 保存老的item(fragment hashCode) position的改变状态
     */
    private val objChangedPosition = SparseIntArray(6)

    init {
        pagerProvider.getPagerItems(context!!).also {
            pagers = ArrayList(it)
        }
        idIndex = (pagers).convertIdIndex()
    }

    /**
     * 更新[pagerProvider]所有页。
     * 保存如果已存在的页，不重复刷新
     */
    fun updatePagers(pagerProvider: PagerItemProvider) {
        pagerProvider.getPagerItems(context!!).also {
            pagers.clear()
            pagers.addAll(it)
        }

        // 记录position change状态
        objChangedPosition.clear()
        for ((index, page) in pagers.withIndex()) {
            val oldIndex = idIndex.get(page.id, -1)
            when {
                oldIndex == -1 -> {
                    // new one
                    objChangedPosition.put(page.fragment.hashCode(), index)
                }
                oldIndex != index -> {
                    // changed
                    objChangedPosition.put(page.fragment.hashCode(), index)
                }
                oldIndex == index -> {
                    objChangedPosition.put(page.fragment.hashCode(), PagerAdapter.POSITION_UNCHANGED)
                }
            }
        }

        // 生成新的id index
        idIndex.clear()
        for ((index, page) in pagers.withIndex()) {
            idIndex.put(page.id, index)
        }

        notifyDataSetChanged()
    }

    /**
     * 通过自己的[PagerItemAdapter.pagerProvider]更新所有页
     * 保存如果已存在的页，不重复刷新
     */
    fun updatePagers() {
        updatePagers(this.pagerProvider)
    }

    override fun getItemPosition(obj: Any): Int {
        // obj is fragment
        if (objChangedPosition.isEmpty()) {
            // 还没有建立修改表，说明，第一次生成
            return PagerAdapter.POSITION_UNCHANGED
        }
        val hash = obj.hashCode()
        return objChangedPosition.get(hash, PagerAdapter.POSITION_NONE)
    }

    fun getPageItem(position: Int): PagerItem = pagers[position]

    override fun getPageTitle(position: Int): CharSequence? = pagers[position].title

    override fun getItem(position: Int): Fragment = pagers[position].fragment

    override fun getCount(): Int = pagers.size

    override fun saveState(): Parcelable? {
        // return super.saveState()
        // 忽略掉state save 。 因为这个adapter主要还是用于已保存了的fragment
        return null
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        // super.restoreState(state, loader)
    }

}