package com.mckj.sceneslib.util

import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import com.dn.baselib.ext.getApplication
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import java.security.cert.CertificateFactory
import java.util.*

/**
 * Describe:APP签名工具
 *
 * Created By yangb on 2020/10/21
 */
object SignatureUtil {

    const val TAG = "SignatureUtil"

    const val APP_SHA1 = "5B:CB:81:A1:F8:6C:5F:A3:7C:2A:F2:2A:69:AD:2C:8D:FE:32:2C:BC"

    @RequiresApi(Build.VERSION_CODES.P)
    fun getCertificateSHA1(): String? {
        try {
            val context = getApplication()
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
            val signingInfo = packageInfo.signingInfo
            val signatures = signingInfo.apkContentsSigners
            val cert = signatures[0].toByteArray()
            val inputStream = ByteArrayInputStream(cert)
            //证书工厂类，这个类实现了出厂合格证算法的功能
            val certificateFactory = CertificateFactory.getInstance("X509")
            //X509 证书，X.509 是一种非常通用的证书格式
            val x509Certificate = certificateFactory.generateCertificate(inputStream)
            //加密算法的类，这里的参数可以使 MD4,MD5 等加密算法
            val messageDigest = MessageDigest.getInstance("SHA1")
            //获得公钥
            val publicKey: ByteArray = messageDigest.digest(x509Certificate.encoded)
            //字节到十六进制的格式转换
            return byte2HexFormatted(publicKey)
        } catch (e: NoSuchFieldError) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 这里是将获取到得编码进行16 进制转换
     */
    private fun byte2HexFormatted(arr: ByteArray): String {
        val str = StringBuilder(arr.size * 2)
        for (i in arr.indices) {
            var h = Integer.toHexString(arr[i].toInt())
            val l = h.length
            if (l == 1) h = "0$h"
            if (l > 2) h = h.substring(l - 2, l)
            str.append(h.toUpperCase(Locale.getDefault()))
            if (i < arr.size - 1) str.append(':')
        }
        return str.toString()
    }

    fun checkSha1(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val sha1 = getCertificateSHA1()
            Log.i(TAG, "checkSha1: sha1:$sha1")
            Log.i(TAG, "checkSha1: SHA1:$APP_SHA1")
            APP_SHA1 == sha1
        } else {
            true
        }
    }

}