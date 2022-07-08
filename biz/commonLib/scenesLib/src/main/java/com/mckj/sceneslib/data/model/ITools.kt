package com.mckj.sceneslib.data.model

import com.mckj.sceneslib.entity.OptItem


/**
 * 文件管理,以及修复页主要功能接口
 *
 * @author mmxm
 * @date 2021/3/4 10:12
 */
interface ITools {

    /**
     * 拉取修复页菜单
     * @return MutableList<OptItem>
     */
    suspend fun getBellsMenuList(): MutableList<OptItem>

}