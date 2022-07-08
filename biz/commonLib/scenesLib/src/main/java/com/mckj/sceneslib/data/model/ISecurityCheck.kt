package com.mckj.sceneslib.data.model

import com.mckj.sceneslib.manager.network.WifiInfo

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
interface ISecurityCheck {

    /**
     * 是否加密
     */
    suspend fun checkEncrypt(wifiInfo: WifiInfo?): Boolean

    /**
     * 检测APP是否异常
     */
    suspend fun checkAppException(wifiInfo: WifiInfo?): Boolean

    /**
     * 检测DNS状态
     */
    suspend fun checkDnsState(wifiInfo: WifiInfo?): Boolean

    /**
     * 检测ARP攻击
     */
    suspend fun checkArpAttack(wifiInfo: WifiInfo?): Boolean

    /**
     * 检测SSL攻击
     */
    suspend fun checkSslAttack(wifiInfo: WifiInfo?): Boolean

    /**
     * 检测是否遭到网页修改
     */
    suspend fun checkWebModify(wifiInfo: WifiInfo?): Boolean

    /**
     * 检测是否是钓鱼WiFi
     */
    suspend fun checkFishWiFi(wifiInfo: WifiInfo?): Boolean

}