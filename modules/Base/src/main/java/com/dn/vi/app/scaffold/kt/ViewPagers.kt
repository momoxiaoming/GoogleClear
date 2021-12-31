/**
 * ViewPager , ViewPager2 相关的功能扩展
 * Created by holmes on 2020/7/13.
 **/
package com.dn.vi.app.scaffold.kt

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.dn.vi.app.cm.log.debug
import com.dn.vi.app.scaffold.PagerItemAdapter
import com.dn.vi.app.scaffold.PagerItemAdapter2
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * 关联 bottomNavigation和viewpager2的默认行为。
 *
 * @param callback 关联回调。在Pager2和bottomNavi里面，
 *  会触发[BottomNaviPager2Callback.onPageSelected], [BottomNaviPager2Callback.onNavigationItemSelected],
 *  常用于，统计一类。
 */
fun associatePagerAndBottomNavigation(
    pager2: ViewPager2,
    bottomNavi: BottomNavigationView,
    adapter: PagerItemAdapter2,
    callback: BottomNaviPager2Callback?
) {
    pager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val id = adapter.getPageItem(position).id
            if (bottomNavi.selectedItemId != id) {
                bottomNavi.selectedItemId = id
                bottomNavi.selectedItemId = id
            }
            debug { "on page2 selected $position" }
            callback?.onPageSelected(position)
        }
    })

    bottomNavi.setOnNavigationItemSelectedListener(object :
        BottomNavigationView.OnNavigationItemSelectedListener {

        private var ignoreReenter = false

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            if (ignoreReenter) {
                return false
            }
            if (bottomNavi.selectedItemId == item.itemId) {
                return true
            }
            val id = item.itemId
            val index = adapter.idIndex.get(id)
            ignoreReenter = true
            bottomNavi.run {
                // 免得在 return true
                // 之前因为， setCurrentItem 里面的 selectedItemId
                // 而再次触发 onNavigationItemSelected
                pager2.setCurrentItem(index, true)
            }
            ignoreReenter = false
            debug { "on bottom nav selected $id with index $index" }
            callback?.onNavigationItemSelected(item)
            return true
        }
    })

}

/**
 * associatePagerAndBottomNavigation
 *
 * 统一的 关联回调
 */
abstract class BottomNaviPager2Callback : ViewPager2.OnPageChangeCallback(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    abstract override fun onPageSelected(position: Int)

}

fun resetPagerAndBottomNavigation(
    pager: ViewPager,
    bottomNavi: BottomNavigationView
) {
    pager.clearOnPageChangeListeners()
    bottomNavi.setOnNavigationItemSelectedListener(null)
}

/**
 * 绑定 viewpager 和 BottomNavigation
 */
fun associatePagerAndBottomNavigation(
    pager: ViewPager,
    bottomNavi: BottomNavigationView,
    adapter: PagerItemAdapter,
    callback: BottomNaviPager2Callback?
) {

    pager.clearOnPageChangeListeners()
    pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            val id = adapter.getPageItem(position).id
            if (bottomNavi.selectedItemId != id) {
                bottomNavi.selectedItemId = id
            }
            debug { "on page2 selected $position" }
            callback?.onPageSelected(position)
        }

    })

    // 同步一下当前page和menu
    val currentPageIndex = pager.currentItem
    val id = adapter.getPageItem(currentPageIndex).id
    if (bottomNavi.selectedItemId != id) {
        bottomNavi.selectedItemId = id
    }

    bottomNavi.setOnNavigationItemSelectedListener(object :
        BottomNavigationView.OnNavigationItemSelectedListener {

        private var ignoreReenter = false

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            if (ignoreReenter) {
                return false
            }
            if (bottomNavi.selectedItemId == item.itemId) {
                return true
            }
            val id = item.itemId
            val index = adapter.idIndex.get(id)
            ignoreReenter = true
            bottomNavi.run {
                // 免得在 return true
                // 之前因为， setCurrentItem 里面的 selectedItemId
                // 而再次触发 onNavigationItemSelected
                pager.setCurrentItem(index, true)
            }
            ignoreReenter = false
            debug { "on bottom nav selected $id with index $index" }
            callback?.onNavigationItemSelected(item)
            return true
        }
    })

    // 在第一次绑定的时候，触发一下默认的选择menu callback
    bottomNavi.selectedItemId.let { itemId ->
        val menu = bottomNavi.menu
        var selectedIndex = -1
        menu.forEachIndexed { index, item ->
            if (selectedIndex != -1) {
                return@forEachIndexed
            }
            if (item.itemId == itemId) {
                selectedIndex = index
            }
        }

        selectedIndex
    }.also { index ->
        if (index != -1) {
            callback?.onPageSelected(index)
        }
    }

}
