package com.mckj.sceneslib.data

import com.mckj.sceneslib.data.model.impl.SecurityCheckImpl
import com.mckj.sceneslib.manager.network.WifiInfo

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
class SecurityCheckRepository {

    companion object {
        const val TAG = "WifiCheckRepository"
    }

    /**
     * wifi数据
     */
    private val mSecurityCheck by lazy { SecurityCheckImpl() }

    suspend fun checkEncrypt(wifiInfo: WifiInfo?): Boolean {
        return mSecurityCheck.checkEncrypt(wifiInfo)
    }

    suspend fun checkDnsState(wifiInfo: WifiInfo?): Boolean {
        return mSecurityCheck.checkDnsState(wifiInfo)
    }

    suspend fun checkNetworkSecurity(wifiInfo: WifiInfo?): Boolean {
        return mSecurityCheck.checkAppException(wifiInfo)
                && mSecurityCheck.checkSslAttack(wifiInfo)
                && mSecurityCheck.checkWebModify(wifiInfo)
                && mSecurityCheck.checkFishWiFi(wifiInfo)
                && mSecurityCheck.checkArpAttack(wifiInfo)
    }

}