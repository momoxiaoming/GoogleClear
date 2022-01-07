package com.dn.baselib.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    const val TAG = "DateUtil"

    const val FORMAT_HHMMSS = "HH:mm:ss"
    const val FORMAT_YYYYMMDD = "yyyy-MM-dd"
    const val FORMAT_YYYYMMDD_ZH = "yyyy年MM月dd日"
    const val FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss"
    const val FORMAT_YYYYMMDD_HHMMSS_ZH = "yyyy年MM月dd日 HH:mm:ss"

    fun getDate(dateText: String, format: String): Date? {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        try {
            return simpleDateFormat.parse(dateText)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取日期格式
     */
    fun getDateText(date: Date, format: String): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun getDateText(time: Long, format: String) = getDateText(Date(time), format)


    fun isToday(time: Long): Boolean {
        return getIntervalDays(time) == 0
    }

    fun getIntervalDays(startTime: Long): Int {
        return getIntervalDays(startTime, Date().time)
    }

    /**
     * 获取日期间隔
     */
    fun getIntervalDays(startTime: Long, endTime: Long): Int {
        var min: Long = 0
        var max: Long = 0
        if (startTime < endTime) {
            min = startTime
            max = endTime
        } else {
            min = endTime
            max = startTime
        }
        val minCalendar = Calendar.getInstance()
        val maxCalendar = Calendar.getInstance()
        minCalendar.timeInMillis = min
        maxCalendar.timeInMillis = max
        return maxCalendar.get(Calendar.DAY_OF_YEAR) - minCalendar.get(Calendar.DAY_OF_YEAR)
    }

}