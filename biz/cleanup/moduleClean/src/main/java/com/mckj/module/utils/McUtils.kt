package com.mckj.module.utils

import android.content.pm.PackageInfo
import android.os.Environment
import android.os.StatFs
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.dn.vi.app.base.app.AppMod
import com.dn.vi.app.cm.utils.AppUtil
import com.mckj.api.client.JunkConstants
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author leix
 * @version 1
 * @createTime 2021/7/26 18:32
 * @desc
 */
object McUtils {
    private const val ONE_DAY_TIME = 1000 * 60 * 60 * 24L
    private const val ONE_WEEK_TIME = 1000 * 60 * 60 * 24 * 7L
    private const val ONE_MONTH_TIME = 1000 * 60 * 60 * 24 * 30L
    private const val ONE_YEAR_TIME = 1000 * 60 * 60 * 24 * 365L
    const val ONE_WEEK = 1
    const val ONE_MONTH = 2
    const val ONE_YEAR = 3
    const val EARLY = 4

    fun getColorSpan(str: String, color: Int): SpannableStringBuilder {
        val spanString = SpannableStringBuilder()
        spanString.append(str)
        val colorSpan = ForegroundColorSpan(color)
        spanString.setSpan(colorSpan, 0, spanString.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        return spanString
    }


    fun getTimeLimit(time: Long): Int {
        val currentTime = System.currentTimeMillis()
        val offset = currentTime - time
//        return (0..4).random()
        return when {
            offset < ONE_WEEK_TIME -> {
                ONE_WEEK
            }
            offset < ONE_MONTH_TIME -> {
                ONE_MONTH
            }
            offset < ONE_YEAR_TIME -> {
                ONE_YEAR
            }
            else -> EARLY
        }
    }

    /**
     * 检查是否安装app
     * JunkType.WECHAT
     * unkType.QQ
     */
    fun checkInstall(type: Int): Boolean {
        val packageInfoList: List<PackageInfo> = AppUtil.getInstalledAppInfoList(AppMod.app)
        var isInstall = false
        when (type) {
            JunkConstants.Session.WECHAT_CACHE -> {
                packageInfoList.forEach {
                    if (it.packageName.equals("com.tencent.mm")) {
                        isInstall = true
                    }
                }
            }
            JunkConstants.Session.QQ_CACHE -> {
                packageInfoList.forEach {
                    if (it.packageName.equals("com.tencent.mobileqq")) {
                        isInstall = true
                    }
                }
            }
        }
        return isInstall
    }

    fun queryStorageUsedPercent(): String {
        val statFs = StatFs(Environment.getExternalStorageDirectory().path)
        val blockCount = statFs.blockCount.toLong()
        val availableCount = statFs.availableBlocks.toLong()
        val blockSize = statFs.blockSize.toLong()
        val total = blockCount * blockSize.toDouble()
        val availableSize = blockSize * availableCount.toDouble()
        val p = doubleBit((1 - availableSize / total) * 100, 2) ?: 0

        return "$p%"
    }

    private fun doubleBit(d: Double?, bit: Int?): Double? {
        if (d == null || bit == null) return null
        val bg = BigDecimal(d).setScale(bit, RoundingMode.DOWN)
        return bg.toDouble()
    }


}