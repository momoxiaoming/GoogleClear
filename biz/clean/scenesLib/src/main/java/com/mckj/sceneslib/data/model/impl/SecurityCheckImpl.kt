package com.mckj.sceneslib.data.model.impl

import com.dn.baselib.util.WifiUtil
import com.mckj.sceneslib.data.model.ISecurityCheck
import com.mckj.sceneslib.manager.network.WifiInfo
import com.mckj.sceneslib.util.NetUtil
import com.mckj.sceneslib.util.SignatureUtil

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * Describe:
 *
 * Created By yangb on 2020/10/21
 */
class SecurityCheckImpl : ISecurityCheck {

    override suspend fun checkEncrypt(wifiInfo: WifiInfo?): Boolean {
        return withContext(Dispatchers.IO) {
            delay(1000)
            wifiInfo?.getWifiEncryptType() != WifiUtil.WIFI_CIPHER_NO_PASS
        }
    }

    override suspend fun checkAppException(wifiInfo: WifiInfo?): Boolean {
        return withContext(Dispatchers.IO) {
            delay(1000)
            SignatureUtil.checkSha1()
        }
    }

    override suspend fun checkDnsState(wifiInfo: WifiInfo?): Boolean {
        return withContext(Dispatchers.IO) {
            delay(1000)
            NetUtil.dnsEnable()
        }
    }

    override suspend fun checkArpAttack(wifiInfo: WifiInfo?): Boolean {
        return withContext(Dispatchers.IO) {
            delay(300)
            true
        }
    }

    override suspend fun checkSslAttack(wifiInfo: WifiInfo?): Boolean {
        return withContext(Dispatchers.IO) {
            delay(300)
            true
        }
    }

    override suspend fun checkWebModify(wifiInfo: WifiInfo?): Boolean {
        return withContext(Dispatchers.IO) {
            delay(300)
            true
        }
    }

    override suspend fun checkFishWiFi(wifiInfo: WifiInfo?): Boolean {
        return withContext(Dispatchers.IO) {
            delay(300)
            true
        }
    }


}