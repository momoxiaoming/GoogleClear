/**
 *
 * Created by holmes on 2020/6/15.
 **/
package com.mckj.baselib.util

import android.text.format.Formatter
import com.dn.vi.app.base.app.AppMod

/**
 * 格式化为 可于阅读的文件大小
 *  e.g: 1024 -> 1KB
 */
fun Long.formatShortFileSize(): String = Formatter.formatShortFileSize(AppMod.app, this)


/**
 * 格式化为 可于阅读的文件大小
 * 返回 格式化的完整元素
 *  e.g: 1024 -> {1, 'KB'}
 */
fun Long.formatShortFileSizeResult(): BytesResult = formatBytes(this, true)

/**
 * 格式化size大小，包括数字与单位分段
 *
 * 代码参考 系统的[Formatter]
 *
 * @see Formatter
 */
fun formatBytes(sizeBytes: Long, short: Boolean): BytesResult {
    val calculateRound = false  // 按math round 来算

    val unit: Long = 1000L  // 1024 or 1000
    var mult: Long = 1L
    var result: Float = sizeBytes.toFloat()
    var suffix: String = "B"    // unit

    if (result > 900) {
        suffix = "KB"
        mult = unit
        result = result / unit;
    }
    if (result > 900) {
        suffix = "MB"
        mult *= unit
        result = result / unit;
    }
    if (result > 900) {
        suffix = "GB"
        mult *= unit;
        result = result / unit;
    }
    if (result > 900) {
        suffix = "TB"
        mult *= unit;
        result = result / unit;
    }
    if (result > 900) {
        suffix = "PB"
        mult *= unit;
        result = result / unit;
    }
    // Note we calculate the rounded long by ourselves, but still let String.format()
    // compute the rounded value. String.format("%f", 0.1) might not return "0.1" due to
    // floating point errors.
    val roundFactor: Int;
    val roundFormat: String;
    if (mult == 1L || result >= 100) {
        roundFactor = 1;
        roundFormat = "%.0f";
    } else if (result < 1) {
        roundFactor = 100;
        roundFormat = "%.2f";
    } else if (result < 10) {
        if (short) {
            roundFactor = 10;
            roundFormat = "%.1f";
        } else {
            roundFactor = 100;
            roundFormat = "%.2f";
        }
    } else { // 10 <= result < 100
        if (short) {
            roundFactor = 1;
            roundFormat = "%.0f";
        } else {
            roundFactor = 100;
            roundFormat = "%.2f";
        }
    }

    val roundedString: String = String.format(roundFormat, result);

    // Note this might overflow if abs(result) >= Long.MAX_VALUE / 100, but that's like 80PB so
    // it's okay (for now)...
    val roundedBytes: Long = if (!calculateRound) {
        0L
    } else {
        (Math.round(result * roundFactor) * mult / roundFactor).toLong()
    }


    return BytesResult(roundedString, suffix, roundedBytes)
}

/**
 * bytes 格式化结果
 */
data class BytesResult(
    val value: String? = null,
    val units: String? = null,
    val roundedBytes: Long = 0
)
