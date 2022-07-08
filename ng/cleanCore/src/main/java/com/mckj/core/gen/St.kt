package com.mckj.core.gen

import android.content.Context
import androidx.collection.ArrayMap
import com.org.proxy.EvAgent
import com.dn.vi.app.base.app.AppMod


/**
 * Analyse
 * Created by [als-gen] at 11:21:30 2021-12-30.
 */
object St {

    private val context: Context
        get() = AppMod.app



    // ================================
    // Events count: 2
    // ================================

    // arrayMap.clear() 会让arrayMap使用缓存池


    /**
     * data文件夹权限弹窗展示  
     * @param from 触发功能来源  
     */
    fun stDataDialogPop(from: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["from"] = from
        
        EvAgent.sendEventMap("data_dialog_pop", map)
        map.clear()
    }

    /**
     * data文件夹权限结果  
     * @param type 授权结果和失败步骤  
     */
    fun stDataPermissionResult(type: String){
        val map: ArrayMap<String, String> = ArrayMap()
        map["type"] = type
        
        EvAgent.sendEventMap("data_permission_result", map)
        map.clear()
    }


}