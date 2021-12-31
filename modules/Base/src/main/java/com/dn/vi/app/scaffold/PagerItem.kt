package com.dn.vi.app.scaffold

import android.content.Context
import android.util.SparseIntArray
import androidx.fragment.app.Fragment
import com.dn.vi.app.base.app.ViFragment

/**
 * 类型view pager adapter item 项
 */
class PagerItem(val fragment: Fragment, val title: CharSequence, var id: Int = -1)

/**
 * tab页组合
 *
 * 添加 Fragmet的时候，可使用 Fragment.[toPagerItem] 来转换
 */
interface PagerItemProvider {

    /**
     * 获取tab的组合页
     */
    fun getPagerItems(context: Context): MutableList<PagerItem>

}


/**
 * Factory
 */
interface PagerItemProviderFactory {
    /**
     * to provide a factory to create a [PagerItemProvider]
     */
    fun getTabProvider(): PagerItemProvider
}


/**
 * 生成pageItem
 */
fun ViFragment.toPagerItem(title: CharSequence, id: Int = 0) = PagerItem(this, title, id)

/**
 * 生成pager id与列表tabIndex的对应表
 */
fun List<PagerItem>.convertIdIndex(): SparseIntArray {
    val idIndex = SparseIntArray(this.size)
    for ((index, page) in this.withIndex()) {
        idIndex.put(page.id, index)
    }
    return idIndex
}
