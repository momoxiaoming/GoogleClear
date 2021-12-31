package com.mckj.module.wifi.entity

import com.mckj.sceneslib.manager.network.WifiInfo

/**
 * Describe:
 *
 * Created By yangb on 2020/11/26
 */
data class WifiInfoMoreEntity(var list: List<WifiInfo>? = null, var isShowMore: Boolean = false){

    override fun equals(other: Any?): Boolean {
        if(other is WifiInfoMoreEntity){
            return list == other.list && isShowMore == other.isShowMore
        }
        return false
    }

    override fun hashCode(): Int {
        var result = list?.hashCode() ?: 0
        result = 31 * result + isShowMore.hashCode()
        return result
    }

}