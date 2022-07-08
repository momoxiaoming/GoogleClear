package com.mckj.sceneslib.data

import com.mckj.sceneslib.data.model.ITools
import com.mckj.sceneslib.data.model.impl.ToolImpl
import com.mckj.sceneslib.entity.OptItem

/**
 * BellsRepository
 *
 * @author mmxm
 * @date 2021/3/4 10:14
 */
class ToolRepository {

    /**
     * 铃铛提示页接口
     */
    private val tool: ITools by lazy { ToolImpl() }

    suspend fun getMenuList(): MutableList<OptItem> {
        return tool.getBellsMenuList()
    }



}