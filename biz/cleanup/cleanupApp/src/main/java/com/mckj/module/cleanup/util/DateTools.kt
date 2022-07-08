package com.mckj.module.cleanup.util

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.ParsePosition


object DateTools {
    fun getDateDiff(dateTimeStamp: Long): String {
        var result = ""
        var minute = 1000 * 60;
        var hour = minute * 60;
        var day = hour * 24;
        var halfamonth = day * 15;
        var month = day * 30;
        var now = System.currentTimeMillis()
        var diffValue = now - dateTimeStamp
        if (dateTimeStamp == 0L) {
            return "未使用过"
        }
        if (diffValue < 0) {
            return "未使用过"
        }
        var weekC = diffValue / (7 * day);
        var dayC = diffValue / day;
        var monthC = dayC / 30;
        var hourC = diffValue / hour;
        var minC = diffValue / minute;

        //月份
        result = (when (monthC.toInt()) {
            0 -> {
                if(hourC.toInt() <24){
                    "今日使用"
                }else {
                    "${dayC.toInt()}天未使用"
                }
            }
            in 1..11 -> {
                "" + monthC.toInt() + "个月未使用";
            }
            12 -> {
                if (dayC.toInt() < 365) {
                    "" + monthC.toInt() + "个月未使用";
                } else if (dayC.toInt() == 365) {
                    "一年未使用"
                } else {
                    "一年以上未使用"
                }
            }
            //13 or 13以上
            else -> {
                "一年以上未使用"
            }
        }).toString()

        return result
    }

    /**
     * 时间戳 -> 日期
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun timeToDate(time: Long): String {
        return SimpleDateFormat("YY-MM-DD HH:mm:ss").format(time)
    }

    /**
     * 日期 -> 时间戳
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun dateToTime(date: String): Long {
        return SimpleDateFormat("YY-MM-DD").parse(date, ParsePosition(0)).time
    }

}