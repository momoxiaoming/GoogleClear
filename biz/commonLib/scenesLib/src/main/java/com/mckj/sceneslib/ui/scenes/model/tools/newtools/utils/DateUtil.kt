package com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils

import com.mckj.sceneslib.util.Log
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.mckj.sceneslib.ui.scenes.model.tools.newtools.utils
 * @data  2022/4/14 09:58
 */
object DateUtil {

    const val FORMAT_Y_M_D="yyyy.MM.dd"
    const val FORMAT_YMD="yyyy年MM月dd日"
    const val FORMAT_YM="yyyy年MM月"
    const val FORMAT_YMDHMS="yyyy-MM-dd HH:mm:ss"


    fun intervalDays(aimTime: Long): Int {
        val diff = aimTime - Date().time
        var days = diff / (1000 * 60 * 60 * 24).toDouble()
        var  big = BigDecimal(days).setScale(1, BigDecimal.ROUND_HALF_UP).toInt()
        Log.d("666","intervalDays------ $diff ------ $days-------$big")
        if (big<0)  big = 0
        return big
    }

    fun getWeekOfDate(): String {
        val week = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val cal = Calendar.getInstance()
        var w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return week[w]
    }

    fun getWeekOfStamp(stamp: Long): String {
        val week = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val cal = Calendar.getInstance()
        cal.time = Date(stamp)
        var w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return week[w]
    }

    fun formatStampDate(stamp: Long,format:String= FORMAT_Y_M_D): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
        return simpleDateFormat.format(Date(stamp))
    }



    fun formatNowDateFormat(format:String= FORMAT_YMD):String{
        val simpleDateFormat = SimpleDateFormat(format, Locale.CHINA)
        return simpleDateFormat.format(Date())
    }

}