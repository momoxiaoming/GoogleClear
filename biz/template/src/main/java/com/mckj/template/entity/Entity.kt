package com.mckj.template.entity

import com.mckj.sceneslib.entity.MenuItem
import java.io.Serializable


/**
 *  author :leo
 *  date : 2022/3/5 11:26
 *  description :
 */

/**
 * @param type @Linked TepConstants.Menu
 *  HOME_MENU_MAIN = 1:主菜单
 *  HOME_MENU_BUS = 2:特色菜单
 *  HOME_MENU_RECOMMEND = 3 推荐菜单
 *
 * @param category:菜单名称
 * @param menuList 菜单列表
 */
data class UIMenuBean(val type: Int,var category: String, var menuList: List<Any>? = null)


//首頁持久化配置信息
data class HomeConfig(var list: MutableList<HomeRecommendConfig>? = null) : Serializable

//首页推荐项目状态记录
data class HomeRecommendConfig(var updateTime: Long? = 0L, var recordType: Int? = -1) :
    Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as HomeRecommendConfig
        if (recordType == other.recordType) return true
        return true
    }
}


data class StorageBean(
    var total: Long = 0,
    var used: Long = 0,
    var available: Long,
    var system: Long = 0
)

data class MenuData(
    /**
     * 主菜单列表
     */
    var homeList: MutableList<MenuItem>,
    /**
     * 使用天数
     */
    val useDays: Int
)