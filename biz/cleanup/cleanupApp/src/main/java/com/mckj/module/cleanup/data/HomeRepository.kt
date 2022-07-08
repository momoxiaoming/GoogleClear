package com.mckj.module.cleanup.data

import com.mckj.module.cleanup.data.model.IHomeData
import com.mckj.module.cleanup.entity.HomeMenuData

import com.mckj.sceneslib.entity.MenuItem
import com.org.openlib.utils.DateUtil
import com.org.proxy.AppProxy


/**
 * Describe:
 *
 * Created By yangb on 2021/3/1
 */
class HomeRepository(val iHomeData: IHomeData) {

    companion object {
        const val TAG = "HomeRepository"
    }

    /**
     * 获取主界面数据
     */
    suspend fun getHomeMenuData(): HomeMenuData {
        val homeList = filterMenu(iHomeData.getHomeMenuList())
        val businessList = filterMenu(iHomeData.getBusinessMenuList())
        val charaList = filterMenu(iHomeData.getCharaMenuList())
        val charaToolsList = filterMenu(iHomeData.getCharaToolsMenuList())
        val jumpList = filterMenu(iHomeData.getJumpMenuList())

        //填充列表
        wrapMenu(homeList)
        wrapMenu(businessList)
        wrapMenu(charaList)
        wrapMenu(charaToolsList)
        wrapMenu(jumpList)

        //加速天数
        val useDays = try {
            val time = AppProxy.bootTime
            DateUtil.getIntervalDays(time)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
        return HomeMenuData(homeList, businessList, charaList, charaToolsList, jumpList, useDays)
    }

    /**
     * 过滤菜单
     */
    private fun <T : MenuItem> filterMenu(list: List<T>?): List<T>? {
        if (list.isNullOrEmpty()) {
            return list
        }
        val mutableList = list.toMutableList()
        val iterator = mutableList.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
//            //是否审核
//            if (next.isAuditConfig && ConfigHelper.isAudit()) {
//                iterator.remove()
//                continue
//            }
//            //是否推荐
//            if (next.recommendAble) {
//                next.isRecommend = DateUtil.getIntervalDays(next.update) >= 1
//            }
        }
        return mutableList
    }

    /**
     * 填充菜单
     */
    private fun <T : MenuItem> wrapMenu(list: List<T>?) {
        if (list.isNullOrEmpty()) {
            return
        }
        list.forEach {
            it.resId = iHomeData.getMenuResId(it.type)
        }
    }
}