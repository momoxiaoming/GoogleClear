package com.mckj.module.wifi.data

import com.mckj.module.wifi.data.model.IHomeData
import com.mckj.module.wifi.entity.HomeMenuData
import com.mckj.openlib.helper.ConfigHelper
import com.mckj.openlib.util.DateUtil
import com.mckj.sceneslib.entity.MenuItem
import com.tz.gg.appproxy.AppProxy
import kotlinx.coroutines.rx3.await

/**
 * Describe:
 *
 * Created By yangb on 2021/5/18
 */
class HomeRepository(val iHomeData: IHomeData) {

    /**
     * 获取主界面数据
     */
    suspend fun getHomeMenuData(): HomeMenuData {
        //过滤列表
        val homeList = filterMenu(iHomeData.getHomeMenuList())
        val businessList = filterMenu(iHomeData.getBusinessMenuList())
        val jumpList = filterMenu(iHomeData.getJumpMenuList())

        //填充列表
        wrapMenu(homeList)
        wrapMenu(businessList)
        wrapMenu(jumpList)

        //加速天数
        val useDays = try {
            val time = AppProxy.getBootTime().await()
            DateUtil.getIntervalDays(time)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
        return HomeMenuData(homeList, businessList, jumpList, useDays)
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
            //是否审核
            if (next.isAuditConfig && ConfigHelper.isAudit()) {
                iterator.remove()
                continue
            }
            //是否推荐
            if (next.recommendAble) {
                next.isRecommend = DateUtil.getIntervalDays(next.update) >= 1
            }
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