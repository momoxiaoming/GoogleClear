package com.mckj.sceneslib.util

import android.os.Environment
import android.os.StatFs
import java.math.BigDecimal
import java.math.RoundingMode

object MemoryUtil {
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